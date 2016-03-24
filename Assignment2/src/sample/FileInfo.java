package sample;

import java.io.File;
public class FileInfo {


    private String fileNames;
    //private String realFileName;

    public FileInfo(String fileNames) {
        this.fileNames = fileNames;
        //this.realFileName = "";
    }

    public String getFileNames()
    {
        return fileNames;
    }

    public void setFileNames(String s)
    {
        this.fileNames = s;
    }
}
