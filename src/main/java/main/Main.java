package main;

import analysis.Analyser;
import utils.Commander;

public class Main {

    public static void main(String[] args) {
        new Commander().listen();
//        FileLoader.getSrcCodeFiles();
//        Analyser analyser = new Analyser();
//        String fileName = "skuska.txt";
//        boolean isOk;
//
//        if(analyser.loadFile(fileName)) {
//            isOk = analyser.analyse();
//            System.out.println("Subor: " +  fileName + " je: " + isOk );
//        }
    }

}
