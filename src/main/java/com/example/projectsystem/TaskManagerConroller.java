package com.example.projectsystem;


import com.example.projectsystem.Models.*;
import com.example.projectsystem.Repositories.ProjectRepository;
import com.example.projectsystem.Repositories.WorkerRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerConroller {
    @FXML
    private ComboBox<Status> StatusComboBox;

    @FXML
    private ComboBox<String> WorkerCombpBox;

    @FXML
    private Button addButton;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker endDateField;

    @FXML
    private TextField nameTaskText;

    @FXML
    private ComboBox<Project> projectComboBox;

    @FXML
    private DatePicker startDateField;
    @Setter
    private Project project;
    public void initialize() {
        List<Project> projects = new ProjectRepository().getData();
        showProjects();
        projectComboBox.getItems().addAll(projects);
        if (project != null) {
            projectComboBox.setValue(project);
            projectComboBox.setDisable(true);
        }
        List<User> workers = new WorkerRepository().getData();
        List<String> workersNames = new ArrayList<>();
        for (User user : workers) {
            workersNames.add(user.getFirstName() + " " + user.getLastName());
        }
        WorkerCombpBox.getItems().removeAll(WorkerCombpBox.getItems());
        WorkerCombpBox.getItems().addAll(workersNames);


    }

    @FXML
    void addTask(MouseEvent event) {

    }
    private void showProjects() {
        projectComboBox.setCellFactory(param -> new ListCell<Project>() {
            @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

}