package com.example.projectsystem;

import com.example.projectsystem.Managers.AuthenticationManager;
import com.example.projectsystem.Models.Role;
import com.example.projectsystem.Models.User;
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
import java.util.Optional;

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
    private User user;



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
    @FXML
    void authorize(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordFiels.getText();

        Optional<User> userOptional = AuthenticationManager.authenticate(username, password);
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (user.getRole() == Role.MANAGER) {
                openManagerScene();
            } else {
                openWorkerScene();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authorize error");
            alert.setHeaderText("Invalid username or password");
            alert.showAndWait();
        }

        usernameField.clear();
        passwordFiels.clear();
        textField.clear();
    }
    private void openManagerScene() throws IOException {
        Stage stage = (Stage) changeScene.getScene().getWindow();
        // do what you have to do
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("managerForm.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        ManagerFormController controller = fxmlLoader.getController();
        controller.setUser(user);
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Register");
        stage.setScene(new Scene(root1));
        stage.show();
    }
    private void openWorkerScene() throws IOException {
        Stage stage = (Stage) changeScene.getScene().getWindow();
        // do what you have to do
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("workerForm.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Register");
        stage.setScene(new Scene(root1));
        stage.show();
    }

}