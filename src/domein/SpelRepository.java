package domein;

import persistentie.PersistentieController;

import java.util.List;

public class SpelRepository {

    private PersistentieController pc;

    public SpelRepository(PersistentieController pc) {
        this.pc = pc;
    }

    public List<String> geefSpelnamen() {
        return pc.geefSpelnamen();
    }

    public int slaSpelOp(Spel spel) {
        return pc.slaSpelOp(spel);
    }

    public void zetHuidigeSpeler(Speler huidigeSpeler, int spelId) {
        pc.zetHuidigeSpeler(huidigeSpeler, spelId);
    }

    public int geefSpelId(String naam) {
        return pc.geefSpelId(naam);
    }

    public void verwijderSpel(Spel spel) {
        pc.verwijderSpel(spel);
    }

    public List<Spel> geefSpellen() {
        return pc.geefSpellen();
    }

    public String geefHuidigeSpeler(int spelId) {
        return pc.geefHuidigeSpeler(spelId);
    }
}
