package com.example.projectsystem;


import com.example.projectsystem.DAO.TaskDao;
import com.example.projectsystem.Models.*;
import com.example.projectsystem.Repositories.ProjectRepository;
import com.example.projectsystem.Repositories.WorkerRepository;
import com.example.projectsystem.Utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import lombok.Setter;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
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
    private ComboBox<String> projectComboBox;

    @FXML
    private DatePicker startDateField;
    @Setter
    private Project project;
    private Task task;
    public void setTask(Task task) {
        this.task = task;
        uploadData();
    }
    private List<User> workers;
    private WorkerRepository workerRepository;
    private ProjectRepository projectRepository;

    public void initialize() {
        workerRepository = new WorkerRepository();
        workers = workerRepository.getData();
        if (task == null) {
            List<Project> allProjects = new ProjectRepository().getData();

            for (Project project : allProjects) {
                projectComboBox.getItems().add(project.getName());
            }
            if (project != null) {
                projectComboBox.setValue(project.getName());
                projectComboBox.setDisable(true);
            }
            projectRepository = new ProjectRepository();
            List<String> workersNames = new ArrayList<>();
            for (User user : workers) {
                workersNames.add(user.getFirstName() + " " + user.getLastName());
            }
            WorkerCombpBox.getItems().removeAll(WorkerCombpBox.getItems());
            WorkerCombpBox.getItems().addAll(workersNames);
            ObservableList<Status> roles = FXCollections.observableArrayList(Status.values());
            StatusComboBox.getItems().removeAll(StatusComboBox.getItems());
            StatusComboBox.getItems().addAll(roles);
        }
    }

    @FXML
    void addTask(MouseEvent event) {
        String taskName = nameTaskText.getText();
        Project project1 = projectRepository.searchByName(projectComboBox.getValue());
        User user = getUserFromComboBox(WorkerCombpBox.getValue());
        Status status = StatusComboBox.getValue();
        LocalDate startDate = startDateField.getValue();
        LocalDate endDate = endDateField.getValue();
        String description = descriptionField.getText();
        LocalDate addDate = LocalDate.now();

        if (task == null) {
            Task newTask = Task.builder()
                    .name(taskName)
                    .project(project1)
                    .addDate(addDate)
                    .user(user)
                    .status(status)
                    .startDate(startDate)
                    .endDate(endDate)
                    .description(description)
                    .build();
            saveTask(newTask);
        } else {
            task.setName(taskName);
            task.setUser(user);
            task.setStatus(status);
            task.setStartDate(startDate);
            task.setEndDate(endDate);
            task.setDescription(description);
            updateTask(task);
        }
    }
    private User getUserFromComboBox(String selectedWorker) {
        String[] parts = selectedWorker.split(" ");
        String firstName = parts[0];
        String lastName = parts[1];
        return workerRepository.findOwner(firstName, lastName);
    }

    private void saveTask(Task task) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        TaskDao taskDao = new TaskDao(sessionFactory);
        taskDao.save(task);
    }

    private void updateTask(Task task) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        TaskDao taskDao = new TaskDao(sessionFactory);
        taskDao.update(task);
    }
    private void uploadData() {
        nameTaskText.setText(task.getName());
        User projectUser = task.getUser();
        List<String> workersNames = new ArrayList<>();
        for (User user : workers) {
            workersNames.add(user.getFirstName() + " " + user.getLastName());
        }
        WorkerCombpBox.getItems().removeAll(WorkerCombpBox.getItems());
        WorkerCombpBox.getItems().addAll(workersNames);
        int index = workersNames.indexOf(task.getUser().getFirstName() + " " + task.getUser().getLastName());
        if (index != -1) {
            WorkerCombpBox.getSelectionModel().select(index);
        }
        List<Project> allProjects = new ProjectRepository().getData();

        for (Project project : allProjects) {
            projectComboBox.getItems().add(project.getName());
        }
        Project taskProject = task.getProject();
        if (taskProject != null) {
            projectComboBox.setValue(taskProject.getName());
            projectComboBox.setDisable(true);
        }
        ObservableList<Status> roles = FXCollections.observableArrayList(Status.values());
        StatusComboBox.getItems().removeAll(StatusComboBox.getItems());
        StatusComboBox.getItems().addAll(roles);
        Status projectStatus = task.getStatus();
        if (roles.contains(projectStatus)) {
            StatusComboBox.getSelectionModel().select(projectStatus);
        }
        startDateField.setValue(task.getStartDate());
        endDateField.setValue(task.getEndDate());
        descriptionField.setText(task.getDescription());
        addButton.setText("Save");
    }


}