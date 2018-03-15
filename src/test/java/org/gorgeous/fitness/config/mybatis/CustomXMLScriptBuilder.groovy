package org.gorgeous.fitness.config.mybatis

import groovy.transform.CompileStatic
import org.apache.ibatis.builder.BaseBuilder
import org.apache.ibatis.builder.BuilderException
import org.apache.ibatis.mapping.SqlSource
import org.apache.ibatis.parsing.XNode
import org.apache.ibatis.scripting.xmltags.*
import org.apache.ibatis.session.Configuration
import org.w3c.dom.Node
import org.w3c.dom.NodeList

@CompileStatic
class CustomXMLScriptBuilder extends BaseBuilder {

    private final XNode context
    private boolean isDynamic
    private final Class<?> parameterType

    CustomXMLScriptBuilder(Configuration configuration, XNode context) {
        this(configuration, context, null)
    }

    CustomXMLScriptBuilder(Configuration configuration, XNode context, Class<?> parameterType) {
        super(configuration)
        this.context = context
        this.parameterType = parameterType
    }

    SqlSource parseScriptNode() {
        List<SqlNode> contents = parseDynamicTags(context)
        MixedSqlNode rootSqlNode = new MixedSqlNode(contents)
        if (isDynamic) {
            return new DynamicSqlSource(configuration, rootSqlNode)
        } else {
            return  new RawSqlSource(configuration, rootSqlNode, parameterType)
        }
    }

    List<SqlNode> parseDynamicTags(XNode node) {
        List<SqlNode> contents = new ArrayList<SqlNode>()
        NodeList children = node.getNode().getChildNodes()
        for (int i = 0; i < children.getLength(); i++) {
            XNode child = node.newXNode(children.item(i))
            if (child.getNode().getNodeType() == Node.CDATA_SECTION_NODE
                    || child.getNode().getNodeType() == Node.TEXT_NODE) {
                String data = child.getStringBody("")
                TextSqlNode textSqlNode = new TextSqlNode(data)
                if (textSqlNode.isDynamic()) {
                    contents.add(textSqlNode)
                    isDynamic = true
                } else {
                    contents.add(new StaticTextSqlNode(data))
                }
            } else if (child.getNode().getNodeType() == Node.ELEMENT_NODE) { // issue #628
                String nodeName = child.getNode().getNodeName()
                NodeHandler handler = nodeHandlers(nodeName)
                if (handler == null) {
                    throw new BuilderException(
                            "Unknown element <" + nodeName + "> in SQL statement.")
                }
                handler.handleNode(child, contents)
                isDynamic = true
            }
        }
        return contents
    }

    NodeHandler nodeHandlers(String nodeName) {
        Map<String, NodeHandler> map = new HashMap<String, NodeHandler>()
        map.put("trim", new TrimHandler())
        map.put("where", new WhereHandler())
        map.put("set", new SetHandler())
        map.put("foreach", new ForEachHandler())
        map.put("if", new IfHandler())
        map.put("choose", new ChooseHandler())
        map.put("when", new IfHandler())
        map.put("otherwise", new OtherwiseHandler())
        map.put("bind", new BindHandler())
        return map.get(nodeName)
    }

    private interface NodeHandler {
        void handleNode(XNode nodeToHandle, List<SqlNode> targetContents)
    }

    private class BindHandler implements NodeHandler {
        BindHandler() {
            // Prevent Synthetic Access
        }

