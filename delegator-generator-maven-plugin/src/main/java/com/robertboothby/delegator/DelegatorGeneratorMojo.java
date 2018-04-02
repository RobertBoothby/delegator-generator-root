package com.robertboothby.delegator;

import com.robertboothby.codegen.AbstractGeneratorMojo;
import com.robertboothby.codegen.FunctionResult;
import com.robertboothby.codegen.model.GenerationModel;
import com.robertboothby.codegen.model.GenerationModelRetriever;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.robertboothby.codegen.Utilities.wrap;

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
                .filter(FunctionResult::isFailure)
                .map(FunctionResult::toString)
                .collect(Collectors.joining("\n"));

        if(failures != null && !failures.isEmpty()){
            throw new MojoExecutionException("Failed to parse files: \n" + failures);
        }

        return () -> results
                .stream()
                .map(FunctionResult::getResult)
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
