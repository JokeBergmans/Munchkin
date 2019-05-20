package domein;

import persistentie.PersistentieController;
import java.util.List;

public class KaartRepository {

    private PersistentieController pc;

    public KaartRepository(PersistentieController pc) {
        this.pc = pc;
    }

    public List<KerkerKaart> geefKerkerKaarten() {
        return pc.geefStapelKerkerKaarten();
    }

    public List<SchatKaart> geefSchatKaarten() {
        return pc.geefStapelSchatKaarten();
    }

    public void slaHandKaartenOp(List<Kaart> handKaarten, int spelerId) {
        pc.slaHandKaartenOp(handKaarten, spelerId);
    }

    public void slaGedragenKaartenOp(List<EquipmentKaart> gedragenKaarten, int spelerId) {
        pc.slaGedragenKaartenOp(gedragenKaarten, spelerId);
    }

    public void slaGespeeldeKaartenOp(List<Kaart> gespeeldeKaarten, int spelId) {
        pc.slaGespeeldeKaartenOp(gespeeldeKaarten, spelId);
    }

    public List<Kaart> geefHandKaarten(int spelId, String naam) {
        return pc.geefHandKaarten(spelId, naam);
    }

    public List<EquipmentKaart> geefGedragenKaarten(int spelId, String naam) {
        return pc.geefGedragenKaarten(spelId,naam);
    }

    public List<Kaart> geefGespeeldeKaarten(int spelId) {
        return pc.geefGespeeldeKaarten(spelId);
    }
}
