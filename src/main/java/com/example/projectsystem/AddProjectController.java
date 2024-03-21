package com.example.projectsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AddProjectController {

    @FXML
    private Button addOwnerBurron;

    @FXML
    void addOwner(ActionEvent event) {
       openNewForm("ownerForm.fxml");
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