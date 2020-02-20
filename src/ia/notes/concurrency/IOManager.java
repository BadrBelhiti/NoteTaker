package ia.notes.concurrency;

import ia.notes.Main;
import ia.notes.files.NotesFile;

import java.util.HashSet;
import java.util.LinkedList;

public class IOManager implements Runnable {

    private final Thread IO_THREAD;

    private Main main;
    private volatile LinkedList<IORequest> queue;
    private HashSet<Thread> workerThreads;
    private volatile boolean running = true;


    public IOManager(Main main){
        this.IO_THREAD = new Thread(this);

        this.main = main;
        this.queue = new LinkedList<>();
        this.workerThreads = new HashSet<>();

        IO_THREAD.start();
    }


    public void run(){

        while (running){

            // Get top priority request
            IORequest currentRequest = null;

            if (!queue.isEmpty()){
                currentRequest = queue.getFirst();
            }

            // Execute pending request if exists then remove request from queue
            if (currentRequest != null){
                System.out.println("Executing request...");
                currentRequest.run(main);
                System.out.println("Request executed");
                queue.removeFirst();
            }

        }

    }

    public synchronized void executeLater(IORequest request){
        queue.addLast(request);
    }

    public void loadLater(NotesFile notesFile){
        IORequest request = new FileRequest(notesFile, FileRequest.FileOperation.LOAD);
        executeLater(request);
    }

    public void saveLater(NotesFile notesFile){
        IORequest request = new FileRequest(notesFile, FileRequest.FileOperation.SAVE);
        executeLater(request);
    }

    public void registerThread(Thread thread){
        workerThreads.add(thread);
    }

    public void shutdown() throws InterruptedException {
        this.running = false;
        for (Thread thread : workerThreads){
            thread.join();
        }
        IO_THREAD.join();
    }

}
