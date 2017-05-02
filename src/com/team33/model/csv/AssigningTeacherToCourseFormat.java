package com.team33.model.csv;

import com.team33.model.Utilities.Util;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Created by Amine on 13/02/2017.
 */
public class AssigningTeacherToCourseFormat extends UserFormat implements CSVFormat {
    private HashMap<String,ArrayList<String>> unHandledEmails;
    private final String tempName = "TeachersListWithCourses.xlsx";
    public HashMap<String, ArrayList<String>> getUnHandledEmails() {
        return unHandledEmails;
    }


    public AssigningTeacherToCourseFormat() {
        unHandledEmails = new HashMap<>();
    }

    /**
     * Génére la première ligne du fichier temporaire (i.e. *.xlsx)
     * **/
    public void generateHeaderInTempFile(XSSFWorkbook workbook,int maxColumn) {
        Row row = workbook.getSheetAt(0).getRow(0);
        int j = 1;
        for (int i = 5; i < maxColumn+4;i += 2)
        {
            row.createCell(i);
            row.createCell(i+1);
            row.getCell(i).setCellValue("course"+j);
            row.getCell(i+1).setCellValue("type"+j);
            j++;
        }
    }
    /**
     * Cette méthde permet l'ajout du nom du cour avec le role dans ce cour à partir de la chine info
     * qui représente la valeur d'une cellule du fichier des charges
     * **/
    private void AddInformation(ArrayList<String> arrayList,String info,boolean isCourse){
        if (!(info == null||info.isEmpty()) ){
            if (isCourse){
                arrayList.add(info.replace("Coordination","")
                        .replace("+","").substring(info.lastIndexOf("*")+1).toUpperCase().replace(" ",""));
                arrayList.add(String.valueOf(2));
            }
            else {
                if (!arrayList.contains(info.substring(info.lastIndexOf("*")+1).toUpperCase().replace(" ",""))){
                    arrayList.add(info.replace("Coordination","")
                            .replace("+","").substring(info.lastIndexOf("*")+1).toUpperCase().replace(" ",""));
                    arrayList.add(String.valueOf(3));

                }

            }
        }

    }
    /**
     * Permet d'obtenir tous les cours d'un enseignants donné avec son rôle dans ces cours-là
     * **/
    private void getAssignedCourses(Row row, int firstCourse, int CourseColumn, ArrayList<String> arrayList){
        String info = row.getCell(CourseColumn).toString();
        boolean isCourse = false;
        if ((firstCourse % 2) == (CourseColumn % 2)) isCourse = true;
       if (info.contains("\n")){
           while (info.contains("\n")){
               String info1 = info.substring(0,info.indexOf('\n'));
               AddInformation(arrayList,info1,isCourse);
               info = info.substring(info.indexOf('\n')+1);
           }
       }
        else AddInformation(arrayList,info,isCourse);
    }
    /**
     * génére une ligne (i.e. les infos d'un enseignants ) dans le fichier de sortie
     * **/
    public void generateRow(int numRow,int index ,ArrayList<String> arrayList)
    {
        Row rw = getWorkbookOut().getSheetAt(0).getRow(numRow);
        for (int j = index; j < index + arrayList.size(); j++) {
            rw.createCell(j).setCellValue(arrayList.get(j-index));
        }
    }

    /***
     *Cette méthode crée un fichier (*.xlsx) qui sera converti après en csv pour l'affectation des  enseignants aux espaces cours associés
     * elle accepte en entrée le fichier des charges + le fichier des e-mails
     * **/
    @Override
    public String buildCSV(ArrayList<String> workbooksPaths) throws IOException, InvalidFormatException {
        String workbookPath = workbooksPaths.get(0);
        String emailWorkbookPath = workbooksPaths.get(1);
        openWorkbookIn(workbookPath);
        openEmailWorkbook(emailWorkbookPath);
        TeacherFormat teacherFormat = new TeacherFormat();
        teacherFormat.setGeneratedPassword(this.isGeneratedPassword());
        String tempFile = teacherFormat.buildCSV(workbooksPaths);
        unHandledEmails = teacherFormat.getUnHandledEmails();
        FileInputStream fileInputStream = new FileInputStream(tempFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        setWorkbookOut(workbook);
        Iterator<Row> rowIterator = getWorkbookIn().getSheetAt(0).rowIterator();
        Row row = rowIterator.next();
        int maxColumn = 0 ;
        boolean found = false ;
        while ((rowIterator.hasNext()) && (!found)){
            if (rowContains(row,"NOM")) found = true;
            else row = rowIterator.next();
        }
        int firstCourseColumn = Util.getInstance().column(row,"COURS\n" + "SEMESTRE1");
        ArrayList<String>  arrayList = new ArrayList<>();
        int numRow = 1 ;
        while (rowIterator.hasNext()){
            row = rowIterator.next();
            for (int i = firstCourseColumn ; i < firstCourseColumn+5 ; i++) getAssignedCourses(row,firstCourseColumn,i,arrayList);
            generateRow(numRow,5, (ArrayList<String>) arrayList.clone());
            if (arrayList.size() > maxColumn) maxColumn = arrayList.size();
            arrayList.clear();
            numRow++;
        }
        generateHeaderInTempFile(workbook,maxColumn);
        File file = new File(tempName);
        saveUsersList(file);
        new File(tempFile).delete();
        return file.getPath() ;
    }
    /**
     *  Cette méthode est utilisée pour ajouter les informations manquantes dans le fichier initiale
     *  à cause de l'ambiguîté ou d'inexistance des e-mails
     * */
    public void AddingMissingEmails(HashMap<String,String> finalEmails) throws IOException, InvalidFormatException {
        FileInputStream file = new FileInputStream(tempName);
        this.setWorkbookOut(new XSSFWorkbook(file));
        for (HashMap.Entry e:finalEmails.entrySet()) {
            StringTokenizer stringTokenizer = new StringTokenizer((String) e.getKey(),"*");
            String lastName = stringTokenizer.nextToken();
            String firstName = stringTokenizer.nextToken();
            String email = (String) e.getValue();
            Row row = getWorkbookOut().getSheetAt(0).getRow(1);
            boolean found = false;
            int i = 1 ;
            while (!found && i < getWorkbookOut().getSheetAt(0).getLastRowNum()+1){
                row = getWorkbookOut().getSheetAt(0).getRow(i);
                if (rowContains(row,"ENS: "+lastName.toUpperCase()) && rowContains(row,firstName.toUpperCase())){
                    row.getCell(0).setCellValue((email.indexOf("@") != -1) ? email.substring(0, email.indexOf("@")):null);
                    row.getCell(4).setCellValue(email);
                    found = true;


                }
                i++;
            }
        }
        file.close();
        saveUsersList(new File(tempName));

    }
}
