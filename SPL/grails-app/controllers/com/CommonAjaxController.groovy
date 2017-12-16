package com

import grails.converters.JSON
import lsi.LSIAlgorithm

import java.text.DecimalFormat

class CommonAjaxController
{
    def mailService
    def FileService

    def mailSending()
    {
        try
        {
            String email = params?.email.toString()
            String message = params?.message.toString()
            String phone = params?.phone.toString()
            String name = params?.name.toString()
            mailService.sendMail
            {
                to "bsse0615@iit.du.ac.bd", "rayhan@du.ac.bd"
                cc email
                subject "Code Doctor Review"
                body message + "\n" + "From: " + name + "\n" + "Contact info: " + phone + "\n"
            }
            render true
        }
        catch(all)
        {
            log.error all
            render false
        }
    }

    def resultCalculation()
    {
        def uploadedSRS = params.srs
        def uploadedSC = params.sc
        int dimensionality = Integer.valueOf(params.dimensionality)
        double threshold = Double.valueOf(params.threshold)

        List<String> filePaths = new ArrayList<>()
        def fileDirectory = servletContext.getRealPath("/") + "upload"
        String stopWordListPath = servletContext.getRealPath("/") + "files\\stopWordList.txt"

        try
        {
            filePaths = FileService.saveFiles(uploadedSRS, uploadedSC, fileDirectory)

            LSIAlgorithm lsiAlgorithm = new LSIAlgorithm(filePaths.get(0), filePaths.get(1), stopWordListPath, dimensionality, threshold)
            lsiAlgorithm.createTermByDocumentMatrix()
            lsiAlgorithm.performSingularValueDecomposition()
            lsiAlgorithm.findSimilarities()

            LinkedHashMap <String, String> data = new LinkedHashMap<>()
            data.put("percentageResult", new DecimalFormat("##.##").format(lsiAlgorithm.calculateSimilarityInPercentage()))

            int counter = 0
            for(String scDocument : lsiAlgorithm.getSimilarityResultOfEachDocument().keySet())
            {
                Map<String, Double> srsDocuments = lsiAlgorithm.getSimilarityResultOfEachDocument().get(scDocument)

                for(String srsDocument : srsDocuments.keySet())
                {
                    data.put("scDoc_" + counter, scDocument)
                    data.put("srsDoc_" + counter, srsDocument)

                    String similarityScore = new DecimalFormat("##.########").format(srsDocuments.get(srsDocument))
                    if(similarityScore.equalsIgnoreCase("-0"))
                    {
                        similarityScore = "0"
                    }

                    data.put("sv_" + counter, similarityScore)
                    counter += 1
                }
            }

            FileService.removeFiles(fileDirectory, filePaths)
            render data as JSON
        }
        catch (Exception e)
        {
            FileService.removeFiles(fileDirectory, filePaths)
            throw e
        }
    }
}
