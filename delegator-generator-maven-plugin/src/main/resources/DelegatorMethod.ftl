<#import "GenericTypeParameters.ftl" as g/>
    default <@g.genericParameters typeParameters=method.typeParameters/>${method.returnType.fullyQualifiedName} ${method.name}(<#list method.parameters as parameter>${parameter.genericValue} ${parameter.name}<#sep>, </#list>)<#if method.exceptions?has_content> throws ${method.exceptions?join(", ")}</#if> {
        <#if method.returnType.fullyQualifiedName != "void">return </#if><#assign parameterNames><#list method.parameters as parameter>${parameter.name} </#list></#assign>get${javaClass.name}Delegate().${method.name}(${parameterNames?word_list?join(", ")});
    }
