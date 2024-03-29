package com.example.projectsystem;

import com.example.projectsystem.DAO.UserDao;
import com.example.projectsystem.Exceptions.RegisterException;
import com.example.projectsystem.Models.Role;
import com.example.projectsystem.Models.User;
import com.example.projectsystem.Utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class InfoController {

    @FXML
    private TextField LastNameField;

    @FXML
    private Label changeScene;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<Role> roleComboBox;

    @FXML
    private TextField textField;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    void changeToLOgin(MouseEvent event) throws IOException {
        Stage stage = (Stage) changeScene.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Register");
        stage.setScene(new Scene(root1));
        stage.show();


    }
    public void initialize() {
        ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());
        roleComboBox.getItems().removeAll(roleComboBox.getItems());
        roleComboBox.getItems().addAll(roles);
    }
    @FXML
    void registration(MouseEvent event) throws IOException {
        try {
            user.setRole(roleComboBox.getValue());
            user.setFirstName(firstNameField.getText());
            user.setLastName(LastNameField.getText());
            user.setPhoneNumber(phoneField.getText());
            File photoFile = new File("D:\\JavaProjects\\ProjectSystem\\src\\main\\resources\\com\\example\\projectsystem\\Styles\\Images\\defaultPhoto.jpg");
            byte[] photoBytes = Files.readAllBytes(photoFile.toPath());
            user.setPhoto(photoBytes);

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            UserDao userDao = new UserDao(sessionFactory);
            userDao.save(user);
        } catch (RegisterException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration error");
            alert.setHeaderText("A user with the same name or email already exists.");
            alert.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}

