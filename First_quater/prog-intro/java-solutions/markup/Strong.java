package markup;

import java.util.List;

public class Strong extends UniversalMark{
    public Strong(List<Object> objects) {
        super(objects);
        markSymbol = "__";
        HtmlMarkSymbol = "strong";
    }
}
