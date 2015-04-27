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
    
    
    public void Indexer() throws IOException{
    
        Analyzer analyzer = new StandardAnalyzer();
        Path indexPath = Paths.get("C:\\index\\");
        Directory directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setRAMBufferSizeMB(512.0);
        IndexWriter iwriter = new IndexWriter(directory, config);
        Connection c = null;
        Statement stmt = null;
        ArrayList<String> ProductId= new ArrayList();
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/music",
                            "postgres", "123");
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            
            ResultSet rs;         
            rs = stmt.executeQuery("SELECT DISTINCT PRODUCTID FROM MUSIC ");
            while (rs.next()) {
                String productId = rs.getString("PRODUCTID");
                ProductId.add(productId);
            }
            for (String ProductId1 : ProductId) {

                String sql = "SELECT text FROM music WHERE productid=?";
                PreparedStatement stm = c.prepareStatement(sql);
                stm.setString(1, ProductId1);
                ResultSet textos = stm.executeQuery();
                while (textos.next()) {
                    String text = textos.getString("text");
                    System.out.println(text);
                }

            }
            c.close();
        } catch (Exception e) {
            System.err.println("Got an exception fuck! ");
            System.err.println(e.getMessage());
        } 
        
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
    
    public ArrayList<String> getTitle(){
        ArrayList<String> titulos = new ArrayList();;
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/music",
                            "postgres", "123");
            stmt = c.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT DISTINCT TITLE FROM MUSIC ");
            
            while (rs.next()) {
                String title = rs.getString("TITLE");
                titulos.add(title);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Got an exception oh dog! ");
            System.err.println(e.getMessage());
        }

        return titulos;

    }

}
