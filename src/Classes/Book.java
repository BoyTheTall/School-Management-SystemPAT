/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author King The Shepherd
 */
public class Book{
    int BookCode;
    String BookName;
    String SubjectCode;
    String SubjectName;
    int Grade;
    int Quantity;
    
    /**
     *Creates a new textbook Object (with the book code(ISBN Number), the book's title and the subject information related to it along with its present quantity and grade)
     * @param grade
     * @param subjectCode
     */
    public Book(int grade, String subjectCode){
        try {
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);
            Statement stmt;
            ResultSet rs;
            try (Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb")) {
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String SQL = "SELECT * FROM tblTextBooks WHERE SubjectCode = " +'"'+ subjectCode + '"'+" AND Grade = " + grade + ";";
                rs = stmt.executeQuery(SQL);
                rs.next();
                BookCode = rs.getInt("BookCode");
                BookName = rs.getString("BookName");
                SubjectCode = rs.getString("SubjectCode");
                Quantity = rs.getInt("Quantity");
                SQL = "SELECT * FROM tblSubjects WHERE SubjectCode = " + '"' + SubjectCode + '"' + ";";
                rs = stmt.executeQuery(SQL);
                rs.next();
                SubjectName = rs.getString("SubjectName");
            }
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *
     * @return book code(integer)
     */
    public int getBookCode(){ 
    
        return BookCode;
    }
    
    /**
     *
     * @return the book title(book name)
     */
    public String getBookName(){
    
        return BookName;
    }
    
    /**
     *
     * @return subject code associated with the book
     */
    public String getSubjectCode(){
    
        return SubjectCode;
    }
    
    /**
     *
     * @return the subject name associated with the book
     */
    public String getSubjectName(){
    
        return SubjectName;
    
    }
    
    /**
     *returns the current available book quantity
     * @return book Quantity
     */
    public int getQuantity(){
    
        return Quantity;
    }
    
    /**
     *changes the book code(ISBN number) to the new book code
     * @param newBookCode
     */
    public void setBookCode(int newBookCode){
    
        BookCode = newBookCode;
    }
    
    /**
     *changes the book name to the new name specified in the parameter
     * @param newBookName
     */
    public void setBookName(String newBookName){
    
        BookName = newBookName;
    }
    
    /**
     *changes the subject code associated with the book
     * @param newSubjectCode
     */
    public void setSubjectCode(String newSubjectCode){
        
        SubjectCode = newSubjectCode;
    }
    
    /**
     *changes the subject name associated with the book
     * @param newSubjectName
     */
    public void setSubjectName(String newSubjectName){
    SubjectName = newSubjectName;
    
    }
    
    /**
     *sets the grade(numerical value) associated with the book
     * @param newGrade
     */
    public void setGrade(int newGrade){
    Grade = newGrade;
    }

    /**
     *changes the book quantity to the new specified value or amount
     * @param newQuantity
     */
    public void setQuantity(int newQuantity){
    
        Quantity = newQuantity;
    }
    
    /**
     *
     * @param newBookName
     * @param Subject
     * @param Grade
     * @param Quantity
     */
    public static void AddNewBook(String newBookName, String Subject, int Grade, int Quantity){
        try {
            String newsubjectCode;
            String SQL;  
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);
            Statement stmt;
            ResultSet rs;
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
          
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQL = "SELECT SubjectCode FROM tblSubjects WHERE SubjectName = " + '"' + Subject + '"' + ';';
            rs = stmt.executeQuery(SQL);
            rs.next();
            newsubjectCode = rs.getString("SubjectCode");
            
            SQL = "SELECT * FROM tblTextBooks";
            rs = stmt.executeQuery(SQL);
            
            rs.moveToInsertRow();
            rs.updateString("SubjectCode", newsubjectCode);
            rs.updateString("BookName", newBookName);
            rs.updateInt("Grade", Grade);
            rs.updateInt("Quantity", Quantity);
            
            rs.insertRow();
            rs.close();
            stmt.close();
            
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQL = "SELECT BookCode FROM tblTextbooks WHERE SubjectCode = " 
                    + '"' + newsubjectCode + '"' + " AND BookName = " + '"'
                    + newBookName + '"' + " AND Grade = " + Grade + " AND Quantity = " + Quantity;
            rs = stmt.executeQuery(SQL);
            rs.next();
            int BookCode = rs.getInt("BookCode");
            rs.close();
            stmt.close();
            
            //Add a loop for the code(done)
            int i = 1;//Counter variable
            
            while(i<= Quantity){
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
            SQL = "SELECT * FROM tblTextbookStock;";
            rs = stmt.executeQuery(SQL);
            
            rs.moveToInsertRow();
            rs.updateInt("BookCode", BookCode);
            rs.insertRow();
            rs.close();
            stmt.close();
            i++;
            }
           
            JOptionPane.showMessageDialog(null, "Book Added to System Successfully");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/**
 for automatically assigning books to students
     * @param Grade
     * @param Subject
     * @param forEntireGrade
     * @return the students with books
 */
    public String assignBooks(String Grade, String Subject, boolean forEntireGrade){
   //Buggy and doesn't work
        //Add the rest of the condition checks. for how many will not have text books
        int [] studentCodes = new int[getNoOfStudents(Grade, forEntireGrade)];
    int [] bookIDs;
    String Year = Classes.Student.getYear();
    String StudentWithBooks = null;
    if(forEntireGrade == false){
        try {
            
            String SQL = "SELECT * FROM tblClassRegistrations, tblClasses WHERE tblClassRegistrations.ClassCode = tblClasses.ClassCode "
                    + "AND tblClasses.ClassName = \""+ Grade +"\" AND Year = \"" + Year + "\";";//SQL codd for fetching the student which are in the specified Class(not grade)
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);
            Statement stmt;
            ResultSet rs;
            
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");//getting the connection to the database
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);//creating the statement object
            
            rs = stmt.executeQuery(SQL);//fething resultset
            
            rs.next();//scrolling to first record

            int GradeCode = rs.getInt("GradeCode");//Fetching grade code associated with with the particular class
            
            int i = 0;//Counter variable
            studentCodes[i] = rs.getInt("tblClassRegistrations.StudentID");
            
            while(rs.next()){
            
               i++;
               studentCodes[i] = rs.getInt("tblClassRegistrations.StudentID");//Assigning the student code s into the array
            
            }
            
        String subjectCode = Classes.Subject.getSubjectCode(SubjectName);//Fetching the subject code of the subject interested in
        bookIDs = new int[getNoOfBooks(subjectCode, GradeCode)];//created the array of books and setting the size using the method which determines how many books area available
        SQL = "SELECT BookCode FROM tblTextBooks WHERE Grade = " + GradeCode + " AND SubjectCode = \"" + subjectCode + "\"";//SQL code for fetching the book code of the textbook associated with the grade
            
        rs = stmt.executeQuery(SQL);//fetching result set of book code
            
        rs.next();//scrooling to first record in result set
            
        int bookCode = rs.getInt("BookCode");//fetching the desired book code
            
        SQL = "SELECT * FROM tblTextBookStock WHERE BookCode = " + bookCode;//SQ code for fetching the book numbers of the desit=red textbooks 
        rs = stmt.executeQuery(SQL);
        rs.next();
        
        int y = 0;//counter variable
        bookIDs[y] = rs.getInt("BookID");
        while(rs.next() && y<(bookIDs.length-1)){
          //Adding the book numbers to the array of book numbers
            y++;
            bookIDs[y] = rs.getInt("BookID");
        }
       int limit = 0;//variable to keep method from exceeding the slots avaiable in array which will have a lower amount of records(this will prevent the array out of bounds exception)
       //If statements checking which array is larger and setting the limit variable to the length of the lower or smaller array
        if(studentCodes.length > bookIDs.length){
           limit = bookIDs.length;
           int noOfStudentsWithoutBooks = studentCodes.length - limit;
           JOptionPane.showMessageDialog(null,noOfStudentsWithoutBooks +" don't have textbooks");
       }
        if(studentCodes.length < bookIDs.length){
        
            limit = studentCodes.length;
        }
        
        int [] assignedBooks = new int[limit];//array that will store the books that have been given to students
        
        //loop for assigning the books
        for(int z = 0; z < limit; z++){
        
        SQL = "SELECT * FROM tblBookLearnerRecords";
        rs = stmt.executeQuery(SQL);
        
        rs.moveToInsertRow();//moving the cursor to the new row(record) position 
        
        rs.updateInt("BookID", bookIDs[z]);//adding the book number to row
        rs.updateInt("StudentID", studentCodes[z]);//adding the student code of the student who, will have the book with the particular book number
        rs.updateBoolean("Returned", false);//setting the returned field to false
        rs.updateString("Year", Year);//setting the year to the current year
        
        rs.insertRow();//insertion of the new row
        //closing the result set and statement objects to commit changes to the database
        rs.close();
        stmt.close();
        //creating a new statement object
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        assignedBooks[z] = bookIDs[z];//adding the book number of the recently assigned book into the array
        updateBookStatus(bookIDs[z], stmt, conn);//updating the status of the particular textbook to unavailable
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);//initialising statement object to be used in the assigning loop
        }
        
        JOptionPane.showMessageDialog(null, "Books Assigned Successfully");//Displaying the message that the assigning process was successful
        if(studentCodes.length > bookIDs.length){
            
      StudentWithBooks = getStudentBookNumbers(bookIDs, conn, Year);
        
        }
        if(studentCodes.length < bookIDs.length){
        StudentWithBooks = getStudentBookNumbers(assignedBooks, conn, Year);
        }
        
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }
    if(forEntireGrade == true){//same as above. difference is that the students are for the entire grade. only difference is in the SQL statement that fecthes the students
            try {
                String SQL = "SELECT * FROM tblClassRegistrations, tblClasses WHERE tblClassRegistrations.ClassCode = tblClasses.ClassCode "
                        + "AND tblClasses.GradeCode = "+ Grade +" AND Year = \"" + Year + "\";";
                String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
                Class.forName(driver);
                Statement stmt;
                ResultSet rs;
                
                Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                
                rs = stmt.executeQuery(SQL);
                
                rs.next();
                
                int GradeCode = rs.getInt("GradeCode");
                
                int i = 0;
                studentCodes[i] = rs.getInt("tblClassRegistrations.StudentID");
                
                while(rs.next()){
                    
                    i++;
                    studentCodes[i] = rs.getInt("tblClassRegistrations.StudentID");
                    
                }
                
                String subjectCode = Classes.Subject.getSubjectCode(SubjectName);
                bookIDs = new int[getNoOfBooks(subjectCode, GradeCode)];
                SQL = "SELECT BookCode FROM tblTextBooks WHERE Grade = " + GradeCode + " AND SubjectCode = \"" + subjectCode + "\"";
                
                rs = stmt.executeQuery(SQL);
                
                rs.next();
                
                int bookCode = rs.getInt("BookCode");
                
                SQL = "SELECT * FROM tblTextBookStock WHERE BookCode = " + bookCode;
                rs = stmt.executeQuery(SQL);
                rs.next();
                
                int y = 0;
                bookIDs[y] = rs.getInt("BookID");
                while(rs.next() && y<(bookIDs.length-1)){
                    y++;
                    bookIDs[y] = rs.getInt("BookID");
                }//Add the branch to check which is lower
                int limit = 0;
                
                if(studentCodes.length > bookIDs.length){
                    limit = bookIDs.length;
                    int noOfStudentsWithoutBooks = studentCodes.length - limit;
                    JOptionPane.showMessageDialog(null,+ noOfStudentsWithoutBooks +" don't have textbooks");
                }
                if(studentCodes.length < bookIDs.length){
                    
                    limit = studentCodes.length;
                }
                
                int [] assignedBooks;
                assignedBooks = new int[limit];
                
                for(int z = 0; z < limit; z++){
                    
                    SQL = "SELECT * FROM tblBookLearnerRecords";
                    rs = stmt.executeQuery(SQL);
                    
                    rs.moveToInsertRow();
                    
                    rs.updateInt("BookID", bookIDs[z]);
                    rs.updateInt("StudentID", studentCodes[z]);
                    rs.updateBoolean("Returned", false);
                    rs.updateString("Year", Year);
                    
                    rs.insertRow();
                    rs.close();
                    stmt.close();
                    stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    assignedBooks[z] = bookIDs[z];
                    updateBookStatus(bookIDs[z], stmt, conn);
                    stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                }   
            JOptionPane.showMessageDialog(null, "Books Assigned Successfully");
        if(studentCodes.length > bookIDs.length){
            
      StudentWithBooks = getStudentBookNumbers(bookIDs, conn, Year);
        
        }
        if(studentCodes.length < bookIDs.length){
        StudentWithBooks = getStudentBookNumbers(assignedBooks, conn, Year);
        }
        
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    
    }
   
      return StudentWithBooks;
    }
    
    /**
     *for getting the number of students based on the given parameters
     * @param Grade
     * @param forEntireGrade
     * @return number of students in the entire grade or a particular class
     */
    public static int getNoOfStudents(String Grade, boolean forEntireGrade){
       int noOfStudents = 0;
       //if statements check if the entire grade or a particular class of studnets should be fetched
        if(forEntireGrade == false){ //for when only a particualr class should be fetched
            try {
            String SQL = "SELECT COUNT(tblClassRegistrations.StudentID) AS [NoOfStudents] FROM tblClassRegistrations, tblClasses WHERE tblClassRegistrations.ClassCode = tblClasses.ClassCode "
                    + "AND tblClasses.ClassName = \""+ Grade +"\"";
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);
            Statement stmt;
            ResultSet rs;
            
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            rs = stmt.executeQuery(SQL);
            rs.next();
            noOfStudents = rs.getInt("NoOfStudents");
            
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
        }
       }
        if(forEntireGrade == true){//for when the entire grae should be fetched
           try {
            
               String SQL = "SELECT COUNT(tblClassRegistrations.StudentID) AS [NoOfStudents] FROM tblClassRegistrations, tblClasses WHERE tblClassRegistrations.ClassCode = tblClasses.ClassCode "
                       + "AND tblClasses.GradeCode = "+ Grade ;
               String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
               Class.forName(driver);
               Statement stmt;
               ResultSet rs;
               
               Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
               stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
               
               rs = stmt.executeQuery(SQL);
               rs.next();
               noOfStudents = rs.getInt("NoOfStudents");
           } catch (SQLException | ClassNotFoundException ex) {
               Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
           }
            
        
        }
       return noOfStudents;
    }
    
