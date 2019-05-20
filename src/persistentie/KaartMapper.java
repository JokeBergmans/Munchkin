package persistentie;

import domein.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KaartMapper {

    private Connection c;

    public KaartMapper() {
        try {
            c = DriverManager.getConnection(Connectie.JDBC_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<KerkerKaart> geefKerkerKaarten() {
        List<KerkerKaart> kerkerKaarten = new ArrayList<>();
        try {
            kerkerKaarten.addAll(geefMonsterKaarten());
            kerkerKaarten.addAll(geefCurseKaarten());
            kerkerKaarten.addAll(geefRasKaarten());
            kerkerKaarten.addAll(geefConsumableKerkerKaarten());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return kerkerKaarten;
    }

    private List<MonsterKaart> geefMonsterKaarten() throws SQLException {
        try (PreparedStatement monsterKaarten = c.prepareStatement("SELECT naam, " +
                     "monsterKaartID, " +
                     "`level`, " +
                     "aantalSchatten, " +
                     "effect.waarde as effectWaarde, " +
                     "effect.effect as effectType, " +
                     "straf.waarde as strafWaarde, " +
                     "straf.effect as strafType, " +
                     "ras " +
                     "FROM MonsterKaart m " +
                     "INNER JOIN Effect straf " +
                     "ON m.strafID = straf.effectID " +
                     "LEFT JOIN Effect effect " +
                     "ON m.effectID = effect.effectID " +
                     "LEFT JOIN Ras r " +
                     "ON effect.beperkingID = r.rasID")) {
            List<MonsterKaart> kaarten = new ArrayList<>();
            ResultSet rs = monsterKaarten.executeQuery();
            while (rs.next()) {
                kaarten.add(maakMonsterKaart(rs));
            }
            return kaarten;
        }
    }

    private MonsterKaart geefMonsterKaart(int id) throws SQLException {
        try (PreparedStatement monsterKaart = c.prepareStatement("SELECT naam, " +
                     "monsterKaartID, " +
                     "`level`, " +
                     "aantalSchatten, " +
                     "effect.waarde as effectWaarde, " +
                     "effect.effect as effectType, " +
                     "straf.waarde as strafWaarde, " +
                     "straf.effect as strafType, " +
                     "ras " +
                     "FROM MonsterKaart m " +
                     "INNER JOIN Effect straf " +
                     "ON m.strafID = straf.effectID " +
                     "LEFT JOIN Effect effect " +
                     "ON m.effectID = effect.effectID " +
                     "LEFT JOIN Ras r " +
                     "ON effect.beperkingID = r.rasID " +
                     "WHERE monsterKaartID = ?")) {
            monsterKaart.setInt(1, id);
            ResultSet rs = monsterKaart.executeQuery();
            rs.next();
            return maakMonsterKaart(rs);
        }
    }

    private MonsterKaart maakMonsterKaart(ResultSet rs) throws SQLException {
        Effect effect;
        if (rs.getString("effectType") == null) {
            effect = null;
        } else {
            Ras beperking = null;
            if (rs.getString("ras") != null) {
                beperking = Ras.valueOf(rs.getString("ras"));
            }
            effect = new Effect(EffectType.valueOf(rs.getString("effectType")), rs.getInt("effectWaarde"), beperking);
        }
        Effect straf = new Effect(EffectType.valueOf(rs.getString("strafType")), rs.getInt("strafWaarde"), null);
        return new MonsterKaart(rs.getString("naam"), effect, rs.getInt("monsterKaartID"), rs.getInt("level"), rs.getInt("aantalSchatten"), straf);
    }

    private List<CurseKaart> geefCurseKaarten() throws SQLException {
        try (PreparedStatement curseKaarten = c.prepareStatement("SELECT naam, " +
                     "waarde, " +
                     "curseKaartID, " +
                     "effect as type " +
                     "FROM CurseKaart c " +
                     "INNER JOIN Effect e " +
                     "ON c.effectID = e.effectID " +
                     "LEFT JOIN Ras r " +
                     "ON e.beperkingID = r.rasID")) {
            List<CurseKaart> kaarten = new ArrayList<>();
            ResultSet rs = curseKaarten.executeQuery();
            while (rs.next()) {
                kaarten.add(maakCurseKaart(rs));
            }
            return kaarten;
        }
    }

    private CurseKaart geefCurseKaart(int id) throws SQLException {
        try (PreparedStatement curseKaart = c.prepareStatement("SELECT naam, " +
                     "waarde, " +
                     "curseKaartID, " +
                     "effect as type " +
                     "FROM CurseKaart c " +
                     "INNER JOIN Effect e " +
                     "ON c.effectID = e.effectID " +
                     "LEFT JOIN Ras r " +
                     "ON e.beperkingID = r.rasID " +
                     "WHERE curseKaartID = ?")) {
            curseKaart.setInt(1, id);
            ResultSet rs = curseKaart.executeQuery();
            rs.next();
            return maakCurseKaart(rs);


        }
    }

    private CurseKaart maakCurseKaart(ResultSet rs) throws SQLException {
        Effect effect = new Effect(EffectType.valueOf(rs.getString("type")), rs.getInt("waarde"), null);
        return new CurseKaart(rs.getString("naam"), effect, rs.getInt("curseKaartID"));
    }

    private List<RasKaart> geefRasKaarten() throws SQLException {
        try (PreparedStatement rasKaarten = c.prepareStatement("SELECT naam, " +
                     "waarde, " +
                     "rasKaartID, " +
                     "effect as type, " +
                     "ras " +
                     "FROM RasKaart rk " +
                     "INNER JOIN Effect e " +
                     "ON rk.effectID = e.effectID " +
                     "INNER JOIN Ras r " +
                     "ON rk.rasID = r.rasID")) {
            List<RasKaart> kaarten = new ArrayList<>();
            ResultSet rs = rasKaarten.executeQuery();
            while (rs.next()) {
                kaarten.add(maakRasKaart(rs));
            }
            return kaarten;
        }
    }

    private RasKaart geefRasKaart(int id) throws SQLException {
        try (PreparedStatement rasKaart = c.prepareStatement("SELECT naam, " +
                     "waarde, " +
                     "rasKaartID, " +
                     "effect as type, " +
                     "ras " +
                     "FROM RasKaart rk " +
                     "INNER JOIN Effect e " +
                     "ON rk.effectID = e.effectID " +
                     "INNER JOIN Ras r " +
                     "ON rk.rasID = r.rasID " +
                     "WHERE rasKaartID = ?")) {
            rasKaart.setInt(1, id);
            ResultSet rs = rasKaart.executeQuery();
            rs.next();
            return maakRasKaart(rs);

        }
    }

    private RasKaart maakRasKaart(ResultSet rs) throws SQLException {
        Effect effect = new Effect(EffectType.valueOf(rs.getString("type")), rs.getInt("waarde"), null);
        return new RasKaart(rs.getString("naam"), effect, Ras.valueOf(rs.getString("ras")), rs.getInt("rasKaartID"));
    }

    private List<ConsumableKerkerKaart> geefConsumableKerkerKaarten() throws SQLException {
        try (PreparedStatement consumableKaarten = c.prepareStatement("SELECT naam, " +
                     "waarde, " +
                     "consumableKerkerKaartID, " +
                     "effect as type " +
                     "FROM ConsumableKerkerKaart c " +
                     "INNER JOIN Effect e " +
                     "ON c.effectID = e.effectID")) {
            List<ConsumableKerkerKaart> kaarten = new ArrayList<>();
            ResultSet rs = consumableKaarten.executeQuery();
            while (rs.next()) {
                kaarten.add(maakConsumableKerkerKaart(rs));
            }
            return kaarten;
        }
    }

    private ConsumableKerkerKaart geefConsumableKerkerKaart(int id) throws SQLException {
        try (PreparedStatement consumableKerkerKaart = c.prepareStatement("SELECT naam, " +
                     "waarde, " +
                     "consumableKerkerKaartID, " +
                     "effect as type " +
                     "FROM ConsumableKerkerKaart c " +
                     "INNER JOIN Effect e " +
                     "ON c.effectID = e.effectID " +
                     "WHERE consumableKerkerKaartID = ?")) {
            consumableKerkerKaart.setInt(1, id);
            ResultSet rs = consumableKerkerKaart.executeQuery();
            rs.next();
            return maakConsumableKerkerKaart(rs);


        }
    }

    private ConsumableKerkerKaart maakConsumableKerkerKaart(ResultSet rs) throws SQLException {
        Effect effect = new Effect(EffectType.valueOf(rs.getString("type")), rs.getInt("waarde"), null);
        return new ConsumableKerkerKaart(rs.getString("naam"), effect, rs.getInt("consumableKerkerKaartID"));
    }

    public List<SchatKaart> geefSchatKaarten() {
        List<SchatKaart> schatKaarten = new ArrayList<>();
        try {
            schatKaarten.addAll(geefEquipmentKaarten());
            schatKaarten.addAll(geefConsumableSchatKaarten());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return schatKaarten;
    }

    private List<EquipmentKaart> geefEquipmentKaarten() throws SQLException {
        try (PreparedStatement equipmentKaarten = c.prepareStatement("SELECT naam, " +
                     "effect.waarde as effectWaarde, " +
                     "effect.effect as effectType, " +
                     "e.waarde, " +
                     "equipmentKaartID, " +
                     "type, " +
                     "ras " +
                     "FROM EquipmentKaart e " +
                     "INNER JOIN Effect effect " +
                     "ON e.effectID = effect.effectID " +
                     "LEFT JOIN Ras r " +
                     "ON effect.beperkingID = r.rasID")) {
            List<EquipmentKaart> kaarten = new ArrayList<>();
            ResultSet rs = equipmentKaarten.executeQuery();
            while (rs.next()) {
                kaarten.add(maakEquipmentKaart(rs));
            }
            return kaarten;
        }
    }

    private EquipmentKaart geefEquipmentKaart(int id) throws SQLException {
        try (PreparedStatement equipmentKaart = c.prepareStatement("SELECT naam, " +
                     "effect.waarde as effectWaarde, " +
                     "effect.effect as effectType, " +
                     "e.waarde, " +
                     "equipmentKaartID, " +
                     "type, " +
                     "ras " +
                     "FROM EquipmentKaart e " +
                     "INNER JOIN Effect effect " +
                     "ON e.effectID = effect.effectID " +
                     "LEFT JOIN Ras r " +
                     "ON effect.beperkingID = r.rasID " +
                     "WHERE equipmentKaartID = ?")) {
            equipmentKaart.setInt(1, id);
            ResultSet rs = equipmentKaart.executeQuery();
            rs.next();
            return maakEquipmentKaart(rs);

        }
    }

    private EquipmentKaart maakEquipmentKaart(ResultSet rs) throws SQLException {
        Ras beperking = null;
        if (rs.getString("ras") != null) {
            beperking = Ras.valueOf(rs.getString("ras"));
        }
        Effect effect = new Effect(EffectType.valueOf(rs.getString("effectType")), rs.getInt("effectWaarde"), beperking);
        return new EquipmentKaart(rs.getString("naam"), effect, rs.getInt("equipmentKaartID"), rs.getInt("waarde"), EquipmentType.valueOf(rs.getString("type")));
    }

    private List<ConsumableSchatKaart> geefConsumableSchatKaarten() throws SQLException {
        try (PreparedStatement consumableKaarten = c.prepareStatement("SELECT naam, " +
                     "e.waarde as effectWaarde, " +
                     "consumableSchatKaartID, " +
                     "effect, " +
                     "c.waarde, " +
                     "beideKanten " +
                     "FROM ConsumableSchatKaart c " +
                     "INNER JOIN Effect e " +
                     "ON c.effectID = e.effectID")) {
            List<ConsumableSchatKaart> kaarten = new ArrayList<>();
            ResultSet rs = consumableKaarten.executeQuery();
            while (rs.next()) {
                kaarten.add(maakConsumableSchatKaart(rs));
            }
            return kaarten;
        }
    }

    private ConsumableSchatKaart geefConsumableSchatKaart(int id) throws SQLException {
        try (PreparedStatement consumableSchatKaart = c.prepareStatement("SELECT naam, " +
                     "e.waarde as effectWaarde, " +
                     "consumableSchatKaartID, " +
                     "effect, " +
                     "c.waarde, " +
                     "beideKanten " +
                     "FROM ConsumableSchatKaart c " +
                     "INNER JOIN Effect e " +
                     "ON c.effectID = e.effectID " +
                     "WHERE consumableSchatKaartID = ?")) {
            consumableSchatKaart.setInt(1, id);
            ResultSet rs = consumableSchatKaart.executeQuery();
            rs.next();
            return maakConsumableSchatKaart(rs);

        }
    }

    private ConsumableSchatKaart maakConsumableSchatKaart(ResultSet rs) throws SQLException {
        Effect effect = new Effect(EffectType.valueOf(rs.getString("effect")), rs.getInt("effectWaarde"), null);
        return new ConsumableSchatKaart(rs.getString("naam"), effect, rs.getInt("waarde"), rs.getBoolean("beideKanten"), rs.getInt("consumableSchatKaartID"));
    }

    public void slaHandKaartenOp(List<Kaart> handKaarten, int spelerId) {
        if (!handKaarten.isEmpty()) {
            try (PreparedStatement voegKaartToe = c.prepareStatement("INSERT INTO HandKaart (spelerID, kaartID) VALUES (?,?)")) {
                for (Kaart kaart : handKaarten) {
                    voegKaartToe.setInt(1, spelerId);
                    voegKaartToe.setInt(2, geefKaartId(kaart));
                    voegKaartToe.executeUpdate();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void slaGedragenKaartenOp(List<EquipmentKaart> gedragenKaarten, int spelerId) {
        if (!gedragenKaarten.isEmpty()) {
            try (PreparedStatement voegKaartToe = c.prepareStatement("INSERT INTO GedragenKaart (spelerID, equipmentKaartID) VALUES (?,?)")) {
                for (Kaart kaart : gedragenKaarten) {
                    voegKaartToe.setInt(1, spelerId);
                    voegKaartToe.setInt(2, geefKaartId(kaart));
                    voegKaartToe.executeUpdate();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void slaGespeeldeKaartenOp(List<Kaart> gespeeldeKaarten, int spelId) {
        if (!gespeeldeKaarten.isEmpty()) {
            try (PreparedStatement voegKaartToe = c.prepareStatement("INSERT INTO GespeeldeKaart (spelID, kaartID) VALUES (?,?)")) {
                for (Kaart kaart : gespeeldeKaarten) {
                    voegKaartToe.setInt(1, spelId);
                    voegKaartToe.setInt(2, geefKaartId(kaart));
                    voegKaartToe.executeUpdate();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private int geefKaartId(Kaart kaart) {
        int kaartId = -1;
        String kaartType;
        if (kaart instanceof ConsumableKerkerKaart) {
            kaartType = "consumableKerker";
        } else if (kaart instanceof ConsumableSchatKaart) {
            kaartType = "consumableSchat";
        } else if (kaart instanceof CurseKaart) {
            kaartType = "curse";
        } else if (kaart instanceof EquipmentKaart) {
            kaartType = "equipment";
        } else if (kaart instanceof MonsterKaart) {
            kaartType = "monster";
        } else {
            kaartType = "ras";
        }
        String sql = "SELECT kaartID FROM Kaart WHERE " + kaartType + "KaartID = ?";
        try (Connection c = DriverManager.getConnection(Connectie.JDBC_URL);
             PreparedStatement geefKaart = c.prepareStatement("SELECT ID FROM " +
                     "(SELECT consumableKerkerKaartID as ID, naam " +
                     "FROM ConsumableKerkerKaart " +
                     "UNION " +
                     "SELECT consumableSchatKaartID, naam " +
                     "FROM ConsumableSchatKaart " +
                     "UNION " +
                     "SELECT curseKaartID, naam " +
                     "FROM CurseKaart " +
                     "UNION " +
                     "SELECT equipmentKaartID, naam " +
                     "FROM EquipmentKaart " +
                     "UNION " +
                     "SELECT monsterKaartID, naam " +
                     "FROM MonsterKaart " +
                     "UNION " +
                     "SELECT rasKaartID, naam " +
                     "FROM RasKaart) " +
                     "as kaarten WHERE naam = ? AND ID = ?");
             PreparedStatement geefKaartId = c.prepareStatement(sql)) {
            geefKaart.setString(1, kaart.getNaam());
            geefKaart.setInt(2, kaart.getId());
            ResultSet rs = geefKaart.executeQuery();
            rs.next();
            int id = rs.getInt("ID");

            geefKaartId.setInt(1, id);
            rs = geefKaartId.executeQuery();
            rs.next();
            kaartId = rs.getInt("kaartID");


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return kaartId;
    }

    public List<Kaart> geefHandKaarten(int spelId, String naam) {
        List<Kaart> handKaarten = new ArrayList<>();
        try (PreparedStatement geefHandKaarten = c.prepareStatement("SELECT consumableKerkerKaartID, " +
                     "consumableSchatKaartID, " +
                     "curseKaartID, " +
                     "equipmentKaartID, " +
                     "monsterKaartID, " +
                     "rasKaartID " +
                     "FROM Kaart " +
                     "INNER JOIN HandKaart on Kaart.kaartID = HandKaart.kaartID " +
                     "INNER JOIN Speler on HandKaart.spelerID = Speler.spelerID " +
                     "WHERE spelID = ? AND Speler.naam = ?")
        ) {
            geefHandKaarten.setInt(1, spelId);
            geefHandKaarten.setString(2, naam);
            ResultSet rs = geefHandKaarten.executeQuery();
            while (rs.next()) {
                handKaarten.add(geefKaart(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return handKaarten;
    }

    private Kaart geefKaart(ResultSet rs) throws SQLException {
        if (rs.getInt("consumableKerkerKaartID") != 0) {
            return geefConsumableKerkerKaart(rs.getInt("consumableKerkerKaartID"));
        } else if (rs.getInt("consumableSchatKaartID") != 0) {
            return geefConsumableSchatKaart(rs.getInt("consumableSchatKaartID"));
        } else if (rs.getInt("curseKaartID") != 0) {
            return geefCurseKaart(rs.getInt("curseKaartID"));
        } else if (rs.getInt("equipmentKaartID") != 0) {
            return geefEquipmentKaart(rs.getInt("equipmentKaartID"));
        } else if (rs.getInt("monsterKaartID") != 0) {
            return geefMonsterKaart(rs.getInt("monsterKaartID"));
        } else
            return geefRasKaart(rs.getInt("rasKaartID"));
    }

    public List<EquipmentKaart> geefGedragenKaarten(int spelId, String naam) {
        List<EquipmentKaart> gedragenKaarten = new ArrayList<>();
        try (PreparedStatement geefGedragenKaarten = c.prepareStatement("SELECT equipmentKaartID " +
                     "FROM GedragenKaart " +
                     "INNER JOIN Speler ON GedragenKaart.spelerID = Speler.spelerID " +
                     "WHERE spelID = ? AND Speler.naam = ?")) {
            geefGedragenKaarten.setInt(1, spelId);
            geefGedragenKaarten.setString(2, naam);
            ResultSet rs = geefGedragenKaarten.executeQuery();
            while (rs.next()) {
                gedragenKaarten.add(geefEquipmentKaart(rs.getInt("equipmentKaartID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gedragenKaarten;
    }

    public List<Kaart> geefGespeeldeKaarten(int spelId) {
        List<Kaart> gespeeldeKaarten = new ArrayList<>();
        try (PreparedStatement geefHandKaarten = c.prepareStatement("SELECT consumableKerkerKaartID, " +
                     "consumableSchatKaartID, " +
                     "curseKaartID, " +
                     "equipmentKaartID, " +
                     "monsterKaartID, " +
                     "rasKaartID " +
                     "FROM Kaart " +
                     "INNER JOIN HandKaart on Kaart.kaartID = HandKaart.kaartID " +
                     "INNER JOIN Speler on HandKaart.spelerID = Speler.spelerID " +
                     "WHERE spelID = ?")
        ) {
            geefHandKaarten.setInt(1, spelId);
            ResultSet rs = geefHandKaarten.executeQuery();
            while (rs.next()) {
                gespeeldeKaarten.add(geefKaart(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gespeeldeKaarten;
    }
}
