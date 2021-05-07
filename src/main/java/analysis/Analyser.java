package analysis;

import parsing.SourceCodeParser;
import utils.DefinitionLoader;
import utils.FileLoader;
import utils.LanguageDefinition;

import java.io.FileNotFoundException;
import java.util.*;

public class Analyser {

    private LanguageDefinition definition;
    private List<String> tokens;

    public Analyser() { }

    public boolean loadFile(String fileToCheck) {

        try {
            definition = DefinitionLoader.load();
            var checkFile = FileLoader.loadSourceCodeFile(fileToCheck);
            var fileLines = FileLoader.readFileLines(checkFile);

            tokens = new SourceCodeParser(fileLines, definition).tokenize();

            return true;
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean analyse(String fileName) {
        loadFile(fileName);
        return analyse();
    }

    public boolean analyse() {
        boolean retVal = true;

        Stack<String> stack = new Stack<>();
        stack.push("S"); //add starting terminal
        int currIndex = 0;
        boolean shouldStop = false;

//        //recover missing BEGIN/END
        if(!tokens.contains("BEGIN")) {
            System.out.println("Missing BEGIN, nevermind bro, I got you!");
            tokens.add(0, "BEGIN");
        }
        if(!tokens.contains("END")) {
            System.out.println("Missing END, nevermind bro, I got you!");
            tokens.add( "END");
        }

        while(!stack.isEmpty()) {
            var currItem = stack.pop();
            var currInput = tokens.get(currIndex);


            //check wether we have same symbols on stack and in input
            if(currItem.equals(currInput)) {
                currIndex++;

                //we reached end of input
                if(currIndex == tokens.size()) {
                    if(!stack.isEmpty()) retVal = false;
                    break;
                }

                continue;
            }

            var rules = definition.getRuleList(currItem, currInput);
            if(rules == null) {
                //try recovering
//
                if(currItem.equals(";")) {
                    System.out.println("*RECOVERING \";\"");
                    stack.push(";");
                    tokens.add(currIndex,";");
                    continue;
                } else if(currItem.equals(")")) {
                    System.out.println("*RECOVERING \")\"");
                    stack.push(")");
                    tokens.add(currIndex,")");
                    continue;
                }

                var hasSemicolon = definition.getRuleList(currItem, ";") != null;
                var hasRightBracket = definition.getRuleList(currItem, ")") != null;
                if(!hasSemicolon && !hasRightBracket) {
                    retVal = false;
                    break;
                } else if(hasRightBracket && hasSemicolon) {
                    //we have to check both
                    tokens.add(currIndex, ")");
                    var res = analyse(stack, currIndex, tokens);
                    if(!res) {
                        tokens.remove(currIndex);
                    }
                    var toAdd = currIndex;
                    if(res) toAdd++;
                    tokens.add(toAdd, ";");
                    res = analyse(stack, currIndex, tokens);
                    if(!res) {
                        //cannot recover
                        retVal = false;
                        break;
                    }

                } else if(hasRightBracket) {
                    System.out.println("RECOVERING SUMTING )");
                    tokens.add(currIndex,")");
                    continue;
                } else if(hasSemicolon) {
                    System.out.println("RECOVERING SUMTING ;");
                    tokens.add(currIndex,";");
                    continue;
                }

                System.out.println("AAAAAAAAAAAAAAAAA");
                continue;
            }

            rules = rules.stream().filter(rule -> !rule.equals("&epsilon")).toList();
            rules.forEach(stack::push);
        }

        return retVal;
    }

    public boolean analyse(Stack<String> _stack, int currIndex, List<String> _t) {
        boolean retVal = true;
        System.out.println("VNORIL SOM SA KUA!");
        Stack<String> stack = new Stack<>();
        Collections.list(_stack.elements()).forEach(stack::push);
        List<String> _tokens = new ArrayList<>(_t);

        while(!stack.isEmpty()) {
            var currItem = stack.pop();
            var currInput = _tokens.get(currIndex);


            //check wether we have same symbols on stack and in input
            if(currItem.equals(currInput)) {
                currIndex++;

                //we reached end of input
                if(currIndex == _tokens.size()) {
                    if(!stack.isEmpty()) retVal = false;
                    break;
                }

                continue;
            }

            var rules = definition.getRuleList(currItem, currInput);
            if(rules == null) {
                    if(currItem.equals(";")) {
                        System.out.println("*RECOVERING \";\"");
                        stack.push(";");
                        _tokens.add(currIndex,";");
                        continue;
                    } else if(currItem.equals(")")) {
                        System.out.println("*RECOVERING \")\"");
                        stack.push(")");
                        _tokens.add(currIndex,")");
                        continue;
                    }

                var hasSemicolon = definition.getRuleList(currItem, ";") != null;
                var hasRightBracket = definition.getRuleList(currItem, ")") != null;
                if(!hasSemicolon && !hasRightBracket) {
                    retVal = false;
                    break;
                } else if(hasRightBracket && hasSemicolon) {
                    //we have to check both
                    tokens.add(currIndex, ")");
                    var res = analyse(stack, currIndex, tokens);
                    if(!res) {
                        tokens.remove(currIndex);
                    }

                    var toAdd = currIndex;
                    if(res) toAdd++;
                    tokens.add(toAdd, ";");
                    res = analyse(stack, currIndex, tokens);
                    if(!res) {
                        //cannot recover
                        retVal = false;
                        break;
                    }

                } else if(hasRightBracket) {
                    System.out.println("RECOVERING SUMTING )");
                    _tokens.add(currIndex,")");
                    continue;
                } else if(hasSemicolon) {
                    System.out.println("RECOVERING SUMTING ;");
                    _tokens.add(currIndex,";");
                    continue;
                }

                System.out.println("AAAAAAAAAAAAAAAAA");
                continue;
            }

            rules = rules.stream().filter(rule -> !rule.equals("&epsilon")).toList();
            rules.forEach(stack::push);
        }
        System.out.println("Vynaram sa kundo!");
        return retVal;
    }

}

