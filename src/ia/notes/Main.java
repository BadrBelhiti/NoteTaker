package ia.notes;

import ia.notes.concurrency.GeneralRequest;
import ia.notes.concurrency.IOManager;
import ia.notes.files.FileManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private FileManager fileManager;
    private IOManager ioManager;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Launch JavaFX
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setTitle("Note Taker");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();


        // Initialize managers
        this.ioManager = new IOManager(this);
        this.fileManager = new FileManager(this);

        // Asynchronously load notes from storage
        ioManager.executeLater(new GeneralRequest() {
            @Override
            public void run(Main main) {
                fileManager.loadNotes();
            }
        });


        // Initialize UI
        controller.init(this);

    }

    public void stop(){
        System.out.println("Closing...");

        // Save all notes
        fileManager.saveNotes();

        /*
        ioManager.executeLater(new GeneralRequest() {
            @Override
            public void run(Main main) {
                fileManager.saveNotes();
            }
        });
        */

        // Safely stop concurrency operations
        try {
            ioManager.shutdown();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
