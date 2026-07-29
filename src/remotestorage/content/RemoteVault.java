package remotestorage.content;

import arc.util.Nullable;
import mindustry.content.Items;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;

import static mindustry.type.ItemStack.with;

public class RemoteVault extends StorageBlock {

    public RemoteVault(String name) {
        super(name);

    research = Blocks.vault;   // Vaultの後に研究解放される
        // 基本設定
        requirements(Category.effect, with(
            Items.titanium, 250,
            Items.thorium, 100,
            Items.silicon, 150
        ));

        size = 3;
        health = 500;
        itemCapacity = 1000;          // 見た目・表示用（実際の上限はコアに従う）
        hasItems = true;
        solid = true;
        update = true;                // リンク維持のため必須
        destructible = true;
        sync = true;
        coreMerge = false;            // バニラの隣接拡張を無効化（重要）
        group = BlockGroup.transportation;
        envEnabled = Env.any;
        flags = EnumSet.of(BlockFlag.storage);

        // 説明
        localizedName = "Remote Vault";
        description = "コアから離れていても資源を共有します。容量上限は増えません。";
    }

    public class RemoteVaultBuild extends StorageBuild {

        @Override
        public void created() {
            super.created();
            tryLink();
        }

        @Override
        public void updateTile() {
            // コアが消えた・チームが変わった場合に再リンク
            if (linkedCore == null || !linkedCore.isValid() || linkedCore.team != team) {
                tryLink();
            }
        }

        /** チームのコアにリンクする */
        void tryLink() {
            Building core = team.core();   // 最高Tierのコアを取得
            if (core != null) {
                linkedCore = core;
                items = core.items;        // 同じItemModuleを共有 → 容量は増えない
            } else {
                // コアがない場合は独立ストレージに戻す
                linkedCore = null;
                if (items == null || (linkedCore == null && items == team.core() != null ? team.core().items : null)) {
                    items = new mindustry.world.modules.ItemModule();
                }
            }
        }

        // acceptItem / handleItem / canUnload / getMaximumAccepted は
        // 親クラス StorageBuild の linkedCore 対応ロジックがそのまま使えるので
        // 特にオーバーライド不要（必要に応じて微調整）
    }
}
