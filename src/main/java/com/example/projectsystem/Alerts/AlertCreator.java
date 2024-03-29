package com.example.projectsystem.Alerts;

import javafx.scene.control.Alert;

public interface AlertCreator {
    void createAlert(String message, String title, String headerText, Alert.AlertType type);
    void createAlert( String title, String headerText, Alert.AlertType type);

}
