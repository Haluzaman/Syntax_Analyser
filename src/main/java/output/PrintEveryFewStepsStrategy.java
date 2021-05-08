package output;

import analysis.AnalysisOutput;
import analysis.StackContent;

import java.util.List;
import java.util.Scanner;

public class PrintEveryFewStepsStrategy implements PrintingStrategy {

    private int numSteps;

    public PrintEveryFewStepsStrategy(int numSteps) {
        this.numSteps = numSteps;
    }

    @Override
    public void print(AnalysisOutput out) {
        var content = out.getStackContent();
        var currIndex = 0;
        Scanner sc = new Scanner(System.in);

        System.out.println("ANALYSING: " + out.getFileName());

        while(currIndex < content.size()) {
            for(int i = 0; i < 5; i++) {
                if(i + currIndex >= content.size() - 1) {
                    for(int j = 0; j < content.size() - currIndex; j++) {
                        System.out.println("PRINTING: " + (j+ currIndex) + "\n" + content.get(j + currIndex));
                    }
                    break;
                } else {
                    System.out.println("PRINTINGGG: " + (i + currIndex) + "\n" + content.get(i + currIndex));
                }
            }

            System.out.println("PRESS ANY KEY TO CONTINUE");
            String cmd = sc.nextLine();
            if(cmd.equalsIgnoreCase("continue")) {
                //just go to the end if we do not want to continue debugging
                for(int j = currIndex; j < content.size(); j++) System.out.println(content.get(j));
                break;
            } else if(cmd.equalsIgnoreCase("exit")) {
                System.out.println(out.getResult());
                return;
            }
            currIndex+=5;
        }

        System.out.println(out.getResult());
    }
}
