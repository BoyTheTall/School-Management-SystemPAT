/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author King The Shepherd
 */
public class swtchOver {
public static void switchOver(){
String usertype;
BufferedReader in;
    try {
        in = new BufferedReader(new FileReader("PassOverParameterFile.txt"));
        String line = in.readLine();
        String [] Columns = line.split("\t");
        
        usertype = Columns[1];
        
        if(usertype.equalsIgnoreCase("Student")){
        
            new GUIs.StudentMainMenuUI(Columns[0]).setVisible(true);
        
        }
        
        if(usertype.equalsIgnoreCase("Teacher")){
        
            new GUIs.TeacherMainMenuUI().setVisible(true);
        
        }
        
        if(usertype.equalsIgnoreCase("Mark Admin")){
        
            new GUIs.AdminMainMenu().setVisible(true);
        
        }
    in.close();
    } catch (FileNotFoundException ex) {
        Logger.getLogger(swtchOver.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(swtchOver.class.getName()).log(Level.SEVERE, null, ex);
    }
        
}    
}
