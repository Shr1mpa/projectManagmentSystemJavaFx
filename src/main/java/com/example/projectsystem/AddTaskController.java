package com.example.projectsystem;

import com.example.projectsystem.Models.Project;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AddTaskController {

    @FXML
    private FontAwesomeIconView addTaskButton;

    @FXML
    private Button deleteTuskButton;
    @FXML
    private Label projectNameLabel;
    private Project project;
    public void setProject(Project project) {
        this.project = project;
    }
    public void initialize() {
        if (project != null) {
            projectNameLabel.setText(project.getName());
        }
    }

    @FXML
    void addTask(MouseEvent event) {
        openNewForm("addTask.fxml", project);
    }


    @FXML
    void deleteTask(MouseEvent event) {

    }
    public void openNewForm(String formName, Project project) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formName));
            Parent root = loader.load();
            TaskManagerConroller controller = loader.getController();
            controller.setProject(project);
            controller.initialize();

            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}