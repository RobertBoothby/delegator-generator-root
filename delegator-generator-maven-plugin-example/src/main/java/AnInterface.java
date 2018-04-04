import java.io.*;
import java.util.Collection;

public interface AnInterface<T, R, E extends Exception> {

    void doSomething(String value, T otherValue);

    OutputStream doSomethingElse();

    R doSomethingWithGenerics(Collection<T> collection) throws E;

    Collection<R> doSomethingElseWithGenerics(T value) throws E;
}
