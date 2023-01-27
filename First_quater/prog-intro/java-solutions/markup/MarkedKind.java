package markup;

@FunctionalInterface
public interface MarkedKind<T, R> {
    R apply(T t);
}