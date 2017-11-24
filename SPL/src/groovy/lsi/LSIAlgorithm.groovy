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
    Map <String, Double> similarityResult = new TreeMap<>()
    int dimensionOfLSISubspace

    LSIAlgorithm(String pdfPath, String zipPath, String stopWordPath, int dimensionOfLSISubspace) throws IOException
    {
        Corpus corpus = new Corpus(stopWordPath)
        corpus.createStopList()
        corpus.parsePDFFile(pdfPath)
        corpus.parseJavaFile(zipPath)

        this.dimensionOfLSISubspace = dimensionOfLSISubspace
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
        for(int i = 0; i < leftSingularMatrix.getRowDimension(); i++)
        {
            for (int j = dimensionOfLSISubspace; j < leftSingularMatrix.getColumnDimension(); j++)
            {
                leftSingularMatrix.set(i, j, 0)
            }
        }

        for(int i = dimensionOfLSISubspace; i < singularValueMatrix.getRowDimension(); i++)
        {
            for(int j = dimensionOfLSISubspace; j < singularValueMatrix.getColumnDimension(); j++)
            {
                singularValueMatrix.set(i, j, 0)
            }
        }

        for(int i = 0; i < rightSingularMatrix.getRowDimension(); i++)
        {
            for (int j = dimensionOfLSISubspace; j < rightSingularMatrix.getColumnDimension(); j++)
            {
                rightSingularMatrix.set(i, j, 0)
            }
        }
    }

}
