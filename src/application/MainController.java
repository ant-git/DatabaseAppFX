package application;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by antant on 19/01/16.
 */
public class MainController implements Initializable {
    private Connection connection;
    private DBManager dbManager;
    @FXML
    private ComboBox cbTables = new ComboBox(); //** never initialize thin in initialize() method

    @FXML
    private TableView tableView = new TableView();

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
        cbTables.setItems(FXCollections.observableArrayList(dbManager.getDBtables()));

        cbTables.getSelectionModel().select(0);
    }
    public void generateTableView() throws SQLException {
        tableView.getItems().clear();

        String tableName = cbTables.getSelectionModel().getSelectedItem().toString();
        ArrayList<TableColumn> columns = dbManager.getTableColumns(tableName);
        tableView.getColumns().clear();

        for(TableColumn column : columns){
            tableView.getColumns().add(column);
        }

        tableView.setItems(dbManager.getTableContent(tableName));

    }


}
