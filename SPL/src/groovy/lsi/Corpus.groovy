package lsi

import java.io.*

abstract class Corpus
{
    protected String stopListPath
    protected List<String> stopWordList = new ArrayList<>()
    protected Map<String, Integer> wordsInDocumentSpaceMap = new TreeMap<>()
    protected Map<String, Integer> documentLengthMap = new TreeMap<>()
    protected Map<String, Map<String, Integer>> wordsInEachDocumentMap = new TreeMap<>()

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

    abstract void parseFile(String filePath)
}
