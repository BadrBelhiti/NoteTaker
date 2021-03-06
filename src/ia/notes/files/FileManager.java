package ia.notes.files;

import ia.notes.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class FileManager {

    private static final File MASTER_DIRECTORY = new File(".", "NoteTaker");
    private static final File NOTES_DIRECTORY = new File(MASTER_DIRECTORY, "notes");

    private Main main;
    private HashSet<NotesFile> notesFiles;

    public FileManager(Main main) {
        this.main = main;
        this.notesFiles = new HashSet<>();
        if (!initDirectories()){
            System.out.println("Critical error creating program directory. Closing application...");
            main.stop();
        }
    }

    private boolean initDirectories(){
        if (!NOTES_DIRECTORY.exists()){
            return NOTES_DIRECTORY.mkdirs();
        }
        return true;
    }

    public void loadNotes(){
        File notesDirectory = new File(MASTER_DIRECTORY, "notes");
        File[] notes = notesDirectory.listFiles();
        HashSet<NotesFile> notesFiles = new HashSet<>();

        if (notes == null){
            return;
        }

        for (File note : notes){
            NotesFile notesFile = new NotesFile(note.getName());
            loadFile(notesFile);
            notesFiles.add(notesFile);
        }

        this.notesFiles = notesFiles;
    }

    public void loadFile(NotesFile notesFile){
        try {
            notesFile.load();
            notesFiles.add(notesFile);
            main.getController().registerNotes(notesFile);
        } catch (IOException e){
            System.out.printf("Error loading notes file '%s'%n", notesFile.getName());
        }
    }

    public void deleteNotes(String notes){
        NotesFile notesFile = getNotesFile(notes);
        File file = new File(NOTES_DIRECTORY, notes);
        if (!file.delete()){
            System.out.printf("Error deleting file '%s'%n", notes);
        }
        notesFiles.remove(notesFile);
    }

    public void saveNotes(){
        for (NotesFile notesFile : notesFiles){
            System.out.printf("Saving notes file: %s...%n", notesFile.name);
            saveFile(notesFile);
            System.out.printf("Saved notes file: %s%n", notesFile.name);
        }
    }

    public void saveFile(NotesFile notesFile){
        try {
            notesFile.save();
        } catch (IOException e){
            String name = notesFile.getName();
            System.out.printf("Error saving notes file '%s'%n", name);
        }
    }

    public NotesFile getNotesFile(String name){
        for (NotesFile notesFile : notesFiles){
            if (notesFile.name.equals(name)){
                return notesFile;
            }
        }
        return null;
    }

    public HashSet<NotesFile> getNotesFiles() {
        return notesFiles;
    }

    public static File getMasterDirectory(){
        return MASTER_DIRECTORY;
    }

    public static File getNotesDirectory() {
        return NOTES_DIRECTORY;
    }
}
