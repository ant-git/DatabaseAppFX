package application;

import com.mysql.fabric.jdbc.FabricMySQLDriver;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Anton
 */
public class DBConnector  {

    private String exceptionText;

    public static void main(String[] args) {

    }

    public Connection connectToDB(String URL, String username, String password){

        exceptionText = "";
        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);

        } catch (SQLException e) {
            exceptionText = "Could not load driver";
        }

        try {

            Connection conn = DriverManager.getConnection(URL, username, password);
            return conn;
        } catch (MySQLSyntaxErrorException e) {
            System.out.println(e);
            exceptionText = "Could not find database with that name!";
            return null;
        } catch (SQLException e) {
            exceptionText = "Something is wrong! Check your input!";
            return null;
        }


    }

    public String getExceptionText(){
        return exceptionText;
    }

}
