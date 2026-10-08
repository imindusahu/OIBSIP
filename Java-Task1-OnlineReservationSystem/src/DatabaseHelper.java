import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:reservation.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void init() {
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "username TEXT PRIMARY KEY, password TEXT NOT NULL)");
            s.execute("CREATE TABLE IF NOT EXISTS trains (" +
                    "train_no INTEGER PRIMARY KEY, train_name TEXT NOT NULL)");
            s.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                    "pnr INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "passenger_name TEXT NOT NULL, train_no INTEGER NOT NULL, " +
                    "train_name TEXT NOT NULL, class_type TEXT NOT NULL, " +
                    "journey_date TEXT NOT NULL, source TEXT NOT NULL, " +
                    "destination TEXT NOT NULL)");

            // PNR 6 digit se start ho (100001, 100002, ...)
            s.execute("INSERT INTO sqlite_sequence(name, seq) SELECT 'reservations', 100000 " +
                    "WHERE NOT EXISTS (SELECT 1 FROM sqlite_sequence WHERE name='reservations')");

            s.execute("INSERT OR IGNORE INTO users VALUES ('admin', 'admin123')");
            s.execute("INSERT OR IGNORE INTO users VALUES ('user', 'user123')");

            s.execute("INSERT OR IGNORE INTO trains VALUES (12301, 'Howrah Rajdhani Express')");
            s.execute("INSERT OR IGNORE INTO trains VALUES (12951, 'Mumbai Rajdhani Express')");
            s.execute("INSERT OR IGNORE INTO trains VALUES (12002, 'Bhopal Shatabdi Express')");
            s.execute("INSERT OR IGNORE INTO trains VALUES (12260, 'Sealdah Duronto Express')");
            s.execute("INSERT OR IGNORE INTO trains VALUES (12009, 'Shatabdi Express')");
            s.execute("INSERT OR IGNORE INTO trains VALUES (22416, 'Vande Bharat Express')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateLogin(String user, String pass) {
        String sql = "SELECT 1 FROM users WHERE username = ? AND password = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getTrainName(int trainNo) {
        String sql = "SELECT train_name FROM trains WHERE train_no = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, trainNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Booking save karta hai aur generated PNR return karta hai (-1 on failure). */
    public static long bookTicket(String name, int trainNo, String trainName,
                                  String classType, String date, String src, String dest) {
        String sql = "INSERT INTO reservations " +
                "(passenger_name, train_no, train_name, class_type, journey_date, source, destination) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setInt(2, trainNo);
            ps.setString(3, trainName);
            ps.setString(4, classType);
            ps.setString(5, date);
            ps.setString(6, src);
            ps.setString(7, dest);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /** PNR se booking fetch karta hai; na mile to null. */
    public static String[] getBooking(long pnr) {
        String sql = "SELECT * FROM reservations WHERE pnr = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, pnr);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{
                            String.valueOf(rs.getLong("pnr")),
                            rs.getString("passenger_name"),
                            String.valueOf(rs.getInt("train_no")),
                            rs.getString("train_name"),
                            rs.getString("class_type"),
                            rs.getString("journey_date"),
                            rs.getString("source"),
                            rs.getString("destination")
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean cancelBooking(long pnr) {
        String sql = "DELETE FROM reservations WHERE pnr = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, pnr);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
