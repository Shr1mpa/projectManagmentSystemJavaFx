package com.example.projectsystem;

import com.example.projectsystem.Models.Role;
import com.example.projectsystem.Models.User;
import com.example.projectsystem.Validators.EmailValidator;
import com.example.projectsystem.Validators.PasswordValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private User user;
    public void initialize() {
        Tooltip tooltip = new Tooltip("The password must contain at least one capital letter, no spaces, and be at least 6 characters in length");
        passwordField.setTooltip(tooltip);
        String validationCriteria = "Validation Criteria:\n" +
                "- The email address must contain the '@' symbol\n" +
                "- Only Latin letters, digits, dots, plus sign, hyphen, underscore, and percent sign are allowed\n" +
                "- The email domain must contain at least one dot";

        String tooltipMessage = "Please enter a valid email address\n\n" + validationCriteria;

        Tooltip tooltip2 = new Tooltip(tooltipMessage);
        emailField.setTooltip(tooltip2);

        user = new User();
    }


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
        user.setUsername(usernameField.getText());
        user.setEmail(emailField.getText());
        user.setPassword(passwordField.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("personalInfo.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        InfoController controller = fxmlLoader.getController();
        controller.setUser(user);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root1));
        stage.show();
    }
    @FXML
    void onPasswordChanged() {
        String password = passwordField.getText();
        if (PasswordValidator.isValidPassword(password)) {
            passwordField.getStyleClass().clear();
            passwordField.getStyleClass().add("valid-password");
        } else {
            passwordField.getStyleClass().clear();
            passwordField.getStyleClass().add("invalid-password");
        }
    }
    @FXML
    void onEmailChanged() {
        String email = emailField.getText();
        if (EmailValidator.isValidEmail(email)) {
            emailField.getStyleClass().clear();
            emailField.getStyleClass().add("valid-password");
        } else {
            emailField.getStyleClass().clear();
            emailField.getStyleClass().add("invalid-password");
        }
    }

    
}
