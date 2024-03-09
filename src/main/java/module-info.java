module com.example.projectsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires static lombok;
    requires java.persistence;
    requires java.naming;
    requires java.sql;

    requires org.postgresql.jdbc;
    requires org.hibernate.orm.core;


    opens com.example.projectsystem to javafx.fxml;
    exports com.example.projectsystem;
}