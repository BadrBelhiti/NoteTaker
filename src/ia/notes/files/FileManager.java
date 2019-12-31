package ia.notes.files;

import ia.notes.Main;

import java.io.File;

public class FileManager {

    private Main main;
    private static File masterDirectory;

    public FileManager(Main main) {
        this.main = main;
        masterDirectory = new File(".", "NoteTaker");
    }

    public static File getMasterDirectory(){
        return masterDirectory;
    }
}
