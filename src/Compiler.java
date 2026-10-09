
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Compiler {

    private JFrame frame;
    private JTextArea codeArea;
    private JTextArea consoleArea;

    private ArrayList<Integer> memory;
    private ArrayList<Block> codeBlocks;

    public static void main(String[] args) {
        Compiler compiler = new Compiler();
    }

    public Compiler() {

        memory = new ArrayList<>();
        codeBlocks = new ArrayList<>();

        frame = new JFrame("JPiler");
        frame.setSize(720, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        codeArea = new JTextArea(30, 80);
        codeArea.setEditable(true);

        consoleArea = new JTextArea(10, 80);
        consoleArea.setEditable(false);

        JButton runButton = new JButton("Run");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                run();
            }
        });

        frame.add(codeArea, BorderLayout.CENTER);
        frame.add(consoleArea, BorderLayout.SOUTH);
        frame.add(runButton, BorderLayout.NORTH);

        codeArea.setVisible(true);
        consoleArea.setVisible(true);
        runButton.setVisible(true);

        codeArea.setText("Block main {\n\n}");
        consoleArea.setText("Console");
    }

    public void consolePrint(String s) {
        String consoleText = consoleArea.getText();
        consoleArea.setText(consoleText + s);
    }

    public void consolePrintln(String s) {
        String consoleText = consoleArea.getText();
        consoleArea.setText(consoleText + "\n" + s);
    }

    public void clearConsole() {
        consoleArea.setText("");
    }

    public void run() {

        try {
            memory.clear();
            codeBlocks.clear();

            consolePrintln("Running code");
            compile(codeArea.getText());
            runBlock("main");
            System.out.println(memory);
        } catch (Exception e) {
            consolePrint("New error");
            consolePrintln(e.toString());
        }

    }

    // Implementera push

    public void push(int value) {
        memory.add(value);
    }

    // Implementera pop
    // Returnar senaste värdet som lades till i stacken och tar bort det ur minnet
    public int pop() {

        if (memory.size() == 0) { // Om minnet är tomt går det inte att poppa

            consolePrintln("Pop empty memory exception");
            return Integer.MAX_VALUE;

        }

        int value = memory.get(memory.size() - 1); // Hämtar värdet längst upp i stacken

        memory.remove(memory.size() - 1); // Tar bort värdet längst upp i stacken

        return value;

    }

    // Implementera add
    // poppar två värden och returnar summan
    public void add() {

        memory.add(pop() + pop());

    }

    // Implementera if equal
    // Kontrollerar om 2 värden är lika
    public boolean ifEqual(int a, int b) {

        if (a == b) {

            return true;

        } else {

            return false;

        }

    }

    // Implementera runBlock
    // kallar run på blocket med givna namnet
    public void runBlock(String name) {

        System.out.println("Trying to run block " + name);
        for (Block block : codeBlocks) { // Loopar igenom all block

            if (block.getName().equals(name)) { // Om blocket har givna namnet
                System.out.println("Running block " + name + " with tokens:");
                block.run();
                break;

            }

        }

    }

    // Implementera add block

    public void addBlock(String blockString) {
        System.out.println("Trying to add block " + blockString);

        String name = blockString; // Hämta namn på block

        for (Block block : codeBlocks) { // Kolla så att det inte finns flera block med samma namn

            if (block.getName().equals(name)) {

                this.consolePrintln("Duplicate blocks error");
                return;

            }

        }

        Block block = new Block(name);

        codeBlocks.add(block);
        codeBlocks.getLast().setName(name);

        System.out.println("Block " + blockString + " added");

    }

    // Implementera tokenizeString

    public ArrayList<Token> tokenizeBlock(String code) {

        System.out.println("Tokenizing block { " + code + "}");

        ArrayList<Token> tokens = new ArrayList<>();

        String tokenText = "";

        for (int i = 0; i < code.length(); i++) {

            if (code.charAt(i) == '\n') { // Rader slutar med ; detta kommer ge error annars :]
                continue;
            }

            if (code.charAt(i) == ' ' || code.charAt(i) == ';') { // Token slut

                if (tokenText.length() == 0) { // Om det är 2 token slut efter varandra skiter koden i
                    continue;
                }

                System.out.println("Adding new token from text \"" + tokenText + "\"");
                tokens.add(new Token(tokenText, this)); // Lägg till färdig token
                tokenText = ""; // Förbered för nytt token
                continue;

            }

            tokenText += code.charAt(i); // Bygg på token

        }

        return tokens;

    }

    private void compile(String code) {
        String token = "";

        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == '\n') {
                token = "";
                continue;
            }
            if (code.charAt(i) == ' ' || code.charAt(i) == ';') {
                if (token.equals("Block")) {

                    int blockNameStart = i + 1;
                    int blockNameEnd = code.indexOf(' ', i + 2);
                    String blockName = code.substring(blockNameStart, blockNameEnd);

                    int blockCodeStart = code.indexOf('{', i) + 1;
                    int blockCodeEnd = code.indexOf('}', i);
                    String blockCode = code.substring(blockCodeStart, blockCodeEnd);

                    addBlock(blockName);
                    System.out.println("Added code block, there are now " + codeBlocks.size());
                    codeBlocks.getLast().getTokens().addAll(tokenizeBlock(blockCode));
                    System.out.println("Added tokens to block main. It has " + codeBlocks.getLast().getTokens().size()
                            + " tokens");

                }
                continue;
            }
            token += code.charAt(i);
        }
    }
}