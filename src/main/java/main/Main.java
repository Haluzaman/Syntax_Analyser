package main;

import analysis.Analyser;
import parsing.SourceCodeParser;
import utils.DefinitionLoader;
import utils.FileLoader;
import utils.LanguageDefinition;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        Analyser analyser = new Analyser();
        String fileName = "skuska.txt";
        boolean isOk;

        if(analyser.loadFile(fileName)) {
            isOk = analyser.analyse();
            System.out.println("Subor: " +  fileName + " je: " + isOk );
        }
    }

}
