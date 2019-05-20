package domein;

public class EquipmentKaart extends SchatKaart {

    private final EquipmentType type;

    public EquipmentKaart(String naam, Effect effect, int id, int waarde, EquipmentType type) {
        super(naam, effect, id, waarde);
        this.type = type;
    }

    public EquipmentType getType() {
        return type;
    }
}

