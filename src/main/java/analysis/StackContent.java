package analysis;

import java.util.ArrayList;
import java.util.List;

public class StackContent {

    private List<String> info;
    private String stackContent;
    private String topOfStack;
    private String currentInput;

    public StackContent() { }

    public StackContent(String stackContent, String topOfStack, String currentInput, List<String> info) {
        this(stackContent, topOfStack, currentInput);
        this.info = info;
    }

    public StackContent(String stackContent, String topOfStack, String currentInput) {
        this.stackContent = stackContent;
        this.topOfStack = topOfStack;
        this.currentInput = currentInput;
    }

    public void setStackContent(String stackContent) {
        this.stackContent = stackContent;
    }

    public void setTopOfStack(String topOfStack) {
        this.topOfStack = topOfStack;
    }

    public  void setCurrentInput(String currentInput) {
        this.currentInput = currentInput;
    }

    public void addInfo(String i) {
        if(this.info == null) this.info = new ArrayList<>();
        this.info.add(i);
    }

    public String toString() {
        StringBuilder i = new StringBuilder();
        if(info != null && !info.isEmpty()) {
            this.info.forEach(info -> i.append(info).append(" "));
        }
        if(i.isEmpty()) {
            return "STACK CONTENT: " + stackContent + "\nTOP: " + topOfStack + " CURRENT INPUT: " + currentInput +
                    "\n --------------------------------------------------";
        } else {
            return "INFO: " + i + "\n" + "STACK CONTENT: " + stackContent + "\nTOP: " + topOfStack + " CURRENT INPUT: " + currentInput +
                "\n --------------------------------------------------";
        }
    }


}
