package org.gorgeous.fitness.config.mybatis

import groovy.transform.CompileStatic
import org.apache.ibatis.builder.SqlSourceBuilder
import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.mapping.SqlSource
import org.apache.ibatis.scripting.xmltags.DynamicContext
import org.apache.ibatis.scripting.xmltags.SqlNode
import org.apache.ibatis.session.Configuration

@CompileStatic
class DynamicSqlSource implements SqlSource {

    private final Configuration configuration
    private final SqlNode rootSqlNode

    DynamicSqlSource(Configuration configuration, SqlNode rootSqlNode) {
        this.configuration = configuration
        this.rootSqlNode = rootSqlNode
    }

    @Override
    BoundSql getBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(configuration, parameterObject)
        rootSqlNode.apply(context)
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration)
        Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass()
        String sql = context.getSql()
        sql = sql.replace('ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000)',
                'TIMESTAMP_MILLSECONDS()')
        int onDuplicateKeyIndex = sql.indexOf('ON DUPLICATE KEY ')
        if (onDuplicateKeyIndex != -1)
            sql = sql.substring(0, onDuplicateKeyIndex)
        sql = sql.replaceFirst("(?i)^INSERT\\s+IGNORE\\s+INTO", "INSERT INTO")
        SqlSource sqlSource = sqlSourceParser.parse(sql, parameterType, context.getBindings())
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject)
        for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
            boundSql.setAdditionalParameter(entry.getKey(), entry.getValue())
        }
        return boundSql
    }

}
