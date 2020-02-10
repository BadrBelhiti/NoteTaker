package ia.notes.files;

import ia.notes.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    private static final File MASTER_DIRECTORY = new File(".", "NoteTaker");

    private Main main;
    private ArrayList<NotesFile> notesFiles;

    public FileManager(Main main) {
        this.main = main;
    }

    public void loadNotes(){
        File notesDirectory = new File(MASTER_DIRECTORY, "notes");
        File[] notes = notesDirectory.listFiles();
        ArrayList<NotesFile> notesFiles = new ArrayList<>();

        if (notes == null){
            return;
        }

        for (File note : notes){
            NotesFile notesFile = new NotesFile(note.getName());

            try {
                notesFile.load();
                notesFiles.add(notesFile);
            } catch (IOException e){
                String name = notesFile.getName();
                System.out.println(String.format("Error loading notes file '%s'", name));
            }
        }

        System.out.println(notesFiles);
        this.notesFiles = notesFiles;
    }

    public void saveAll(){
        for (NotesFile notesFile : notesFiles){
            try {
                notesFile.save();
            } catch (IOException e){
                String name = notesFile.getName();
                System.out.println(String.format("Error saving notes file '%s'", name));
            }
        }
    }


    public static File getMasterDirectory(){
        return MASTER_DIRECTORY;
    }
}
