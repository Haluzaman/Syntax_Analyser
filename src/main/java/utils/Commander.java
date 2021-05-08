package utils;

import analysis.Analyser;
import output.PrintEveryFewStepsStrategy;
import output.PrintEverythingStrategy;
import output.PrintSilentStrategy;
import output.PrintingStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Commander {

    private Analyser analyser;
    private List<String> fileNames;


    public Commander() {
        fileNames = FileLoader.getSrcCodeFiles();
        analyser = new Analyser();
    }

    public void listen() {
        boolean stop = false;

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Welcome to SMALL language Syntax Analyser!");
            while(!stop) {
                System.out.println("What do you want to do?");
                printHelp();
                String answer = reader.readLine();
                if(answer.equalsIgnoreCase("exit")) {
                    stop = true;
                } else if(answer.equalsIgnoreCase("help")) {
                    printHelp();
                }else if(answer.equalsIgnoreCase("list")) {
                    fileNames.forEach(e -> System.out.print(e + '|'));
                    System.out.println();
                } else if(answer.startsWith("check")) {
                    String[] cmdParsed = answer.split(" ");
                    if(cmdParsed.length == 1) {
                        System.out.println("Please write file name!");
                        continue;
                    }


                    System.out.println("Which mode do you prefer?:");
                    System.out.println("\t1 - print everything (stack content, top of stack, current input)");
                    System.out.println("\t2 - print everything (stack content, top of stack, current input) - every 5 steps");
                    System.out.println("\t3 - print only result");

                    boolean inputOk = false;
                    String i = "";
                    while(!inputOk) {
                        i = reader.readLine();
                        if(i.equals("1") || i.equals("2") || i.equals("3")) {
                            inputOk = true;
                        }
                    }

                    printResult(cmdParsed[1], getPrintingstrategy(i));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private PrintingStrategy getPrintingstrategy(String cmd) {
        int mode = Integer.parseInt(cmd);
        PrintingStrategy result;

        switch (mode) {
            case 1 -> result = new PrintEverythingStrategy();
            case 2 -> result = new PrintEveryFewStepsStrategy(5);
            default -> result = new PrintSilentStrategy();
        }

        return result;
    }

    private void printResult(String name, PrintingStrategy strategy) {
        List<String> files = new ArrayList<>();

        if(name.equals("all")) files = fileNames;
        else files.add(name);

        files.forEach(file -> {
            var output = analyser.analyse(file);
            strategy.print(output);
//
//            if(isOk) {
//                System.out.println("Source code file: " + file + " is Ok");
//            } else {
//                System.out.println("Source code file: " + file + " is wrong");
//            }
        });


    }

    private void printHelp() {
        System.out.println("HELP:");
        System.out.println("\t type \"list\" for listing all available source code files");
        System.out.println("\t type \"check filename[all]\" for checking concrete file or all files");
        System.out.println("\t type \"exit\" to exit program");
    }
}
