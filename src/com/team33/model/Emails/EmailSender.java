package com.team33.model.Emails;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Amine on 18/04/2017.
 */
public class EmailSender {



    public static void SendEmails(String user ,String pass,String subject,String messageText,String filePath) throws IOException, MessagingException {
        SendEmails(user,pass,subject,messageText,filePath,false);

    }

    public static void SendEmails(String user ,String pass,String subject,String messageText,String filePath,boolean withPassword) throws MessagingException, IOException {

        XSSFWorkbook wb = null;  // Pour manipuler le fichier Excel du service web contenant les emails
        String mailSearcher; // Servira a extraire les mails de l'Excel
        String mailCriteria = "@esi.dz"; // Pour vérifier si c'est un mail ESI
        int i = 0;




        // On commence d'abord par remplir le format texte de notre mail qui contient nos informations :

        String host = "smtp.gmail.com"; // SMTP = Simple Message Transfer Protocol
        String to = "fm_aklil@esi.dz"; // Initialisation email destinataire
        String from = user; // Expediteur
        boolean sessionDebug = false;

        // Ensuite on configure les propriétés qui seront transmises au serveur

        Properties props = System.getProperties();

        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.required","true");

        // Et puis on execute notre requête de session sécurisée au serveur afin de connecter le compte expéditeur

        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Session mailSession = Session.getDefaultInstance(props, null);
        mailSession.setDebug(sessionDebug);
        Message msg = new MimeMessage(mailSession);
        msg.setFrom(new InternetAddress(from));

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(host, user, pass);

        if (!withPassword){

            // On accède au fichier excel contenant les emails

            wb = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            Sheet sh = wb.getSheetAt(0);
            for (Row rw : sh) {
                for (Cell cell : rw) {

                    // Cette boucle sert à parcourir toutes les cellules du fichier excel

                    // On récupère le contenu de la cellule en "chaine de caractere"

                    mailSearcher = cell.getStringCellValue();

                    // Si notre chaine vérifie les conditions du mail, dans notre cas une adresse @esi.dz
                    // Le message lui sera transmis grace au protocole SMTP

                    if (mailSearcher.contains(mailCriteria) && i <= 80) {
                        to = mailSearcher;
                        InternetAddress[] adress = {new InternetAddress(to)};
                        //msg.addRecipients(Message.RecipientType.TO, adress);
                        msg.setRecipients(Message.RecipientType.TO, adress);

                        msg.setSubject(subject);
                        msg.setSentDate(new Date());
                        msg.setText(messageText);
                        transport.sendMessage(msg, msg.getAllRecipients());
                        i++; // On incrémente le nombre de destinataires
                    }
                }

            }
        }
        else{
            wb = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            Sheet sh = wb.getSheetAt(0);
            for (Row rw : sh) {

                if (i<80) {

                    String username = ""; // Moodle username
                    String pwd = ""; // Moodle password
                    String messagePersonnalise = messageText; // Default message + username and password
                    // for each mail

                    mailSearcher = (rw.getCell(4)).getStringCellValue();

                    if (mailSearcher.contains(mailCriteria)) {
                        to = mailSearcher;

                        username = (rw.getCell(0)).getStringCellValue();
                        pwd = (rw.getCell(1)).getStringCellValue();
                        messagePersonnalise += ("\n username : " + username + "\t password : " + pwd);

                        InternetAddress[] adress = {new InternetAddress(to)};
                        //msg.addRecipients(Message.RecipientType.TO, adress);
                        msg.setRecipients(Message.RecipientType.TO, adress);

                        msg.setSubject(subject);
                        msg.setSentDate(new Date());
                        msg.setText(messagePersonnalise);
                        transport.sendMessage(msg, msg.getAllRecipients());
                        i++; // On incrémente le nombre de destinataires
                    }
                }

            }
        }
        transport.close();

    }

    public static void testLogin(String user, String pass) throws Exception{
        String host = "smtp.gmail.com"; // SMTP = Simple Message Transfer Protocol

        boolean sessionDebug = false;

        Properties props = System.getProperties();

        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.required","true");

        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Session mailSession = Session.getDefaultInstance(props, null);
        mailSession.setDebug(sessionDebug);
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(host, user, pass);
        transport.close();

    }
}