    /**
     *getting the number of books available for the students
     * @param SubjectCode
     * @param Grade
     * @return number of books
     */
    public static int getNoOfBooks(String SubjectCode, int Grade){
        int noOfBooks = 0;//variable for the number of books
        try {
            String SQL = "SELECT COUNT(tblTextBookStock.BookID) AS [noOfBooks] FROM tblTextBooks, tblTextBookStock WHERE tblTextBooks.BookCode = tblTextBookStock.BookCode AND tblTextBooks.Grade = " + Grade + " AND tblTextBooks.SubjectCode = \"" + SubjectCode + "\" AND tblTextBookStock.Returned = true";
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);
            Statement stmt;
            ResultSet rs;
            
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            rs = stmt.executeQuery(SQL);
            rs.next();
            
            noOfBooks = rs.getInt("noOfBooks");
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
        }
      
           return noOfBooks;
    }

//For when Students Are Greater than the number of books
    private String getStudentBookNumbers(int [] BookIDs, Connection conn, String Year){
         String StudentsWithBooks = null;
        try {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String SQL;
            for(int i =0; i< BookIDs.length; i++){
            SQL = "SELECT * FROM tblBookLearnerRecords WHERE BookID = " + BookIDs[i] + " AND Year = \"" + Year + "\" AND Returned = false;"; 
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            
            String StudentID = Integer.toString(rs.getInt("StudentID"));
            
            Student student = new Student(StudentID); 
            
            StudentsWithBooks = StudentsWithBooks + student.getSCode() + "\t" + student.getName() + " " + student.getSurname() + "\t" + BookIDs[i] + "\n";
            
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
        }
        return StudentsWithBooks;
    
    }
    //for updating the status of a single textbook to unavaiable
    private void updateBookStatus(int BookID, Statement stmt, Connection conn){
        try {
            String SQL = "SELECT * FROM tblTextBookStock WHERE BookID = " + BookID;
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(SQL);
            
            rs.next();
            rs.updateBoolean("Returned", false);
            rs.updateRow();
            rs.close();
            stmt.close();
           
        } catch (SQLException ex) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
