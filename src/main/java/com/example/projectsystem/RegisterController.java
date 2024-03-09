package com.example.projectsystem;

import com.example.projectsystem.Models.Role;
import com.example.projectsystem.Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RegisterController {

    @FXML
    private Label changeScene;

    @FXML
    private Button continueButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox showPassword;

    @FXML
    private TextField textField;

    @FXML
    private TextField usernameField;


    @FXML
    void changeToLOgin(MouseEvent event) throws IOException {
        Stage stage = (Stage) changeScene.getScene().getWindow();
        // do what you have to do
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
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
            textField.setText(passwordField.getText());
            textField.setVisible(true);
            passwordField.setVisible(false);
            return;

        }
        passwordField.setText(textField.getText());
        passwordField.setVisible(true);
        textField.setVisible(false);

    }
    @FXML
    void continueRegistration(ActionEvent event) throws IOException {

        Stage stage = (Stage) continueButton.getScene().getWindow();
        // do what you have to do
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("personalInfo.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Register");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    
}
