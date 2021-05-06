package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DefinitionLoader {

    private DefinitionLoader() { }

    public static LanguageDefinition load() {
        LanguageDefinition def = null;
        try {
            var defFile = FileLoader.loadLangDefinitionFile();
            var lines = FileLoader.readFileLines(defFile);
            var parseTable = FileLoader.loadParsingTableAsCSV("parsingTable.csv");

            if(lines.size() == 4) {
                String resWordsLine = lines.get(1);
                String delimsLine = lines.get(3);
                Map<String, Map<String, List<String>>> parseTableMap = parseParsingTableCSV(parseTable);

                List<String> reservedWords = Arrays.asList(resWordsLine.split("~"));
                List<Character> delimiters = Arrays.stream(delimsLine.split("~")).map(str -> str.charAt(0)).toList();


                def = new LanguageDefinition(delimiters, reservedWords, parseTableMap);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return def;
    }

    private static Map<String, Map<String, List<String>>> parseParsingTableCSV(File csv) {
        Map<String, Map<String, List<String>>> result = new HashMap<>();

        var lines = FileLoader.readFileLines(csv);
        List<String[]> parsedLines = new ArrayList<>();

        //parse lines from csv
        for (String line: lines) {
            String[] currLine = line.split(";");
            // in csv real semicolon could not be used and was placehold with "SEMI"
            for(int i = 0; i < currLine.length; i++) {
                currLine[i] = currLine[i].replaceAll("SEMI", ";");
                currLine[i] = currLine[i].replaceAll("~", "-");
            }

            parsedLines.add(currLine);
        }

        List<String> inputSymbols = Arrays.asList(parsedLines.get(0));

        for(int i = 1; i < parsedLines.size(); i++) {
            String[] line = parsedLines.get(i);

            //first tokens are terminals
            String currTerminal = line[0];
            Map<String, List<String>> rulesForTerminal = new HashMap<>();

            //others are production lines
            //from index 1 because first token is terminal
            for(int j = 1; j < line.length; j++) {
                String currToken = line[j];
                // we have no rules here
                if(currToken.isEmpty() || currToken.isBlank()) continue;

                List<String> rules = Arrays.asList(currToken.trim().split("\\s+"));
                Collections.reverse(rules);
                rulesForTerminal.put(inputSymbols.get(j),rules);
            }

            result.put(currTerminal, rulesForTerminal);
        }

        return result;
    }
}
