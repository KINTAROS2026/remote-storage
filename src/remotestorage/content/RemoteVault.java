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
        
    alwaysUnlocked = true;   // ← これを追加（最初から使える）

        requirements(Category.effect, with(
            Items.copper, 100,
            Items.lead, 100
        ));

        size = 2;
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
@Override
public boolean acceptItem(Building source, mindustry.type.Item item) {
    // コアにリンクしていない場合は通常通り
    if (linkedCore == null) {
        return items.get(item) < getMaximumAccepted(item);
    }

    // コアが満タンなら受け入れない（破棄しない）
    CoreBuild core = (CoreBuild) linkedCore;
    return core.items.get(item) < core.storageCapacity;
}

@Override
public void handleItem(Building source, mindustry.type.Item item) {
    if (linkedCore != null) {
        CoreBuild core = (CoreBuild) linkedCore;
        // 満タンなら何もしない（焼却しない）
        if (core.items.get(item) >= core.storageCapacity) {
            return;
        }
        core.noEffect = true;
        linkedCore.handleItem(source, item);
    } else {
        super.handleItem(source, item);
    }
}       
    }
}
