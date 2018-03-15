package org.gorgeous.fitness.config.mybatis

import groovy.transform.CompileStatic
import org.apache.ibatis.builder.SqlSourceBuilder
import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.mapping.SqlSource
import org.apache.ibatis.scripting.xmltags.DynamicContext
import org.apache.ibatis.scripting.xmltags.SqlNode
import org.apache.ibatis.session.Configuration

import java.util.regex.Matcher

@CompileStatic
class RawSqlSource implements SqlSource {

    private final SqlSource sqlSource

//    private final static def DATETIME_COLUMN_PATTERN = /[\s,]\s*`?(\w+_time)`?\s*[,\s]/

    RawSqlSource(Configuration configuration, SqlNode rootSqlNode, Class<?> parameterType) {
        this(configuration, getSql(configuration, rootSqlNode), parameterType)
    }

    RawSqlSource(Configuration configuration, String sql, Class<?> parameterType) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration)
        Class<?> clazz = parameterType == null ? Object.class : parameterType
        sql = sql.replace('ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000)',
                'TIMESTAMP_MILLSECONDS()')
        int onDuplicateKeyIndex = sql.indexOf('ON DUPLICATE KEY ')
        if (onDuplicateKeyIndex != -1)
            sql = sql.substring(0, onDuplicateKeyIndex)
        sql = sql.replaceFirst("(?i)^INSERT\\s+IGNORE\\s+INTO", "INSERT INTO")
        sqlSource = sqlSourceParser.parse(sql, clazz, new HashMap<String, Object>())
    }

    private static String getSql(Configuration configuration, SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(configuration, null)
        rootSqlNode.apply(context)
        return context.sql
    }

    @Override
    BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject)
    }

}