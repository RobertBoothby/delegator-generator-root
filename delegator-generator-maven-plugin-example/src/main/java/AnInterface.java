import java.io.OutputStream;

public interface AnInterface<T, R> {

    public void doSomething(String value, T otherValue);

    public OutputStream doSomethingElse();

    public R doSomethingWithGenerics();


}
