package analysis;

import parsing.SourceCodeParser;
import utils.DefinitionLoader;
import utils.FileLoader;
import utils.LanguageDefinition;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Analyser {

    private LanguageDefinition definition;
    private List<String> tokens;

    public Analyser() {

    }

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

        while(!stack.isEmpty()) {
            var currItem = stack.pop();
            var currInput = tokens.get(currIndex);

            System.out.println("Input: " + currInput + " Top " + currItem);
            Collections.list(stack.elements()).forEach(e -> System.out.print(e + ' '));
            System.out.println();

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
                retVal = false;
                break;
            }

            if(rules.contains("&epsilon"))
                System.out.println();
            //dont add epsilon rule
            rules = rules.stream().filter(rule -> !rule.equals("&epsilon")).toList();
            //debug shit
            if(rules.contains(""))
                System.out.println("Fuck off!");
            rules.forEach(stack::push);
        }

        return retVal;
    }

}
