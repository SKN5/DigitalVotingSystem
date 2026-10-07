package voting;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {

    private VotingSystem system;
    private JTextArea output;

    public AdminFrame(VotingSystem system) {
        this.system = system;

        setTitle("Digital Voting System - Admin");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("ADMIN DASHBOARD", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel buttons = new JPanel(new GridLayout(1, 5, 8, 8));

        JButton candidate = new JButton("Add Candidate");
        JButton voter = new JButton("Add Voter");
        JButton results = new JButton("Results");
        JButton audit = new JButton("Audit Log");
        JButton logout = new JButton("Logout");

        buttons.add(candidate);
        buttons.add(voter);
        buttons.add(results);
        buttons.add(audit);
        buttons.add(logout);

        output = new JTextArea();
        output.setEditable(false);

        add(title, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);
        add(new JScrollPane(output), BorderLayout.SOUTH);

        candidate.addActionListener(e -> addCandidate());
        voter.addActionListener(e -> addVoter());
        results.addActionListener(e -> showResults());
        audit.addActionListener(e -> showAudit());

        logout.addActionListener(e -> {
            dispose();
            new LoginFrame(system);
        });

        setVisible(true);
    }

    private void addCandidate() {
        JTextField name = new JTextField();
        JTextField party = new JTextField();

        Object[] fields = {"Candidate name:", name, "Party:", party};

        if (JOptionPane.showConfirmDialog(this, fields, "Add Candidate",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                system.addCandidate(name.getText(), party.getText());
                JOptionPane.showMessageDialog(this, "Candidate added.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Could not add candidate.");
            }
        }
    }

    private void addVoter() {
        JTextField name = new JTextField();
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        Object[] fields = {
                "Name:", name,
                "Username:", username,
                "Password:", password
        };

        if (JOptionPane.showConfirmDialog(this, fields, "Add Voter",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                system.addVoter(username.getText(),
                        new String(password.getPassword()), name.getText());
                JOptionPane.showMessageDialog(this, "Voter added.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Username may already exist.");
            }
        }
    }

    private void showResults() {
        StringBuilder text = new StringBuilder();

        text.append("TOTAL VOTES: ").append(system.getTotalVotes()).append("\n");
        text.append("TOTAL VOTERS: ").append(system.getVoterCount()).append("\n\n");
        text.append("RESULTS\n-------------------------\n");

        int rank = 1;
        for (Candidate c : system.getCandidates()) {
            text.append(rank++).append(". ")
                    .append(c.getName()).append(" - ")
                    .append(c.getVotes()).append(" votes\n");
        }

        output.setText(text.toString());
    }

    private void showAudit() {
        StringBuilder text = new StringBuilder("AUDIT LOG\n-------------------------\n");

        for (String[] row : system.getAuditLog()) {
            text.append(row[2]).append(" | ")
                    .append(row[0]).append(" | ")
                    .append(row[1]).append("\n");
        }

        output.setText(text.toString());
    }
}
