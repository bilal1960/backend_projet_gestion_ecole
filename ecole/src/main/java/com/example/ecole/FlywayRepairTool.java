package com.example.ecole;
import org.flywaydb.core.Flyway;

public class FlywayRepairTool {
    public static void main(String[] args) {
        // Configuration de Flyway
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:h2:C:/Users/bilal/Desktop/DB_projet", "bilal", "lolo")
                .load();

        // Réparation de la base de données
        flyway.repair();

        // Migration de la base de données
        flyway.migrate();


    }
}

