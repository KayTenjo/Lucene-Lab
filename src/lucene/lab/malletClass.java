/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Rodrigo
 */
public class malletClass {

    public void createTextForMallet() throws IOException {

        String fileName = "music_for_mallet.txt";
        FileWriter fileWriter
                = new FileWriter(fileName);

        BufferedWriter bufferedWriter
                = new BufferedWriter(fileWriter);

            // Note that write() does not automatically
        // append a newline character.
        
        
        ArrayList<String> datos = new ArrayList();
        ResultSet rs = null;
        ResultSet rs_id = null;
        Connection c = null;
        PreparedStatement pst = null;
        PreparedStatement pst_id = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/music",
                            "postgres", "123");
         
            String sql = "SELECT DISTINCT PRODUCTID FROM MUSIC";
            pst = c.prepareStatement(sql);
            //pst.setString(1, productId);
            rs = pst.executeQuery();
            while (rs.next()) {
                
                
                String productID = rs.getString("PRODUCTID");
                
                String query_id = "SELECT * FROM MUSIC WHERE PRODUCTID = ?";

                pst_id = c.prepareStatement(query_id);
                pst_id.setString(1, productID);
                
                rs_id = pst.executeQuery();
                
                int cont_id =0;
                
                while (rs_id.next()){
                
                    String comentario = rs.getString("TEXT");
                    String score = rs.getString("HELPFULNESS");
                    String sentimiento ="";
                    if (Float.parseFloat(score) > 3){
                        sentimiento = "POSITIVO";
                    } 
                
                    else if (Float.parseFloat(score) < 3){
                        sentimiento = "NEGATIVO";
                    }
                    else {
                        sentimiento = "NEUTRO";          
                    }
                    
                    String id = rs.getString("PRODUCTID") +"-"+cont_id;
                    
                    String resultado = id + " " + sentimiento + " " + comentario;
                    
                    bufferedWriter.write(resultado);
                    bufferedWriter.newLine();
                    
                    cont_id++;
                    
                }
             
            fileWriter.close();
            c.close();
            
            System.out.println("Creación archivo mallet finalizada");
                
            }
            
            

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Got an exception oh dog! ");
            System.err.println(e.getMessage());
        }
        
        

    }

}
