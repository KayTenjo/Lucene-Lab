/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;




/**
 *
 * @author Rodrigo
 */
public class IndexClass {
    
    
    public void Indexer() throws IOException, SQLException{
    
        Analyzer analyzer = new StandardAnalyzer();
        Path indexPath = Paths.get("C:\\index\\");
        Directory directory = FSDirectory.open(indexPath);
        //IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //config.setRAMBufferSizeMB(512.0);
        //IndexWriter iwriter = new IndexWriter(directory, config);
        Statement stmt = null;
        MBSearch mbsearch=new MBSearch();
        ArrayList<String> productId = mbsearch.getId();
        Connection c = null;
        //System.out.println(productId);
        for (String productId1 : productId) {
            System.out.println(productId1);        
            
            ArrayList<String> delRelease = getFromRelease(productId1);
            System.out.println(delRelease);

            ArrayList<String> datos=getFromMusic(productId1);
            System.out.println(datos);
            System.out.println("------------------------------------------");

        }
        
        //addDoc(iwriter, productId, title, userId, profileName, helpfulness, score, summary, text); 
        
        //iwriter.close();

        
    }
    public ArrayList<String> getFromRelease(String productId) {
        ArrayList<String> datos=new ArrayList();
        ResultSet rs=null;
        Connection c = null;
        PreparedStatement pst = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/music",
                            "postgres", "123");
            String sql = "SELECT * FROM RELEASE WHERE PRODUCTID=?";
            pst = c.prepareStatement(sql);
            pst.setString(1, productId);
            rs = pst.executeQuery();
            while (rs.next()) {

                datos.add(rs.getString("ARTIST"));
                datos.add(rs.getString("TITLE"));
                datos.add(rs.getString("DATE"));
                datos.add(rs.getString("COUNTRY"));
                datos.add(rs.getString("BARCODE"));
                datos.add(String.valueOf(rs.getInt("TRACKCOUNT")));
                datos.add(rs.getString("LABEL"));
                datos.add(rs.getString("LANGUAGE"));

            }

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Got an exception oh dog! ");
            System.err.println(e.getMessage());
        }
        return datos;
    
    }
    
    
    
    public ArrayList<String> getFromMusic(String productId) {
        ArrayList<String> datos=new ArrayList();
        ResultSet rs=null;
        Connection c = null;
        PreparedStatement pst = null;
        String textos="";
        String help="";
        String summary="";
        String userID="";
        String profileName="";
        float prom=0;        
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/music",
                            "postgres", "123");
            String sql = "SELECT * FROM MUSIC WHERE PRODUCTID=?";
            pst = c.prepareStatement(sql);
            pst.setString(1, productId);
            rs=pst.executeQuery();
            int count=0;
            while (rs.next()) {
                textos+=rs.getString("TEXT")+"##";
                help+=rs.getString("HELPFULNESS")+"##";
                summary+=rs.getString("SUMMARY")+"##";
                prom+=Float.parseFloat(rs.getString("SCORE"));
                userID+=rs.getString("USERID")+"##";
                profileName+=rs.getString("PROFILENAME")+"##";
                //System.out.println(prom);

                 count=count+1;
            }
            prom=prom/count;
            datos.add(userID);
            datos.add(profileName);
            datos.add(help);
            datos.add(Float.toString(prom));
            datos.add(summary);
            datos.add(textos);


        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("excepcion den getFromMusic ");
            System.err.println(e.getMessage());
        }
        return datos;
    

    }

    

        
       
    
    
    private static void addDoc(IndexWriter w, String productId, String title, String userID, String profileName, 
            String helpfulness, String score, String summary, String text) throws IOException {
        Document doc = new Document();
        //doc.add(new TextField("title", title, Field.Store.YES));
        //doc.add(new StringField("isbn", isbn, Field.Store.YES));
        doc.add(new StringField("productId", productId, Field.Store.YES));
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new StringField("userID", userID, Field.Store.YES));
        doc.add(new TextField("profileName", profileName, Field.Store.YES));
        doc.add(new StringField("helpfulness", helpfulness, Field.Store.YES));
        doc.add(new StringField("score", score, Field.Store.YES));
        doc.add(new TextField("summary", summary, Field.Store.YES));
        doc.add(new TextField("text", text, Field.Store.YES));
        
        w.addDocument(doc);
    }
    
    

}
