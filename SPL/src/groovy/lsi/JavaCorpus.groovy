package lsi

import org.apache.commons.io.IOUtils

import java.util.regex.Pattern
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
        String regex = "^(?<indent>\\s*)(?<mod1>\\w+)\\s(?<mod2>\\w+)?\\s*(?<mod3>\\w+)?\\s*(?<return>\\b\\w+)\\s(?<name>\\w+)\\((?<arg>.*?)\\)\\s*\\{(?<body>.+?)^\\k<indent>\\}"
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL)

        while(entries.hasMoreElements())
        {
            ZipEntry entry = entries.nextElement()
            System.out.println(entry.getName())
            if(entry.getName().contains(".java"))
            {
                InputStream stream = zipFile.getInputStream(entry)
                System.out.println(IOUtils.toString(stream))
            }
        }
        zipFile.close()
    }
}
