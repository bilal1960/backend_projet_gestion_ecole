package com.example.ecole.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bilal45001@hotmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendRegistrationConfirmationEmail(String to, LocalDate deadline, String section, String secondaire_anne) {
        String subject = "Confirmation d'Inscription à l'École";
        String content = String.format("Cher/Chère Étudiant,\n\n" +
                        "Nous vous confirmons votre préinscription à notre école en Humanité %s pour l'année secondaire: %s. " +
                        "Veuillez noter que vous devez finaliser votre inscription en personne avant le %s à Rue Ernest Laude 29 1030 Schaerbeek.\n\n" +
                        "Cordialement,\nL'Équipe Administrative de l'École",
                section, secondaire_anne, deadline.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

        sendEmail(to, subject, content);
    }

    public void sendCourseConfirmationEmail(String to, LocalDate courseDate, String courseName, String courseTime, String finTime) {
        String subject = "Confirmation de Programmation de Cours";

        String content = String.format("Cher/Chère Professeur,\n\n" +
                        "Nous avons le plaisir de vous confirmer que vous êtes programmé(e) pour enseigner le cours '%s'. " +
                        "Détails du cours :\n" +
                        "- Date : %s\n" +
                        "- Heure de début : %s\n" +
                        "- Heure de fin : %s\n\n" +
                        "Nous sommes impatients de bénéficier de votre expertise et de votre contribution au succès de nos étudiants.\n\n" +
                        "Cordialement,\nL'Équipe Administrative de l'École",
                courseName,
                courseDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                courseTime,
                finTime);

        sendEmail(to, subject, content);
    }

    public void sendStudentResultEmail(String to, String studentName, String matiereName, LocalDate deliberation, String session, boolean reussi, double resultat) {
        String subject = "Résultat de la Matière";


        String content = String.format("Cher/Chère %s,\n\n" +
                        "Nous souhaitons vous informer des résultats de la matière '%s'.\n" +
                        "Session : %s\n" +
                        "Délibération : %s\n" +
                        "Vous avez %s à cette matière avec une note de %.2f.\n\n" +
                        "Nous vous encourageons à continuer vos efforts et restons à votre disposition pour tout soutien nécessaire.\n\n" +
                        "Cordialement,\nL'Équipe Administrative de l'École",
                studentName,
                matiereName,
                session,
                deliberation.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                reussi ? "réussi"  : "échouer",
                resultat);

        sendEmail(to, subject, content);
    }

    public void sendVacationNotificationEmail(String to, String studentName, String vacationName, LocalDate vacationStart, LocalDate vacationEnd) {
        String subject = "Notification de Vacances Scolaires - " + vacationName;

        String content = String.format("Cher/Chère %s,\n\n" +
                        "Nous vous informons que les vacances de %s débuteront le %s et se termineront le %s.\n\n" +
                        "Nous vous souhaitons de passer d'agréables vacances et de profiter de ce temps pour vous reposer et vous ressourcer. N'hésitez pas à explorer de nouvelles activités et à enrichir vos connaissances pendant cette période.\n\n" +
                        "Au plaisir de vous retrouver en pleine forme pour la reprise des cours.\n\n" +
                        "Cordialement,\nL'Équipe Administrative de l'École",
                studentName,
                vacationName,
                vacationStart.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                vacationEnd.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

        sendEmail(to, subject, content);
    }

    public void sendProfessorAbsenceNotificationEmail(String to, String professorName, LocalDate absenceDate, LocalTime heuredebut, LocalTime heurefin) {
        String subject = "Notification d'Absence de Professeur";

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedHeureDebut = heuredebut.format(timeFormatter);
        String formattedHeureFin = heurefin.format(timeFormatter);

        String content = String.format("Cher/Chère Étudiant(e),\n\n" +
                        "Nous tenons à vous informer de l'absence de notre professeur %s le %s, de %s à %s.\n" +
                        "Raison de l'absence : maladie.\n\n" +
                        "En conséquence, le cours prévu pendant cette période est suspendu. Nous mettons tout en œuvre pour minimiser l'impact de cette absence et vous informerons des arrangements pris en conséquence, y compris la reprogrammation du cours si nécessaire.\n\n" +
                        "Nous vous remercions de votre compréhension et nous excusons pour les désagréments éventuels.\n\n" +
                        "Cordialement,\nL'Équipe Administrative de l'École",
                professorName,
                absenceDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                formattedHeureDebut,
                formattedHeureFin);

        sendEmail(to, subject, content);
    }


}

