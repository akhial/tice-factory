package com.team33.model.csv.students.courses;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by hamza on 18/04/2017.
 */
public class CoursesStore implements Serializable {

    private CyclePreparatiore cyclePreparatoire = new CyclePreparatiore();
    private CycleSuperieur cycleSuperieur = new CycleSuperieur();

    public CyclePreparatiore getCyclePreparatoire() {
        return cyclePreparatoire;
    }

    public CycleSuperieur getCycleSuperieur() {
        return cycleSuperieur;
    }

    public void saveChanges(){
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("courses")));
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (oos != null) try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void load(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("courses")));
            CoursesStore coursesStore = (CoursesStore) ois.readObject();
            this.cyclePreparatoire = coursesStore.cyclePreparatoire;
            this.cycleSuperieur = coursesStore.cycleSuperieur;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initialize(){
        this.getCycleSuperieur().getCS2().ajouterOption("SIQ");
        this.getCycleSuperieur().getCS2().ajouterOption("SIT");
        this.getCycleSuperieur().getCS2().ajouterOption("SIL");
        this.getCyclePreparatoire().getCPI1().ajouterCoursSemsetre1("TEO1","Techniques d'expression orale");
        this.getCyclePreparatoire().getCPI1().ajouterCoursSemsetre1("SYS1","Introduction aux Système d'Exploitation 1");
        this.getCyclePreparatoire().getCPI1().ajouterCoursSemsetre1("ALSDS","Algorithmique et Structure de Données Statiques (ALDS)");
        this.getCyclePreparatoire().getCPI1().ajouterCoursSemsetre1("BW","Bureautique et Windows");
        this.getCyclePreparatoire().getCPI1().ajouterCoursSemsetre1("ARCH1","Architecture des ordinateurs 1");
        this.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("ANG2","Anglais 2");
        this.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("MECA","Mécanique du point");
        this.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("ALG2","Algèbre 2");
        this.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("SYST2","Introduction aux Système d'Exploitation 2");
        this.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("ALSDD","Algorithmique et structure de données dynamiques");
        this.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("TEE","Techniques d'expression écrite");
        this.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("ELECTF1","Electronique fondamentale 1");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("SFSD","Structure fichiers et structures de données");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("ANG3","Anglais 3");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("ECON","Economie d'entreprise");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("PRST1","Probabilités et statistiques 1");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("ELECT2","Electronique fondamentale 2");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("ALG3","Algèbre 3");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("ANA3","Analyse mathématiques 3");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("ARCH2","Architechture des ordinateurs 2");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("ANG4","Anglais 4");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("PRJP","Projet pluridisciplinaire");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("OOE","Optique et ondes électromagneétiques");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("LOGM","Logique mathématique");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("ANA4","Analyse mathématique 4");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("SINF","Introduction aux systèmes d'information");
        this.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("POO","Programmation orientée objets");
        this.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("ANG5","Anglias 1");
        this.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("RO","RO (Graphes & Algorithmes)");
        this.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("ANUM","Analyse Numérique");
        this.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("ORG","Analyse des organisations");
        this.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("THP","Théorie des langages de programmation et applications");
        this.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("RES1","Réseaux I");
        this.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("SYSC","Systèmes Centralisés I");
        this.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("GL","Introduction au Génie logiciel");
        this.getCycleSuperieur().getCS1().ajouterCoursSemstre2("ProjTrans","Projet Transversal");
        this.getCycleSuperieur().getCS1().ajouterCoursSemstre2("BDD","Base de Données");
        this.getCycleSuperieur().getCS1().ajouterCoursSemstre2("ANG6","Anglias 2");
        this.getCycleSuperieur().getCS1().ajouterCoursSemstre2("CPROJ","Conduite de Projet");
        this.getCycleSuperieur().getCS1().ajouterCoursSemstre2("MCSI","Méthodologies d'analyse et conception de Système d'Information");
        this.getCycleSuperieur().getCS1().ajouterCoursSemstre2("ARCH3","Architecture");
        this.getCycleSuperieur().getCS1().ajouterCoursSemstre2("SEC","Introduction à la sécurité");
        this.getCycleSuperieur().getCS1().ajouterCoursSemstre2("RES2","Réseaux II");
        this.getCycleSuperieur().getCS1().ajouterCoursSemstre2("SYSC2","Systèmes Centralisés II");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIQ", "FAS_2CS_SIQ", "File d'attente et simulation");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIQ", "AQ_SIQ", "Stage Pratique en Entreprise");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIQ", "ANAD_SIQ", "Analyse et fouilles de Données");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIQ", "TPRO_SIQ", "Théorie de la programmation");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIQ", "RESA_SIQSIL", "Réseaux avancés");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIQ", "PRS_SIQ", "Projet de Spécialité");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIQ", "BDDA", "SGBD et Bases de Données Avancées");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIQ", "OPT", "Optimisation combinatoire");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIQ", "SSR_SIQ", "Sécurité des système et réseaux");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIQ", "SYSR", "Système répartis");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "SPE", "Stage pratique en entreprise");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "ANAD", "Analyse et fouille de données");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "AQUA", "Assurance qualité");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "MPSI", "Management du changement dans les projets SI");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "SIAD", "S.I d'aide à la décision : Méthodes et Outils");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "TICO", "TIC en Organisation");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "BDA", "Base de données avancées");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "SI", "Analyse des Systèmes d'infformation");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIT", "PRJS", "Projet de spécialité");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIT", "COFI", "Comptabilité et Finance");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIT", "FASI", "Files d'Attentes et Simulation");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIT", "PGI", "Système d'Information à base de PGI");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIT", "MSSI", "Ingenierie et manangement de la Sécurité de S.I");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIT", "SIC", "Systèmes d'Information Copératifs");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIL", "MAGIL", "Méthodes agiles");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIL", "ANAFD", "Analyse et fouilles de donnes");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIL", "PATCON", "Patrons de conception");
        this.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIL", "TECWEB", "Technologies et developement Web");
        saveChanges();
    }

    public Collection<String> getListOfCourses(String level, String optin){
        ArrayList<String> list = new ArrayList<>();
        switch (level){
            case  "1CPI" :
                for(Course course : cyclePreparatoire.getCPI1().getSemestre1()){
                    list.add(course.getShortName());
                }
                for (Course course : cyclePreparatoire.getCPI1().getSemestre2()){
                    list.add(course.getShortName());
            }
            return list;

            case "2CPI" :
                for(Course course : cyclePreparatoire.getCPI2().getSemestre1()){
                    list.add(course.getShortName());
                }
                for (Course course : cyclePreparatoire.getCPI2().getSemestre2()){
                    list.add(course.getShortName());
                }
                return list;
            case "1CS" :
                for(Course course : cycleSuperieur.getCS1().getSemestre1()){
                    list.add(course.getShortName());
                }
                for (Course course : cycleSuperieur.getCS1().getSemestre2()){
                    list.add(course.getShortName());
                }
                return list;
            case "2CS":
                for(Course course : cycleSuperieur.getCS2().getSemestre1(optin)){
                    list.add(course.getShortName());
                }
                for (Course course : cycleSuperieur.getCS2().getSemestre2(optin)){
                    list.add(course.getShortName());
                }
                for (Course course : cycleSuperieur.getCS2().getModulesCommuns()){
                    list.add(course.getShortName());
                }
                return list;
            case "3CS" :
                for(Course course : cycleSuperieur.getCS3courses()){
                    list.add(course.getShortName());
                }
                return list;
        }

        return list;
    }


    public int getNumberOfCourses(String level, String optin) {
        return getListOfCourses(level,optin).size();
    }
}
