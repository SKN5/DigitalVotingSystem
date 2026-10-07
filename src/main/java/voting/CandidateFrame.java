package voting;

import javax.swing.*;
import java.awt.*;

public class CandidateFrame extends JFrame {

    public CandidateFrame(VotingSystem system, User user) {
        setTitle("Digital Voting System - Candidate");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel(
                "CANDIDATE DASHBOARD - " + user.getName(),
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JTextArea results = new JTextArea();
        results.setEditable(false);

        StringBuilder text = new StringBuilder("CURRENT RESULTS\n\n");
        int rank = 1;

        for (Candidate c : system.getCandidates()) {
            text.append(rank++).append(". ")
                    .append(c.getName()).append(" - ")
                    .append(c.getVotes()).append(" votes\n");
        }

        results.setText(text.toString());

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame(system);
        });

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(results), BorderLayout.CENTER);
        add(logout, BorderLayout.SOUTH);

        setVisible(true);
    }
}
