/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import GUIs.LogInUI;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author King The Shepherd
 */
public class UtilityClass {
    
    /**
     *fetches the teacher id of the teacher that teaches a particular class in that particular grade
     * @param Grade
     * @param SubjectCode
     * @return teacher id
     */
    public static int getSubjectTeacherCode(String Grade, String SubjectCode){
     int teacherCode = 0;
     try {
         Connection conn;
         Statement stmt;
         ResultSet rs;
         
         
         //Stage 1

String SQL = "SELECT tblGradeCodes.GradeCode, tblClasses.ClassCode FROM tblGradeCodes, tblClasses "
             + "WHERE tblGradeCodes.GradeCode = tblClasses.GradeCode "
                 + "AND tblClasses.ClassName = " + '"' + Grade + '"';
//Establishing connection         
String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
Class.forName(driver);
conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
rs = stmt.executeQuery(SQL);
rs.next();
int GradeCode = rs.getInt("GradeCode");
       
//stage 2
SQL = "SELECT tblTeachers.TeacherID, tblTeachers.Name, tblTeachers.Surname"
        + " FROM tblTeachers, tblSubjectTeachersAndClasses "
        + "WHERE tblSubjectTeachersAndClasses.TeacherCode = tblteachers.TeacherID"
        + " AND tblSubjectTeachersAndClasses.GradeCode = " + GradeCode +" AND tblTeachers.SubjectCode = " +'"' + SubjectCode + '"';
rs = stmt.executeQuery(SQL);
rs.next();
teacherCode = rs.getInt("TeacherID");
         
conn.close();
stmt.close();
rs.close();
     } catch (ClassNotFoundException | SQLException ex) {
         Logger.getLogger(UtilityClass.class.getName()).log(Level.SEVERE, null, ex);
     }
     return teacherCode;
 }   

    /**
     *fetches all the students that have not returned a book(s) in the entire school
     * @return String containing all the names and class rooms of those that have not returned their books
     */
    public static String getAllOwingStudents(){
    String Names = null;
    try {
         
         Connection conn;
         Statement stmt;
         ResultSet rs;
         
         String SQL = "SELECT DISTINCT tblBookLearnerRecords.StudentID, tblStudents.Name, tblStudents.Surname, tblClasses.ClassName\n" +
"FROM tblBookLearnerRecords, tblStudents, tblClasses, tblGradeCodes, tblClassRegistrations\n" +
"WHERE tblBookLearnerRecords.StudentID = tblStudents.StudentID\n" +
"AND tblBookLearnerRecords.Returned = false\n" +
"AND tblClasses.ClassCode = tblClassRegistrations.ClassCode\n" +
"AND tblBookLearnerRecords.StudentID = tblClassRegistrations.StudentID;";
         
         String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
         Class.forName(driver);
         conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
         stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rs = stmt.executeQuery(SQL);
         rs.next();
         
         Names = rs.getInt("tblBookLearnerRecords.StudentID") + "\t" + rs.getString("tblStudents.Name") + "\t" + rs.getString("tblStudents.Surname") + "\t" +rs.getString("tblClasses.ClassName") + "\n"; 
    
        while(rs.next()){
         Names = Names + rs.getInt("tblBookLearnerRecords.StudentID") + "\t" + rs.getString("tblStudents.Name") + "\t" + rs.getString("tblStudents.Surname") + "\t" +rs.getString("tblClasses.ClassName") + "\n";
        
        }
    } catch (ClassNotFoundException | SQLException ex) {
         Logger.getLogger(UtilityClass.class.getName()).log(Level.SEVERE, null, ex);
     }
    return Names;
}

    /**
     *fetches the grade code of a particular class room
     * @param GradeName
     * @return integer grade code
     */
    public static int getGradeCode(String GradeName){
    int GradeCode = 0; 
    try {
         Connection conn;
         Statement stmt;
         ResultSet rs;
         String SQL = "SELECT GradeCode FROM tblClasses WHERE ClassName = " + '"' + GradeName +'"';
         String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
         Class.forName(driver);
         conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
         stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rs = stmt.executeQuery(SQL);
         rs.next();
         
         GradeCode = rs.getInt("GradeCode");
          
          conn.close();
          stmt.close();
          rs.close();
     } catch (ClassNotFoundException | SQLException ex) {
         Logger.getLogger(UtilityClass.class.getName()).log(Level.SEVERE, null, ex);
     }
     return GradeCode;
}

