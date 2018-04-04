<#import "GenericTypeParameters.ftl" as g/>
<#if javaSource.packageName?has_content>package ${javaSource.packageName};

</#if><#list javaSource.imports as import>import ${import};
</#list>

/**
 * Delegator interface for ${javaClass.name} that simplfies decoration and delegation and allows multiple inheritance of
 * delegations.
 *
 * Generated by com.robertboothby:delegator-generator-maven-plugin
 */
public interface ${javaClass.name}Delegator <@g.genericParameters typeParameters = javaClass.typeParameters/> extends  ${javaClass.name}<@g.genericNames typeParameters = javaClass.typeParameters/>{

    /**
     * Get the instance of ${javaClass.name} that is to be delegated to.
     * @return the  delegate.
     */
    ${javaClass.name}<@g.genericNames typeParameters = javaClass.typeParameters/> get${javaClass.name}Delegate();

    <#list javaClass.methods as method><#if !method.modifiers?seq_contains("static") && !method.modifiers?seq_contains("final") && !method.modifiers?seq_contains("private")><#include "DelegatorMethod.ftl"/></#if></#list>
}
