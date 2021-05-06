package parsing;

import utils.LanguageDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SourceCodeParser {

    private final List<String> lines;
    private final LanguageDefinition languageDefinition;

    public SourceCodeParser(List<String> lines, LanguageDefinition langDefinition) {
        this.lines = lines;
        this.languageDefinition = langDefinition;
    }

    public List<String> tokenize() {
        var words = parseWords();
        return makeTokens(words);
    }

    /**
     * Creates from words tokens to be checked,
     * reserved words (READ atc) leaves as one token, others are split into 1 char tokens
     * */
    private List<String> makeTokens(List<String> words) {
        List<String> result = new ArrayList<>();

        for (String word: words) {
            if(languageDefinition.isReservedWord(word)) {
                result.add(word);
            } else {
                var tokenizedWord = Arrays.stream(word.split("(?!^)")).map(String::toLowerCase).toList();
                result.addAll(tokenizedWord);
            }
        }

        return result;
    }

    private List<String> parseWords() {
        List<String> result = new ArrayList<>();

        for(String a : lines) {
            List<String> words = new ArrayList<>();
            StringBuilder currWord = new StringBuilder();

            var chars = a.toCharArray();
            for(int i = 0; i < chars.length; i++) {
                char c = chars[i];

                /*
                 *  just add := and move on
                 * */
                if(c == ':') {
                    //add previous word
                    if(!currWord.isEmpty()) {
                        words.add(currWord.toString().replaceAll("\\s+",""));
                    }

                    if((i + 1 < chars.length) && chars[i+1] == '=') {
                        words.add(":=");
                        i++;
                    } else {
                        //cornerstone, input is just ':'
                        words.add(String.valueOf(c).replaceAll("\\s+",""));
                    }

                    //clear word
                    currWord = new StringBuilder();
                    continue;
                }

                if(!languageDefinition.isDelimiter(c)) {
                    //if we find no delimiter, we continue building word
                    currWord.append(c);
                } else {
                    //there was whitespace so we just move on
                    if(!currWord.isEmpty()) {
                        words.add(currWord.toString().replaceAll("\\s+",""));
                    }

                    // add only non whitespace delimiters!
                    if(c != ' ') {
                        words.add(String.valueOf(c).replaceAll("\\s+",""));
                    }

                    //clear word
                    currWord = new StringBuilder();

                }
            }

            //add last remaining word if the word is alone on the line
            if(!currWord.isEmpty()) words.add(currWord.toString());
            result.addAll(words);
        }

        return result;
    }

}
