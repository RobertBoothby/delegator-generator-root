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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.robertboothby.utilities.lambda.LambdaUtilities.wrap;

/**
 * This Mojo generates decorator classes from source files.
 */
@Mojo(
        name = "generate-decorators-from-sources",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.RUNTIME
)
public class DecoratorGeneratorFromSourcesMojo extends AbstractGeneratorFromSourcesMojo {


    @Override
    protected Stream<GenerationModel> createGenerationModel(JavaSource javaSource) {
        return javaSource.getClasses()
                .stream()
                .filter(JavaClass::isPublic)
                .filter(not(JavaClass::isInner))
                .filter(not(JavaClass::isFinal))
                .filter(this::hasDefaultConstructor)//Initially will only work to decorate classes with default constructor.
                .map(javaClass ->
                        new GenerationModel(
                                "Decorator.ftl",
                                getModel(javaSource, javaClass),
                                javaClass.getFullyQualifiedName().replace('.', '/') + "Decorator.java"
                        )
                );
    }

    private boolean hasDefaultConstructor(JavaClass javaClass) {
        return javaClass.getConstructor(Collections.emptyList()) != null || javaClass.isInterface();
    }


}
