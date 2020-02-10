package ia.notes;

import ia.notes.files.FileManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private FileManager fileManager;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Launch JavaFX
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setTitle("Note Taker");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setOnCloseRequest((e) -> stop());
        primaryStage.show();


        // Initialize files
        this.fileManager = new FileManager(this);
        fileManager.loadNotes();


        // Initialize UI
        controller.init(this);

    }

    public void stop(){
        // TODO: Save files, close IO threads
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
