package com

import lsi.Corpus

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
        def dimensionality = params.dimensionality

        List<String> filePaths = new ArrayList<>()
        def fileDirectory = servletContext.getRealPath("/") + "upload"
        String stopWordListPath = servletContext.getRealPath("/") + "files\\stopWordList.txt"

        try
        {
            filePaths = FileService.saveFiles(uploadedSRS, uploadedSC, fileDirectory)

            Corpus corpus = new Corpus(stopWordListPath)
            corpus.parsePDFFile(filePaths.get(0))
            corpus.parseJavaFile(filePaths.get(1))

            FileService.removeFiles(fileDirectory, filePaths)
            render true
        }
        catch (Exception e)
        {
            FileService.removeFiles(fileDirectory, filePaths)
            throw e
        }
    }
}
