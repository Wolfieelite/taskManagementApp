package org.wolfie.taskmanagerapp.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {
    private final Connection connection;

    public SchemaInitializer(Connection connection) {
        this.connection = connection;
    }

    public void initialize() throws SQLException {
        //SQLite disables FK enforcement by default - must opt in per connection
        try (Statement pragma = connection.createStatement()) {
            pragma.execute("PRAGMA foreign_keys = ON");
        }

        try (Statement stmt = connection.createStatement()) {
            //creating the projects table.
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS projects
                    (id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    project_type TEXT NOT NULL CHECK (project_type IN ('WEB_DEV', 'GAME_DEV')),
                    documentation TEXT,
                    notes TEXT)
                    """);

            //creating the sprint table
            stmt.execute("""
                                CREATE TABLE IF NOT EXISTS sprints
                                (id INTEGER PRIMARY KEY AUTOINCREMENT,
                                project_id INTEGER NOT NULL,
                                name TEXT NOT NULL,
                                start_date TEXT NOT NULL,
                                end_date TEXT NOT NULL, 
                                FOREIGN KEY (project_id) REFERENCES projects(id))
                    """);

            //creating the tasks label which will reference the project table
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS labels
                    (id INTEGER PRIMARY KEY AUTOINCREMENT,
                    project_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    FOREIGN KEY (project_id) REFERENCES projects(id))""");

            //creating the tasks themselves which references the project and sprint
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS tasks
                    (id INTEGER PRIMARY KEY AUTOINCREMENT,
                    project_id INTEGER NOT NULL,
                    sprint_id INTEGER,
                    name TEXT NOT NULL,
                    priority TEXT NOT NULL CHECK (priority in ('LOW', 'MEDIUM', 'HIGH', 'RISK')),
                    status TEXT NOT NULL CHECK (status in ('BACKLOG', 'SPRINT', 'DONE', 'CLOSED')),
                    notes TEXT,
                    due_date TEXT,
                    is_active INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY (project_id) REFERENCES projects(id),
                    FOREIGN KEY (sprint_id) REFERENCES sprints(id))""");

            //creating the subTasks table which references the tasks
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS sub_tasks
                    (id INTEGER PRIMARY KEY AUTOINCREMENT,
                    task_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    is_done INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY (task_id) REFERENCES tasks(id)
                    )
                    """);

            //creating the task_labels table which is a joined table
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS task_labels
                        (task_id INTEGER NOT NULL,
                        label_id INTEGER NOT NULL,
                        PRIMARY KEY (task_id, label_id),
                        FOREIGN KEY (task_id) REFERENCES tasks(id),
                        FOREIGN KEY (label_id) REFERENCES labels(id)
                        )
                    """);
            //creating the project attachment table
            stmt.execute("""
                    CREATE TABLE if NOT EXISTS project_attachments
                    (id INTEGER PRIMARY KEY AUTOINCREMENT,
                    project_id INTEGER NOT NULL,
                    file_path TEXT,
                    file_type TEXT NOT NULL CHECK (file_type in ('IMAGE', 'DOCUMENT', 'VIDEO', 'AUDIO', 'OTHER')),
                    FOREIGN KEY (project_id) REFERENCES projects(id)
                    )""");

            //creating the task attachment table
            stmt.execute("""
                    CREATE TABLE if NOT EXISTS task_attachments
                    (id INTEGER PRIMARY KEY AUTOINCREMENT,
                    task_id INTEGER NOT NULL,
                    file_path TEXT,
                    file_type TEXT NOT NULL CHECK (file_type in ('IMAGE', 'DOCUMENT', 'VIDEO', 'AUDIO', 'OTHER')),
                    FOREIGN KEY (task_id) REFERENCES tasks(id)
                    )""");
        }
    }
}
