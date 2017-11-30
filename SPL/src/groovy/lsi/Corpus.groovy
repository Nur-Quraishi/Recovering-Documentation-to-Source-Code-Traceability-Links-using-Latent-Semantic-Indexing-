package lsi

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import jvp.ClassVisitor
import jvp.MethodVisitor
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.PDFTextStripperByArea

import java.io.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class Corpus
{
    String stopListPath
    List <String> stopWordList = new ArrayList<>()
    List <String> orderedDocumentNameList = new ArrayList<>()
    Map <String, Integer> wordsInDocumentSpaceMap = new TreeMap<>()
    Map <String, Integer> documentLengthMap = new TreeMap<>()
    Map <String, Map<String, Integer>> wordsInEachDocumentMap = new TreeMap<>()
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

    List <String> getOrderedDocumentNameList()
    {
        return orderedDocumentNameList
    }

    int getNumberOfExternalDocuments()
    {
        return numberOfExternalDocuments
    }

    int getNumberOfSourceCodeDocuments()
    {
        return numberOfSourceCodeDocuments
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
        int row, column
        row = 0

        for (String term : wordsInDocumentSpaceMap.keySet())
        {
            column = 0
            for (String document : orderedDocumentNameList)
            {
                matrixTerms[row][column] = getTFInDocument(term, document) * getIDF(term)
                column++
            }
            row++
        }

        return matrixTerms
    }

    void parsePDFFile(String filePath) throws Exception
    {
        PDDocument pdfDocument = PDDocument.load(new File(filePath))
        pdfDocument.getClass()

        if (!pdfDocument.isEncrypted())
        {
            int wordCounter, documentCounter
            String fragmentedHeadLine = null
            String currentDocument
            boolean readerFlag = false
            boolean isTOCFinish = false
            boolean isLOFFinish = false

            PDFTextStripperByArea stripper = new PDFTextStripperByArea()
            stripper.setSortByPosition(true)

            PDFTextStripper tStripper = new PDFTextStripper()
            String pdfFileInText = tStripper.getText(pdfDocument)

            // split by whitespace
            String[] lines = pdfFileInText.split("\\r?\\n")
            for (String line : lines)
            {
                line = line.toLowerCase().replaceAll("[^a-z0-9 .&]+", "")

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
                    if(line.contains("list of figures"))
                    {
                        isTOCFinish = true
                        documentCounter = 0
                        numberOfExternalDocuments = orderedDocumentNameList.size()
                        continue
                    }
                    else
                    {
                        String[] documentName = line.trim().split(" ")
                        int documentNameListSize = documentName.size()

                        if(documentNameListSize < 2)
                        {
                            continue
                        }

                        String parsedDocumentName = ""

                        for(int i = 0; i < documentNameListSize-1; i++)
                        {
                            if(!documentName[i].contains("...."))
                            {
                                if(documentName[i].equalsIgnoreCase("&"))
                                {
                                    parsedDocumentName  = parsedDocumentName + "and "
                                }
                                else
                                {
                                    if(documentName[i].endsWith("."))
                                    {
                                        documentName[i] = documentName[i].substring(0, documentName[i].length()-1)
                                    }
                                    parsedDocumentName  = parsedDocumentName + documentName[i] + " "
                                }
                            }
                            else
                                break
                        }

                        parsedDocumentName = parsedDocumentName.trim()
                        orderedDocumentNameList.add(parsedDocumentName)
                        documentLengthMap.put(parsedDocumentName, 0)
                    }
                }

                if(!isLOFFinish && isTOCFinish)
                {
                    if(line.length() < orderedDocumentNameList.get(0).length() && line.contains(orderedDocumentNameList.get(0).substring(0, line.trim().length())))
                    {
                        fragmentedHeadLine = line.trim() + " "
                        continue
                    }

                    if(orderedDocumentNameList.size() > 0 && line.contains(orderedDocumentNameList.get(0)) && !line.contains("..."))
                    {
                        isLOFFinish = true
                        continue
                    }
                }

                if(readerFlag && isTOCFinish && isLOFFinish)
                {
                    currentDocument = orderedDocumentNameList.get(documentCounter)

                    if(documentCounter + 1 < orderedDocumentNameList.size() && (line.trim().equalsIgnoreCase(orderedDocumentNameList.get(documentCounter + 1)) || line.trim().equalsIgnoreCase(orderedDocumentNameList.get(documentCounter + 1) + "s") || line.trim().equalsIgnoreCase(orderedDocumentNameList.get(documentCounter + 1).substring(0, orderedDocumentNameList.get(documentCounter + 1).length()-1))))
                    {
                        documentLengthMap.put(currentDocument, wordCounter)
                        wordCounter = 0
                        documentCounter += 1
                        continue
                    }
                    else if(documentCounter + 1 < orderedDocumentNameList.size() && line.length() < orderedDocumentNameList.get(documentCounter + 1).length() && line.contains(orderedDocumentNameList.get(documentCounter + 1).substring(0, line.trim().length())))
                    {
                        fragmentedHeadLine = line.trim() + " "
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

                            populateMaps(word, currentDocument)
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
        Pattern pattern = Pattern.compile("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")

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
                parsedString = parsedString.trim().replaceAll("[^A-Za-z_]+", " ")

                int wordCounter = 0
                List<String> wordsInParsedString = Arrays.asList(parsedString.split(" "))

                for(String word : wordsInParsedString)
                {
                    word = word.trim()
                    if(word.equalsIgnoreCase(""))
                    {
                        continue
                    }

                    if(word.contains("_"))
                    {
                        for(String splitWord : splitWordWithUnderscore(word))
                        {
                            splitWord = splitWord.toLowerCase()
                            populateMaps(splitWord, entry.getName())
                            wordCounter += 1
                        }
                    }

                    Matcher matcher = pattern.matcher(word)

                    if(matcher.find())
                    {
                        for(String splitWord : splitCamelCaseWord(word))
                        {
                            splitWord = splitWord.toLowerCase()
                            populateMaps(splitWord, entry.getName())
                            wordCounter += 1
                        }
                    }

                    word = word.toLowerCase()
                    populateMaps(word, entry.getName())
                    wordCounter += 1
                }

                documentCounter += 1
                orderedDocumentNameList.add(entry.getName())
                documentLengthMap.put(entry.getName(), wordCounter)
            }
        }
        numberOfSourceCodeDocuments = documentCounter
        zipFile.close()
    }

    List <String> splitCamelCaseWord(String camelCaseWord)
    {
        List <String> splitWords = camelCaseWord.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])").toList()

        return splitWords
    }

    List <String> splitWordWithUnderscore(String WordWithUnderscore)
    {
        List <String> splitWords = WordWithUnderscore.split("_").toList()

        return splitWords
    }

    void populateMaps(String word, String currentDocument)
    {
        if (isStopListWord(word))
        {
            return
        }

        if (wordsInDocumentSpaceMap.containsKey(word))
        {
            Map <String, Integer> wordFrequency = wordsInEachDocumentMap.get(word)
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
            Map <String, Integer> wordFrequency = new TreeMap <String, Integer>()
            wordFrequency.put(currentDocument, 1)
            wordsInEachDocumentMap.put(word, wordFrequency)
        }

        return
    }
}
