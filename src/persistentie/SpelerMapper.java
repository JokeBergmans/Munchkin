package persistentie;

import domein.Geslacht;
import domein.Ras;
import domein.Spel;
import domein.Speler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpelerMapper {

    private Connection c;

    public SpelerMapper() {
        try {
            c = DriverManager.getConnection(Connectie.JDBC_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    List<Speler> geefSpelers(int spelId, Spel spel) {
        List<Speler> spelers = new ArrayList<>();
        try (PreparedStatement geefSpelers = c.prepareStatement("SELECT naam, geslacht, niveau, ras FROM Speler INNER JOIN Ras on Speler.rasID = Ras.rasID WHERE spelID = ?")) {
            geefSpelers.setInt(1, spelId);
            ResultSet rs = geefSpelers.executeQuery();
            while (rs.next()) {
                Speler speler = new Speler(spel, rs.getString("naam"), Geslacht.valueOf(rs.getString("geslacht")),rs.getInt("niveau"), Ras.valueOf(rs.getString("ras")));
                spelers.add(speler);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spelers;
    }

    int slaSpelerOp(Speler s, int spelId) {
        int rasId = geefRasId(s.getRas());
        int spelerId = -1;
        try (PreparedStatement voegSpelerToe = c.prepareStatement("INSERT INTO Speler (naam, geslacht, niveau, rasID, spelID) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            voegSpelerToe.setString(1, s.getNaam());
            voegSpelerToe.setString(2, s.getGeslacht().toString());
            voegSpelerToe.setInt(3, s.getNiveau());
            voegSpelerToe.setInt(4, rasId);
            voegSpelerToe.setInt(5, spelId);
            voegSpelerToe.executeUpdate();
            ResultSet keys = voegSpelerToe.getGeneratedKeys();
            if (keys.next()) {
                spelerId = keys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spelerId;
    }

    private int geefRasId(Ras ras) {
        int id = -1;
        try (PreparedStatement rasId = c.prepareStatement("SELECT rasID FROM Ras WHERE ras = ?")) {
            rasId.setString(1, ras.toString());
            ResultSet rs = rasId.executeQuery();
            rs.next();
            id = rs.getInt("rasID");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
