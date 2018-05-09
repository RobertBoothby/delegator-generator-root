<h1>Delegator Generator</h1>

This project contains a Maven plugin that creates Delegator Interfaces and Decorator Classes and a module that shows how to use it.
<h2>What is a Delegator Interface</h2>

A Delegator Interface is a pattern that only became feasible in Java 8 with the introduction of default methods on interfaces.
It aims to take out the pain of creating classes that implement an interface and delegate the implementation of most of
the methods to a wrapped instance of that same interface. This is most often seen in the decorator pattern but may be seen
in other contexts.

A Delegator Interface extends the primary interface and consists of a getXXXDelegate() method and a complete set of
default methods for the primary interface methods that delegate to the instance returned by the getXXXDelegate() method.

A class wishing to perform delegation simply implements the relevant Delegator Interface, implements the getXXXDelegate() method
and overrides only the methods that it wishes to override.

This class may choose to implement multiple Delegator interface interfaces where it does not violate separation of concerns.

To better understand this pattern please take a look at the examples in the example module.   

<h2>Usage</h2>
To use, please add the plugin to your project's pom.xml as:
<pre>
    &lt;plugin&gt;
        &lt;groupId&gt;com.robertboothby&lt;/groupId&gt;
        &lt;artifactId&gt;delegator-generator-maven-plugin&lt;/artifactId&gt;
        &lt;version&gt;0.2.0&lt;/version&gt;
        &lt;executions&gt;
            &lt;execution&gt;
                &lt;id&gt;generate-delegators-from-sources&lt;/id&gt;
                &lt;!--
                  This goal generates Delegator Interfaces from any Interface source files defined in the configuration 
                  below.
                  --&gt;
                &lt;goals&gt;
                    &lt;goal&gt;generate-delegators-from-sources&lt;/goal&gt;
                &lt;/goals&gt;
                &lt;configuration&gt;
                    &lt;!--
                      Define the source files for which to generate Delegator Interfaces. Uses standard file set 
                      wildcards.
                      --&gt;
                    &lt;fileSets&gt;
                        &lt;fileSet&gt;
                            &lt;directory&gt;${project.build.sourceDirectory}&lt;/directory&gt;
                            &lt;includes&gt;**/*.java&lt;/includes&gt;
                        &lt;/fileSet&gt;
                        ...
                    &lt;/fileSets&gt;
                    &lt;!--
                      Define the output directory for the project. Default is &apos;${project.build.directory}/generated-sources/java&apos;
                      --&gt;
                    &lt;outputDirectory&gt;${project.build.directory}/generated-test-sources&lt;/javaoutputDirectory&gt;
                    &lt;!--
                      Define the source type of the output directory so that the generated files are added to the 
                      project build correctly. Default is 'SOURCE'. For detail of available source types see below.
                      --&gt;
                    &lt;outputDirectoryType&gt;TEST_SOURCE&lt;/outputDirectoryType&gt;
                &lt;/configuration&gt;
            &lt;/execution&gt;
            &lt;execution&gt;
                &lt;id&gt;generate-decorators-from-sources&lt;/id&gt;
                &lt;!--
                  This goal generates Decorator classes from any Interface or Class (with no arguments constructor) 
                  source files defined in the configuration below.
                  --&gt;
                &lt;goals&gt;
                    &lt;goal&gt;generate-decorators-from-sources&lt;/goal&gt;
                &lt;/goals&gt;
                &lt;configuration&gt;
                    &lt!--
                      The configuration options for this goal is the same as the one above.
                      --&gt;
                    ...  
                &lt;/configuration&gt;
            &lt;/execution&gt;
        &lt;/executions&gt;
    &lt;/plugin&gt;
</pre>
<h3>Source Types</h3>
The plugin will add the generated sources to the project build as specified below:
- SOURCE - the generated source files will be automatically added to the project&apos;s main compile sources (equivalent to src/main/java).
- TEST_SOURCE - the generated source files will be automatically added to project&apos; main test sources (equivalent to src/test/java).
- RESOURCE - the generated source files will be automatically added to the project&apos;s main resources (equivalent to src/main/resources).
- TEST_RESOURCE - the generated source files will be automatically added to project&apos; main test resources (equivalent to src/test/resources).
- NONE - the generated source files will not be automatically added to the project at all.

It is unlikely that you will want to add the generated file as any form of project resources but... you never know.