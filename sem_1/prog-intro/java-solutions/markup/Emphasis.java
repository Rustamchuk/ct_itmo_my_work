package markup;

import java.util.List;

public class Emphasis extends UniversalMark{
    public Emphasis(List<Object> objects) {
        super(objects);
        markSymbol = "*";
        HtmlMarkSymbol = "em";
    }
}
