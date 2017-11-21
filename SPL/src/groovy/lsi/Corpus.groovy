package lsi

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import jvp.ClassVisitor
import jvp.MethodVisitor
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.PDFTextStripperByArea

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class Corpus
{
    String stopListPath
    List<String> stopWordList = new ArrayList<>()
    Map<String, Integer> wordsInDocumentSpaceMap = new TreeMap<>()
    Map<String, Integer> documentLengthMap = new HashMap<>()
    Map<String, Map<String, Integer>> wordsInEachDocumentMap = new TreeMap<>()
    int numberOfExternalDocuments, numberOfSourceCodeDocuments

    Corpus(String stopListPath)
    {
        this.stopListPath = stopListPath
    }

    void createStopList() throws IOException
    {
        FileInputStream fileInputStream = null
        try
        {
            fileInputStream = new FileInputStream(stopListPath)
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File does not exist. Set correct path")
        }

        DataInputStream dataInputStream = new DataInputStream(fileInputStream)
        BufferedReader br = new BufferedReader(new InputStreamReader(dataInputStream))
        String line = br.readLine()

        while(line != null)
        {
            stopWordList.add(line.trim().toLowerCase())
            line = br.readLine()
        }
    }

    boolean isStopListWord(String word)
    {
        if (stopWordList.contains(word.toLowerCase()))
            return true

        return false
    }

    boolean isTermOnWordList(String term)
    {
        if (wordsInDocumentSpaceMap.containsKey(term.toLowerCase()))
            return true

        return false
    }

    boolean isTermInDocument(String term, String document)
    {
        Map<String, Integer> documentMap = wordsInEachDocumentMap.get(term.toLowerCase())

        if (documentMap.containsKey(document))
            return true

        return false
    }

    int getNumberOfTerms()
    {
        return wordsInDocumentSpaceMap.size()
    }

    int getNumberOfDocuments()
    {
        return documentLengthMap.size()
    }

    Map<String, Integer> getWordsInDocumentSpaceMap()
    {
        return wordsInDocumentSpaceMap
    }

    List<String> getDocumentList()
    {
        List<String> documentList = new ArrayList<>()

        for (String document : documentLengthMap.keySet())
            documentList.add(document)

        return documentList
    }

    int getTermFrequency(String term)
    {
        if (wordsInDocumentSpaceMap.get(term.toLowerCase()) != null)
            return wordsInDocumentSpaceMap.get(term.toLowerCase())

        return 0
    }

    double getTFInDocument(String term, String document)
    {
        if (isTermInDocument(term.toLowerCase(), document))
        {
            int termAppearance = wordsInEachDocumentMap.get(term.toLowerCase()).get(document)
            return (1 + Math.log10(termAppearance))
        }

        return 0
    }

    double getIDF(String term)
    {
        int termFreq = getTermFrequency(term.toLowerCase())
        if (termFreq != 0)
            return (1 + Math.log10(getNumberOfDocuments()) - Math.log10(termFreq))

        return (Double) null
    }



    double[][] getTermMatrixValues()
    {
        double[][] matrixTerms = new double[getNumberOfTerms()][getNumberOfDocuments()]
        int i, j
        i = 0

        for (String term : wordsInDocumentSpaceMap.keySet())
        {
            j = 0
            for (String document : documentLengthMap.keySet())
            {
                matrixTerms[i][j] = getTFInDocument(term, document) * getIDF(term)
                j++
            }
            i++
        }

        return matrixTerms
    }

    void parsePDFFile(String filePath) throws Exception
    {
        PDDocument pdfDocument = PDDocument.load(new File(filePath))
        pdfDocument.getClass()

        if (!pdfDocument.isEncrypted())
        {
            Map<String, Integer> wordFrequency = null
            int wordCounter, documentCounter
            List <String> parsedDocumentNameList = new ArrayList<>()
            String fragmentedHeadLine = null
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

                if(fragmentedHeadLine != null)
                {
                    line = fragmentedHeadLine + line.trim()
                    fragmentedHeadLine = null
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
                        numberOfExternalDocuments = parsedDocumentNameList.size()
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
                        fragmentedHeadLine = line.trim() + " "
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

    void parseJavaFile(String filePath) throws Exception
    {
        ZipFile zipFile = new ZipFile(filePath)
        Enumeration<? extends ZipEntry> entries = zipFile.entries()
        int documentCounter = 0

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
                documentCounter += 1
                documentLengthMap.put(entry.getName(), wordCounter)
            }
        }
        numberOfSourceCodeDocuments = documentCounter
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
