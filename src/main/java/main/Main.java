package main;

import analysis.Analyser;
import parsing.SourceCodeParser;
import utils.DefinitionLoader;
import utils.FileLoader;
import utils.LanguageDefinition;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        var a = new Analyser().loadFile("skuska.txt");

    }

}
