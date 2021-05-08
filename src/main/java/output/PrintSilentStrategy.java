package output;

import analysis.AnalysisOutput;
import analysis.StackContent;

import java.util.List;

public class PrintSilentStrategy implements PrintingStrategy {

    public PrintSilentStrategy() { }

    @Override
    public void print(AnalysisOutput out) {
        System.out.println(out.getResult());
    }

}
