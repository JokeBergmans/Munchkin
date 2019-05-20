package persistentie;

import domein.Spel;
import domein.Speler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpelMapper {

    private Connection c;

    public SpelMapper() {
        try {
            c = DriverManager.getConnection(Connectie.JDBC_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> geefSpelnamen() {
        List<String> namen = new ArrayList<>();
        try (PreparedStatement spelnamen = c.prepareStatement("SELECT naam FROM Spel")) {
            ResultSet rs = spelnamen.executeQuery();
            while (rs.next()) {
                namen.add(rs.getString("naam"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return namen;
    }

    public int slaSpelOp(Spel spel) {
        int spelId = -1;
        if (geefSpelnamen().contains(spel.getNaam())) {
            verwijderSpel(spel);
        }
        try (PreparedStatement voegSpelToe = c.prepareStatement("INSERT INTO Spel (naam) VALUE (?)", Statement.RETURN_GENERATED_KEYS)) {
            voegSpelToe.setString(1, spel.getNaam());
            voegSpelToe.executeUpdate();
            ResultSet keys = voegSpelToe.getGeneratedKeys();
            if (keys.next()) {
                spelId = keys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spelId;
    }

    public void verwijderSpel(Spel spel) {
        try (PreparedStatement geefSpelId = c.prepareStatement("SELECT spelID FROM Spel WHERE naam = ?");
             PreparedStatement geefSpelerIds = c.prepareStatement("SELECT spelerID FROM Speler WHERE spelID = ?");
             PreparedStatement verwijderSpeler = c.prepareStatement("DELETE FROM Speler WHERE spelerID = ?");
             PreparedStatement verwijderHandkaarten = c.prepareStatement("DELETE FROM HandKaart WHERE spelerID = ?");
             PreparedStatement verwijderGedragenKaarten = c.prepareStatement("DELETE FROM GedragenKaart WHERE spelerID = ?");
             PreparedStatement verwijderGespeeldeKaarten = c.prepareStatement("DELETE FROM GespeeldeKaart WHERE spelID = ?");
             PreparedStatement verwijderSpel = c.prepareStatement("DELETE FROM Spel WHERE spelID = ?")) {
            geefSpelId.setString(1, spel.getNaam());
            ResultSet rs = geefSpelId.executeQuery();
            rs.next();
            int spelId = rs.getInt("spelID");

            geefSpelerIds.setInt(1, spelId);
            rs = geefSpelerIds.executeQuery();
            while (rs.next()) {
                int spelerId = rs.getInt("spelerID");
                verwijderHandkaarten.setInt(1, spelerId);
                verwijderHandkaarten.executeUpdate();
                verwijderGedragenKaarten.setInt(1, spelerId);
                verwijderGedragenKaarten.executeUpdate();
                verwijderSpeler.setInt(1, spelerId);
                verwijderSpeler.executeUpdate();
            }
            verwijderGespeeldeKaarten.setInt(1, spelId);
            verwijderGespeeldeKaarten.executeUpdate();
            verwijderSpel.setInt(1, spelId);
            verwijderSpel.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void zetHuidigeSpeler(Speler huidigeSpeler, int spelId) {
        try (PreparedStatement geefHuidigeSpeler = c.prepareStatement("SELECT spelerID FROM Speler WHERE naam = ? AND spelID = ?");
             PreparedStatement zetHuidigeSpeler = c.prepareStatement("UPDATE Spel SET huidigeSpelerID = ? WHERE spelID = ?")) {
            geefHuidigeSpeler.setString(1, huidigeSpeler.getNaam());
            geefHuidigeSpeler.setInt(2, spelId);
            ResultSet rs = geefHuidigeSpeler.executeQuery();
            rs.next();
            int spelerId = rs.getInt("spelerID");
            zetHuidigeSpeler.setInt(1, spelerId);
            zetHuidigeSpeler.setInt(2, spelId);
            zetHuidigeSpeler.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<Spel> geefSpellen() {
        List<Spel> spellen = new ArrayList<>();
        try (PreparedStatement geefSpellen = c.prepareStatement("SELECT spelID, naam FROM Spel");
             PreparedStatement geefAantalSpelers = c.prepareStatement("SELECT COUNT(spelerID) FROM Speler WHERE spelID = ?")) {
            ResultSet rs = geefSpellen.executeQuery();
            while (rs.next()) {
                int spelId = rs.getInt("spelID");
                String naam = rs.getString("naam");
                geefAantalSpelers.setInt(1, spelId);
                ResultSet rs2 = geefAantalSpelers.executeQuery();
                rs2.next();
                int aantalSpelers = rs2.getInt(1);
                spellen.add(new Spel(aantalSpelers, naam));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spellen;
    }

    public int geefSpelId(String naam) {
        int spelId = -1;
        try (PreparedStatement geefSpelId = c.prepareStatement("SELECT spelID FROM Spel WHERE naam = ?")) {
            geefSpelId.setString(1, naam);
            ResultSet rs = geefSpelId.executeQuery();
            rs.next();
            spelId = rs.getInt("spelID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spelId;
    }

    public String geefHuidigeSpeler(int spelId) {
        String huidigeSpeler = "";
        try (PreparedStatement geefHuidigeSpeler = c.prepareStatement("SELECT Speler.naam FROM Speler INNER JOIN Spel on Speler.spelerID = Spel.huidigeSpelerID WHERE Spel.spelID = ?")) {
            geefHuidigeSpeler.setInt(1, spelId);
            ResultSet rs = geefHuidigeSpeler.executeQuery();

            if (rs.next()) {
                huidigeSpeler = rs.getString("naam");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return huidigeSpeler;
    }
}
