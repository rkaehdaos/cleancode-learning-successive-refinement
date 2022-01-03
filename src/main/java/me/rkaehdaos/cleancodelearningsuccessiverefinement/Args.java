package me.rkaehdaos.cleancodelearningsuccessiverefinement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Args {
    private String schema;
    private String[] args;
    private boolean valid;
    private Set<Character> unexpectedArguments = new TreeSet<>();
    private Map<Character, Boolean> booleanArgs = new HashMap<>();
    private int numberOfArguments = 0;

    //생성자
    public Args(String schema, String[] args) {
        this.schema = schema;
        this.args = args;
        this.valid = parse();
    }

    public boolean isValid() {
        return valid;
    }

    private boolean parse() {
        if (schema.length() == 0 && args.length == 0)
            return true;
        parseSchema();
        parseArguments();
        return unexpectedArguments.size() == 0;
    }

    private boolean parseSchema() {
        for (String element : schema.split(",")) {
            parseSchemaElement(element);
        }
        return true;
    }

    private void parseSchemaElement(String element) {
        if (element.length() == 1 && element.equals("l"))
            parseBooleanSchemaElement(element);
    }

    private void parseBooleanSchemaElement(String element) {
        char c = element.charAt(0);
        if (Character.isLetter(c))
            booleanArgs.put(c, false);
    }

    private boolean parseArguments() {
        for (String arg : args)
            parseArgument(arg);
        return true;

    }

    private void parseArgument(String arg) {
        if (arg.startsWith("-"))
            parseElements(arg);
    }

    private void parseElements(String arg) {
        for (int i = 1; i < arg.length(); i++) {
            parseElement(arg.charAt(i));
        }
    }

    private void parseElement(char argChar) {
        if (isBoolean(argChar)){
            numberOfArguments++;
            setBooleanArg(argChar, true);
        } else
            unexpectedArguments.add(argChar);
    }

    private void setBooleanArg(char argChar, boolean value) {
        booleanArgs.put(argChar, value);
    }
    private boolean isBoolean(char argChar) {
        return booleanArgs.containsKey(argChar);
    }

    public int cardinality() {
        return numberOfArguments;
    }

    public String usage(){
        if(schema.length()>0)
            return "-["+schema+"]";
        else
            return "";
    }

    public String errorMessage() {
        if(unexpectedArguments.size()>0){
            return unexpectedArgumentMessage();
        } else
            return "";
    }

    private String unexpectedArgumentMessage() {
        StringBuffer message = new StringBuffer("Argument(s) -");
        for (Character c : unexpectedArguments) {
            message.append(c);
        }
        message.append(" unexpected.");

        return message.toString();
    }


    public boolean getBoolean(char arg) {
        return booleanArgs.get(arg);
    }

    public int getInt(char c) throws ArgsException {
        if (true) {

        } else {
            throw new ArgsException();
        }
        return 0;
    }

    public String getString(char c) throws ArgsException {
        if (true) {

        } else {
            throw new ArgsException();
        }
        return null;
    }


}
