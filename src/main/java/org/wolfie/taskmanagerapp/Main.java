package org.wolfie.taskmanagerapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.wolfie.taskmanagerapp.db.DatabaseManager;
import org.wolfie.taskmanagerapp.db.SchemaInitializer;
import org.wolfie.taskmanagerapp.view.RootView;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    private DatabaseManager databaseManager;

    @Override
    public void init() throws Exception {
        try {
            databaseManager = new DatabaseManager();
            new SchemaInitializer(databaseManager.getConnection()).initialize();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new RootView(), 400, 400);
        stage.setTitle("Overflow - Task Manager App");
        stage.setScene(scene);
        stage.show();
    }
}
