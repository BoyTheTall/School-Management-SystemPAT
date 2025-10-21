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
public class Teacher {
private int OldTID;//to be used to house the old teacher ID for the updating process
private int TID;//Handled by Constructor
private String TName;//Handled by Constructor
private String TSurname;//Handled by Constructor
private String TSubject;
private String TClassTaught;
private String TPhoneNumber;//Handled by Constructor
private ResultSet TRess;//to be used to return all SQL Query Result Sets
private Connection TConn;//Connection to the database to facilitate the execution of all queries
private Statement TStatement;//Statement object to create the neccessary result set type and prvides the SQL statement path
private String TSubjectCode;
private boolean isClassTeacher;//For Storing whether the teacher is a class teacher
private String Class_Where_Is_Class_Teacher;//for storing the class where he or she is a class teacher that's if he or she is one
            //Remember to add the get and set methods of Class_Where_Is_Class_Teacher and isClassTeacher fields 
    //Remember to add the parametised class constructor(Done)

    /**
     *instatiates the teacher object using the teacher ID as a parameter
     * @param TeacherID
     */
    public Teacher(String TeacherID){
        try {
            getTConn();
            TID = Integer.parseInt(TeacherID);
            OldTID = TID;
            //Fetching of the personal information of the teacher
            String SQL = "SELECT * FROM tblTeachers WHERE TeacherID = " + TID;
            
            TRess = TStatement.executeQuery(SQL);
            TRess.next();
            TName = TRess.getString("Name");
            TSurname = TRess.getString("Surname");
            TPhoneNumber = TRess.getString("PhoneNumber");
            TSubjectCode = TRess.getString("SubjectCode");
            isClassTeacher = TRess.getBoolean("ClassTeacher");
            //Checks if the teacher is a class teacher
            if(isClassTeacher == true){
                //If the teacher is a class teacher then the program fetches the class where the teacher is the class teacher
            SQL = "SELECT ClassName FROM tblClasses WHERE ClassTeacherCode = " + TID;
            TRess = TStatement.executeQuery(SQL);
            
            Class_Where_Is_Class_Teacher = TRess.getString("ClassName");
            }
            else{//if not the then the Class_Where_Is_Class_Teacher field is set to null
            Class_Where_Is_Class_Teacher = null;
            }
            //Fetching of the subject being taught by the teacher
            SQL = "SELECT SubjectName From tblSubjects WHERE SubjectCode = " + '"' + TSubjectCode + '"' ;
            TRess = TStatement.executeQuery(SQL);
            TRess.next();
            TSubject = TRess.getString("SubjectName");
             //Returning to the teachers table as most of the class operations lie there
            SQL = "SELECT * FROM tblTeachers WHERE TeacherID = " + TID;
            TRess = TStatement.executeQuery(SQL);   
            TRess.next();
        } catch (SQLException ex) {
            Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    /**
     *instatiates the teacher class with the default values
     */
    public Teacher(){
    TID = 0;
    TName = "";
    TSurname = "";
    TSubject = "";
    TPhoneNumber = "";
    TSubjectCode = "";
    isClassTeacher = false;
    Class_Where_Is_Class_Teacher = "";
    }
    
    /**
     *fetches the string representation of the teacher's Identity number
     * @return String Identity Number
     */
    public String getTeacherID(){
    return Integer.toString(TID);
    }

    /**
     *fetches the teacher's name
     * @return teacher name
     */
    public String getName(){
    return TName;
    }
    
    /**
     *fetches the teacher's surname
     * @return teacher surname
     */
    public String getSurname(){
    return TSurname;
    }
    
    /**
     *fetches the subject that is taught by the teacher
     * @return String subject
     */
    public String getTSubject(){
    return TSubject;
    }
    
    /**
     *fetches the class name where the teacher is teaching
     * @return teacher class taught
     */
    public String TClassTaught(){
    return TClassTaught;
    }
    
    /**
     *
     * @return teacher phone number
     */
    public String TPhoneNumber(){
    return TPhoneNumber;
    }
    
    /**
     *changes the teacher's name to the value present in the parameter
     * @param NTName
     */
    public void setName(String NTName){
    TName = NTName;
    }

    /**
     *changes the teacher's Identity Number
     * @param NTID
     */
    public void setTID(String NTID){
    TID = Integer.parseInt(NTID);
    }
    
    /**
     *changes the teacher's surname to the value present in the parameter
     * @param NTSurname
     */
    public void setTSurname(String NTSurname){
    TSurname = NTSurname;
    }
    
    /**
     *changes the teacher's class taught to the value present in the parameter
     * @param NTClassTaught
     */
    public void setTClassTaught(String NTClassTaught){
    TClassTaught = NTClassTaught;
    }
    
    /**
     *changes the teacher's phone number to the value present in the parameter
     * @param NTPhoneNumber
     */
    public void setTPhoneNumber(String NTPhoneNumber){
    TPhoneNumber = NTPhoneNumber;
    }
    
    private void getTConn(){
        try {
            //Code to connect to the teacher realated tables only
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);
            TConn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
            TStatement = TConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *updates the teacher's personal information accepting the subject as a parameter
     * @param Subject
     */
    public void updateTeacherInfo(String Subject){
        try {
            //Updating the the teacher's login account ID
            String SQL;
            SQL = "SELECT * FROM tblTPasswords WHERE TeacherID = " + OldTID;
            TRess = TStatement.executeQuery(SQL);
            
            TRess.next();
            TRess.updateInt("TeacherID", TID);
            TRess.updateRow();
            TRess.close();
            TStatement.close();
            
            
            //Getting the new subject code
            TStatement = TConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQL = "SELECT * FROM tblSubjects WHERE SubjectName = " +'"' + Subject + '"';
            TRess = TStatement.executeQuery(SQL);
            TRess.next();
            TSubjectCode = TRess.getString("SubjectCode");
            TStatement.close();
            TRess.close();
            
            
            //Updating the teacher information table
            TStatement = TConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQL = "SELECT * FROM tblTeachers WHERE TeacherID = " + OldTID;
            
            TRess = TStatement.executeQuery(SQL);
            TRess.updateInt("TeacherID", TID);
            TRess.updateString("Name", TName);
            TRess.updateString("Surname", TSurname);
            TRess.updateString("PhoneNumber", TPhoneNumber);
            TRess.updateString("SubjectCode", TSubjectCode);
            TRess.updateRow();
            TRess.close();
            TStatement.close();
            TConn.close();
           
            getTConn();//Getting the connection again
            
            //Updating the class teacher field
            if(isClassTeacher == true){
            SQL = "SELECT * FROM tblClasses WHERE ClassTeacherCode = " + OldTID;
            TRess = TStatement.executeQuery(SQL);
            
            TRess.next();
            TRess.updateInt("ClassTeacherCode", TID);
            TRess.updateRow();
            TRess.close();
            TStatement.close();
            //Remember to open the TStatement again
            }
                        
            
            //Add the updating process of the subject Registration tables and the tables where the class teachers are stored
            String updateSubjectRegistrations = "UPDATE tblSubjectRegistrations "
                    + "SET TeacherCode = ? "+ "WHERE TeacherCode = ?";//Update query to update the subject registration table teacher code
            PreparedStatement updatePS = TConn.prepareStatement(updateSubjectRegistrations);
            updatePS.setInt(1, TID);
            updatePS.setInt(2, OldTID);
            updatePS.executeUpdate();//Updates the tblRegistrations table
            updatePS.close();
            
            updateSubjectRegistrations = "UPDATE tblSubjectTeachersAndClasses "
            + "SET TeacherCode = ? "+ "WHERE TeacherCode = ?";//Update query to update the subject teachers table teacher code
            updatePS = TConn.prepareStatement(updateSubjectRegistrations);
            updatePS.setInt(1, TID);
            updatePS.setInt(2, OldTID);
            updatePS.executeUpdate();//Updates the tblRegistrations table
            updatePS.close();
            
            //Use the update SQL Statement to execute the above function^(Done)(needs test)
            JOptionPane.showMessageDialog(null, "Update Successful");
            
            //Updating the session text file to the new user ID
             try {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("PassOverParameterFile.txt")))) {
           
            out.print(TID + "\t");
            out.print("Teacher" + "\n");
        }
    } catch (IOException ex) {
        Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
    }
             JOptionPane.showMessageDialog(null, "Information Updated Successfully");
            
            //TName = TRess.getString("Name");
            //TSurname = TRess.getString("Surname");
            //TPhoneNumber = TRess.getString("PhoneNumber");
            /* 
           //Add the SQL Statement where The Class Taught can be updated
            
            TRess.updateString("Name", TName);
            TRess.updateString("Name", TName);*/
        } catch (SQLException ex) {
            Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }
    
    /**
     *creates the new login account for the teacher
     * @param Username
     * @param Password
     */
    public void createLoginAccount(String Username, String Password){
        try {
            String SQL = "SELECT * FROM tblTPasswords";
            TRess = TStatement.executeQuery(SQL);
            TRess.moveToInsertRow();
            
            TRess.updateInt("TeacherID", TID);
            TRess.updateString("Username", Username);
            TRess.updateString("Password", Password);
            
            TRess.insertRow();
            TRess.close();
            TStatement.close();
            TConn.close();
            
            JOptionPane.showMessageDialog(null, "Login Account Created Successfully"
            + "\n" + "Usertype: Teacher (To be selected whenever user is logging in)" + "\n"
            + "Username: " + Username + "\n"
            + "Password: " + Password);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }

    /**
     *updates the subject code of the subject the teacher teaches to the value in the parameter
     * @param SubjectCode
     */
    public void setTSubjectCode(String SubjectCode){
this.TSubjectCode = SubjectCode;
}

    /**
     *add the information of the new teacher to the database
     * @throws SQLException
     */
    public void addTeacher() throws SQLException{
getTConn();

String SQL = "SELECT * FROM tblTeachers";
this.TRess = this.TStatement.executeQuery(SQL);

TRess.moveToInsertRow();
TRess.updateInt("TeacherID", TID);
TRess.updateString("Name", this.TName);
TRess.updateString("Surname", this.TSurname);
TRess.updateString("PhoneNumber", this.TPhoneNumber);
TRess.updateString("SubjectCode", this.TSubjectCode);
TRess.updateBoolean("ClassTeacher", isClassTeacher);
TRess.insertRow();

TRess.close();
TConn.close();
this.TStatement.close();
}

    /**
     *update the status of the class where the teacher is a class teacher
     * @param IsClassTeacher
     */
    public void updateIsClassTeacher(boolean IsClassTeacher){
    try {
        String SQL = "SELECT * FROM tblTeachers WHERE TeacherID = " + TID;
        
        TRess = TStatement.executeQuery(SQL);
        TRess.next();
        
        TRess.updateBoolean("ClassTeacher", IsClassTeacher);
        TRess.updateRow();
        TRess.close();
        TStatement.close();
        TConn.close();
    } catch (SQLException ex) {
        Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
    }
           
}
}
