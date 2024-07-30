package lab2.tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public class LexicalAnalyzer {
    private final InputStream is;
    private int curChar;
    private String curTerminal;
    private int curPos;
    private Token prevToken;
    private Token curToken;

    public LexicalAnalyzer(InputStream is) throws ParseException {
        this.is = is;
        curPos = 0;
        nextChar();
    }

    private void nextChar() throws ParseException {
        curPos++;
        try {
            curChar = is.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), curPos);
        }
    }

    private String getVarOrName() throws ParseException {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append((char) curChar);
            nextChar();
        } while (Character.isLetter(curChar) || Character.isDigit(curChar));
        return sb.toString();
    }

    public void nextToken() throws ParseException {
        while (Character.isWhitespace(curChar)) {
            nextChar();
        }
        prevToken = curToken;
        if (Character.isLetter(curChar)) {
            String terminal = getVarOrName();
            if (terminal.equals("var")) {
                curToken = Token.VAR;
            } else {
                curToken = Token.NAME;
            }
            curTerminal = terminal;
            return;
        }
        switch (curChar) {
            case '<' -> curToken = Token.LESS;
            case '>' -> curToken = Token.GREATER;
            case ':' -> curToken = Token.COLON;
            case ';' -> curToken = Token.SEMICOLON;
            case ',' -> curToken = Token.COMMA;
            case -1 -> curToken = Token.END;
            default -> throw new ParseException("Illegal character :" + (char) curChar + ".", curPos);
        }
        curTerminal = String.valueOf((char) curChar);
        nextChar();
    }

    public Token getCurToken() {
        return curToken;
    }

    public Token getPrevToken() {
        return prevToken;
    }

    public String getCurTerminal() {
        return curTerminal;
    }

    public int getCurPos() {
        return curPos;
    }
}
