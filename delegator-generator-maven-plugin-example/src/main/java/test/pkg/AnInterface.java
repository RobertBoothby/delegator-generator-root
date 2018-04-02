package test.pkg;

import java.io.OutputStream;

public interface AnInterface<T, R, E extends Exception> {

    public void doSomething(String value, T otherValue);

    public OutputStream doSomethingElse();

    public R doSomethingWithGenerics() throws E;


}
