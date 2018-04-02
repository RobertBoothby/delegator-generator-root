<h1>Delegator Generator</h1>
This project contains a module that creates a Maven plugin creates Delegator Interfaces and a module that shows how to use it.
<h2>What is a Delegator Interface</h2>
A Delegator Interface is a pattern that only became feasible in Java 8 with the introduction of default methods on interfaces.
It aims to take out the pain of creating classes that implement an interface and delegate the implementation of most of
the methods to a wrapped instance of that same interface. This is most often seen in the decorator pattern but may be seen
in other contexts.

A Delegator Interface extends the primary interface and consists of a getXXXDelegate() method and a complete set of
default methods for the primary interface methods that delegate to the instance returned by the getXXXDelegate() method.

A class wishing to perform delegation simply implements the Delegator Interface, implements the getXXXDelegate() method
and overrides only the methods that it wishes to override.

To better understand this pattern please take a look at the examples in the example module.   