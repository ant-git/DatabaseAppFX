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
