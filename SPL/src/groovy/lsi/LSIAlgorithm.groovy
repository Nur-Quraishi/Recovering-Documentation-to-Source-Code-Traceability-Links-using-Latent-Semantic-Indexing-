package lsi

import Jama.Matrix
import Jama.SingularValueDecomposition

class LSIAlgorithm
{
    Corpus corpus
    SingularValueDecomposition singularValueDecomposition
    Matrix termByDocumentMatrix
    Matrix leftSingularMatrix
    Matrix rightSingularMatrix
    Matrix singularValueMatrix
    Map <String, Map <String, Double>> similarityResultOfEachDocument = new TreeMap<>()
    Map <String, Map <String, Double>> maxSimilarityResultOfEachDocument = new TreeMap<>()
    int dimensionOfLSISubspace

    LSIAlgorithm(String pdfPath, String zipPath, String stopWordPath, int dimensionOfLSISubspace) throws IOException
    {
        Corpus corpus = new Corpus(stopWordPath)
        corpus.createStopList()
        corpus.parsePDFFile(pdfPath)
        corpus.parseJavaFile(zipPath)

        this.corpus = corpus
        this.dimensionOfLSISubspace = dimensionOfLSISubspace
    }

    Map<String, Map<String, Double>> getSimilarityResultOfEachDocument()
    {
        return similarityResultOfEachDocument
    }

    void createTermByDocumentMatrix()
    {
        termByDocumentMatrix = new Matrix(corpus.getTermMatrixValues())
    }

    void performSingularValueDecomposition()
    {
        singularValueDecomposition = new SingularValueDecomposition(termByDocumentMatrix)
        leftSingularMatrix = singularValueDecomposition.getU()
        singularValueMatrix = singularValueDecomposition.getS()
        rightSingularMatrix = singularValueDecomposition.getV()

        if (dimensionOfLSISubspace > 0 && dimensionOfLSISubspace < leftSingularMatrix.getColumnDimension())
        {
            prepareMatrixForLSI()
        }
        else
        {
            dimensionOfLSISubspace = leftSingularMatrix.getColumnDimension()
        }
    }

    void prepareMatrixForLSI()
    {
        for(int row = 0; row < leftSingularMatrix.getRowDimension(); row++)
        {
            for (int column = dimensionOfLSISubspace; column < leftSingularMatrix.getColumnDimension(); column++)
            {
                leftSingularMatrix.set(row, column, 0)
            }
        }

        for(int row = dimensionOfLSISubspace; row < singularValueMatrix.getRowDimension(); row++)
        {
            for(int column = dimensionOfLSISubspace; column < singularValueMatrix.getColumnDimension(); column++)
            {
                singularValueMatrix.set(row, column, 0)
            }
        }

        for(int row = 0; row < rightSingularMatrix.getRowDimension(); row++)
        {
            for (int column = dimensionOfLSISubspace; column < rightSingularMatrix.getColumnDimension(); column++)
            {
                rightSingularMatrix.set(row, column, 0)
            }
        }
    }

    Matrix getIndividualDocumentMatrix(int columnNumber)
    {
        Matrix columnMatrix = new Matrix(rightSingularMatrix.getColumnDimension(), 1)
        Matrix individualDocumentMatrix

        for (int i = 0; i<columnMatrix.getRowDimension(); i++)
        {
            columnMatrix.set(i, 0, rightSingularMatrix.get(columnNumber, i))
        }

        individualDocumentMatrix = leftSingularMatrix.times(singularValueMatrix)
        individualDocumentMatrix = individualDocumentMatrix.times(columnMatrix)

        return individualDocumentMatrix
    }

    double calculateVectorModulus(Matrix matrix)
    {
        double modulusResult = 0

        for (int i = 0; i < matrix.getRowDimension(); i++)
        {
            modulusResult += Math.pow(matrix.get(i, 0), 2)
        }

        modulusResult = Math.sqrt(modulusResult)
        return modulusResult
    }

    double measureSimilarity(Matrix srsDocumentMatrix, Matrix scDocumentMatrix)
    {
        Matrix productMatrix
        double denominator, similarityResult

        productMatrix = (srsDocumentMatrix.transpose()).times(scDocumentMatrix)
        denominator = calculateVectorModulus(srsDocumentMatrix) * calculateVectorModulus(scDocumentMatrix)

        similarityResult = productMatrix.det() / denominator
        return similarityResult
    }

    void findSimilarities()
    {
        int numberOfExternalDocuments = corpus.getNumberOfExternalDocuments()
        int numberOfSourceCodeDocuments = corpus.getNumberOfSourceCodeDocuments()
        int totalNumberOfDocuments = numberOfExternalDocuments + numberOfSourceCodeDocuments

        for(int i= numberOfExternalDocuments; i < totalNumberOfDocuments; i++)
        {
            Matrix scDocumentMatrix = getIndividualDocumentMatrix(i)
            Map <String, Double> similarityMap = new TreeMap<>()

            for(int j = 0; j < numberOfExternalDocuments; j++)
            {
                Matrix srsDocumentMatrix = getIndividualDocumentMatrix(j)
                similarityMap.put(corpus.getOrderedDocumentNameList().get(j), measureSimilarity(srsDocumentMatrix, scDocumentMatrix))
            }
            similarityResultOfEachDocument.put(corpus.getOrderedDocumentNameList().get(i), similarityMap)
        }
    }

    void getMaxSimilarities()
    {
        for(String scDocoment : similarityResultOfEachDocument.keySet())
        {
            Map <String, Double> similarityValueMap = similarityResultOfEachDocument.get(scDocoment)
            Double maxSimilarityValue = Double.NEGATIVE_INFINITY
            String similarSrsDocument

            for(String srsDocument : similarityValueMap.keySet())
            {
                Double similarityValue = similarityValueMap.get(srsDocument)
                if(similarityValue > maxSimilarityValue)
                {
                    maxSimilarityValue = similarityValue
                    similarSrsDocument = srsDocument
                }
            }

            if(maxSimilarityValue > 0.07)
            {
                similarityValueMap = new TreeMap<>()
                similarityValueMap.put(similarSrsDocument, maxSimilarityValue)
                maxSimilarityResultOfEachDocument.put(scDocoment, similarityValueMap)
            }
        }
    }

    double calculateSimilarityInPercentage()
    {
        double finalResult

        getMaxSimilarities()
        finalResult = (maxSimilarityResultOfEachDocument.size() / similarityResultOfEachDocument.size()) * 100.0

        return  finalResult
    }
}
