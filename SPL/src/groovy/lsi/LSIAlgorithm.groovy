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

    //TODO
}
