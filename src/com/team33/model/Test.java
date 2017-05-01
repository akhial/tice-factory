package com.team33.model;

import com.team33.model.csv.students.*;
import com.team33.model.csv.students.courses.Course;
import com.team33.model.csv.students.courses.CoursesStore;
import com.team33.model.csv.students.courses.UnExistingOptionException;

import java.io.IOException;
import java.util.ArrayList;

public class Test {

    private static ArrayList<String> listOfUsedEmails = new ArrayList<>();
    public static void main(String[] args) throws IOException {
       ArrayList<String> workbooksPaths = new ArrayList<String>();

        /*workbooksPaths.add("liste email tous les etudiants.xlsx");
        workbooksPaths.add("NOTES ORGA 1CS 2017 BENYAHIA.xlsx");
        GradesFormat studentFormat = new GradesFormat("1CS","SC","");
        CSVBuilder csvBuilder = new CSVBuilder(workbooksPaths,studentFormat,"C:\\Users\\hamza\\Desktop\\TICE");
        csvBuilder.buildCSV();
        String email = null;
        for(Student student : studentFormat.getListOfStudentsWithoutEmail())
        {
            System.out.print(student);

            if(student.getListOfEmails().isEmpty())
            { //poopup 1
                System.out.println(" : ");
                Scanner scanner = new Scanner(System.in);
                email = scanner.nextLine();
                student.setEmail(email);

            }else
            {
                deleteUsedEmails(student);
                student.tryToSetEmail();
                if(!student.hasEmail())
                {
                    System.out.println(student.getListOfEmails());
                    Scanner scanner = new Scanner(System.in);
                    int i = scanner.nextInt();
                    email =(String) student.getListOfEmails().get(i);
                    student.setEmail(email);
                }
            }
            student.generateUsename();
            studentFormat.updateRow(student.getPositionInWorkbookOut(),student);
            listOfUsedEmails.add(email);
        }
        studentFormat.saveUsersList(new File(csvBuilder.getTempPath()));
        csvBuilder.convertToCSV();*/
        /*CoursesStore coursesStore = new CoursesStore();
        coursesStore.getCycleSuperieur().getCS2().ajouterOption("SIQ");
        coursesStore.getCycleSuperieur().getCS2().ajouterOption("SIT");
        coursesStore.getCycleSuperieur().getCS2().ajouterOption("SIL");
        coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemsetre1("TEO1","Techniques d'expression orale");
        coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemsetre1("SYS1","Introduction aux Système d'Exploitation 1");
        coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemsetre1("ALSDS","Algorithmique et Structure de Données Statiques (ALDS)");
        coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemsetre1("BW","Bureautique et Windows");
        coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemsetre1("ARCH1","Architecture des ordinateurs 1");
        coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("ANG2","Anglais 2");
        coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("MECA","Mécanique du point");
        coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("ALG2","Algèbre 2");
        coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("SYST2","Introduction aux Système d'Exploitation 2");
        coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("ALSDD","Algorithmique et structure de données dynamiques");
        coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("TEE","Techniques d'expression écrite");
        coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("ELECTF1","Electronique fondamentale 1");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("SFSD","Structure fichiers et structures de données");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("ANG3","Anglais 3");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("ECON","Economie d'entreprise");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("PRST1","Probabilités et statistiques 1");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("ELECT2","Electronique fondamentale 2");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("ALG3","Algèbre 3");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("ANA3","Analyse mathématiques 3");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("ARCH2","Architechture des ordinateurs 2");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("ANG4","Anglais 4");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("PRJP","Projet pluridisciplinaire");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("OOE","Optique et ondes électromagneétiques");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("LOGM","Logique mathématique");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("ANA4","Analyse mathématique 4");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("SINF","Introduction aux systèmes d'information");
        coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("POO","Programmation orientée objets");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("ANG5","Anglias 1");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("RO","RO (Graphes & Algorithmes)");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("ANUM","Analyse Numérique");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("ORG","Analyse des organisations");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("THP","Théorie des langages de programmation et applications");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("RES1","Réseaux I");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("SYSC","Systèmes Centralisés I");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("GL","Introduction au Génie logiciel");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemstre2("ProjTrans","Projet Transversal");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemstre2("BDD","Base de Données");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemstre2("ANG6","Anglias 2");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemstre2("CPROJ","Conduite de Projet");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemstre2("MCSI","Méthodologies d'analyse et conception de Système d'Information");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemstre2("ARCH3","Architecture");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemstre2("SEC","Introduction à la sécurité");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemstre2("RES2","Réseaux II");
        coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemstre2("SYSC2","Systèmes Centralisés II");
        try {


            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIQ", "FAS_2CS_SIQ", "File d'attente et simulation");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIQ", "AQ_SIQ", "Stage Pratique en Entreprise");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIQ", "ANAD_SIQ", "Analyse et fouilles de Données");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIQ", "TPRO_SIQ", "Théorie de la programmation");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIQ", "RESA_SIQSIL", "Réseaux avancés");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIQ", "PRS_SIQ", "Projet de Spécialité");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIQ", "BDDA", "SGBD et Bases de Données Avancées");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIQ", "OPT", "Optimisation combinatoire");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIQ", "SSR_SIQ", "Sécurité des système et réseaux");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIQ", "SYSR", "Système répartis");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "SPE", "Stage pratique en entreprise");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "ANAD", "Analyse et fouille de données");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "AQUA", "Assurance qualité");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "MPSI", "Management du changement dans les projets SI");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "SIAD", "S.I d'aide à la décision : Méthodes et Outils");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "TICO", "TIC en Organisation");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "BDA", "Base de données avancées");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIT", "SI", "Analyse des Systèmes d'infformation");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIT", "PRJS", "Projet de spécialité");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIT", "COFI", "Comptabilité et Finance");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIT", "FASI", "Files d'Attentes et Simulation");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIT", "PGI", "Système d'Information à base de PGI");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIT", "MSSI", "Ingenierie et manangement de la Sécurité de S.I");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("SIT", "SIC", "Systèmes d'Information Copératifs");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIL", "MAGIL", "Méthodes agiles");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIL", "ANAFD", "Analyse et fouilles de donnes");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIL", "PATCON", "Patrons de conception");
            coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("SIL", "TECWEB", "Technologies et developement Web");
        }catch (UnExistingOptionException e){
            e.printStackTrace();
        }
        coursesStore.saveChanges();*/
        /*coursesStore.load();
        coursesStore.getCycleSuperieur().getCS2().ajouterModuleCommun("ALOG","Arcihetctures logicielles");
        coursesStore.getCycleSuperieur().getCS2().ajouterModuleCommun("COM","Compilation");
        coursesStore.saveChanges();
        System.out.println(coursesStore.getCycleSuperieur().getCS2().getSemestre1("SIT"));*/

        CoursesStore coursesStore = new CoursesStore();
        coursesStore.load();
        System.out.println("CYCLE PREPARATOIRE");
        System.out.println("\t>1CP");
        System.out.println("\t\t>SEMESTRE1");

        for(Course course: coursesStore.getCyclePreparatoire().getCPI1().getSemestre1()){
            System.out.println("\t\t\t"+course);
        }
        System.out.println("\t\tSEMESTRE2");
        //System.out.println("coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemsetre1("+'"'+course.getShortName()+'"'+","+'"'+course.getFullName()+'"'+");");
        for(Course course: coursesStore.getCyclePreparatoire().getCPI1().getSemestre2()){
            System.out.println("\t\t\t"+course);
        }
        System.out.println("\t>2CP");
        System.out.println("\t\t>SEMESTRE1");
        //System.out.println("coursesStore.getCyclePreparatoire().getCPI1().ajouterCoursSemstre2("+'"'+course.getShortName()+'"'+','+'"'+course.getFullName()+'"'+");");
        for(Course course: coursesStore.getCyclePreparatoire().getCPI2().getSemestre1()){
            System.out.println("\t\t\t"+course);
        }
        System.out.println("\t\t>SEMESTRE2");
        //System.out.println("coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemsetre1("+'"'+course.getShortName()+'"'+","+'"'+course.getFullName()+'"'+");");
        for(Course course: coursesStore.getCyclePreparatoire().getCPI2().getSemestre2()){
            System.out.println("\t\t\t"+course);
        }
        System.out.println("CYCLE SUPERIEUR");
        System.out.println("\t>1CS");
        System.out.println("\t\t>SEMESTRE1");
        //System.out.println("coursesStore.getCyclePreparatoire().getCPI2().ajouterCoursSemstre2("+'"'+course.getShortName()+'"'+','+'"'+course.getFullName()+'"'+");");
        for(Course course: coursesStore.getCycleSuperieur().getCS1().getSemestre1()){
            System.out.println("\t\t\t"+course);
        }
        System.out.println("\t\t>SEMESTRE2");
        //System.out.println("coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemsetre1("+'"'+course.getShortName()+'"'+','+'"'+course.getFullName()+'"'+");");
        for(Course course: coursesStore.getCycleSuperieur().getCS1().getSemestre2()){
            System.out.println("\t\t\t"+course);
        }
        System.out.println("\t>2CS");
        System.out.println("\t\t>SIQ");
        System.out.println("\t\t\t>SEMESTRE1");
        //System.out.println("coursesStore.getCycleSuperieur().getCS1().ajouterCoursSemstre2("+'"'+course.getShortName()+'"'+','+'"'+course.getFullName()+'"'+");");
        for(Course course: coursesStore.getCycleSuperieur().getCS2().getSemestre1("SIQ")){
            System.out.println("\t\t\t\t"+course);
        }
        System.out.println("\t\t\t>SEMESTRE2");
        //System.out.println("coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("+'"'+"SIQ"+'"'+','+'"'+course.getShortName()+'"'+','+'"'+course.getFullName()+'"'+");");
        for(Course course: coursesStore.getCycleSuperieur().getCS2().getSemestre2("SIQ")){
            System.out.println("\t\t\t\t"+course);
        }

        System.out.println("\t\t>SIT");
        System.out.println("\t\t\t>SEMESTRE1");
        //System.out.println("coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("+'"'+"SIQ"+'"'+','+'"'+course.getShortName()+'"'+','+'"'+course.getFullName()+'"'+");");
        for(Course course: coursesStore.getCycleSuperieur().getCS2().getSemestre1("SIT")){
            System.out.println("\t\t\t\t"+course);
        }
        System.out.println("\t\t\t>SEMESTRE2");
        //System.out.println("coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("+'"'+"SIT"+'"'+','+'"'+course.getShortName()+'"'+','+'"'+course.getFullName()+'"'+");");
        for(Course course: coursesStore.getCycleSuperieur().getCS2().getSemestre2("SIT")){
            System.out.println("\t\t\t\t"+course);
        }
        System.out.println("\t\t>SIL");
        System.out.println("\t\t\t>SEMESTRE1");
        //System.out.println("coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre2("+'"'+"SIT"+'"'+','+'"'+course.getShortName()+'"'+','+'"'+course.getFullName()+'"'+");");
        for(Course course: coursesStore.getCycleSuperieur().getCS2().getSemestre1("SIL")){
            System.out.println("\t\t\t\t"+course);
        }
        System.out.println("\t\t\t>SEMESTRE2");
        //System.out.println("coursesStore.getCycleSuperieur().getCS2().ajouterCoursSemestre1("+'"'+"SIL"+'"'+','+'"'+course.getShortName()+'"'+','+'"'+course.getFullName()+'"'+");");
        for(Course course: coursesStore.getCycleSuperieur().getCS2().getSemestre2("SIL")){
            System.out.println("\t\t\t\t"+course);
        }
        System.out.println("\t\t>Modules Communs");
        for(Course course : coursesStore.getCycleSuperieur().getCS2().getModulesCommuns()){
            System.out.println("\t\t\t"+course);
        }

    }
    public static void deleteUsedEmails(Student student)
    {
        for(int i = 0; i < student.getListOfEmails().size();i++)
            if (listOfUsedEmails.contains((String) student.getListOfEmails().get(i)))
            {
                student.getListOfEmails().remove(student.getListOfEmails().get(i));
            }
    }
}
