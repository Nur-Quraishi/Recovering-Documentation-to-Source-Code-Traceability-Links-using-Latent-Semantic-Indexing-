package com

class CommonAjaxController {
    def mailService
    def FileService

    def mailSending()
    {
        try{
            String email = params?.email.toString()
            String message = params?.message.toString()
            String phone = params?.phone.toString()
            String name = params?.name.toString()
            mailService.sendMail {
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
        def fileDirectory = servletContext.getRealPath("/") + "upload"
        def uploadedSRS = params.srs
        def uploadedSC = params.sc

        File srs = new File(fileDirectory, "SRS.pdf")
        File sc = new File(fileDirectory, "SC.zip")
        uploadedSRS.transferTo(srs)
        uploadedSC.transferTo(sc)



        FileService.readPDF(srs.absolutePath)

        srs.delete()
        sc.delete()

        render true
    }
}
