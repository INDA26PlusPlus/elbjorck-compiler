import java.util.ArrayList;

public class Block {

    private String name;

    private ArrayList<Token> tokens;

    public Block(String name) {
        this.name = name;
        tokens = new ArrayList<>();
    }

    public void setName(String n) {
        this.name = n;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public String getName() {
        return this.name;
    }

    public void run() {
        for (Token token : tokens) {
            token.run();
        }
    }
}
