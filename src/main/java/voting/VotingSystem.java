package voting;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VotingSystem {

    private static final String URL = "jdbc:sqlite:voting.db";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public void initDatabase() {
        String users = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL," +
                "full_name TEXT NOT NULL)";

        String candidates = "CREATE TABLE IF NOT EXISTS candidates (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "party TEXT NOT NULL)";

        String votes = "CREATE TABLE IF NOT EXISTS votes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "voter_id INTEGER UNIQUE NOT NULL," +
                "candidate_id INTEGER NOT NULL," +
                "voted_at TEXT DEFAULT CURRENT_TIMESTAMP)";

        String audit = "CREATE TABLE IF NOT EXISTS audit_log (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL," +
                "action TEXT NOT NULL," +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP)";

        try (Connection con = connect(); Statement st = con.createStatement()) {
            st.execute(users);
            st.execute(candidates);
            st.execute(votes);
            st.execute(audit);

            addUser(con, "admin", "admin123", "ADMIN", "System Admin");
            addUser(con, "candidate", "candidate123", "CANDIDATE", "Arun Kumar");
            addUser(con, "voter", "voter123", "VOTER", "Anu Thomas");

            addCandidate(con, "Arun Kumar", "Independent");
            addCandidate(con, "Meera Nair", "Student Group");
            addCandidate(con, "Rahul Das", "Progressive Group");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addUser(Connection con, String username, String password,
                          String role, String name) throws SQLException {
        String sql = "INSERT OR IGNORE INTO users(username,password,role,full_name) VALUES(?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, Security.hash(password));
            ps.setString(3, role);
            ps.setString(4, name);
            ps.executeUpdate();
        }
    }

    private void addCandidate(Connection con, String name, String party) throws SQLException {
        String sql = "INSERT INTO candidates(name,party) " +
                "SELECT ?,? WHERE NOT EXISTS (SELECT 1 FROM candidates WHERE name=?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, party);
            ps.setString(3, name);
            ps.executeUpdate();
        }
    }

    public User auth(String username, String password, String role) {
        String sql = "SELECT id,username,full_name,role FROM users " +
                "WHERE username=? AND password=? AND role=?";

        try (Connection con = connect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, Security.hash(password));
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("full_name"), rs.getString("role"));
                log(username, "LOGIN");
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Candidate> getCandidates() {
        List<Candidate> list = new ArrayList<>();

        String sql = "SELECT c.id,c.name,c.party,COUNT(v.id) AS votes " +
                "FROM candidates c LEFT JOIN votes v ON c.id=v.candidate_id " +
                "GROUP BY c.id ORDER BY votes DESC,c.id";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Candidate(rs.getInt("id"), rs.getString("name"),
                        rs.getString("party"), rs.getInt("votes")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean hasVoted(int voterId) {
        String sql = "SELECT id FROM votes WHERE voter_id=?";

        try (Connection con = connect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, voterId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean vote(int voterId, int candidateId) {
        try (Connection con = connect()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO votes(voter_id,candidate_id) VALUES(?,?)")) {

                ps.setInt(1, voterId);
                ps.setInt(2, candidateId);
                ps.executeUpdate();

                con.commit();
                log(String.valueOf(voterId), "VOTE CAST");
                return true;
            } catch (SQLException e) {
                con.rollback();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addCandidate(String name, String party) throws SQLException {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO candidates(name,party) VALUES(?,?)")) {

            ps.setString(1, name);
            ps.setString(2, party);
            ps.executeUpdate();
        }

        log("admin", "ADDED CANDIDATE: " + name);
    }

    public void addVoter(String username, String password, String name) throws SQLException {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO users(username,password,role,full_name) VALUES(?,?,?,?)")) {

            ps.setString(1, username);
            ps.setString(2, Security.hash(password));
            ps.setString(3, "VOTER");
            ps.setString(4, name);
            ps.executeUpdate();
        }

        log("admin", "ADDED VOTER: " + username);
    }

    public int getTotalVotes() {
        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM votes")) {
            return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }

    public int getVoterCount() {
        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users WHERE role='VOTER'")) {
            return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }

    public List<String[]> getAuditLog() {
        List<String[]> list = new ArrayList<>();

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT username,action,created_at FROM audit_log ORDER BY id DESC")) {

            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("username"),
                        rs.getString("action"),
                        rs.getString("created_at")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private void log(String username, String action) {
        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO audit_log(username,action) VALUES(?,?)")) {

            ps.setString(1, username);
            ps.setString(2, action);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
