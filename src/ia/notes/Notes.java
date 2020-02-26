package ia.notes;

import ia.notes.files.NotesFile;
import ia.notes.modifications.Deletion;
import ia.notes.modifications.Insertion;
import ia.notes.modifications.Modification;

import javax.sound.sampled.Clip;
import java.io.IOException;
import java.util.TreeSet;

public class Notes implements Runnable {

    private static final long AUTOSAVE_INTERVAL_MS = 60 * 1000;
    private final Thread WORKER_THREAD;

    private boolean listening;
    private long lastSave;
    private boolean running = false;

    private String title;
    private TreeSet<Modification> modifications;
    private String mostRecent;
    private Clip audio;
    private NotesFile notesFile;

    public Notes(NotesFile notesFile, String title, TreeSet<Modification> modifications){
        this.notesFile = notesFile;
        this.title = title;
        this.modifications = modifications;
        this.mostRecent = "";

        this.WORKER_THREAD = new Thread(this);
    }

    public void start(){
        this.running = true;
        WORKER_THREAD.start();
    }

    @Override
    public void run() {
        while (running){

            if (System.currentTimeMillis() - lastSave > AUTOSAVE_INTERVAL_MS){

                try {
                    notesFile.save();

                    // Reset last save if successful
                    this.lastSave = System.currentTimeMillis();

                } catch (IOException e){
                    // TODO: Handle exception
                }

            } else {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    // TODO: Handle exception
                }
            }

        }
    }

    public synchronized boolean stop(){
        this.running = false;
        boolean success = true;
        try {
            WORKER_THREAD.join();
        } catch (InterruptedException e){
            success = false;
        }
        return success;
    }

    public void edit(Modification modification){
        modifications.add(modification);
        char[] chars = mostRecent.toCharArray();
        int pos = modification.getPos();

        if (modification instanceof Insertion){
            Insertion insertion = (Insertion) modification;
            char[] newChars = new char[chars.length + 1];

            System.arraycopy(chars, 0, newChars, 0, pos);
            newChars[pos] = insertion.getCharacter();
            System.arraycopy(chars, pos, newChars, pos + 1, newChars.length - pos - 1);

            chars = newChars;
        } else if (modification instanceof Deletion){
            char[] newChars = new char[chars.length - 1];
            System.out.println(pos);
            System.arraycopy(chars, 0, newChars, 0, pos);
            System.arraycopy(chars, pos, newChars, pos - 1, newChars.length - pos);
            chars = newChars;
        }
        this.mostRecent = new String(chars);
        System.out.println(mostRecent);
    }

    public void toggleMic(){
        this.listening = !listening;
    }

    public boolean isListening() {
        return listening;
    }

    public String getTitle() {
        return title;
    }

    public TreeSet<Modification> getModifications() {
        return modifications;
    }

    public String getMostRecent() {
        return mostRecent;
    }
}
