package voting;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VotingSystem system = new VotingSystem();
            system.initDatabase();
            new LoginFrame(system);
        });
    }
}
