package domein;

public class ConsumableSchatKaart extends SchatKaart {

    private final boolean beideKanten;

    public ConsumableSchatKaart(String naam, Effect effect, int waarde, boolean beideKanten, int id) {
        super(naam, effect, id, waarde);
        this.beideKanten = beideKanten;
    }

    public boolean isBeideKanten() {
        return beideKanten;
    }
}
