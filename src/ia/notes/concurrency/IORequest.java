package ia.notes.concurrency;

import ia.notes.Main;
import ia.notes.files.NotesFile;

public abstract class IORequest {

    protected NotesFile notesFile;

    protected IORequest(){

    }

    protected IORequest(NotesFile notesFile){
        this.notesFile = notesFile;
    }

    public abstract void run(Main main);

}
