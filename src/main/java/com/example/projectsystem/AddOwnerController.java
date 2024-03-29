package com.example.projectsystem;

import com.example.projectsystem.Alerts.AlertCreator;
import com.example.projectsystem.Alerts.AlertManager;
import com.example.projectsystem.DAO.OwnerDao;
import com.example.projectsystem.DAO.UserDao;
import com.example.projectsystem.Models.Owner;
import com.example.projectsystem.Utils.HibernateUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class AddOwnerController {

    @FXML
    private Button addOwner;

    @FXML
    private TextField emailText;

    @FXML
    private TextField firstNameText;

    @FXML
    private TextField lastNameText;

    @FXML
    private AnchorPane ownerInfoPannel;

    @FXML
    private TextField phoneText;
    private AddProjectController secondFormController;
    public void setProjectConroller(AddProjectController conroller) {
        secondFormController = conroller;
    }
    @FXML
    void addNewOwner(MouseEvent event) {
        String firstName = firstNameText.getText();
        String lastName = lastNameText.getText();
        String email = emailText.getText();
        String phone = phoneText.getText();
        Owner owner = Owner.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phone)
                .build();
        SessionFactory session = HibernateUtil.getSessionFactory();
        OwnerDao ownerDao = new OwnerDao(session);
        ownerDao.save(owner);
        AlertCreator creator = new AlertManager();
        creator.createAlert("Owner successfully added!", "Success", null, Alert.AlertType.INFORMATION);
        if (secondFormController != null) {
            secondFormController.updateComboBoxData();
        }
    }


}
