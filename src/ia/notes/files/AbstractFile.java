package ia.notes.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class AbstractFile {

    protected String name;
    protected File directory;
    protected File file;


    protected AbstractFile(String name, File directory){
        this.name = name;
        this.directory = directory;
        this.file = new File(directory, name);

        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e){
                System.out.printf("Error creating file '%s' in directory '%s'%n", name, directory.getAbsolutePath());
            }
        }

    }

    public abstract void load() throws FileNotFoundException;

    public abstract void save() throws IOException;

    public String getName() {
        return name;
    }

    public File getDirectory() {
        return directory;
    }
}
