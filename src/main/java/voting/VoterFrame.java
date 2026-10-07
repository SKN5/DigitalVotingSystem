package voting;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VoterFrame extends JFrame {

    private VotingSystem system;
    private User user;

    public VoterFrame(VotingSystem system, User user) {
        this.system = system;
        this.user = user;

        setTitle("Digital Voting System - Voter");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel(
                "VOTER DASHBOARD - " + user.getName(),
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (system.hasVoted(user.getId())) {
            panel.add(new JLabel("You have already voted."));
        } else {
            List<Candidate> candidates = system.getCandidates();
            ButtonGroup group = new ButtonGroup();
            JRadioButton[] choices = new JRadioButton[candidates.size()];

            for (int i = 0; i < candidates.size(); i++) {
                Candidate c = candidates.get(i);

                choices[i] = new JRadioButton(c.getName() + " - " + c.getParty());
                choices[i].putClientProperty("candidateId", c.getId());

                group.add(choices[i]);
                panel.add(choices[i]);
            }

            JButton vote = new JButton("CAST VOTE");
            panel.add(vote);

            vote.addActionListener(e -> castVote(choices));
        }

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame(system);
        });

        panel.add(logout);

        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void castVote(JRadioButton[] choices) {
        int selectedId = -1;

        for (JRadioButton choice : choices) {
            if (choice.isSelected()) {
                selectedId = (int) choice.getClientProperty("candidateId");
            }
        }

        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a candidate.");
            return;
        }

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cast your vote?",
                "Confirm Vote",
                JOptionPane.YES_NO_OPTION
        );

        if (answer == JOptionPane.YES_OPTION) {
            if (system.vote(user.getId(), selectedId)) {
                JOptionPane.showMessageDialog(this, "Vote recorded successfully.");
                dispose();
                new LoginFrame(system);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Vote could not be recorded. You may have already voted."
                );
            }
        }
    }
}
