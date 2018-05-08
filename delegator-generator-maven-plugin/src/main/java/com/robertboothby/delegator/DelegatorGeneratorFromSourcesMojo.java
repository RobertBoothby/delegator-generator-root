package com.robertboothby.delegator;

import com.robertboothby.template.model.GenerationModel;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.util.stream.Stream;

/**
 * This Mojo generates delegator interfaces from source files.
 */
@Mojo(
        name = "generate-delegators-from-sources",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.RUNTIME
)
public class DelegatorGeneratorFromSourcesMojo extends AbstractGeneratorFromSourcesMojo {

    protected Stream<GenerationModel> createGenerationModel(JavaSource javaSource) {
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
}
