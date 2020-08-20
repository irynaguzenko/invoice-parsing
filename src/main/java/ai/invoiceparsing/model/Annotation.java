package ai.invoiceparsing.model;

public class Annotation {
    private String displayName;
    private TextExtraction textExtraction;

    public Annotation() {
    }

    public Annotation(String displayName, TextExtraction textExtraction) {
        this.displayName = displayName;
        this.textExtraction = textExtraction;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public TextExtraction getTextExtraction() {
        return textExtraction;
    }

    public void setTextExtraction(TextExtraction textExtraction) {
        this.textExtraction = textExtraction;
    }
}
