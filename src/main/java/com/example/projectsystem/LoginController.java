package com.example.projectsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label changeScene;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordFiels;

    @FXML
    private CheckBox showPassword;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField textField;



    @FXML
    void changeToRegister(MouseEvent event) throws IOException {
        Stage stage = (Stage) changeScene.getScene().getWindow();
        // do what you have to do
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegisterForm.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Register");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    void showPassword(ActionEvent event) {
        if (showPassword.isSelected()) {
            textField.setText(passwordFiels.getText());
            textField.setVisible(true);
            passwordFiels.setVisible(false);
            return;

        }
        passwordFiels.setText(textField.getText());
        passwordFiels.setVisible(true);
        textField.setVisible(false);
    }

}