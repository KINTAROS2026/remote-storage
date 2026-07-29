package remotestorage;

import mindustry.mod.Mod;
import remotestorage.content.RemoteVault;

public class RemoteStorageMod extends Mod {

    @Override
    public void loadContent() {
        // ブロックを登録
        new RemoteVault("remote-vault");
    }
}