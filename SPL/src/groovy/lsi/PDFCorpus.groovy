package lsi

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.PDFTextStripperByArea

class PDFCorpus extends Corpus
{
    PDFCorpus(String stopListPath)
    {
        super(stopListPath)
    }

    void parseFile(String filePath) throws Exception
    {
        PDDocument pdfDocument = PDDocument.load(new File(filePath))
        pdfDocument.getClass()

        if (!pdfDocument.isEncrypted())
        {
            Map<String, Integer> wordFrequency = null
            int wordCounter, documentCounter
            List <String> parsedDocumentNameList = new ArrayList<>()
            boolean readerFlag = false
            boolean isTOCFinish = false

            PDFTextStripperByArea stripper = new PDFTextStripperByArea()
            stripper.setSortByPosition(true)

            PDFTextStripper tStripper = new PDFTextStripper()
            String pdfFileInText = tStripper.getText(pdfDocument)

            // split by whitespace
            String[] lines = pdfFileInText.split("\\r?\\n")
            for (String line : lines)
            {
                line = line.toLowerCase().replaceAll("[^a-z0-9 ]+?[^\\d(\\.\\d)*\$]", "")

                if(line.contains("table of contents"))
                {
                    readerFlag = true
                    continue
                }

                if(readerFlag && !isTOCFinish)
                {
                    if(parsedDocumentNameList.size() > 0 && line.contains(parsedDocumentNameList.get(0)))
                    {
                        isTOCFinish = true
                        documentCounter = 0
                        continue
                    }
                    else
                    {
                        String[] documentName = line.trim().split(" ")
                        if(documentName.size() > 2)
                        {
                            parsedDocumentNameList.add(documentName[0].trim() + documentName[1].trim())
                            documentLengthMap.put(documentName[1].trim(),0)
                        }
                        else
                        {
                            parsedDocumentNameList.add(documentName[0].trim())
                            documentLengthMap.put(documentName[0].trim(),0)
                        }
                    }
                }

                if(readerFlag && isTOCFinish)
                {
                    String[] documentName = parsedDocumentNameList.get(documentCounter).trim().split(" ")
                    String currentDocument
                    if(documentName.size() > 1)
                    {
                        currentDocument = documentName[1].trim()
                    }
                    else
                    {
                        currentDocument = documentName[0].trim()
                    }

                    if(line.contains(parsedDocumentNameList.get(documentCounter)))
                    {
                        documentLengthMap.put(currentDocument, wordCounter)
                        wordCounter = 0
                        documentCounter += 1
                        wordFrequency = null
                        continue
                    }
                    else
                    {
                        line = line.replaceAll("[^a-z ]+","")
                        String[] wordsInLine = line.trim().split(" ")

                        for (String word : wordsInLine)
                        {
                            word = word.trim()
                            if (isStopListWord(word))
                            {
                                wordCounter += 1
                                continue
                            }

                            if (wordsInDocumentSpaceMap.containsKey(word))
                            {
                                wordFrequency = wordsInEachDocumentMap.get(word)
                                if (wordFrequency.containsKey(currentDocument))
                                {
                                    wordFrequency.put(currentDocument, wordFrequency.get(currentDocument) + 1)
                                }
                                else
                                {
                                    wordFrequency.put(currentDocument, 1)
                                }
                                wordsInDocumentSpaceMap.put(word, wordsInDocumentSpaceMap.get(word) + 1)
                            }
                            else
                            {
                                wordsInDocumentSpaceMap.put(word, 1)
                                wordFrequency = new TreeMap<String, Integer>()
                                wordFrequency.put(currentDocument, 1)
                                wordsInEachDocumentMap.put(word, wordFrequency)
                            }
                            wordCounter += 1
                        }
                    }
                }
            }
            pdfDocument.close()
        }
        else
        {
            pdfDocument.close()
            throw new Exception("File is encrypted.")
        }
    }
}
