package com.example.projectsystem.Alerts;

import javafx.scene.control.Alert;

public class AlertManager implements AlertCreator {
    @Override
    public void createAlert(String message, String title, String headerText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void createAlert(String title, String headerText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
}
