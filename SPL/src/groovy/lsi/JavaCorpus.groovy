package lsi

import org.apache.commons.io.IOUtils

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
        System.out.println(entries)

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
