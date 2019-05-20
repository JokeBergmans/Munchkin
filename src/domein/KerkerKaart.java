package domein;

public abstract class KerkerKaart extends Kaart {

    private final String achterkant;

    public KerkerKaart(String naam, Effect effect, int id) {
        super(naam, effect, id);
        achterkant = "resources/afbeeldingen/kaarten/Achterkant.png";
    }

}
