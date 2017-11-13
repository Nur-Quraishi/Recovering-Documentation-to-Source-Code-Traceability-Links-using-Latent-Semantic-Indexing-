package com

import grails.transaction.Transactional
import org.apache.commons.io.IOUtils
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.PDFTextStripperByArea

import java.nio.charset.StandardCharsets
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

@Transactional
class FileService {

    def readPDF(String pdfPath) throws Exception
    {
        PDDocument pdfDocument = PDDocument.load(new File(pdfPath))
        pdfDocument.getClass()

        if (!pdfDocument.isEncrypted())
        {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea()
            stripper.setSortByPosition(true)

            PDFTextStripper tStripper = new PDFTextStripper()

            String pdfFileInText = tStripper.getText(pdfDocument)

            // split by whitespace
            String[] lines = pdfFileInText.split("\\r?\\n")
            for (String line : lines)
            {
                System.out.println(line)
            }
            pdfDocument.close()
        }
        else
        {
            pdfDocument.close()
            throw new Exception("File is encrypted.")
        }
    }

    def readZip(String zipPath) throws Exception
    {
        ZipFile zipFile = new ZipFile(zipPath)
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
