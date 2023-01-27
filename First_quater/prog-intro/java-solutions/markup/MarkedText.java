package markup;

import java.util.List;

public abstract class MarkedText {
    private MarkedKind<Object, String> kindPart;

    protected List<Object> objects;
    protected final StringBuilder markedText = new StringBuilder();

    public MarkedText(List<Object> objects) {
        this.objects = objects;
    }

    protected String getStdText() {
        kindPart = x -> ((UniversalMark) x).getStandardMarked();
        return getMarkedText();
    }

    protected String getHtmlText() {
        kindPart = x -> ((UniversalMark) x).getHtmlMarked();
        return getMarkedText();
    }

    private String getMarkedText() {
        for (Object obj : objects) {
            if (obj instanceof Text) {
                markedText.append(((Text) obj).getText());
            } else if (obj instanceof UniversalMark) {
                markedText.append(kindPart.apply(obj));
            }
        }
        return markedText.toString();
    }
}
