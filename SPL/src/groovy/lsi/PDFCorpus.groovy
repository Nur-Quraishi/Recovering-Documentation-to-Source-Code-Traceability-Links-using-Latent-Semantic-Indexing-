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
            String fragmantedHeadLine = null
            String currentDocument
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
                line = line.toLowerCase().replaceAll("[^a-z0-9 .]+", "")

                if(fragmantedHeadLine != null)
                {
                    line = fragmantedHeadLine + line.trim()
                    fragmantedHeadLine = null
                }

                if(line.contains("table of contents"))
                {
                    readerFlag = true
                    continue
                }

                if(readerFlag && !isTOCFinish)
                {
                    if(parsedDocumentNameList.size() > 0 && line.contains(parsedDocumentNameList.get(0)) && !line.contains(". . ."))
                    {
                        isTOCFinish = true
                        documentCounter = 0
                        continue
                    }
                    else
                    {
                        String[] documentName = line.trim().split(" ")
                        int documentNameListSize = documentName.size()
                        String parsedDocumentName = ""

                        for(int i = 0; i < documentNameListSize-1; i++)
                        {
                            if(documentName[i] != ".")
                                parsedDocumentName  = parsedDocumentName + documentName[i] + " "
                            else
                                break
                        }
                        parsedDocumentName = parsedDocumentName.trim()
                        if(parsedDocumentName.endsWith("."))
                        {
                            parsedDocumentName = parsedDocumentName.substring(0, parsedDocumentName.length()-1)
                        }
                        parsedDocumentNameList.add(parsedDocumentName)
                        documentLengthMap.put(parsedDocumentName, 0)
                    }
                }

                if(readerFlag && isTOCFinish)
                {
                    currentDocument = parsedDocumentNameList.get(documentCounter)

                    if(documentCounter + 1 < parsedDocumentNameList.size() && line.length() < parsedDocumentNameList.get(documentCounter + 1).length() && line.contains(parsedDocumentNameList.get(documentCounter + 1).substring(0, line.trim().length())))
                    {
                        fragmantedHeadLine = line.trim() + " "
                        continue
                    }

                    if(documentCounter + 1 < parsedDocumentNameList.size() && line.contains(parsedDocumentNameList.get(documentCounter + 1)))
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

                        if(line.trim().equalsIgnoreCase(""))
                        {
                            continue
                        }

                        String[] wordsInLine = line.trim().split(" ")

                        for (String word : wordsInLine)
                        {
                            word = word.trim()

                            if(word.equalsIgnoreCase(""))
                            {
                                continue
                            }

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
            documentLengthMap.put(currentDocument, wordCounter)
            pdfDocument.close()
        }
        else
        {
            pdfDocument.close()
            throw new Exception("File is encrypted.")
        }
    }
}
