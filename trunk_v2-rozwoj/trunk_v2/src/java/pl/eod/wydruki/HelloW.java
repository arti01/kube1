package pl.eod.wydruki;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * This class demonstrates the conversion of an FO file to PDF using FOP.
 */
public class HelloW {

    public static void main(String args[]) {
        System.out.println("Hi Testing");
        ArrayList employeeList = new ArrayList();
        String templateFilePath = "web/resources/wydruki/";

        Employee e1 = new Employee();
        e1.setName("Debasmita1 Sahoo");
        e1.setEmployeeId("10001");
        e1.setAddress("Pune");
        employeeList.add(e1);

        Employee e2 = new Employee();
        e2.setName("Debasmita2 Sahoo");
        e2.setEmployeeId("10002");
        e2.setAddress("Test");
        employeeList.add(e2);

        Employee e3 = new Employee();
        e3.setName("Debasmita3 Sahoo");
        e3.setEmployeeId("10003");
        e3.setAddress("Mumbai");
        employeeList.add(e3);

        EmployeeData data = new EmployeeData();
        data.setEemployeeList(employeeList);
        PDFHandlerTest handler = new PDFHandlerTest();

        try {
            ByteArrayOutputStream streamSource = handler.getXMLSource(data);

            handler.createPDFFile(streamSource, templateFilePath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}