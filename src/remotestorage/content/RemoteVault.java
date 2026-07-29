package remotestorage.content;

import arc.struct.EnumSet;
import mindustry.content.Items;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BlockGroup;
import mindustry.world.meta.Env;
import mindustry.world.modules.ItemModule;

import static mindustry.type.ItemStack.with;

public class RemoteVault extends StorageBlock {

    public RemoteVault(String name) {
        super(name);
 //   research = mindustry.content.Blocks.vault;  // ← ここに追加        
        
        requirements(Category.effect, with(
            Items.titanium, 250,
            Items.thorium, 100,
            Items.silicon, 150
        ));

        size = 3;
        health = 500;
        itemCapacity = 1000;
        hasItems = true;
        solid = true;
        update = true;
        destructible = true;
        sync = true;
        coreMerge = false;
        group = BlockGroup.transportation;
        envEnabled = Env.any;
        flags = EnumSet.of(BlockFlag.storage);

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
            if (linkedCore == null || !linkedCore.isValid() || linkedCore.team != team) {
                tryLink();
            }
        }

        void tryLink() {
            CoreBuild core = team.core();
            if (core != null) {
                linkedCore = core;
                items = core.items;
            } else {
                linkedCore = null;
                if (items == null) {
                    items = new ItemModule();
                }
            }
        }
    }
}
