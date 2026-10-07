package voting;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private VotingSystem system;
    private JTextField username;
    private JPasswordField password;
    private JComboBox<String> role;

    public LoginFrame(VotingSystem system) {
        this.system = system;

        setTitle("Digital Voting System - DEMO");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel box = new JPanel(new GridLayout(0, 1, 8, 8));

        JLabel title = new JLabel("DIGITAL VOTING SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel demo = new JLabel("DEMO - EDUCATIONAL PROJECT", SwingConstants.CENTER);
        demo.setForeground(Color.RED);

        username = new JTextField();
        password = new JPasswordField();
        role = new JComboBox<>(new String[]{"ADMIN", "CANDIDATE", "VOTER"});

        JButton login = new JButton("LOGIN");

        box.add(title);
        box.add(demo);
        box.add(new JLabel("Username"));
        box.add(username);
        box.add(new JLabel("Password"));
        box.add(password);
        box.add(new JLabel("Role"));
        box.add(role);
        box.add(login);

        add(box, BorderLayout.CENTER);

        login.addActionListener(e -> login());
        setVisible(true);
    }

    private void login() {
        User user = system.auth(
                username.getText().trim(),
                new String(password.getPassword()),
                role.getSelectedItem().toString()
        );

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid login details.");
            return;
        }

        dispose();

        if (user.getRole().equals("ADMIN")) {
            new AdminFrame(system);
        } else if (user.getRole().equals("CANDIDATE")) {
            new CandidateFrame(system, user);
        } else {
            new VoterFrame(system, user);
        }
    }
}
