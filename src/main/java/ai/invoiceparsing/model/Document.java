package ai.invoiceparsing.model;


import java.util.List;

public class Document {
    private TextSnippet textSnippet;
    private List<Annotation> annotations;

    public Document() {
    }

    public Document(TextSnippet textSnippet, List<Annotation> annotations) {
        this.textSnippet = textSnippet;
        this.annotations = annotations;
    }

    public TextSnippet getTextSnippet() {
        return textSnippet;
    }

    public void setTextSnippet(TextSnippet textSnippet) {
        this.textSnippet = textSnippet;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
}