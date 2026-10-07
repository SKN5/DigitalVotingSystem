package voting;

public class Candidate {
    private int id;
    private String name;
    private String party;
    private int votes;

    public Candidate(int id, String name, String party, int votes) {
        this.id = id;
        this.name = name;
        this.party = party;
        this.votes = votes;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getParty() { return party; }
    public int getVotes() { return votes; }

    @Override
    public String toString() {
        return name + " - " + party;
    }
}
