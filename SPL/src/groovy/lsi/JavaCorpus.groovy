package lsi

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import jvp.ClassVisitor
import jvp.MethodVisitor

import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class JavaCorpus extends Corpus
{
    JavaCorpus(String stopListPath)
    {
        super(stopListPath)
    }

    void parseFile(String filePath) throws Exception
    {
        ZipFile zipFile = new ZipFile(filePath)
        Enumeration<? extends ZipEntry> entries = zipFile.entries()

        while(entries.hasMoreElements())
        {
            ZipEntry entry = entries.nextElement()

            if(entry.getName().contains(".java"))
            {
                InputStream inputStream = zipFile.getInputStream(entry)
                CompilationUnit compilationUnit = JavaParser.parse(inputStream)
                String parsedString = ""

                ClassVisitor classVisitor = new ClassVisitor()
                MethodVisitor methodVisitor = new MethodVisitor()

                classVisitor.visit(compilationUnit, null)
                parsedString += classVisitor.getParsedText()

                methodVisitor.visit(compilationUnit, null)
                parsedString += methodVisitor.getParsedText()

                TreeMap<String, Integer> wordFreq = null
                int wordCounter = 0

                System.out.println(parsedString)
                documentLengthMap.put(entry.getName(), wordCounter)
            }
        }
        zipFile.close()
    }

    String splitCamelCaseWord(String camelCaseWord)
    {
        String spittedWords = ""
        String[] wordsInCamelCaseWord = camelCaseWord.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")

        for(String word : wordsInCamelCaseWord)
        {
            spittedWords = spittedWords + word + " "
        }

        return spittedWords.trim()
    }
}
