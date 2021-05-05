package utils;

import java.util.List;
import java.util.Map;

public record LanguageDefinition(List<Character> delimiters,
                                 List<String> reservedWords,
                                 Map<String, Map<String,List<String>>> parseTableMap) {

    public boolean isDelimiter(Character c) {
        return delimiters.stream().anyMatch(x -> x == c);
    }

    public boolean isReservedWord(String word) {
        return reservedWords.stream().anyMatch(x -> x.equals(word));
    }

    public List<String> getRuleList(String terminal, String input) {
        if(!parseTableMap.containsKey(terminal)) return null;
        var rules = parseTableMap.get(terminal);

        if(!rules.containsKey(input)) return null;

        return rules.get(input);
    }
}
