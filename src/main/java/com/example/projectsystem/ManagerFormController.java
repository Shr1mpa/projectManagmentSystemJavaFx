package com.example.projectsystem;

import com.example.projectsystem.Alerts.AlertCreator;
import com.example.projectsystem.Alerts.AlertManager;
import com.example.projectsystem.DAO.UserDao;
import com.example.projectsystem.Exceptions.AccountChangesException;
import com.example.projectsystem.Exceptions.RegisterException;
import com.example.projectsystem.Managers.QueryManager;
import com.example.projectsystem.Models.*;
import com.example.projectsystem.Repositories.OwnerRepository;
import com.example.projectsystem.Repositories.ProjectRepository;
import com.example.projectsystem.Repositories.Repository;
import com.example.projectsystem.Repositories.TaskRepository;
import com.example.projectsystem.Utils.HibernateUtil;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.SessionFactory;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    private TextField phoneNumberField;

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

    //tasks
    @FXML
    private TableColumn<Task, LocalDate> taskAddDateColumn;

    @FXML
    private TableColumn<Task, Void> taskChangeTaskColumn;

    @FXML
    private TableColumn<Task, LocalDate> taskEndDateColumn;

    @FXML
    private TableColumn<Task, String> taskNameColumn;

    @FXML
    private TableColumn<Task, String> taskProjectColumn;

    @FXML
    private TableColumn<Task, LocalDate> taskStartDateColumn;

    @FXML
    private TableColumn<Task, Status> taskStatusColumn;

    @FXML
    private TableColumn<Task, String> taskWorkerColumn;

    @FXML
    private TableView<Task> tasksTable;


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
    @FXML
    private ComboBox<String> ownerComboBox;
    private User user;
    private ProjectRepository repository;
    private List<Project> projects;
    @FXML
    private TableColumn<Project, Void> progressColumn;
    @FXML
    private ComboBox<Status> statusComboBox;
    private File selectedFile;
    private byte[] photo;
    private OwnerRepository ownerRepository;

    public void initialize() {
        ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());
        roleBox.getItems().removeAll(roleBox.getItems());
        roleBox.getItems().addAll(roles);
        ObservableList<Status> statuses = FXCollections.observableArrayList(Status.values());
        statusComboBox.getItems().removeAll(statusComboBox.getItems());
        statusComboBox.getItems().addAll(statuses);
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ownerRepository = new OwnerRepository(sessionFactory);
        List<Owner> owners = ownerRepository.getData();
        List<String> ownerNames = new ArrayList<>();
        for (Owner owner1 : owners) {
            ownerNames.add(owner1.getFirstName() + " " + owner1.getLastName());
        }
        ownerComboBox.getItems().removeAll(ownerComboBox.getItems());
        ownerComboBox.getItems().addAll(ownerNames);
        ChangeListener<Object> listener = (obs, oldValue, newValue) -> {
            QueryManager queryManager = new QueryManager();
            Status selectedStatus = statusComboBox.getValue();
            String ownerFullName = ownerComboBox.getValue();

            if (selectedStatus != null && ownerFullName != null) {
                String[] nameParts = ownerFullName.split(" ");
                String lastName = nameParts[1];
                String firstName = nameParts[0];
                List<Project> projects = queryManager.getProjectsByStatusAndOwner(selectedStatus, lastName, firstName);
                addProjectsToTable(projects);
            }
            else if (selectedStatus != null) {
                List<Project> projects = queryManager.getProjectsByStatus(selectedStatus);
                addProjectsToTable(projects);
            } else if (ownerFullName != null) {
                String[] nameParts = ownerFullName.split(" ");
                String lastName = nameParts[0];
                String firstName = nameParts[1];
                List<Project> projects = queryManager.getProjectsByOwnerName(lastName, firstName);
                addProjectsToTable(projects);
            }
        };
        statusComboBox.valueProperty().addListener(listener);
        ownerComboBox.valueProperty().addListener(listener);
        addProjectsToTable();
        createButton();
    }
    @FXML
    void chooseImageForUser(MouseEvent event) {
           chooseImage();
    }
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Виберіть фото");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Зображення", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Всі файли", "*.*")
        );
        selectedFile = fileChooser.showOpenDialog(null); // Представлення діалогу вибору файлу
        if (selectedFile != null) {
            byte[] photoBytes = new byte[0];
            try {
                photoBytes = imageToByteArray(selectedFile);
                photo = photoBytes;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Image image = new Image(new ByteArrayInputStream(photoBytes));
            double radius = photoUserInAccount.getRadius();
            ImageView imageView = new ImageView(image);
            photoUserInAccount.setFill(new ImagePattern(image));
            photoUserInAccount.setStyle("-fx-background-color: #bbb;");
        }
    }
    public byte[] imageToByteArray(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        return baos.toByteArray();
    }
    private void addProjectsToTable() {
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
            String fullName = owner.getFirstName() + " " + owner.getLastName();
            return new SimpleStringProperty(fullName);
        });
        progressColumn.setCellFactory(tc -> new TableCell<>() {
            private final ProgressBar progressBar = new ProgressBar();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                Project project = (Project) getTableRow().getItem();
                long completedTasks = project.getTasks().stream()
                        .filter(task -> task.getStatus() == Status.COMPLETED)
                        .count();
                long totalTasks = project.getTasks().size();

                double progress = (double) completedTasks / totalTasks;


                progressBar.setProgress(progress);

                setGraphic(progressBar);
            }
        });

        repository = new ProjectRepository();
        projects = repository.getData();
        table.getItems().clear();
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
                            case COMPLETED:
                                label.setStyle("-fx-background-color: #97e574; -fx-padding: 5px; -fx-background-radius: 10;");
                                break;
                            case PENDING:
                                label.setStyle("-fx-background-color: #ffc000; -fx-padding: 5px; -fx-background-radius: 10;");
                                break;
                            case POSTPONED:
                                label.setStyle("-fx-background-color: #ff8000; -fx-padding: 5px; -fx-background-radius: 10;");
                                break;
                            case TESTING:
                                label.setStyle("-fx-background-color: #ff6666; -fx-padding: 5px; -fx-background-radius: 10;");
                                break;
                            case FINISHED:
                                label.setStyle("-fx-background-color: #a0a0a0; -fx-padding: 5px; -fx-background-radius: 10;");
                                break;
                            default:
                                break;
                        }

                        setGraphic(label);
                    }
                }
            };
        });
    }
    private void addProjectsToTable(List<Project> projects) {
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
            String fullName = owner.getFirstName() + " " + owner.getLastName();
            return new SimpleStringProperty(fullName);
        });
        progressColumn.setCellFactory(tc -> new TableCell<>() {
            private final ProgressBar progressBar = new ProgressBar();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                Project project = (Project) getTableRow().getItem();
                long completedTasks = project.getTasks().stream()
                        .filter(task -> task.getStatus() == Status.COMPLETED)
                        .count();
                long totalTasks = project.getTasks().size();

                double progress = (double) completedTasks / totalTasks;


                progressBar.setProgress(progress);

                setGraphic(progressBar);
            }
        });

        table.getItems().clear();
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
                            case COMPLETED:
                                label.setStyle("-fx-background-color: #97e574; -fx-padding: 5px; -fx-background-radius: 10;");
                                break;
                            case PENDING:
                                label.setStyle("-fx-background-color: #ffc000; -fx-padding: 5px; -fx-background-radius: 10;");
                                break;
                            case POSTPONED:
                                label.setStyle("-fx-background-color: #ff8000; -fx-padding: 5px; -fx-background-radius: 10;");
                                break;
                            case TESTING:
                                label.setStyle("-fx-background-color: #ff6666; -fx-padding: 5px; -fx-background-radius: 10;");
                                break;
                            case FINISHED:
                                label.setStyle("-fx-background-color: #a0a0a0; -fx-padding: 5px; -fx-background-radius: 10;");
                                break;
                            default:
                                break;
                        }

                        setGraphic(label);
                    }
                }
            };
        });
    }
    private long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    private void createButton() {
        changeData.setCellFactory(new Callback<TableColumn<Project, Void>, TableCell<Project, Void>>() {
            @Override
            public TableCell<Project, Void> call(TableColumn<Project, Void> param) {
                return new TableCell<Project, Void>() {
                    private final Button button = new Button();
                    private final ContextMenu contextMenu = new ContextMenu();

                    {
                        MenuItem changeProjectDataItem = new MenuItem("Change Project Data");
                        MenuItem changeTasksItem = new MenuItem("Change Tasks");

                        changeProjectDataItem.setOnAction(event -> {
                            Project project = getTableView().getItems().get(getIndex());
                            openNewProjectForm("projectForm.fxml", project);
                            //change project data
                        });

                        changeTasksItem.setOnAction(event -> {
                            Project project = getTableView().getItems().get(getIndex());
                            //change taskData
                            openNewForm("projectTasksForm.fxml", project);
                        });

                        contextMenu.getItems().addAll(changeProjectDataItem, changeTasksItem);
                        button.setOnAction(event -> {
                            double x = button.localToScreen(button.getBoundsInLocal()).getMinX();
                            double y = button.localToScreen(button.getBoundsInLocal()).getMaxY();
                            contextMenu.show(button, x, y);
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
                };
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
        updateUserInfo(photoUser, username);
        photo = user.getPhoto();
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
        phoneNumberField.setText(user.getPhoneNumber());
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
    void updateData(MouseEvent event) {
        addProjectsToTable();

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
        addTaskToTable();
        createButtonForTask();
    }
    @FXML
    void addButton(MouseEvent event) {
        openNewForm("projectForm.fxml");

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formName));
            Parent root = loader.load();
            AddTaskController controller = loader.getController();
            controller.setProject(project);
            controller.initialize();

            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openNewProjectForm(String formName, Project project) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(formName));
            Parent root = loader.load();
            AddProjectController controller = loader.getController();
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
    private void addTaskToTable() {
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        taskProjectColumn.setCellValueFactory(cellData -> {
            Task task = cellData.getValue();
            return new SimpleStringProperty(task.getProject().getName());
        });
        taskWorkerColumn.setCellValueFactory(cellData -> {
            Task task = cellData.getValue();
            User worker = task.getUser();
            return new SimpleStringProperty(worker.getFirstName() + " " + worker.getLastName());
        });
        taskStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        taskStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        taskEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        taskAddDateColumn.setCellValueFactory(new PropertyValueFactory<>("addDate"));


        tasksTable.getItems().clear();
        tasksTable.getItems().addAll(new TaskRepository().getData());
    }
    private void createButtonForTask() {
        taskChangeTaskColumn.setCellFactory(param -> new TableCell<>() {
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
    void saveAccChanges(MouseEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String usernameText = usernameField.getText();
        String email = emailField.getText();
        String phone = phoneNumberField.getText();
        Role role = roleBox.getValue();
        User userChanged = User.builder()
                .id(user.getId())
                        .firstName(firstName)
                .lastName(lastName)
                .username(usernameText)
                .email(email)
                .role(role)
                .phoneNumber(phone)
                .photo(photo)
                .password(user.getPassword())
        .build();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        AlertCreator alertCreator = new AlertManager();
        try {
            UserDao userDao = new UserDao(sessionFactory);
            userDao.checkUserExistByUsernameOrEmail(userChanged);
            userDao.update(userChanged);
            user = userChanged;
            updateUserInfo(photoUser, username);
            alertCreator.createAlert("Success", "Success changes.", Alert.AlertType.INFORMATION);
        } catch (AccountChangesException e) {
            alertCreator.createAlert("Error", "An error occurred while updating user.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }

    }

}