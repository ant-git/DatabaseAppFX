package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by antant on 20/01/16.
 */
public class DBManager {
    Connection connection;
    private ObservableList<ObservableList> content = FXCollections.observableArrayList();

    public DBManager(Connection connection) {
        this.connection = connection;
    }


    private ResultSet getResultSet(String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return resultSet;
    }

    public ObservableList<String> getDBtables() throws SQLException {
        ObservableList<String> tableNames = FXCollections.observableArrayList();
        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        while (rs.next()) {
            tableNames.add(rs.getString(3));
        }

        return tableNames;
    }

    public ArrayList<TableColumn> getTableColumns(String tableName) throws SQLException {
        ResultSet resultSet = getResultSet(tableName);
        ArrayList<TableColumn> columns = new ArrayList<>();
        int columnCount = getResultSet(tableName).getMetaData().getColumnCount();
        for(int i=0; i<columnCount; i++){
            final int j = i;
            TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j));
                }
            });


            Callback<TableColumn<Map, String>, TableCell<Map, String>> cellFactoryForMap
                    = p -> new TextFieldTableCell(new StringConverter() {
                        @Override
                        public String toString(Object t) {
                            return t.toString();
                        }

                        @Override
                        public Object fromString(String string) {
                            return string;
                        }
                    });

           // if (j != 1) {
                col.setCellFactory(cellFactoryForMap);
           // }
            col.setMaxWidth(200);
            columns.add(col);
        }
        resultSet.close();
        return columns;
    }

    public ObservableList<ObservableList> getTableContent(String tableName) throws SQLException {
            ResultSet resultSet = getResultSet(tableName);
            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    row.add(resultSet.getString(i));
                }
                content.add(row);
            }

            resultSet.close();
            return content;
    }

    public void cellUpdate(String dbname, String columnName, String tableName, String id, String idValue, String newValue) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE '?'.'?' SET '?' = '?' WHERE '?' = '?'");
        //"UPDATE `mydbtest`.`animal` SET `anim_name`='batch3fdf' WHERE `id`='4'";
        preparedStatement.setString(1, dbname);
        preparedStatement.setString(2, tableName);
        preparedStatement.setString(3, columnName);
        preparedStatement.setString(4, newValue);
        preparedStatement.setString(4, id);
        preparedStatement.setString(6, idValue);
        System.out.println(preparedStatement.toString());

    }

}
