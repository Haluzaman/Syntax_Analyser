package output;

import analysis.AnalysisOutput;

public class PrintEverythingStrategy implements PrintingStrategy {
    @Override
    public void print(AnalysisOutput out) {
        System.out.println("ANALYSING: " + out.getFileName() + "\n\n");
        out.getStackContent().forEach(System.out::println);
        System.out.println(out.getResult());
    }
}
