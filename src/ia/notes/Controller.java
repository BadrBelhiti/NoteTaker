package ia.notes;

import ia.notes.files.FileManager;
import ia.notes.files.NotesFile;
import ia.notes.modifications.Modification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;


public class Controller {

    @FXML
    private ListView<String> notesList;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextArea notesArea;

    private ObservableList<String> notes = FXCollections.observableArrayList();

    private Main main;
    private FileManager fileManager;
    private NotesFile currentNotes;

    public void init(Main main){
        this.main = main;
        this.fileManager = main.getFileManager();
        notesArea.setEditable(false);
    }

    public void initialize(){

        notesList.setItems(notes);

        // Show Note Creation dialog when user clicks on 'Add' button and handle input
        addButton.setOnAction((e) -> {
            TextInputDialog dialog = new TextInputDialog("untitled notes");
            dialog.setTitle("Add Notes");
            dialog.setHeaderText(null);
            dialog.setContentText("Name of notes:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> notes.add(name));
            if (result.isPresent()){
                String notes = result.get();
                NotesFile notesFile = new NotesFile(notes);
                fileManager.loadFile(notesFile);
            }
        });


        // Show Deletion Confirmation dialog when user clicks 'Remove' button and handle input
        removeButton.setOnAction((e) -> {

            String name = notesList.getSelectionModel().getSelectedItem();

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
                fileManager.deleteNotes(name);
            }

        });

        // Update Note's modifications set upon editing
        notesArea.textProperty().addListener((e, o , n) -> {
            Modification modification = Utils.getChange(o, n, System.currentTimeMillis());
            if (currentNotes != null){
                currentNotes.getNotes().edit(modification);
            }
        });

        // Select which Notes to show
        notesList.getSelectionModel().selectedItemProperty().addListener((e, previous, selected) -> {
            NotesFile notesFile = main.getFileManager().getNotesFile(selected);
            if (notesFile != null){
                this.currentNotes = notesFile;
                notesArea.setEditable(true);
            } else {
                notesArea.setEditable(false);
            }
            notesArea.clear();
        });

    }

}
