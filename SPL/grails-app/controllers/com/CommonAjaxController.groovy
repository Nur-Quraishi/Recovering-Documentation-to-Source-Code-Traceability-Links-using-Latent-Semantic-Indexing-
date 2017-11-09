package com

class CommonAjaxController {
    def mailService

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
        }
        catch(Exception e)
        {
            e.printStackTrace()
            return false
        }

        return true
    }
}
