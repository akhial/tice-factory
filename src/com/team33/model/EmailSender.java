import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by mouha on 21/02/2017.
 */

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EmailSender {

public static void main(String args[])
{

    XSSFWorkbook wb = null;  // Pour manipuler le fichier Excel du service web contenant les emails
    String mailSearcher = new String(); // Servira a extraire les mails de l'Excel
    String mailCriteria = "@esi.dz"; // Pour vérifier si c'est un mail ESI



    try{
        // On commence d'abord par remplir le format texte de notre mail qui contient nos informations :

        String host = "smtp.gmail.com"; // SMTP = Simple Message Transfer Protocol
        String user = "makliltest@gmail.com"; // Email expéditeur
        String pass = "20162017"; // Mot de passe du compte expéditeur
        String to = "fm_aklil@esi.dz"; // Initialisation email destinataire
        String from = "makliltest@gmail.com"; // Expediteur
        String subject = "The way to the 1000 !  "; // Objet ou titre du message à transmettre
        String messageText = " This Email is to test if we can reach 1000 sent mails "; // Contenu du message à transmettre
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

        try {

            // On accède au fichier excel contenant les emails

            wb = new XSSFWorkbook(new FileInputStream(new File("lesmails.xlsx")));
            for (Sheet sh : wb) {
                for (Row rw : sh) {
                    for (Cell cell : rw) {

                        // Cette boucle sert à parcourir toutes les cellules du fichier excel

                        // On récupère le contenu de la cellule en "chaine de caractere"

                        mailSearcher = cell.getStringCellValue();

                        // Si notre chaine vérifie les conditions du mail, dans notre cas une adresse @esi.dz
                        // Le message lui sera transmis grace au protocole SMTP

                        if (mailSearcher.contains(mailCriteria) || mailSearcher.contains("@gmail.com"))
                        {
                            to = mailSearcher;
                            InternetAddress[] adress = {new InternetAddress(to)};
                            msg.setRecipients(Message.RecipientType.TO, adress);
                            msg.setSubject(subject); msg.setSentDate(new Date());
                            msg.setText(messageText);



                            transport.sendMessage(msg, msg.getAllRecipients());

                            System.out.println("\t Message successfully sent to " + to +  " !");
                        }
                    }
                    System.out.println();
                }
            }
            transport.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (wb != null) wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }catch (Exception ex)
    {
        System.out.println(ex);
    }

}

}
