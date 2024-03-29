module com.example.projectsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires static lombok;
    requires java.persistence;
    requires java.naming;
    requires java.sql;
    requires java.desktop;

    requires org.postgresql.jdbc;
    requires org.hibernate.orm.core;



    opens com.example.projectsystem to javafx.fxml;
    opens com.example.projectsystem.Models;
    exports com.example.projectsystem;
}