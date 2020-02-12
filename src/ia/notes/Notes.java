package ia.notes;

import ia.notes.files.NotesFile;
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
}
