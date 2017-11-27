package jvp

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.comments.Comment
import com.github.javaparser.ast.type.ClassOrInterfaceType
import com.github.javaparser.ast.visitor.VoidVisitorAdapter


class ClassVisitor extends  VoidVisitorAdapter
{
    private String parsedText = ""

    void visit(ClassOrInterfaceDeclaration n, Object arg)
    {
        super.visit(n, arg)

        parsedText += n.getName().toString() + "\n"

        if(n.getChildNodes().size() > 0)
        {
            for (Node child : n.getChildNodes())
            {
                processNode(child)
            }
        }

        if (n.getAllContainedComments().size() > 0)
        {
            for(Comment comment : n.getAllContainedComments())
            {
                parsedText += comment.toString() + "\n"
            }
        }

        return
    }

    void processNode(Node node)
    {
        if (node instanceof ClassOrInterfaceType)
        {
            parsedText += node.getNameAsString() + "\n"
        }
        else if (node instanceof MethodDeclaration)
        {
            parsedText += node.getNameAsString() + "\n"
        }
        else if (node instanceof VariableDeclarator)
        {
            parsedText += node.nameAsString + "\n"
            return
        }
        else if (node instanceof Parameter)
        {
            parsedText += node.nameAsString + "\n"
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
