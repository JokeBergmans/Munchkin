package domein;

public abstract class Kaart {

    private final String naam;
    private final Effect effect;
    private final int id;
    private final String afbeeldig;
    private final String achterkant;

    public Kaart(String naam, Effect effect, int id) {
        this.naam = naam;
        this.effect = effect;
        this.id = id;
        afbeeldig = "resources/afbeeldingen/kaarten/" + naam + ".png";
        achterkant = "resources/afbeeldingen/kaarten/Achterkant.png";
    }

    public String getNaam() {
        return naam;
    }

    public Effect getEffect() {
        return effect;
    }

    public int getId() {
        return id;
    }

    public String getAfbeeldig() {
        return afbeeldig;
    }

    public String getAchterkant() {
        return achterkant;
    }
}
