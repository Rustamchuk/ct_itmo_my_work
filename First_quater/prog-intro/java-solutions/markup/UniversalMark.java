package markup;

import java.util.List;

abstract class UniversalMark extends MarkedText {
    protected String markSymbol;
    protected String HtmlMarkSymbol;

    private StringBuilder answerText = new StringBuilder();

    public UniversalMark(List<Object> objects) {
        super(objects);
    }

    public String getStandardMarked() {
        answerText.setLength(0);
        answerText.append(markSymbol);
        answerText.append(getStdText());
        answerText.append(markSymbol);
        return answerText.toString();
    }

    public String getHtmlMarked() {
        answerText.setLength(0);
        answerText.append("<" + HtmlMarkSymbol + ">");
        answerText.append(getHtmlText());
        answerText.append("</" + HtmlMarkSymbol + ">");
        return answerText.toString();
    }
}
