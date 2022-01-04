package me.rkaehdaos.cleancodelearningsuccessiverefinement;

import java.text.ParseException;
import java.util.*;

public class Args {
    private String schema;
    private String[] args;
    private boolean valid = true;
    private Set<Character> unexpectedArguments = new TreeSet<>();
    private Map<Character, ArgumentMarshaler> booleanArgs = new HashMap<>();
    private Map<Character, ArgumentMarshaler> stringArgs = new HashMap<>();
    private Map<Character, ArgumentMarshaler> integerArgs = new HashMap<>();
    private Set<Character> argsFound = new HashSet<>();
    private int currentArgument;
    private char errorArgument = '\0';

    enum ErrorCode {
        OK, MISSING_STRING, INVALID_INTEGER
    }

    private ErrorCode errorCode = ErrorCode.OK;

    //생성자
    public Args(String schema, String[] args) throws ParseException, ArgsException {
        this.schema = schema;
        this.args = args;
        this.valid = parse();
    }

    private boolean parse() throws ParseException, ArgsException {
        if (schema.length() == 0 && args.length == 0)
            return true;
        parseSchema();
        parseArguments();
//        return unexpectedArguments.size() == 0;
        return valid;
    }

    private boolean parseSchema() throws ParseException {
        for (String element : schema.split(",")) {
            if (element.length() > 0) {
                String trimmedElement = element.trim();
                parseSchemaElement(trimmedElement);
            }
        }
        return true;
    }

    private void parseSchemaElement(String element) throws ParseException {
        char elementId = element.charAt(0);
        String elementTail = element.substring(1);
        validateSchemaElementId(elementId);
        if (isBooleanSchemaElement(elementTail))
            parseBooleanSchemaElement(elementId);
        else if (isStringSchemaElement(elementTail))
            parseStringSchemaElement(elementId);
        else if (isIntegerSchemaElement(elementTail))
            parseIntegerSchemaElement(elementId);
    }

    private void validateSchemaElementId(char elementId) throws ParseException {
        if (!Character.isLetter(elementId)) {
            throw new ParseException(
                    "Bad Chracter:" + elementId + "in Args format: " + schema, 0);
        }
    }

    private void parseIntegerSchemaElement(char elementId) {
        integerArgs.put(elementId, new IntegerArgumentMarshaler());
    }

    private boolean isIntegerSchemaElement(String elementTail) {
        return elementTail.equals("#");
    }

    private void parseStringSchemaElement(char elementId) {
        stringArgs.put(elementId, new StringArgumentMarshaler());
    }

    private boolean isStringSchemaElement(String elementTail) {
        return elementTail.equals("*");
    }

    private boolean isBooleanSchemaElement(String elementTail) {
        return elementTail.length() == 0;
    }

    private void parseBooleanSchemaElement(char elementId) {
        booleanArgs.put(elementId, new BooleanArgumentMarshaler());
    }

    private boolean parseArguments() throws ArgsException {
        for (currentArgument = 0; currentArgument < args.length; currentArgument++) {
            String arg = args[currentArgument];
            parseArgument(arg);
        }
        return true;
    }

    private void parseArgument(String arg) throws ArgsException {
        if (arg.startsWith("-"))
            parseElements(arg);
    }

    private void parseElements(String arg) throws ArgsException {
        for (int i = 1; i < arg.length(); i++) {
            parseElement(arg.charAt(i));
        }
    }

    private void parseElement(char argChar) throws ArgsException {
        if (setArgument(argChar))
            argsFound.add(argChar);
        else {
            unexpectedArguments.add(argChar);
            valid = false;
        }
    }

    private boolean setArgument(char argChar) throws ArgsException {
        boolean set = true;
        if (isBoolean(argChar)) {
            setBooleanArg(argChar, true);
        } else if (isString(argChar))
            setStringArg(argChar, "");
        else if (isInteger(argChar))
            setIntegerArg(argChar, 10);
        else
            set = false;
        return set;
    }

    private void setIntegerArg(char argChar, int i) throws ArgsException {
        currentArgument++;
        String parameter = null;
        try {
            parameter = args[currentArgument];
//            integerArgs.get(argChar).setIntegerValue(Integer.parseInt(parameter));
            integerArgs.get(argChar).set(parameter);
        } catch (ArrayIndexOutOfBoundsException e) {
            valid = false;
            errorArgument = argChar;
            errorCode = ErrorCode.MISSING_STRING;
            throw new ArgsException();
        } catch (NumberFormatException e) {
            valid = false;
            errorArgument = argChar;
            errorCode = ErrorCode.INVALID_INTEGER;
            throw new ArgsException();
        }

    }

    private boolean isInteger(char argChar) {
        return integerArgs.containsKey(argChar);
    }

    private void setStringArg(char argChar, String s) {
        currentArgument++;
        try {
//            stringArgs.get(argChar).setStringValue(args[currentArgument]);
            stringArgs.get(argChar).set(args[currentArgument]);
        } catch (ArrayIndexOutOfBoundsException e) {
            valid = false;
            errorArgument = argChar;
            errorCode = ErrorCode.MISSING_STRING;
        }
    }

    private boolean isString(char argChar) {
        return stringArgs.containsKey(argChar);
    }

    private void setBooleanArg(char argChar, boolean value) {
        booleanArgs.get(argChar).set("ture");
    }

    private boolean isBoolean(char argChar) {
        return booleanArgs.containsKey(argChar);
    }

    public int cardinality() {
        return argsFound.size();
    }

    public String usage() {
        if (schema.length() > 0)
            return "-[" + schema + "]";
        else
            return "";
    }

    public String errorMessage() throws Exception {
        if (unexpectedArguments.size() > 0) {
            return unexpectedArgumentMessage();
        } else
            switch (errorCode) {
                case MISSING_STRING:
                    return String.format("Could Not find String parameter for -%c.", errorArgument);
                case OK:
                    throw new Exception("TILT: Should not get here.");
            }
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
        ArgumentMarshaler am = booleanArgs.get(arg);
        return am != null && (Boolean) am.get();
    }

    public int getInt(char arg) throws ArgsException {
        ArgumentMarshaler am = integerArgs.get(arg);
        return am == null ? 0 : (int) am.get();
    }

    public String getString(char arg) throws ArgsException {
        ArgumentMarshaler am = stringArgs.get(arg);
        return am == null ? "" : (String) am.get();
    }

    private abstract class ArgumentMarshaler {
        protected boolean booleanValue = false;
        protected String stringValue;
        protected Integer integerValue;

        //abstract
        public abstract void set(String s);

        public abstract Object get();

    }

    private class BooleanArgumentMarshaler extends ArgumentMarshaler {
        @Override
        public void set(String s) {
            booleanValue = true;
        }

        @Override
        public Object get() {
            return booleanValue;
        }
    }

    private class StringArgumentMarshaler extends ArgumentMarshaler {

        @Override
        public void set(String s) {
            stringValue = s;
        }

        @Override
        public Object get() {
            return stringValue;
        }
    }

    private class IntegerArgumentMarshaler extends ArgumentMarshaler {
        @Override
        public void set(String s) {
            integerValue = Integer.valueOf(s);
        }

        @Override
        public Object get() {
            return integerValue;
        }
    }


}

