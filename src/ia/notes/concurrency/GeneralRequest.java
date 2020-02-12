package ia.notes.concurrency;

import ia.notes.Main;

public abstract class GeneralRequest extends IORequest {

    public abstract void run(Main main);
}
