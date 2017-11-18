package com

import grails.transaction.Transactional
import org.apache.commons.io.FileUtils

@Transactional
class FileService
{
    def saveFiles(def uploadedSRS, def uploadedSC, def fileDirectory) throws Exception
    {
        List<String> filePaths = new ArrayList<>()

        UUID uuid = UUID.randomUUID()
        String srsName = uuid.toString() + System.currentTimeMillis().toString() + ".pdf"
        String scName = uuid.toString() + System.currentTimeMillis().toString() + ".zip"

        File uploadDirectory = new File(fileDirectory)
        if(!uploadDirectory.exists())
        {
            uploadDirectory.mkdir()
        }

        File srs = new File(fileDirectory, srsName)
        File sc = new File(fileDirectory, scName)

        uploadedSRS.transferTo(srs)
        uploadedSC.transferTo(sc)

        filePaths.add(srs.absolutePath)
        filePaths.add(sc.absolutePath)

        return filePaths
    }

    def removeFiles(def fileDirectory, List<String> filePaths) throws Exception
    {
        File uploadDirectory = new File(fileDirectory)

        FileUtils.deleteQuietly(FileUtils.getFile(filePaths.get(0)))
        FileUtils.deleteQuietly(FileUtils.getFile(filePaths.get(1)))

        if(uploadDirectory.list().length == 0)
            FileUtils.deleteDirectory(uploadDirectory)
    }
}
