package com.example.ecole;
import org.flywaydb.core.Flyway;

public class FlywayRepairTool {
    public static void main(String[] args) {
        // Configuration de Flyway
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:h2:mem:41f6e242-3f46-4867-ad5c-793ff0142a47", "bilal", "lolo")
                .load();

        // Réparation de la base de données
        flyway.repair();

        // Migration de la base de données
        flyway.migrate();


    }
}

