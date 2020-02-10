package ia.notes.files;

import ia.notes.Notes;
import ia.notes.modifications.Modification;

import java.io.*;
import java.util.Scanner;
import java.util.TreeSet;

public class NotesFile extends AbstractFile {

    private Notes notes;

    public NotesFile(String name) {
        super(name, new File(FileManager.getMasterDirectory(), "notes"));
    }

    @Override
    public synchronized void load() throws FileNotFoundException {
        Scanner input = new Scanner(file);
        String title = input.nextLine();
        TreeSet<Modification> modifications = new TreeSet<>();

        int lineNumber = 2;

        while (input.hasNextLine()){
            String modificationData = input.nextLine();
            Modification modification = Modification.fromData(modificationData);

            // Prevent malformed modification data from entering set
            if (modification == null){
                throw new IllegalStateException(
                        String.format("Invalid modification data on line %d in file %s", lineNumber, file.getAbsolutePath()));
            }

            modifications.add(modification);
            lineNumber++;
        }

        this.notes = new Notes(this, title, modifications);
    }

    public synchronized void save() throws IOException {

        // Prevent trying to save before loading
        if (this.notes == null){
            throw new IllegalStateException(String.format("Attempted save on null notes. (%s)", file.getAbsolutePath()));
        }

        PrintWriter writer = new PrintWriter(new File(directory, name), "UTF-8");
        writer.println(notes.getTitle());

        // Write all modification data in chronological order
        for (Modification modification : notes.getModifications()){
            writer.println(modification);
        }
    }

    public Notes getNotes() throws IllegalStateException {
        if (notes == null){
            throw new IllegalStateException("Attempted to access unloaded Notes instance: " + name);
        }
        return notes;
    }
}
