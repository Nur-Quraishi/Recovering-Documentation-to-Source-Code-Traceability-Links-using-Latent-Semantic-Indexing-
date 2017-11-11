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
        String srsPath = params.srs
        String scPath = params.sc

        FileService.readPDF(srsPath)

        render true
    }
}
