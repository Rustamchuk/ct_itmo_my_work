package markup;

import java.util.List;

public class Paragraph extends MarkedText {
    public Paragraph(List<Object> objects) { super(objects); }

    public void toMarkdown(StringBuilder string) { string.append(getStdText()); }

    public void toHtml(StringBuilder string) { string.append(getHtmlText()); }
}
