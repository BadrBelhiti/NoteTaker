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
        Controller controller = loader.getController();
        Parent root = loader.load();
        primaryStage.setTitle("Note Taker");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();


        // Initialize files
        this.fileManager = new FileManager(this);
        fileManager.loadNotes();


        // Initialize UI
        controller.init(this);

    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
