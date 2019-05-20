package persistentie;

import domein.*;

import java.util.List;

public class PersistentieController {

    private KaartMapper kaartMapper;
    private SpelerMapper spelerMapper;
    private SpelMapper spelMapper;

    public PersistentieController() {
        kaartMapper = new KaartMapper();
        spelerMapper = new SpelerMapper();
        spelMapper = new SpelMapper();
    }

    public List<KerkerKaart> geefStapelKerkerKaarten() {
        return kaartMapper.geefKerkerKaarten();
    }

    public List<SchatKaart> geefStapelSchatKaarten() {
        return kaartMapper.geefSchatKaarten();
    }

    public List<String> geefSpelnamen() {
        return spelMapper.geefSpelnamen();
    }

    public int slaSpelOp(Spel spel) {
        return spelMapper.slaSpelOp(spel);
    }

    public int slaSpelerOp(Speler s, int spelId) {
        return spelerMapper.slaSpelerOp(s,spelId);
    }

    public void slaHandKaartenOp(List<Kaart> handKaarten, int spelerId) {
        kaartMapper.slaHandKaartenOp(handKaarten, spelerId);
    }

    public void slaGedragenKaartenOp(List<EquipmentKaart> gedragenKaarten, int spelerId) {
        kaartMapper.slaGedragenKaartenOp(gedragenKaarten, spelerId);
    }

    public void slaGespeeldeKaartenOp(List<Kaart> gespeeldeKaarten, int spelId) {
        kaartMapper.slaGespeeldeKaartenOp(gespeeldeKaarten, spelId);
    }

    public void zetHuidigeSpeler(Speler huidigeSpeler, int spelId) {
        spelMapper.zetHuidigeSpeler(huidigeSpeler, spelId);
    }

    public List<Spel> geefSpellen() {
        return spelMapper.geefSpellen();
    }

    public int geefSpelId(String naam) {
        return spelMapper.geefSpelId(naam);
    }

    public List<Speler> geefSpelers(int spelId, Spel spel) {
        return spelerMapper.geefSpelers(spelId, spel);
    }

    public List<Kaart> geefHandKaarten(int spelId, String naam) {
        return kaartMapper.geefHandKaarten(spelId, naam);
    }

    public void verwijderSpel(Spel spel) {
        spelMapper.verwijderSpel(spel);
    }

    public String geefHuidigeSpeler(int spelId) {
        return spelMapper.geefHuidigeSpeler(spelId);
    }

    public List<EquipmentKaart> geefGedragenKaarten(int spelId, String naam) {
        return kaartMapper.geefGedragenKaarten(spelId, naam);
    }

    public List<Kaart> geefGespeeldeKaarten(int spelId) {
        return kaartMapper.geefGespeeldeKaarten(spelId);
    }
}
