package org.gorgeous.fitness.config.mybatis

import groovy.transform.CompileStatic
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver
import org.apache.ibatis.executor.parameter.ParameterHandler
import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.mapping.MappedStatement
import org.apache.ibatis.mapping.SqlSource
import org.apache.ibatis.parsing.PropertyParser
import org.apache.ibatis.parsing.XNode
import org.apache.ibatis.parsing.XPathParser
import org.apache.ibatis.scripting.LanguageDriver
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler
import org.apache.ibatis.scripting.xmltags.TextSqlNode
import org.apache.ibatis.session.Configuration

@CompileStatic
class CustomXMLLanguageDriver implements LanguageDriver {
    @Override
    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql)
    }

    @Override
    SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType) {
        CustomXMLScriptBuilder builder = new CustomXMLScriptBuilder(configuration, script, parameterType)
        return builder.parseScriptNode()
    }

    @Override
    SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        // issue #3
        if (script.startsWith("<script>")) {
            XPathParser parser = new XPathParser(script, false, configuration.getVariables(), new XMLMapperEntityResolver())
            return createSqlSource(configuration, parser.evalNode("/script"), parameterType)
        } else {
            // issue #127
            script = PropertyParser.parse(script, configuration.getVariables())
            TextSqlNode textSqlNode = new TextSqlNode(script)
            if (textSqlNode.isDynamic()) {
                return new DynamicSqlSource(configuration, textSqlNode)
            } else {
                return new RawSqlSource(configuration, script, parameterType)
            }
        }
    }
}
