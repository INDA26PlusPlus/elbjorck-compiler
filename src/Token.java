
public class Token {

    public static enum TokenType {
        PUSH, ADD, POP, RUN, NULL
    }

    private Object value;
    private TokenType type;
    private Compiler compiler;

    public Token(String text, Compiler compiler) {

        type = TokenType.NULL;
        this.compiler = compiler;

        // Gör text till token

        String token = "";

        for (int i = 0; i < text.length(); i++) {

            token += text.charAt(i);

            switch (token) {
                case "Push" -> {
                    type = TokenType.PUSH;
                    value = 0;
                    setValuePush(text);
                }

                case "Add" -> type = TokenType.ADD;

                case "Pop" -> type = TokenType.POP;

                case "Run" -> {
                    type = TokenType.RUN;
                    value = "";
                    setValueRun(text);
                }

                default -> type = TokenType.NULL; // Null token exception
            }

            if (type != TokenType.NULL) {
                break;
            }
        }

    }

    private void setValuePush(String text) {

        System.out.println("Setting value for push token from \"" + text + "\"");

        value = Integer.parseInt(text.substring(text.indexOf('(') + 1, text.indexOf(')')));
    }

    private void setValueRun(String text) {

        System.out.println("Setting value for run token from \"" + text + "\"");

        value = text.substring(text.indexOf('(') + 1, text.indexOf(')'));
    }

    public TokenType getType() {
        return type;
    }

    public boolean run() {
        System.out.println("Running " + type + " token");

        switch (type) {

            case TokenType.PUSH -> {
                if (value instanceof Integer i) {
                    compiler.push(i);
                }
                return true;
            }

            case TokenType.ADD -> {
                compiler.add();
                return true;
            }

            case TokenType.POP -> {
                compiler.pop();
                return true;
            }

            case TokenType.RUN -> {
                if (value instanceof String s) {
                    compiler.runBlock(s);
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "(type: " + type + ", " + value + ")";
    }

}
