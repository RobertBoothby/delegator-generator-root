package test.pkg;

import java.io.OutputStream;
import java.util.Collection;

public class AClass<T, R, E extends Exception> {

    public AClass(){

    }

    public AClass(T type){

    }

    public void doSomething(String value, T otherValue) {

    }

    private OutputStream doSomethingElse() {
        return null;
    }

    protected R doSomethingWithGenerics(Collection<T> collection) throws E{
        return null;
    }

    Collection<R> doSomethingElseWithGenerics(T value) throws E{
        return null;
    }

}
