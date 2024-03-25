package com.example.projectsystem;

import com.example.projectsystem.Models.*;
import com.example.projectsystem.Repositories.ProjectRepository;
import com.example.projectsystem.Repositories.Repository;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

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
    private TableColumn<Project, Void> changeData;


    @FXML
    private TextField usernameField;
    @FXML
    private TableView<Project> table;
    @FXML
    private TableColumn<Project, String> nameColumn;
    @FXML
    private TableColumn<Project, String> ownerColumn;
    @FXML
    private TableColumn<Project, Status> statusColumn;
    @FXML
    private TableColumn<Project, LocalDate> startDateColumn;
    @FXML
    private TableColumn<Project, LocalDate> endDateColumn;
    @FXML
    private TableColumn<Project, Long> daysColumn;
    @FXML
    private TableColumn<Project, Integer> tasksColumn;
    private User user;


    public void initialize() {
        ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());
        roleBox.getItems().removeAll(roleBox.getItems());
        roleBox.getItems().addAll(roles);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tasksColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfTasks"));

        daysColumn.setCellValueFactory(cellData -> {
            Project project = cellData.getValue();
            long days = calculateDaysBetween(project.getStartDate(), project.getEndDate());
            return new SimpleLongProperty(days).asObject();
        });
        ownerColumn.setCellValueFactory(cellData -> {
            Owner owner = cellData.getValue().getOwner();
            // Отримуємо ім'я та прізвище з класу Owner
            String fullName = owner.getFirstName() + " " + owner.getLastName();
            return new SimpleStringProperty(fullName);
        });

        ProjectRepository repository = new ProjectRepository();
        List<Project> projects = repository.getData();
        table.getItems().addAll(projects);
        statusColumn.setCellFactory(column -> {
            return new TableCell<Project, Status>() {
                private final Label label = new Label();

                {
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }

                @Override
                protected void updateItem(Status item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        label.setText(item.toString());

                        switch (item) {
                            case IN_PROGRESS:
                                label.setStyle("-fx-background-color: #f3ff5e; -fx-padding: 5px; -fx-background-radius: 10;");
                                break;
                            case FINISHED:
                                label.setStyle("-fx-background-color: #97e574; -fx-padding: 5px; -fx-background-radius: 10;");
                                break;
                            case PENDING:
                                break;
                            default:
                                break;
                        }

                        setGraphic(label);
                    }
                }
            };
        });
        createButton();
    }
    private long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        // Реалізуйте розрахунок кількості днів між startDate та endDate
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    private void createButton() {
        changeData.setCellFactory(new Callback<TableColumn<Project, Void>, TableCell<Project, Void>>() {
            @Override
            public TableCell<Project, Void> call(TableColumn<Project, Void> param) {
                return new TableCell<Project, Void>() {
                    private final Button button = new Button("Options");
                    private final ContextMenu contextMenu = new ContextMenu();

                    {
                        MenuItem changeProjectDataItem = new MenuItem("Change Project Data");
                        MenuItem changeTasksItem = new MenuItem("Change Tasks");

                        changeProjectDataItem.setOnAction(event -> {
                            Project project = getTableView().getItems().get(getIndex());
                            // Виклик методу для зміни даних проєкту
                        });

                        changeTasksItem.setOnAction(event -> {
                            Project project = getTableView().getItems().get(getIndex());
                            // Виклик методу для зміни завдань проєкту
                            openNewForm("projectTasksForm.fxml", project);
                        });

                        contextMenu.getItems().addAll(changeProjectDataItem, changeTasksItem);
                        button.setOnAction(event -> {
                            double x = button.localToScreen(button.getBoundsInLocal()).getMinX();
                            double y = button.localToScreen(button.getBoundsInLocal()).getMaxY();
                            contextMenu.show(button, x, y);
                        });

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
                };
            }
        });
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
    public AddTaskController getFormController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("projectTasksForm.fxml"));
        try {
            Parent root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fxmlLoader.getController();
    }
    public void openNewForm(String formName, Project project) {
        try {
            // Загрузка новой формы
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formName));
            Parent root = loader.load();
            AddTaskController controller = loader.getController();
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