        @Override
        void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            final String name = nodeToHandle.getStringAttribute("name")
            final String expression = nodeToHandle.getStringAttribute("value")
            final VarDeclSqlNode node = new VarDeclSqlNode(name, expression)
            targetContents.add(node)
        }
    }

    private class TrimHandler implements NodeHandler {
        TrimHandler() {
            // Prevent Synthetic Access
        }

        @Override
        void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            List<SqlNode> contents = parseDynamicTags(nodeToHandle)
            MixedSqlNode mixedSqlNode = new MixedSqlNode(contents)
            String prefix = nodeToHandle.getStringAttribute("prefix")
            String prefixOverrides = nodeToHandle.getStringAttribute("prefixOverrides")
            String suffix = nodeToHandle.getStringAttribute("suffix")
            String suffixOverrides = nodeToHandle.getStringAttribute("suffixOverrides")
            TrimSqlNode trim = new TrimSqlNode(configuration, mixedSqlNode, prefix, prefixOverrides, suffix, suffixOverrides)
            targetContents.add(trim)
        }
    }

    private class WhereHandler implements NodeHandler {
        WhereHandler() {
            // Prevent Synthetic Access
        }

        @Override
        void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            List<SqlNode> contents = parseDynamicTags(nodeToHandle)
            MixedSqlNode mixedSqlNode = new MixedSqlNode(contents)
            WhereSqlNode where = new WhereSqlNode(configuration, mixedSqlNode)
            targetContents.add(where)
        }
    }

    private class SetHandler implements NodeHandler {
        SetHandler() {
            // Prevent Synthetic Access
        }

        @Override
        void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            List<SqlNode> contents = parseDynamicTags(nodeToHandle)
            MixedSqlNode mixedSqlNode = new MixedSqlNode(contents)
            SetSqlNode set = new SetSqlNode(configuration, mixedSqlNode)
            targetContents.add(set)
        }
    }

    private class ForEachHandler implements NodeHandler {
        ForEachHandler() {
            // Prevent Synthetic Access
        }

        @Override
        void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            List<SqlNode> contents = parseDynamicTags(nodeToHandle)
            MixedSqlNode mixedSqlNode = new MixedSqlNode(contents)
            String collection = nodeToHandle.getStringAttribute("collection")
            String item = nodeToHandle.getStringAttribute("item")
            String index = nodeToHandle.getStringAttribute("index")
            String open = nodeToHandle.getStringAttribute("open")
            String close = nodeToHandle.getStringAttribute("close")
            String separator = nodeToHandle.getStringAttribute("separator")
            ForEachSqlNode forEachSqlNode = new ForEachSqlNode(configuration, mixedSqlNode, collection, index, item, open, close, separator)
            targetContents.add(forEachSqlNode)
        }
    }

    private class IfHandler implements NodeHandler {
        IfHandler() {
            // Prevent Synthetic Access
        }

        @Override
        void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            List<SqlNode> contents = parseDynamicTags(nodeToHandle)
            MixedSqlNode mixedSqlNode = new MixedSqlNode(contents)
            String test = nodeToHandle.getStringAttribute("test")
            IfSqlNode ifSqlNode = new IfSqlNode(mixedSqlNode, test)
            targetContents.add(ifSqlNode)
        }
    }

    private class OtherwiseHandler implements NodeHandler {
        OtherwiseHandler() {
            // Prevent Synthetic Access
        }

        @Override
        void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            List<SqlNode> contents = parseDynamicTags(nodeToHandle)
            MixedSqlNode mixedSqlNode = new MixedSqlNode(contents)
            targetContents.add(mixedSqlNode)
        }
    }

    private class ChooseHandler implements NodeHandler {
        ChooseHandler() {
            // Prevent Synthetic Access
        }

        @Override
        void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            List<SqlNode> whenSqlNodes = new ArrayList<SqlNode>()
            List<SqlNode> otherwiseSqlNodes = new ArrayList<SqlNode>()
            handleWhenOtherwiseNodes(nodeToHandle, whenSqlNodes, otherwiseSqlNodes)
            SqlNode defaultSqlNode = getDefaultSqlNode(otherwiseSqlNodes)
            ChooseSqlNode chooseSqlNode = new ChooseSqlNode(whenSqlNodes, defaultSqlNode)
            targetContents.add(chooseSqlNode)
        }

        private void handleWhenOtherwiseNodes(XNode chooseSqlNode, List<SqlNode> ifSqlNodes, List<SqlNode> defaultSqlNodes) {
            List<XNode> children = chooseSqlNode.getChildren()
            for (XNode child : children) {
                String nodeName = child.getNode().getNodeName()
                NodeHandler handler = nodeHandlers(nodeName)
                if (handler instanceof IfHandler) {
                    handler.handleNode(child, ifSqlNodes)
                } else if (handler instanceof OtherwiseHandler) {
                    handler.handleNode(child, defaultSqlNodes)
                }
            }
        }

        private SqlNode getDefaultSqlNode(List<SqlNode> defaultSqlNodes) {
            SqlNode defaultSqlNode = null
            if (defaultSqlNodes.size() == 1) {
                defaultSqlNode = defaultSqlNodes.get(0)
            } else if (defaultSqlNodes.size() > 1) {
                throw new BuilderException("Too many default (otherwise) elements in choose statement.")
            }
            return defaultSqlNode
        }
    }
}
