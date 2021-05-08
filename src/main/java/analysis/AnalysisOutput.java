package analysis;

import java.util.ArrayList;
import java.util.List;

public class AnalysisOutput {

    private List<StackContent> stackContent;
    private final String fileName;
    private boolean status;

    public AnalysisOutput(String fileName) {
        this.fileName = fileName;
    }

    public void addContent(StackContent s) {
        if(stackContent == null) this.stackContent = new ArrayList<>();
        stackContent.add(s);
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public StackContent getContent(int i) {
        return stackContent.get(i);
    }

    public String getContentString(int i) {
        return stackContent.get(i).toString();
    }

    public List<StackContent> getStackContent() {
        return this.stackContent;
    }

    public String getFileName() { return this.fileName; }

    public String getResult() {
        String res = "OK";
        if(!status) res = "WRONG";
        return "File " + fileName + " is " + res + "\n";
    }
}
