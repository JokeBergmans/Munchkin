package domein;

import persistentie.PersistentieController;

import java.util.List;

public class SpelerRepository {

    private PersistentieController pc;

    public SpelerRepository(PersistentieController pc) {
        this.pc = pc;
    }

    public int slaSpelerOp(Speler s, int spelId) {
        return pc.slaSpelerOp(s,spelId);
    }

    public List<Speler> geefSpelers(int spelId, Spel spel) {
        return pc.geefSpelers(spelId, spel);
    }
}
