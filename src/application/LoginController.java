package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    @FXML
    private TextField user;
    @FXML
    private PasswordField password;
    @FXML
    private TextField URL;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblFullURL;
    @FXML
    private TextField dbname;
    @FXML
    private TextField port;
    @FXML
    private Button btnConnect;

    Connection connection;



    public void connect() throws IOException {
        lblStatus.setText("");
        if(txtFieldsCheck()) {
            DBConnector connector = new DBConnector();
            connection = connector.connectToDB(getFullURL(), user.getText(), password.getText());
            if (connection != null) {
                System.out.println("Connection is successful!...");
                lblStatus.setTextFill(Color.GREEN);
                setLblStatus("Connection is successful!");
                openMainWindow();
            } else{
                lblStatus.setTextFill(Color.RED);
                setLblStatus(connector.getExceptionText());
            }
        }
        else
        {
            lblStatus.setText("Please, fill in all of the required fields! ");
            password.setText("");
        }
    }

    //**to check if text fields are empty or not
    public boolean txtFieldsCheck(){
        if(!user.getText().trim().isEmpty()
                && !URL.getText().trim().isEmpty()
                && !password.getText().trim().isEmpty()
                && !dbname.getText().trim().isEmpty()){
           return true;
        }
        else{
            return false;
        }
    }


    //** to get button press actions
    @FXML
    public void enterKeyPressed(KeyEvent e) throws IOException {
        if(e.getCode().toString().equals("ENTER"))
        {
           connect();
        }
    }

    @FXML
    public void updateFullURL(){
        lblFullURL.setText(getFullURL());
    }

    private String getFullURL(){
        StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://");
        sb.append(URL.getText() + ":");
        sb.append(port.getText() + "/");
        sb.append(dbname.getText());

        return sb.toString();
    }
    public Connection getConnection() {
        return connection;
    }


    public void setLblStatus(String status){
        lblStatus.setText(status);
    }

    //to open new window and close current
    private void openMainWindow() throws IOException {
        MainController mainController = new MainController();
        mainController.setConnection(connection);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        fxmlLoader.setController(mainController);
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("MySQL Database App");
        stage.show();
        Stage currentStage = (Stage) btnConnect.getScene().getWindow();
        currentStage.close();
    }


    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {

    }
}
