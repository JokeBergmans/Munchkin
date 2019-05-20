package domein;

public class Effect {

    private final EffectType type;
    private final int waarde;
    private final Ras beperking;

    public Effect(EffectType type, int waarde, Ras beperking) {
        this.type = type;
        this.waarde = waarde;
        this.beperking = beperking;
    }


    public EffectType getType() {
        return type;
    }

    public int getWaarde() {
        return waarde;
    }

    public Ras getBeperking() {
        return beperking;
    }

    /**
     * Voert zijn effect uit op de gegeven speler, afhankelijk van effectType
     * - NIVEAUBONUS:  niveau van de speler verhoogt/verlaagt met waarde
     * - SPELERBONUS:  level van de speler verhoogt/verlaagt met waarde
     * - MONSTERBONUS: level van monster verhoogt/verlaagt met waarde
     * - WEGLOOPBONUS: speler krijgt een bonus van waarde bij zijn worp als hij wilt weglopen
     * @param speler        gegeven speler
     */
    public void voerUit(Speler speler) {
        switch (type) {
            case NIVEAUBONUS:
                if (waarde > 0 || speler.getNiveau() > 1) {
                    speler.veranderNiveau(waarde);
                }
                break;
            case SPELERBONUS:
                speler.veranderLevel(waarde);
                break;
            case MONSTERBONUS:
                //TODO: UC5
                break;
            case WEGLOOPBONUS:
                //TODO: UC6
                break;
        }
    }
}
