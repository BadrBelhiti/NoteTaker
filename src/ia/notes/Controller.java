package ia.notes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;


public class Controller {

    @FXML
    private ListView notesList;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    private ObservableList<String> notes = FXCollections.observableArrayList();

    private Main main;

    public void init(Main main){
        this.main = main;
    }

    public void initialize(){

        notesList.setItems(notes);

        addButton.setOnAction((e) -> {
            TextInputDialog dialog = new TextInputDialog("untitled notes");
            dialog.setTitle("Add Notes");
            dialog.setHeaderText(null);
            dialog.setContentText("Name of notes:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> notes.add(name));
        });

        removeButton.setOnAction((e) -> {

            String name = (String) notesList.getSelectionModel().getSelectedItem();

            if (name == null){
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle(String.format("Confirm permanently deleting '%s'", name));
            confirmation.setHeaderText(null);
            confirmation.setContentText("This action cannot be undone. Are you sure you wish to delete " + name + "?");

            ButtonType confirm = new ButtonType("Delete " + name);

            confirmation.getButtonTypes().set(0, confirm);

            Optional<ButtonType> clicked = confirmation.showAndWait();

            if (clicked.get() == confirm){
                notes.remove(name);
            }

        });

    }

}
