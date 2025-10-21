/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
public class Subject {
  private String SubjectName;
    private String SubjectCode;
    private String oldSubjectCode;
    private ResultSet RSubject;
    private Connection SSConn;
    Statement SubjectStatement;
    //Remember to add parametised constructor
    
    /**
     *initializes the subject object accepting the subject name of the as a parameter
     * @param Subject
     */
    public Subject(String Subject){
      try {
          getConnection();
          String SQL = "SELECT * FROM tblSubjects WHERE SubjectName = "+ '"' + Subject + '"';
          RSubject = SubjectStatement.executeQuery(SQL);
          RSubject.next();
          SubjectCode = RSubject.getString("SubjectCode");
          SubjectName = RSubject.getString("SubjectName");
          oldSubjectCode = SubjectCode;
          SSConn.close();
          RSubject.close();
          SubjectStatement.close();
      } catch (SQLException ex) {
          Logger.getLogger(Subject.class.getName()).log(Level.SEVERE, null, ex);
      }
   
   }

    /**
     *
     * @return subject name
     */
    public String getSubjectName(){
    return SubjectName;
    }
    
    /**
     *
     * @return subject code
     */
    public String getSubjectCode(){
    return SubjectCode;
    }
    
    /**
     *changes the subject name of the object accepting the new name as a parameter
     * @param NSubjectName
     */
    public void setSubjectName(String NSubjectName){
    SubjectName = NSubjectName;
    }
    
    /**
     *changes the subject code of the object accepting the new subject name as a parameter
     * @param NSubjectCode
     */
    public void setSubjectCode(String NSubjectCode){
    SubjectCode = NSubjectCode;
    }
   
private ResultSet getRSubject(){
      try {
          String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
          Class.forName(driver);
          SSConn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
          SubjectStatement = SSConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          String SQL = "SELECT * FROM tblSubjects";
          RSubject = SubjectStatement.executeQuery(SQL);
      } catch (SQLException | ClassNotFoundException ex) {
          Logger.getLogger(Subject.class.getName()).log(Level.SEVERE, null, ex);
      }
return RSubject;
}

    /**
     *Add the new subject to the database accepting the subject name and code 
     * @param SubjectCode
     * @param SubjectName
     */
    public static void addNewSubject(String SubjectCode, String SubjectName){

      try {
          String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
          Class.forName(driver);
          Connection Conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
          Statement SubjectStatement = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          String SQL = "SELECT * FROM tblSubjects";
          ResultSet insertResultSet = SubjectStatement.executeQuery(SQL);
         
          insertResultSet.moveToInsertRow();
          insertResultSet.updateString("SubjectCode", SubjectCode);
          insertResultSet.updateString("SubjectName", SubjectName);
          insertResultSet.insertRow();
          
          insertResultSet.close();
          Conn.close();
          SubjectStatement.close();
          JOptionPane.showMessageDialog(null, "Subject Added successfully." + "\n" + "Hint: Remeber to add a subject Teacher");
      } catch (ClassNotFoundException | SQLException ex) {
          Logger.getLogger(Subject.class.getName()).log(Level.SEVERE, null, ex);
      }


}
private void getConnection(){
      try {
          String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
          Class.forName(driver);
          SSConn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
          SubjectStatement = SSConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      } catch (SQLException | ClassNotFoundException ex) {
          Logger.getLogger(Subject.class.getName()).log(Level.SEVERE, null, ex);
      }

}

    /**
     *returns String representation of the Subject Class
     * @return String representation of subject class
     * 
     */
    @Override
public String toString(){
String toString = SubjectCode + "\t" + SubjectName;
return toString;
}
//For times where instatiating the class would be a waste of resources
public static String getSubjectCode(String SubjectName){
    String SubjectCode = null;  
    try {
          Connection conn;
          Statement stmt;
          ResultSet rs;
          String SQL = "SELECT * FROM tblSubjects WHERE SubjectName =" +'"' +SubjectName + '"';
          
          
          String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
          Class.forName(driver);
          conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
          stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          rs = stmt.executeQuery(SQL);
          
          rs.next();
          
          SubjectCode = rs.getString("SubjectCode");
          
          rs.close();
          stmt.close();
          conn.close();
      } catch (ClassNotFoundException | SQLException ex) {
          Logger.getLogger(Subject.class.getName()).log(Level.SEVERE, null, ex);
      }
      return SubjectCode;
}

    /**
     *updates the subject information to the values present in the object's state
     use set methods first
     */
    @SuppressWarnings("ConvertToTryWithResources")
  public void updateSubjectInfo(){
      try {
          getConnection();
          String SQL = "SELECT * FROM tblSubjects WHERE = " + '"' + oldSubjectCode + '"';//Selecting the current record. 
          
          ResultSet rs = SubjectStatement.executeQuery(SQL);
          
          rs.next();//scrooling to the record in the database
          
          rs.updateString("SubjectCode", SubjectCode);//updating the subject code
          rs.updateString("SubjectName", SubjectName);//updating the subject name
          
          rs.updateRow();//updating the row
          //commiting the changes to the database
          rs.close();
          SubjectStatement.close();
          //for updating the multiple records relating to the particular subject
          SQL = "UPDATE tblSubjectRegistrations "
                  + "SET SubjectCode = \"?\""
                   +" WHERE SubjectCode = \"?\"";
          PreparedStatement psmt = SSConn.prepareStatement(SQL);
          psmt.setString(1, SubjectCode);
          psmt.setString(2, oldSubjectCode);
          psmt.executeUpdate();
          psmt.close();
          
          SQL = "UPDATE tblTextbooks "
                  + "SET SubjectCode = \"?\""
                   +" WHERE SubjectCode =\" ?\"";
           psmt = SSConn.prepareStatement(SQL);
          psmt.setString(1, SubjectCode);
          psmt.setString(2, oldSubjectCode);
          psmt.executeUpdate();
          psmt.close();
          JOptionPane.showMessageDialog(null, "Update successfully");
      } catch (SQLException ex) {
          Logger.getLogger(Subject.class.getName()).log(Level.SEVERE, null, ex);
      }


}
}