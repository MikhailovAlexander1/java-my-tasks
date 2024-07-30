package lab2.parser;

import lab2.tokenizer.*;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Map;

public class Parser {
    private final LexicalAnalyzer analyzer;
    private final static String varNameParserExceptionMessage = "Variable name is missing.";
    private final static String typeNameParserExceptionMessage = "Type name is missing.";
    private final static String arrayKeyWordParserExceptionMessage = "Unexpected type for array, it must be \"Array\".";

    private final static Map<Token, String> messages = Map.of(
            Token.VAR, "Array declaration in Kotlin starts with \"var\" key word.",
            Token.COLON, "key \":\" must follow after Kotlin array's name.",
            Token.SEMICOLON, "Array declaration in Kotlin ends with key \";\".",
            Token.COMMA, "Suppose to be \",\".",
            Token.LESS, "Expected token is \"<\", the array type is passed with <_>.",
            Token.GREATER, "Expected token is \">\", the array type is passed with <_>.",
            Token.END, "Unexpected continuation after Kotlin array declaration.");

    private int getActualExceptionPos() {
        return analyzer.getCurPos() - analyzer.getCurTerminal().length();
    }

    private void assertToken(Token token) throws ParseException {
        if (analyzer.getCurToken() != token) {
            switch (token) {
                case NAME -> {
                    if (analyzer.getPrevToken() == Token.VAR) {
                        throw new ParseException(varNameParserExceptionMessage, getActualExceptionPos());
                    } else {
                        throw new ParseException(typeNameParserExceptionMessage, getActualExceptionPos());
                    }
                }
                default -> throw new ParseException(messages.get(token), getActualExceptionPos());
            }
        }
    }

    private void assertAndAdd(Token token, Tree tree) throws ParseException {
        assertToken(token);
        tree.addChild(new Tree(analyzer.getCurTerminal()));
        analyzer.nextToken();
    }

    private Tree E() throws ParseException {
        Tree res = new Tree("E");
        switch (analyzer.getCurToken()) {
            case VAR -> {
                res.addChild(V());
                assertAndAdd(Token.COLON, res);
                res.addChild(A());
                assertAndAdd(Token.SEMICOLON, res);
            }
            case END -> {
                return res;
            }
            default -> assertToken(Token.VAR);
        }
        assertToken(Token.END);
        return res;
    }

    private Tree V() throws ParseException {
        Tree res = new Tree("V");
        switch (analyzer.getCurToken()) {
            case VAR -> {
                assertAndAdd(Token.VAR, res);
                assertAndAdd(Token.NAME, res);
            }
            default -> assertToken(Token.VAR);
        }
        return res;
    }

    private Tree A() throws ParseException {
        Tree res = new Tree("A");
        switch (analyzer.getCurToken()) {
            case NAME -> {
                if (analyzer.getPrevToken() == Token.COLON && !analyzer.getCurTerminal().equals("Array")) {
                    throw new ParseException(arrayKeyWordParserExceptionMessage, getActualExceptionPos());
                }
                res.addChild(T());
                res.addChild(AT());
            }
            default -> assertToken(Token.NAME);
        }
        return res;
    }

    private Tree T() throws ParseException {
        Tree res = new Tree("T");
        switch (analyzer.getCurToken()) {
            case NAME -> assertAndAdd(Token.NAME, res);
            default -> assertToken(Token.NAME);
        }
        return res;
    }

    private Tree AT() throws ParseException {
        Tree res = new Tree("AT");
        switch (analyzer.getCurToken()) {
            case LESS -> {
                assertAndAdd(Token.LESS, res);
                res.addChild(TT());
                res.addChild(ATCOUNT());
                assertAndAdd(Token.GREATER, res);
            }
            default -> assertToken(Token.LESS);
        }
        return res;
    }

    private Tree ATCOUNT() throws ParseException {
        Tree res = new Tree("ATCOUNT");
        switch (analyzer.getCurToken()) {
            case COMMA -> {
                assertAndAdd(Token.COMMA, res);
                res.addChild(TT());
                res.addChild(ATCOUNT());
            }
            case GREATER -> assertToken(Token.GREATER);
            default -> assertToken(Token.COMMA);
        }
        return res;
    }

    private Tree TT() throws ParseException {
        Tree res = new Tree("TT");
        switch (analyzer.getCurToken()) {
            case NAME -> {
                res.addChild(T());
                res.addChild(TTT());
            }
            default -> assertToken(Token.NAME);
        }
        return res;
    }

    private Tree TTT() throws ParseException {
        Tree res = new Tree("TTT");
        switch (analyzer.getCurToken()) {
            case LESS -> res.addChild(AT());
            case GREATER, COMMA -> res.addChild(new Tree("ε"));
            default -> assertToken(Token.GREATER);
        }
        return res;
    }

    public Parser(InputStream is) throws ParseException {
        this.analyzer = new LexicalAnalyzer(is);
    }

    public Tree parse() throws ParseException {
        analyzer.nextToken();
        return E();
    }
}
