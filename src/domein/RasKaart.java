package domein;

public class RasKaart extends KerkerKaart {

    private final Ras ras;

    public RasKaart(String naam, Effect effect, Ras ras, int id) {
        super(naam, effect, id);
        this.ras = ras;
    }

    public Ras getRas() {
        return ras;
    }
}
