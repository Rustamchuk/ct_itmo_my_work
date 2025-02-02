package markup;

import java.util.List;

public class Strikeout extends UniversalMark{
    public Strikeout(List<Object> objects) {
        super(objects);
        markSymbol = "~";
        HtmlMarkSymbol = "s";
    }
}
