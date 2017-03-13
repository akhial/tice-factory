package com.team33.model.csv;

import java.util.ArrayList;

/**
 * Created by hamza on 09/03/2017.
 */
public class Student {
    private String username;
    private String firstName;
    private String lastName;
    private String lastNameInMoodle;
    private String level;
    private String optin;
    private String groupe;
    private String email;
    private String password;
    private ArrayList<String> listOfEmails;
    private int positionInWorkbookIn;
    private int positionInWorkbookOut;

    public void setEmail(String email) {
        this.email = email;
    }

    public Student() {
        this.listOfEmails = new ArrayList<String>();
    }

    public int getPositionInWorkbookIn() {
        return positionInWorkbookIn;
    }

    public void setPositionInWorkbookIn(int positionInWorkbookIn) {
        this.positionInWorkbookIn = positionInWorkbookIn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPositionInWorkbookOut() {
        return positionInWorkbookOut;
    }

    public void setPositionInWorkbookOut(int positionInWorkbookOut) {
        this.positionInWorkbookOut = positionInWorkbookOut;
    }

    public ArrayList getListOfEmails() {
        return listOfEmails;
    }

    public void setListOfEmails(ArrayList listOfEmails) {
        this.listOfEmails = listOfEmails;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail() {
        if(this.listOfEmails.size() == 1)
        {
            this.email = listOfEmails.get(0);
        }
        else
        {
            this.email = "";
        }
    }

    public boolean hasEmail()
    {
        if(this.email.equals("") ) return false;
        else return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastNameInMoodle() {
        return lastNameInMoodle;
    }

    public void setLastNameInMoodle(String lastNameInMoodle) {
        this.lastNameInMoodle = lastNameInMoodle;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOptin() {
        return optin;
    }

    public void setOptin(String optin) {
        this.optin = optin;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public void generateUsename()
    {
        if(!(this.email).equals("")) {
            this.username = this.email.replaceFirst(String.valueOf(email.charAt(0)), "");
            this.username = username.replace("@esi.dz", "");
        }else this.username = "";
    }

    public void createLastNameInMoodle()
    {
        this.lastNameInMoodle = this.lastName+" "+this.level+this.groupe;
    }

    public String getUsername() {
        return username;
    }
    public String toString()
    {
        return this.firstName+" "+this.lastName;
    }

    public boolean equals(Student student)
    {
        return ((this.firstName.toLowerCase().equals(student.getFirstName().toLowerCase()) && (this.lastName.toLowerCase().charAt(0) == student.getLastName().toLowerCase().charAt(0))) && (this.positionInWorkbookIn != student.getPositionInWorkbookIn()));
    }

    public Object clone() {
        Object o = null;
        try {
            // On récupère l'instance à renvoyer par l'appel de la
            // méthode super.clone()
            o = super.clone();
        } catch(CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implémentons
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }
        // on renvoie le clone
        return o;
    }

}
