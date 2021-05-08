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

    public AnalysisOutput analyse(String fileName) {
        loadFile(fileName);
        return getResult(fileName);
    }

    private AnalysisOutput getResult(String fileName) {
        AnalysisOutput output = new AnalysisOutput(fileName);
        boolean fileOk = true;

        Stack<String> stack = new Stack<>();
        stack.push("S"); //add starting terminal
        int currIndex = 0;
        var currContent = new StackContent(stack.toString(), stack.peek(), "");

        //recover missing BEGIN/END
        if(!tokens.contains("BEGIN")) {
            tokens.add(0, "BEGIN");
            currContent.addInfo("BEGIN added!");
        }
        if(!tokens.contains("END")) {
            tokens.add( "END");
            currContent.addInfo("END added!");
        }

        output.addContent(currContent);
        boolean result = analyse(stack, currIndex, tokens, definition, output);
        output.setStatus(result);
        return output;

    }

    public boolean analyse(Stack<String> _stack, int currIndex, List<String> _t, LanguageDefinition _definition, AnalysisOutput output) {
        boolean retVal = true;
        Stack<String> stack = new Stack<>();
        Collections.list(_stack.elements()).forEach(stack::push);
        List<String> _tokens = new ArrayList<>(_t);

        StackContent s;

        while(!stack.isEmpty()) {
            s = new StackContent(stack.toString(), stack.peek(), _tokens.get(currIndex));
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

            var rules = _definition.getRuleList(currItem, currInput);
            if(rules == null) {
                    if(currItem.equals(";")) {
                        s.addInfo("Adding \";\"");
                        stack.push(";");
                        _tokens.add(currIndex,";");
                        output.addContent(s);
                        continue;
                    } else if(currItem.equals(")")) {
                        s.addInfo("Adding \")\"");
                        stack.push(")");
                        _tokens.add(currIndex,")");
                        output.addContent(s);
                        continue;
                    }

                var hasSemicolon = _definition.getRuleList(currItem, ";") != null;
                var hasRightBracket = _definition.getRuleList(currItem, ")") != null;
                if(!hasSemicolon && !hasRightBracket) {
                    retVal = false;
                    break;
                } else if(hasRightBracket && hasSemicolon) {
                    //we have to check both
                    s.addInfo("TRYING adding \")\" ");
                    _t.add(currIndex, ")");
                    var res = analyse(stack, currIndex, _t, _definition, output);
                    if(!res) {
                        s.addInfo("COULD NOT ADD \")\" REMOVING");
                        _t.remove(currIndex);
                    }

                    s.addInfo("TRYING adding \")\" ");
                    var toAdd = currIndex;
                    if(res) toAdd++;
                    _t.add(toAdd, ";");
                    res = analyse(stack, currIndex, _t, _definition, output);
                    if(!res) {
                        //cannot recover
                        s.addInfo("COULD NOT ADD \")\" REMOVING");
                        retVal = false;
                        output.addContent(s);
                        break;
                    }

                } else if(hasRightBracket) {
                    s.addInfo("ADDING \")\" ");
                    _tokens.add(currIndex,")");
                    output.addContent(s);
                    continue;
                } else {
                    s.addInfo("ADDING adding \";\" ");
                    _tokens.add(currIndex,";");
                    output.addContent(s);
                    continue;
                }

                continue;
            }

            s.addInfo("ADDING RULE: " + currItem + " " + "(" + currInput + ")" + " -> " + rules);
            rules = rules.stream().filter(rule -> !rule.equals("&epsilon")).toList();
            rules.forEach(stack::push);

            output.addContent(s);
        }


        return retVal;
    }

}