    /**
     *fetches the class code(ID) of a particular class room
     * @param GradeName
     * @return  integer class code
     */
    public static int getClassCode(String GradeName){
     int ClassCode = 0;
    try {
        
         Connection conn;
         Statement stmt;
         ResultSet rs;
         String SQL = "SELECT ClassCode FROM tblClasses WHERE ClassName = " + '"' + GradeName +'"';
         String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
         Class.forName(driver);
         conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
         stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rs = stmt.executeQuery(SQL);
         rs.next();
         
         ClassCode = rs.getInt("ClassCode");
         
         conn.close();
         stmt.close();
         rs.close();
        
     } catch (SQLException | ClassNotFoundException ex) {
         Logger.getLogger(UtilityClass.class.getName()).log(Level.SEVERE, null, ex);
     }
      return ClassCode;
}

    /**
     *fetches all the students registered in a particular subject and grade
     * @param GradeCode
     * @param SubjectCode
     * @return result set students
     */
    public static ResultSet getStudents(int GradeCode, String SubjectCode){
    ResultSet rs = null;
    try {
         Connection conn;
         Statement stmt;
         
         String SQL = "SELECT DISTINCT(tblStudents.StudentID)\n" +
"FROM tblStudents, tblClassRegistrations, tblGradeCodes, tblClasses, tblSubjectRegistrations\n" +
"WHERE tblStudents.StudentID = tblClassRegistrations.StudentID\n" +
"AND tblClassRegistrations.ClassCode = tblClasses.ClassCode\n" +
"AND tblClasses.GradeCode = tblClasses.GradeCode\n" +
"AND tblClasses.GradeCode = " + GradeCode +" AND tblSubjectRegistrations.SubjectCode = \""+ SubjectCode+"\""+""
                 + " AND tblSubjectRegistrations.Year = \""+ Classes.Student.getYear()+"\";";
         String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
         Class.forName(driver);
         conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
         stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rs = stmt.executeQuery(SQL);
        
         
        
         
         conn.close();
         stmt.close();
         
     } catch (ClassNotFoundException | SQLException ex) {
         Logger.getLogger(UtilityClass.class.getName()).log(Level.SEVERE, null, ex);
     }

return rs;
}

    /**
     *fetches the students registered for a particular subject and in a particular class
     * @param SubjectCode
     * @param ClassCode
     * @return Result set Students
     */
    public static ResultSet getStudents( String SubjectCode, int ClassCode){
    ResultSet rs = null;
    try {
         Connection conn;
         Statement stmt;
         String year = Student.getYear();
         
         String SQL = "SELECT DISTINCT(tblStudents.StudentID)\n" +
"FROM tblStudents, tblClassRegistrations, tblClasses, tblSubjectRegistrations\n" +
"WHERE tblStudents.StudentID = tblClassRegistrations.StudentID\n" +
"AND tblClassRegistrations.ClassCode = tblClasses.ClassCode\n" +
"AND tblClasses.ClassCode = " + ClassCode +" AND tblSubjectRegistrations.SubjectCode = \""+ SubjectCode+"\" "
                 + "AND tblClassRegistrations.Year = \""+ year + "\" ";
         String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
         Class.forName(driver);
         conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
         stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         
         
         rs = stmt.executeQuery(SQL);
        
         
        
         
         conn.close();
         stmt.close();
         
     } catch (ClassNotFoundException | SQLException ex) {
         Logger.getLogger(UtilityClass.class.getName()).log(Level.SEVERE, null, ex);
     }

return rs;
}

    /**
     *adds the mark attained in a particular subject and test
     * @param StudentID
     * @param SubjectName
     * @param Score
     * @param date
     * @param TestNumber
     */
    public static void addMarks(int StudentID,String SubjectName, int Score, Date date, int TestNumber){
     try {
         String SQL = "SELECT * FROM tblMarks";
         Connection conn;
         Statement stmt;
         ResultSet rs;
         String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
         Class.forName(driver);
         conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
         stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         rs = stmt.executeQuery(SQL);
         
         rs.moveToInsertRow();
         rs.updateInt("StudentID", StudentID);
         rs.updateString("SubjectName", SubjectName);
         rs.updateInt("Score", Score);
         rs.updateDate("DateWritten", date);
         rs.updateInt("TestNumber", TestNumber);
         rs.insertRow();
         
         rs.close();
         stmt.close();
         conn.close();
     } catch (SQLException | ClassNotFoundException ex) {
         Logger.getLogger(UtilityClass.class.getName()).log(Level.SEVERE, null, ex);
     }
         
}

    /**
     *writes the new user id of the new person being added to the school system
     * @param ID
     * @param Usertype
     */
    public static void writeNewUser(int ID, String Usertype){
 try {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("NewUserProfilePassOverFile.txt")))) {
           
            out.print(ID + "\t");
            out.print(Usertype + "\n");
        }
    } catch (IOException ex) {
        Logger.getLogger(LogInUI.class.getName()).log(Level.SEVERE, null, ex);
    }

}
}
