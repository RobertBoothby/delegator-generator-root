package com.robertboothby.delegator;

import com.robertboothby.template.AbstractGeneratorMojo;
import com.robertboothby.template.model.GenerationModel;
import com.robertboothby.template.model.GenerationModelRetriever;
import com.robertboothby.utilities.lambda.FunctionResult;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.robertboothby.utilities.lambda.LambdaUtilities.wrap;

@Mojo(
        name = "delegator-generator",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.RUNTIME
)
public class DelegatorGeneratorMojo extends AbstractGeneratorMojo {

    @Parameter(required = true)
    private FileSet[] fileSets;

    private final FileSetManager fileSetManager = new FileSetManager();

    protected GenerationModelRetriever getGenerationModelRetriever() throws MojoExecutionException {

        JavaProjectBuilder builder = new JavaProjectBuilder();

        List<FunctionResult<JavaSource>> results = Arrays
                .stream(fileSets)
                .flatMap(this::getIncludedFiles)
                .collect(Collectors.toSet()) //Ensure uniqueness
                .stream()
                .map(wrap(builder::addSource))
                .collect(Collectors.toList());

        //Check for failures
        String failures = results.stream()
                .filter(FunctionResult::isExceptional)
                .map(FunctionResult::toString)
                .collect(Collectors.joining("\n"));

        if(failures != null && !failures.isEmpty()){
            throw new MojoExecutionException("Failed to parse files: \n" + failures);
        }

        return () -> results
                .stream()
                .map(FunctionResult::getResult)
                .map(Optional::get)
                .flatMap(this::createGenerationModel)
                .collect(Collectors.toList());
    }

    private Stream<GenerationModel> createGenerationModel(JavaSource javaSource) {

        return javaSource.getClasses()
                .stream()
                .filter(JavaClass::isInterface)
                .filter(JavaClass::isPublic)
                .filter(not(JavaClass::isInner))
                .map(javaClass ->
                        new GenerationModel(
                                "Delegator.ftl",
                                getModel(javaSource, javaClass),
                                javaClass.getFullyQualifiedName().replace('.', '/') + "Delegator.java"
                        )
                );
    }

    private Map<String, Object> getModel(JavaSource javaSource, JavaClass javaClass) {
        Map<String, Object> result = new HashMap<>();
        result.put("javaSource", javaSource);
        result.put("javaClass", javaClass);
        return result;
    }

    private Stream<File> getIncludedFiles(FileSet fileSet) {
        return Arrays
                .stream(fileSetManager.getIncludedFiles(fileSet))
                .map(file -> new File(new File(fileSet.getDirectory()), file));
    }

    private static <T> Predicate<T> not(Predicate<T> predicate){
        return t -> !predicate.test(t);
    }
}
