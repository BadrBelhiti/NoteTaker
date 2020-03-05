package ia.notes;

import ia.notes.concurrency.IOManager;
import ia.notes.files.NotesFile;
import ia.notes.modifications.Deletion;
import ia.notes.modifications.Insertion;
import ia.notes.modifications.Modification;
import javafx.application.Platform;

import javax.sound.sampled.Clip;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
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

    private ArrayList<Modification> currentPlayback;
    private String playbackState;
    private Controller controller;
    private int cursor;
    private boolean playbackPlaying = false;

    public Notes(NotesFile notesFile, String title, TreeSet<Modification> modifications){
        this.notesFile = notesFile;
        this.title = title;
        this.modifications = modifications;
        this.mostRecent = "";

        this.WORKER_THREAD = new Thread(this);
    }

    public void start(IOManager ioManager){
        this.running = true;
        WORKER_THREAD.start();
        ioManager.registerThread(WORKER_THREAD);
    }

    @Override
    public void run() {
        while (running){

            if (playbackPlaying){
                if (hasNext()){
                    next();

                    if (cursor < currentPlayback.size()){
                        Modification curr = currentPlayback.get(cursor - 1);
                        Modification next = currentPlayback.get(cursor);

                        try {
                            Thread.sleep(next.getTime() - curr.getTime());
                        } catch (InterruptedException e){
                            System.out.println("Playback out of sync");
                        }

                    }

                }


            } else {
                if (System.currentTimeMillis() - lastSave > AUTOSAVE_INTERVAL_MS) {

                    try {
                        notesFile.save();

                        // Reset last save if successful
                        this.lastSave = System.currentTimeMillis();

                    } catch (IOException e) {
                        System.out.println("Failed autosave");
                    }

                } else {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Thread sleep failed");
                    }
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

    public void startPlayback(Controller controller){
        this.controller = controller;
        this.playbackPlaying = true;
        this.currentPlayback = new ArrayList<>();
        this.playbackState = "";
        this.cursor = 0;
        currentPlayback.addAll(modifications);
    }

    public void stopPlayback(){
        this.playbackPlaying = false;
    }


    public void next(){
        if (!playbackPlaying){
            throw new IllegalStateException("Playback not initialized");
        }

        if (cursor >= currentPlayback.size()){
            throw new IllegalStateException("Playback exceeded modification set");
        }

        this.playbackState = applyModification(playbackState, getNext());
        cursor++;

        Platform.runLater(() -> controller.showPlaybackState(this));
    }

    private Modification getNext(){
        return currentPlayback.get(cursor);
    }

    public void edit(Modification modification){
        modifications.add(modification);
        this.mostRecent = applyModification(mostRecent, modification);
    }

    private String applyModification(String text, Modification modification){
        char[] chars = text.toCharArray();
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

            System.arraycopy(chars, 0, newChars, 0, pos);
            System.arraycopy(chars, pos, newChars, Math.max(pos - 1, 0), newChars.length - pos);

            chars = newChars;
        }
        return new String(chars);
    }

    public boolean hasNext(){
        return cursor != modifications.size();
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

    public String getPlaybackState() {
        return playbackState;
    }

    public boolean isPlaybackPlaying() {
        return playbackPlaying;
    }
}
