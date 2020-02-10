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
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Note Taker");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();


        // Initialize files
        this.fileManager = new FileManager(this);
        fileManager.loadNotes();


        // Initialize UI
        
    }


    public static void main(String[] args) {
        launch(args);
    }
}
