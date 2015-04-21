/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        IndexWriter iwriter = new IndexWriter(directory, config);
        
        iwriter.close();
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
