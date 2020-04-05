package ia.notes.files;

import ia.notes.Notes;
import ia.notes.modifications.Modification;

import java.io.*;
import java.util.Scanner;
import java.util.TreeSet;

public class NotesFile extends AbstractFile {

    private Notes notes;

    public NotesFile(String name) {
        super(name, new File(FileManager.getNotesDirectory(), name));
    }

    @Override
    public synchronized void load() throws FileNotFoundException {
        this.notes = new Notes(this, name, new TreeSet<>());

        Scanner input = new Scanner(file);

        if (!input.hasNext()){
            return;
        }

        String title = input.nextLine();

        int lineNumber = 2;

        while (input.hasNextLine()){
            String modificationData = input.nextLine();
            Modification modification = Modification.fromData(modificationData);

            // Prevent malformed modification data from entering set
            if (modification == null){
                throw new IllegalStateException(
                        String.format("Invalid modification data on line %d in file %s", lineNumber, file.getAbsolutePath()));
            }

            notes.edit(modification);
            lineNumber++;
        }
    }

    public synchronized void save() throws IOException {

        // Prevent trying to save before loading
        if (this.notes == null){
            throw new IllegalStateException(String.format("Attempted save on null notes. (%s)", file.getAbsolutePath()));
        }

        PrintWriter writer = new PrintWriter(new File(directory, name), "UTF-8");
        writer.println(notes.getTitle());

        // Write all modification data in chronological order
        System.out.println("Size: " + notes.getModifications().size());
        for (Modification modification : notes.getModifications()){
            writer.println(modification);
        }

        writer.flush();
        writer.close();
    }

    public Notes getNotes() throws IllegalStateException {
        if (notes == null){
            throw new IllegalStateException("Attempted to access unloaded Notes instance: " + name);
        }
        return notes;
    }
}
