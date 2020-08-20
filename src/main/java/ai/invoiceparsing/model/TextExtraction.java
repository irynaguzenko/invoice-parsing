package ai.invoiceparsing.model;

public class TextExtraction {
    private TextSegment textSegment;

    public TextExtraction() {
    }

    public TextExtraction(int startOffset, int endOffset) {
        this.textSegment = new TextSegment(startOffset, endOffset);
    }

    public TextSegment getTextSegment() {
        return textSegment;
    }

    public void setTextSegment(TextSegment textSegment) {
        this.textSegment = textSegment;
    }
}

class TextSegment {
    private int startOffset;
    private int endOffset;

    TextSegment(int startOffset, int endOffset) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }
}