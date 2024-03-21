package com.example.projectsystem;

import com.example.projectsystem.Models.DeveloperLevel;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ManagerFormController {

    @FXML
    private Button accountButton;

    @FXML
    private Tab accountTab;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ComboBox<DeveloperLevel> levelBox;

    @FXML
    private Circle photoUser;

    @FXML
    private Circle photoUserInAccount;

    @FXML
    private Button projectButton;

    @FXML
    private Tab projectTab;

    @FXML
    private ComboBox<Role> roleBox;

    @FXML
    private Button taskButton;

    @FXML
    private Tab taskTab;

    @FXML
    private Label username;

    @FXML
    private TextField usernameField;
    private User user;


    public void initialize() {
        ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());
        roleBox.getItems().removeAll(roleBox.getItems());
        roleBox.getItems().addAll(roles);
    }
    public void setUser(User user) {
        this.user = user;
        updateUserInfo(photoUser, username);
    }
    public void updateUserInfo(Circle photoUser, Label username) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        username.setText(firstName + " " + lastName);
        if (user.getPhoto() != null) {
            byte[] photoBytes = user.getPhoto();
            Image image = new Image(new ByteArrayInputStream(photoBytes));
            double radius = photoUser.getRadius();
            ImageView imageView = new ImageView(image);
            photoUser.setFill(new ImagePattern(image));

            photoUser.setStyle("-fx-background-color: #bbb;");
        }
    }
    public void updateUserInfo(Circle photoUser, TextField firstNameField,
                               TextField lastNameField, TextField usernameField,
                               TextField emailField) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String username = user.getUsername();
        String email = user.getEmail();
        firstNameField.setText(firstName);
        lastNameField.setText(lastName);
        usernameField.setText(username);
        emailField.setText(email);
        if (user.getPhoto() != null) {
            byte[] photoBytes = user.getPhoto();
            Image image = new Image(new ByteArrayInputStream(photoBytes));
            double radius = photoUser.getRadius();
            ImageView imageView = new ImageView(image);
            photoUser.setFill(new ImagePattern(image));

            photoUser.setStyle("-fx-background-color: #bbb;");
        }
    }

    @FXML
    void changeToAccountTab(MouseEvent event) {
        if (event.getSource() == accountButton) {
            accountTab.getTabPane().getSelectionModel().select(accountTab);
        }
        updateUserInfo(photoUserInAccount, firstNameField, lastNameField,
                usernameField, emailField);
        if (user != null && user.getRole() != null) {
            roleBox.setValue(user.getRole());
        }

    }

    @FXML
    void changeToPtojectTab(MouseEvent event) {
        if (event.getSource() == projectButton) {
            projectTab.getTabPane().getSelectionModel().select(projectTab);
        }
    }

    @FXML
    void changeToTaskTab(MouseEvent event) {
        if (event.getSource() == taskButton) {
            taskTab.getTabPane().getSelectionModel().select(taskTab);
        }
    }
    @FXML
    void addButton(MouseEvent event) {
        openNewForm("projectForm.fxml");

    }
    public void openNewForm(String formName) {
        try {
            // Загрузка новой формы
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formName));
            Parent root = loader.load();

            // Создание новой сцены
            Scene scene = new Scene(root);

            // Отображение новой сцены на новом окне
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}