package domein;

public class MonsterKaart extends KerkerKaart {

    private final int level;
    private final int aantalSchatten;
    private final Effect straf;

    public MonsterKaart(String naam, Effect effect, int id, int level, int aantalSchatten, Effect straf) {
        super(naam, effect, id);
        this.level = level;
        this.aantalSchatten = aantalSchatten;
        this.straf = straf;
    }

    public int getLevel() {
        return level;
    }

    public int getAantalSchatten() {
        return aantalSchatten;
    }

    public Effect getStraf() {
        return straf;
    }
}
