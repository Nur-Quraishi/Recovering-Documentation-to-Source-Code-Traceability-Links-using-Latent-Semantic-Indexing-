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

        List<String> filePaths = new ArrayList<>()
        def fileDirectory = servletContext.getRealPath("/") + "upload"
        String stopWordListPath = servletContext.getRealPath("/") + "files\\stopWordList.txt"

        try
        {
            filePaths = FileService.saveFiles(uploadedSRS, uploadedSC, fileDirectory)

            LSIAlgorithm lsiAlgorithm = new LSIAlgorithm(filePaths.get(0), filePaths.get(1), stopWordListPath, dimensionality)
            lsiAlgorithm.createTermByDocumentMatrix()
            lsiAlgorithm.performSingularValueDecomposition()
            lsiAlgorithm.findSimilarities()

            LinkedHashMap <String, String> data = new LinkedHashMap<>()
            data.put("percentageResult", new DecimalFormat("##.##").format(lsiAlgorithm.calculateSimilarityInPercentage()))

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
