package ia.notes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


public class Controller {

    @FXML
    private ListView notesList;

    private ObservableList<String> notes = FXCollections.observableArrayList();

    private Main main;

    public void init(Main main){
        this.main = main;
    }

    public void initialize(){

        System.out.println(main == null);

    }

}
