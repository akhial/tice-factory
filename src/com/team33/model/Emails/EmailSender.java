package com.team33.model.Emails;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class EmailSender {


    public static void SendEmails(String user, String APIKey, String SecretKey, String subject, String messageText, String filePath) throws IOException, MessagingException, Exception  {
        SendEmails(user, APIKey, SecretKey, subject, messageText, filePath, false);

    }

    public static void SendEmails(String user, String APIKey, String SecretKey, String subject, String messageText, String filePath, boolean withPassword) throws MessagingException, IOException,Exception {


        String mailSearcher; // Servira a extraire les mails de l'Excel
        String mailCriteria = "@esi.dz"; // Pour vérifier si c'est un mail ESI


        // On commence d'abord par remplir le format texte de notre mail qui contient nos informations :

        String To; // Initialisation email destinataire


        // Ensuite on configure les propriétés qui seront transmises au serveur
        Properties props = new Properties();

        props.put("mail.smtp.host", "in.mailjet.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(APIKey, SecretKey);
                    }
                });


        if (!withPassword) { // Message simple

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine(); // Lecture de la 1 ère ligne

            if (!line.equals("")) { // Si la ligne n'est pas vide
                if ((line.split(",")).length < 5) throw new Exception(" Nombre de champs insuffisants "); // Nombre de champs insuffisant
                else {
                    if ((!(line.split(","))[0].equals("username")) || (!(line.split(","))[1].equals("password") || (!(line.split(","))[4].equals("email"))))
                        throw new Exception(" Format incorrect ");
                    // Check les champs 0, 1 et 4 du fichier CSV pour vérifier le format
                }
            } else throw new Exception(" Fichier incorrect "); // 1 ere ligne vide ==> Mauvais fichier;


            while ((line = br.readLine()) != null) {
                String[] Splitter = line.split(",");
                if (!(Splitter.length < 5)) {
                    // Ignorer si la ligne contient moins de 4 champs c'est a dire email manquant
                    mailSearcher = Splitter[4];

                    if (mailSearcher.contains(mailCriteria)) {

                        To = mailSearcher;
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(user));
                        message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(To));
                        message.setSubject(subject);
                        message.setText(messageText);
                        Transport.send(message);
                    }
                }

            }
        } else { // Message personnalisé

            String username; // Moodle username
            String pwd; // Moodle password
            String messagePersonnalise; // Default message + username and password
            // for each mail
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine(); // Lecture de la 1 ère ligne

            if (!line.equals("")) { // Si la ligne n'est pas vide
                if ((line.split(",")).length < 5) throw new Exception(" Nombre de champs insuffisant "); // Nombre de champs insuffisant
                else {
                    if ((!(line.split(","))[0].equals("username")) || (!(line.split(","))[1].equals("password") || (!(line.split(","))[4].equals("email"))))
                        throw new Exception(" Format incorrect ");
                    // Check les champs 0, 1 et 4 du fichier CSV pour vérifier le format
                }
            } else throw new Exception("Fichier incorrect "); // 1 ere ligne vide ==> Mauvais fichier;


            while ((line = br.readLine()) != null) {

                messagePersonnalise = messageText;

                String[] Splitter = line.split(",");
                if (!(Splitter.length < 5)) {
                 // Ignorer si la ligne contient moins de 4 champs c'est a dire email manquant
                    username = Splitter[0];
                    pwd = Splitter[1];
                    messagePersonnalise += ("\n username : " + username + "\t password : " + pwd);
                    mailSearcher = Splitter[4];

                    if (!username.equals("") && mailSearcher.contains(mailCriteria)) {
                        To = mailSearcher;
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(user));
                        message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(To));
                        message.setSubject(subject);
                        message.setText(messagePersonnalise);
                        Transport.send(message);
                    }
                }
            }
        }
    }
}