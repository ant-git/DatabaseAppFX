package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Anton
 */
public class MainController implements Initializable {
    private Connection connection;
    private DBManager dbManager;
    private String dbname;
    @FXML
    private ComboBox cbTables = new ComboBox(); //** never initialize thin in initialize() method
    @FXML
    private TableView tableView = new TableView();
    @FXML
    private Label lblError;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbManager = new DBManager(connection);
        try {
            setComboBox();
            generateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private void setComboBox() throws SQLException {
        ObservableList<String> tableNames = FXCollections.observableArrayList(dbManager.getDBtables());
        if(!tableNames.isEmpty()) {
            cbTables.setItems(tableNames);
            cbTables.getSelectionModel().select(0);
        }
        else {
            lblError.setTextFill(Color.RED);
            lblError.setText("There are no tables in the selected schema!");
        }
    }
    public void generateTableView() throws SQLException {
        tableView.getItems().clear();
        if(!cbTables.getItems().isEmpty()) {
            //tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
          //  tableView.setEditable(true);
          //  tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                //Check whether item is selected and set value of selected item to Label
               // if (tableView.getSelectionModel().getSelectedItem() != null) {
                  //  TableView.TableViewSelectionModel selectionModel = tableView.getSelectionModel();
                  //  ObservableList selectedCells = selectionModel.getSelectedCells();
                   // TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                   // Object val = tablePosition.getTableColumn().getCellData(newValue);

                    //System.out.println("Selected Value " + val);
                    //System.out.println("Selected row " + newValue);
                   // ObservableList<TableColumn> columns = tableView.getColumns();
                    //String idColumn = columns.get(0).getText();
                    //System.out.println(idColumn);
                    //System.out.printf("DB:%s\nColumn: %s\nid: %s\nidValue:\nnewValue: %s\n", dbname, tablePosition.getTableColumn().getText(), idColumn, newValue.toString());

                    //System.out.println(newValue.getClass().toString());
                    //UPDATE `mydbtest`.`animal` SET `anim_name`='batch3fdf' WHERE `id`='4';
                    //dbManager.cellUpdate(dbname, tablePosition.getTableColumn().getText(), newValue.toString(), );
               // }
            //});

            String tableName = cbTables.getSelectionModel().getSelectedItem().toString();
            ArrayList<TableColumn> columns = dbManager.getTableColumns(tableName);
            tableView.getColumns().clear();

            for (TableColumn column : columns) {
                tableView.getColumns().add(column);
            }

            tableView.setItems(dbManager.getTableContent(tableName));
        }

    }

    public void setDBname(String dbname){
        this.dbname = dbname;
    }

}
