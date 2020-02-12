package ia.notes.concurrency;

import ia.notes.Main;
import ia.notes.files.NotesFile;

public class FileRequest extends IORequest {

    private FileOperation fileOperation;

    public FileRequest(NotesFile notesFile, FileOperation fileOperation) {
        super(notesFile);
        this.fileOperation = fileOperation;
    }

    @Override
    public void run(Main main) {
        if (fileOperation == FileOperation.LOAD){
            main.getFileManager().loadFile(notesFile);
        } else if (fileOperation == FileOperation.SAVE){
            main.getFileManager().saveFile(notesFile);
        }
    }

    public enum FileOperation {
        LOAD, SAVE;
    }

}
