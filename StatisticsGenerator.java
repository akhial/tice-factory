/**
 * Created by mouha on 28/02/2017.
 */

    import com.aspose.cells.CellArea;
    import com.aspose.cells.Cells;
    import com.aspose.cells.DataSorter;
    import com.aspose.cells.SortOrder;
    import com.aspose.cells.Workbook;
    import com.aspose.cells.Worksheet;

    import java.io.File;

    import java.io.FileInputStream;
    import java.io.FileOutputStream;

    import java.io.FileNotFoundException;
    import java.io.IOException;

    import org.apache.poi.ss.usermodel.Cell;
    import org.apache.poi.ss.usermodel.Row;
    import org.apache.poi.ss.usermodel.Sheet;
    import org.apache.poi.xssf.usermodel.XSSFWorkbook;

    public class StatisticsGenerator {


        interface StatisticType{
            void Stat(String filepath);
        }
       // public class AsposeDataSort
        //{
            public void sortAWorksheet(String filepath, int key1, int key2)

            {
                try {

                    System.out.println(" Sorting process, please wait...");

                    // Instantiating a Workbook object
                    Workbook workbook = new Workbook(filepath);


                    // Accessing the first worksheet in the Excel file
                    Worksheet worksheet = workbook.getWorksheets().get(0);

                    // Get the cells collection in the sheet
                    Cells cells = worksheet.getCells();

                    // Obtain the DataSorter object in the workbook
                    DataSorter sorter = workbook.getDataSorter();

                    // Set the first order
                    sorter.setOrder1(SortOrder.ASCENDING);

                    // Define the first key.
                    sorter.setKey1(key1);

                    // Set the second order
                    sorter.setOrder2(SortOrder.ASCENDING);

                    // Define the second key
                    sorter.setKey2(key2);
                    // Create a cells area (range).
                    CellArea ca = new CellArea();

                    // Specify the start row index.
                    ca.StartRow = 1;
                    // Specify the start column index.
                    ca.StartColumn = 0;
                    // Specify the last row index.
                    ca.EndRow = 1000;
                    // Specify the last column index.
                    ca.EndColumn = 5;

                    // Sort data in the specified data range (A2:C10)
                    sorter.sort(cells, ca);

                    // Saving the excel file
                    workbook.save("SORTEDINPUT.xlsx");
                    System.out.println(" Sorting process done.");
                }catch(Exception e){
                    e.printStackTrace();
            }
            }

//----------------------------------------------------------------------------------------------------------------


        public void Occur(String filepath) throws IOException {


            XSSFWorkbook wb = null;
            String searcher = ""; String reference = "";
            int occurence = 0;


            //------------------------------
            String excelFileName = "OCCUROUTPUT.xlsx";//name of excel file

            String sheetName = "Sheet1";//name of sheet

            XSSFWorkbook w = new XSSFWorkbook();
            Sheet s = w.createSheet(sheetName) ;
            int index = 0;
            //---------------------------------

            try {

                sortAWorksheet(filepath,5,0);

                wb = new XSSFWorkbook(new FileInputStream(new File("SORTEDINPUT.xlsx")));
                Sheet sheet = wb.getSheetAt(0);

                System.out.println(" Counting number of occurencies... ");

            /*for (Sheet sh : wb) { */
                for (Row rw : sheet) {
                    Cell cell = rw.getCell(5);
                    searcher = cell.getStringCellValue();

                    if (searcher.equals("Event name")) {}
                    else {
                        if (!searcher.equals(reference)) {
                            System.out.println(reference);
                            System.out.println(occurence);

                            if (index > 0) {
                                Row r = s.createRow(index);
                                Cell c = r.createCell(0);
                                c.setCellValue(reference);
                                c = r.createCell(1);
                                c.setCellValue(occurence);
                            }
                            index++;

                            occurence = 0;
                            reference = searcher;
                        }

                        occurence++;
                    }



                }
                System.out.println(reference);
                System.out.println(occurence);

                Row r = s.createRow(index);
                Cell c = r.createCell(0);
                c.setCellValue(reference);
                c = r.createCell(1);
                c.setCellValue(occurence);

                FileOutputStream fileOut = new FileOutputStream(excelFileName);
                w.write(fileOut);
                fileOut.flush();
                fileOut.close();


                System.out.print(" Counting occurencies process terminated ");


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
        }

        //}

    }
