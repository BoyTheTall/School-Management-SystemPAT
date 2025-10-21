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
public class ClassRoom {
    String ClassName;
    int ClassCode;
    int GradeCode;
    Teacher ClassTeacher;
    Connection Conn;
    Statement stmt;
    ResultSet rs;

    /**
     *fetches the class room code of the specific class
     * @return integer value of the class room's class code
     */
    public int getClassCode(){
    return ClassCode;
    }
    
    /**
     *fetches the grade code of the class room
     * @return integer value of the class room's grade code
     */
    public int getGradeCode(){
        return GradeCode;}
    
    /**
     *fetches the class room name
     * @return class room name
     */
    public String getClassName(){
    return ClassName;
    }
    
    /**
     *returns a teacher object representation of the class teacher
     * @return teacher object which is the class teacher of the particular class
     */
    public Teacher getClassTeacher(){
    return ClassTeacher;
    }
    
    /**
     *changes the class room name to the one specified by the parameter
     * @param NewClassname
     */
    public void setClassName( String NewClassname){
    ClassName = NewClassname;
    }
    
    /**
     *changes the class teacher field. accepts a teacher object as a parameter
     * @param newTeacherID
     */
    public void setClassTeacher(String newTeacherID){
    if(ClassTeacher == null){
    ClassTeacher = new Teacher(newTeacherID);
    ClassTeacher.updateIsClassTeacher(true);
    }
    else{
    ClassTeacher.updateIsClassTeacher(false);
    ClassTeacher = new Teacher(newTeacherID);
    ClassTeacher.updateIsClassTeacher(true);
    
    }    
        
    }
    
    /**
     *changes the grade code of the class room
     * @param newGradeCode
     */
    public void setGradeCode(int newGradeCode){
    GradeCode = newGradeCode;
    }

    /**
     *method that updates the information of a class room to the one that is contained in the current state of the class room object
     * 
     */
    public void updateClassInfo(){
        try {
            getConnection();
            rs = stmt.executeQuery("SELECT * FROM tblClasses WHERE ClassCode = " + ClassCode);
            
            rs.next();
            
            rs.updateString("ClassName", ClassName);
            rs.updateInt("GradeCode", GradeCode);
            rs.updateInt("ClassTeacherCode", Integer.parseInt(ClassTeacher.getTeacherID()));
            
            rs.updateRow();
            close();
        } catch (SQLException ex) {
            Logger.getLogger(ClassRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *constructor that accepts the class name as a parameter and fetches the relevant data from the database
     * @param className
     */
    public ClassRoom(String className){
        try {
            getConnection();
            rs = stmt.executeQuery("SELECT * FROM tblClasses WHERE ClassName = \"" + className + "\"");
            rs.next();
            
          ClassCode =  rs.getInt("ClassCode");
          GradeCode = rs.getInt("GradeCode");
          ClassName = rs.getString("ClassName");
          if(rs.getInt("ClassTeacherCode") == 0){
          ClassTeacher = null;
          }
          else{
          ClassTeacher  = new Teacher(Integer.toString(rs.getInt("ClassTeacherCode")));
          }
          close();
        } catch (SQLException ex) {
            close();
            Logger.getLogger(ClassRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     Constructor that initializes to the first classroom in the database by default
     */
    public ClassRoom(){
        try {
            getConnection();
            rs = stmt.executeQuery("SELECT * FROM tblClasses");
            rs.next();
            
            ClassCode =  rs.getInt("ClassCode");
            GradeCode = rs.getInt("GradeCode");
            ClassName = rs.getString("ClassName");
            if(rs.getInt("ClassTeacherCode") == 0){
                ClassTeacher = null;
            }
            else{
                ClassTeacher  = new Teacher(Integer.toString(rs.getInt("ClassTeacherCode")));
            }
            close();
        } catch (SQLException ex) {
            Logger.getLogger(ClassRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    private void getConnection(){
        try {
            String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
            Class.forName(driver);
            Conn = DriverManager.getConnection("jdbc:ucanaccess://" + "SchoolDatabase.mdb");
            stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ClassRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *for the addition of a new class room. no return type
     */
    public void AddClassRoom(){
     try {
            getConnection();
            rs = stmt.executeQuery("SELECT * FROM tblClasses");
            
            rs.moveToInsertRow();
            
            rs.updateString("ClassName", ClassName);
            rs.updateInt("GradeCode", GradeCode);
            rs.updateInt("ClassTeacherCode", Integer.parseInt(ClassTeacher.getTeacherID()));
            
            rs.insertRow();
            close();
            JOptionPane.showMessageDialog(null, "Addition Successful");
        } catch (SQLException ex) {
            Logger.getLogger(ClassRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *for fetching the classrooms in the entire grade
     * @return result set containing the classroom in a grade
     */
    public ResultSet getClasses(){
    getConnection();
        try {
            rs = stmt.executeQuery("SELECT * FROM tblClasses");
        } catch (SQLException ex) {
            Logger.getLogger(ClassRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    return rs;
    }
    
    private void close(){
        try {
            Conn.close();
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ClassRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
