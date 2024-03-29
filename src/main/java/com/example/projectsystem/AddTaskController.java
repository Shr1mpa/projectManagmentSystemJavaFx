package com.example.projectsystem;

import com.example.projectsystem.DAO.ProjectDao;
import com.example.projectsystem.DAO.TaskDao;
import com.example.projectsystem.Models.Owner;
import com.example.projectsystem.Models.Project;
import com.example.projectsystem.Models.Task;
import com.example.projectsystem.Models.User;
import com.example.projectsystem.Repositories.ProjectRepository;
import com.example.projectsystem.Repositories.TaskRepository;
import com.example.projectsystem.Utils.HibernateUtil;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.Status;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AddTaskController {

    @FXML
    private FontAwesomeIconView addTaskButton;
    @FXML
    private TableColumn<Task, Status> StatusColumn;

    @FXML
    private TableColumn<Task, String> WorkerColumn;

    @FXML
    private TableColumn<Task, LocalDate> addDateColumn;
    @FXML
    private TableColumn<Task, String> descriptionColumn;

    @FXML
    private TableColumn<Task, LocalDate> endDateColumn;

    @FXML
    private TableColumn<Task, String> nameColumn;

    @FXML
    private TableColumn<Task, String> projectColumn;

    @FXML
    private TableColumn<Task, LocalDate> StartDateColumn;
    @FXML
    private TableColumn<Task, Void> editTask;
    @FXML
    private TableView<Task> table;



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
        updateTable();
    }

    @FXML
    void addTask(MouseEvent event) {
        openNewForm("addTask.fxml", project);
    }


    @FXML
    void deleteTask(MouseEvent event) {
        Task selectedTask = table.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Task");
            alert.setContentText("Are you sure you want to delete this task?");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                    TaskDao taskDao = new TaskDao(sessionFactory);
                    taskDao.delete(selectedTask);
                    int id = project.getId();
                    ProjectDao projectDao = new ProjectDao(sessionFactory);
                    Optional<Project> project1 = projectDao.get(id);
                    project1.ifPresent(value -> project = value);
                    updateTable();
                }
            });
        }

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
    public void openNewForm(String formName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formName));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openNewForm(String formName, Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formName));
            Parent root = loader.load();
            TaskManagerConroller controller = loader.getController();
            controller.setTask(task);
            controller.initialize();
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void updateData(MouseEvent event) {
        int id = project.getId();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ProjectDao projectDao = new ProjectDao(sessionFactory);
        Optional<Project> project1 = projectDao.get(id);
        project1.ifPresent(value -> project = value);
        updateTable();
    }
    private void updateTable() {
        if (project != null) {

            projectNameLabel.setText(project.getName());

            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            StatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            StartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            addDateColumn.setCellValueFactory(new PropertyValueFactory<>("addDate"));
            WorkerColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            projectColumn.setCellValueFactory(new PropertyValueFactory<>("project"));

            WorkerColumn.setCellValueFactory(cellData -> {
                User worker = cellData.getValue().getUser();
                String fullName = worker.getFirstName() + " " + worker.getLastName();
                return new SimpleStringProperty(fullName);
            });
            projectColumn.setCellValueFactory(cellData -> {
                Project project1 = cellData.getValue().getProject();
                String fullName = project.getName();
                return new SimpleStringProperty(fullName);
            });
            List<Task> tasks = project.getTasks();
            table.getItems().clear();
            table.getItems().addAll(tasks);
            createButton();
        }
    }
    private void createButton() {
        editTask.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button();

            {
                button.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    openNewForm("addTask.fxml", task);
                });

                button.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
                FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
                icon.setFont(Font.font("FontAwesome", 12));
                button.setGraphic(icon);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });
    }



}