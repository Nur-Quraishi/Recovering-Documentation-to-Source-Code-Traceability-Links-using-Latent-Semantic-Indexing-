package com

import grails.transaction.Transactional
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.PDFTextStripperByArea

@Transactional
class FileService {

    def readPDF(String pdfPath)
    {
        try
        {
            PDDocument pdfDocument = PDDocument.load(new File(pdfPath))
            pdfDocument.getClass()

            if (!pdfDocument.isEncrypted()) {

                PDFTextStripperByArea stripper = new PDFTextStripperByArea()
                stripper.setSortByPosition(true)

                PDFTextStripper tStripper = new PDFTextStripper()

                String pdfFileInText = tStripper.getText(pdfDocument)

                // split by whitespace
                String[] lines = pdfFileInText.split("\\r?\\n")
                for (String line : lines) {
                    System.out.println(line)
                }
            }
            else
            {
                System.out.println("File is encrypted.")
            }
        }
        catch (Exception e)
        {
            System.out.println("File doesn't exist.")
        }
    }
}
