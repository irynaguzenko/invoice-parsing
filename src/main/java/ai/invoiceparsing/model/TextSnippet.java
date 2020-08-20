package ai.invoiceparsing.model;

public class TextSnippet {
    private String content;

    public TextSnippet() {
    }

    public TextSnippet(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
