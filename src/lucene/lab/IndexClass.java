/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatDocValuesField;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;




/**
 *
 * @author Rodrigo
 */
public class IndexClass {
    
    
    public void Indexer() throws IOException, SQLException, Exception{
    
        Map<String, Analyzer> analyzerPerField = new HashMap<>();
        
        analyzerPerField.put("title", new WhitespaceAnalyzer() ); 
        analyzerPerField.put("productId", new KeywordAnalyzer()); 
        analyzerPerField.put("userID", new KeywordAnalyzer());
        analyzerPerField.put("profileName", new WhitespaceAnalyzer() );
        analyzerPerField.put("score", new KeywordAnalyzer());
        analyzerPerField.put("summary", new StandardAnalyzer());
        analyzerPerField.put("text", new StandardAnalyzer());
        analyzerPerField.put("artist", new WhitespaceAnalyzer());
        analyzerPerField.put("date",new KeywordAnalyzer());
        analyzerPerField.put("country",new KeywordAnalyzer());
        analyzerPerField.put("barcode", new KeywordAnalyzer());
        analyzerPerField.put("trackCount", new KeywordAnalyzer());
        analyzerPerField.put("label", new WhitespaceAnalyzer());
        analyzerPerField.put("language", new KeywordAnalyzer());
        
                 // create a per-field analyzer wrapper using the StandardAnalyzer as .. standard analyzer ;)
        PerFieldAnalyzerWrapper analyzerWrapper = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerPerField);
               
        Path indexPath = Paths.get("C:\\index\\");
        Directory directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(analyzerWrapper);
        config.setRAMBufferSizeMB(512.0);
        IndexWriter iwriter = new IndexWriter(directory, config);
        Statement stmt = null;
        DB db=new DB();

        MBSearch mbsearch=new MBSearch();
        ArrayList<String> productId = db.getId("RELEASE");
        Connection c = null;
        //System.out.println(productId);
        for (String productId1 : productId) {
            System.out.println("Obteniendo datos Para el documento asociado a: "+productId1);        
            
            ArrayList<String> delRelease = db.getFromRelease(productId1);
            //System.out.println(delRelease);

            ArrayList<String> delMusic=db.getFromMusic(productId1);
            //System.out.println(datos);
            //System.out.println("------------------------------------------");
            addDoc(iwriter, productId1, delRelease.get(1), delMusic.get(0), delRelease.get(1), delMusic.get(2), Float.valueOf(delMusic.get(3)), delMusic.get(4), delMusic.get(5), delRelease.get(0), delRelease.get(2), delRelease.get(3), delRelease.get(4), 
                    Integer.valueOf(delRelease.get(5)), delRelease.get(6), delRelease.get(7));
            System.out.println("Añadido el documento "+productId1+" al índice");


        }
        
        
        
        //addDoc(iwriter, productId, title, userId, profileName, helpfulness, score, summary, text); 
        
        iwriter.close();

        
    }
    

    

        
       
    
    
    private static void addDoc(IndexWriter w, String productId, String title, String userID, String profileName, 
            
        String helpfulness, Float score, String summary, String text, String artist, String date,
        String country, String barcode, Integer trackCount, String label, String language) throws IOException {
        Document doc = new Document();
        if(date==null) date="";
        if(title==null)title="";            
        if(userID==null) userID="";
        if(profileName==null) profileName="";
        if(helpfulness==null) helpfulness="";
        if(summary==null) summary="";
        if(text==null) text="";
        if(artist==null) artist="";
        if(country==null) country="";
        if(barcode==null) barcode="";
        if(trackCount==null) trackCount=0;
        if(score==null) score=(float)0;
        if(label==null) label="";
        if(language==null) language="";
        
        doc.add(new StringField("productId", productId, Field.Store.YES));//
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new SortedDocValuesField("title", new BytesRef(title)));
        doc.add(new StringField("userID", userID, Field.Store.YES));
        doc.add(new TextField("profileName", profileName, Field.Store.YES));
        doc.add(new StringField("helpfulness", helpfulness, Field.Store.YES));
        doc.add(new FloatField("score", score, Field.Store.YES));
        doc.add(new FloatDocValuesField("score", score));
        doc.add(new TextField("summary", summary, Field.Store.YES));
        doc.add(new TextField("text", text, Field.Store.YES));        
        doc.add(new TextField("artist", artist, Field.Store.YES));
        doc.add(new SortedDocValuesField("artist", new BytesRef(artist)));
        doc.add(new StringField("date", date, Field.Store.YES));
        doc.add(new StringField("country", country, Field.Store.YES));
        doc.add(new StringField("barcode", barcode, Field.Store.YES));
        doc.add(new IntField("trackCount", trackCount, Field.Store.YES));
        doc.add(new StringField("label", label, Field.Store.YES));
        doc.add(new StringField("language", language, Field.Store.YES));
        
        w.addDocument(doc);
    }
    



}
