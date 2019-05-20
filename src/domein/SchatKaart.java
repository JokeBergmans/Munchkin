package domein;

public abstract class SchatKaart extends Kaart {
    private final int waarde;
    private final String achterkant;


    public SchatKaart(String naam, Effect effect, int id, int waarde) {
        super(naam, effect, id);
        this.waarde = waarde;
        achterkant = "resources/afbeeldingen/kaarten/AchterkantSchatKaart.png";
    }

    public int getWaarde() {
        return waarde;
    }


    @Override
    public String getAchterkant() {
        return achterkant;
    }
}
