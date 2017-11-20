package jvp

import com.github.javaparser.ast.Node

import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.SimpleName
import com.github.javaparser.ast.expr.StringLiteralExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter

class MethodVisitor extends  VoidVisitorAdapter
{
    private String parsedText = ""

    void visit(MethodCallExpr n, Object arg)
    {
        super.visit(n, arg)

        if(n.getChildNodes().size() > 0)
        {
            for (Node child : n.getChildNodes())
            {
                processNode(child)
            }
        }

        return
    }

    void processNode(Node node)
    {
        if (node instanceof SimpleName)
        {
            parsedText += node.toString() + "\n"
            return
        }

        else if(node instanceof StringLiteralExpr)
        {
            parsedText += node.value + "\n"
            return
        }

        if(node.getChildNodes().size() > 0)
        {
            for (Node child : node.getChildNodes())
            {
                processNode(child)
            }
        }
        else
        {
            return
        }
    }

    String getParsedText()
    {
        return parsedText
    }
}
