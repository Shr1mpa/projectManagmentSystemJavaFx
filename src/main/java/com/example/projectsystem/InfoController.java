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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;

public class InfoController {

    @FXML
    private Label changeScene;

    @FXML
    private ComboBox<Role> roleComboBox;

    @FXML
    private TextField textField;

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
    public void initialize() {
        ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());
        roleComboBox.getItems().removeAll(roleComboBox.getItems());
        roleComboBox.getItems().addAll(roles);
    }
    @FXML
    void registration(MouseEvent event) {

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.configure();
        try(SessionFactory sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.openSession()) {
            User user = User.builder()
                    .username("example")
                    .email("example@example.com")
                    .password("password")
                    .firstName("John")
                    .lastName("Doe")
                    .phoneNumber("123456789")
                    .role(Role.MANAGER)
                    .build();
            session.beginTransaction();

            session.save(user);
            session.getTransaction().commit();
        }

    }

}

