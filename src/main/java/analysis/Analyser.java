package analysis;

import parsing.SourceCodeParser;
import utils.DefinitionLoader;
import utils.FileLoader;
import utils.LanguageDefinition;

import java.io.FileNotFoundException;
import java.util.List;

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
        return true;
    }

}
