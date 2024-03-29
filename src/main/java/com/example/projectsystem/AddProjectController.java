package com.example.projectsystem;

import com.example.projectsystem.DAO.ProjectDao;
import com.example.projectsystem.Models.Owner;
import com.example.projectsystem.Models.Project;
import com.example.projectsystem.Models.Role;
import com.example.projectsystem.Models.Status;
import com.example.projectsystem.Repositories.OwnerRepository;
import com.example.projectsystem.Utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class AddProjectController {

    @FXML
    private Button addOwnerBurron;
    @FXML
    private ComboBox<String> ownersBox;
    @FXML
    private ComboBox<Status> statusBox;
    @FXML
    private DatePicker endDateText;
    @FXML
    private TextField projectName;

    @FXML
    private DatePicker startDateText;
    @FXML
    private Button addProjectButton;
    private List<Owner> owners;
    private OwnerRepository ownerRepository;
    private Project project;
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public void setProject(Project project) {
        projectName.setText(project.getName());
        this.project = project;
        uploadData();
    }
    private void uploadData() {
        ownerRepository = new OwnerRepository(sessionFactory);
        Owner owner = ownerRepository.findOwner(project.getOwner().getFirstName()
                ,project.getOwner().getLastName());
        owners = ownerRepository.getData();
        List<String> ownerNames = new ArrayList<>();
        for (Owner owner1 : owners) {
            ownerNames.add(owner1.getFirstName() + " " + owner1.getLastName());
        }
        ownersBox.getItems().removeAll(ownersBox.getItems());
        ownersBox.getItems().addAll(ownerNames);
        int index = ownerNames.indexOf(project.getOwner().getFirstName() + " " + project.getOwner().getLastName());
        if (index != -1) {
            ownersBox.getSelectionModel().select(index);
        }
        ObservableList<Status> roles = FXCollections.observableArrayList(Status.values());
        statusBox.getItems().removeAll(statusBox.getItems());
        statusBox.getItems().addAll(roles);
        Status projectStatus = project.getStatus();
        if (roles.contains(projectStatus)) {
            statusBox.getSelectionModel().select(projectStatus);
        }
        startDateText.setValue(project.getStartDate());
        endDateText.setValue(project.getEndDate());
        addProjectButton.setText("Save");
    }
    public void initialize() {
        if (project == null) {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            ownerRepository = new OwnerRepository(sessionFactory);
            owners = ownerRepository.getData();
            List<String> ownerNames = new ArrayList<>();
            for (Owner owner : owners) {
                ownerNames.add(owner.getFirstName() + " " + owner.getLastName());
            }
            ObservableList<Status> roles = FXCollections.observableArrayList(Status.values());
            statusBox.getItems().removeAll(statusBox.getItems());
            statusBox.getItems().addAll(roles);
            ownersBox.getItems().removeAll(ownersBox.getItems());
            ownersBox.getItems().addAll(ownerNames);
        }


    }

    @FXML
    void addOwner(ActionEvent event) {
       openNewForm("ownerForm.fxml");
    }
    public void openNewForm(String formName) {
        try {
            FXMLLoader currentLoader = new FXMLLoader(getClass().getResource("projectForm.fxml"));
            Parent currentRoot = currentLoader.load();
            AddProjectController currentController = currentLoader.getController(); // Получаем контроллер текущей формы
            FXMLLoader loader = new FXMLLoader(getClass().getResource(formName));
            Parent root = loader.load();
            AddOwnerController secondController = loader.getController(); // Получаем контроллер новой формы

            secondController.setProjectConroller(currentController);

            Scene scene = new Scene(root);

            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateComboBoxData() {
        ownersBox.getItems().clear();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        List<Owner> owners = new OwnerRepository(sessionFactory).getData();
        List<String> ownerNames = new ArrayList<>();
        for (Owner owner : owners) {
            ownerNames.add(owner.getFirstName() + " " + owner.getLastName());
        }
        ownersBox.getItems().addAll(ownerNames);
    }
    @FXML
    void addProject(ActionEvent event) {
        if (project != null) {
            String name = projectName.getText();
            String selectedOwner = ownersBox.getValue();
            String[] parts = selectedOwner.split(" ");
            String firstName = parts[0];
            String lastName = parts[1];
            try {
                Owner owner = ownerRepository.findOwner(firstName, lastName);
                Status status = statusBox.getValue();
                LocalDate startDate = startDateText.getValue();
                LocalDate endDate = endDateText.getValue();
                project.setName(name);
                project.setOwner(owner);
                project.setStatus(status);
                project.setStartDate(startDate);
                project.setEndDate(endDate);
                ProjectDao projectDao = new ProjectDao(sessionFactory);
                projectDao.update(project);

            } catch (NoSuchElementException e) {

            }

        } else {
            String name = projectName.getText();
            String selectedOwner = ownersBox.getValue();
            String[] parts = selectedOwner.split(" ");
            String firstName = parts[0];
            String lastName = parts[1];
            try {
                Owner owner = ownerRepository.findOwner(firstName, lastName);
                Status status = statusBox.getValue();
                LocalDate startDate = startDateText.getValue();
                LocalDate endDate = endDateText.getValue();
                Project project = Project.builder()
                        .name(name)
                        .owner(owner)
                        .status(status)
                        .startDate(startDate)
                        .endDate(endDate)
                        .build();
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                ProjectDao projectDao = new ProjectDao(sessionFactory);
                projectDao.save(project);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Project successfully added!");
                alert.showAndWait();

            } catch (NoSuchElementException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Find owner error");
                alert.setHeaderText("Owner nor found");
                alert.showAndWait();
            }
        }


    }


}