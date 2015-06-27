/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import cc.mallet.classify.Classifier;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;
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
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
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
import org.musicbrainz.model.TagWs2;
import org.musicbrainz.model.searchresult.ReleaseResultWs2;




/**
 *
 * @author Rodrigo
 */
public class IndexClass {
    
    
    public void Indexer() throws FileNotFoundException ,Exception{
        
        
        malletClass mallet = new malletClass();
        File archivo_clasificador = new File("music.classifier");
        Classifier clasificador = mallet.loadClassifier(archivo_clasificador);
        Map<String, Analyzer> analyzerPerField = new HashMap<>();
        analyzerPerField.put("productId", new KeywordAnalyzer());
        analyzerPerField.put("title", new WhitespaceAnalyzer());
        analyzerPerField.put("titleMin", new WhitespaceAnalyzer());
        analyzerPerField.put("price", new KeywordAnalyzer());
        analyzerPerField.put("avScore", new KeywordAnalyzer());
        analyzerPerField.put("score", new KeywordAnalyzer());
        analyzerPerField.put("UserId", new KeywordAnalyzer());
        analyzerPerField.put("UserName", new KeywordAnalyzer());
        analyzerPerField.put("summary", new StandardAnalyzer());
        analyzerPerField.put("text", new StandardAnalyzer());
        analyzerPerField.put("artist", new WhitespaceAnalyzer());
        analyzerPerField.put("artistMin", new WhitespaceAnalyzer());
        analyzerPerField.put("titlemb", new WhitespaceAnalyzer());
        analyzerPerField.put("mbRating", new KeywordAnalyzer());
        analyzerPerField.put("year", new KeywordAnalyzer());
        analyzerPerField.put("countryid", new KeywordAnalyzer());
        analyzerPerField.put("barcode", new KeywordAnalyzer());
        analyzerPerField.put("trackCount", new KeywordAnalyzer());
        analyzerPerField.put("label", new WhitespaceAnalyzer());
        analyzerPerField.put("lang", new KeywordAnalyzer());  
        analyzerPerField.put("tags", new WhitespaceAnalyzer()); 
        analyzerPerField.put("tagsMin", new WhitespaceAnalyzer() );
        analyzerPerField.put("sentimiento", new KeywordAnalyzer());
        analyzerPerField.put("sentProm", new KeywordAnalyzer());       

        
        PerFieldAnalyzerWrapper analyzerWrapper = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerPerField);               
        Path indexPath = Paths.get("C:\\index\\");
        Directory directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(analyzerWrapper);
        config.setRAMBufferSizeMB(512.0);
        IndexWriter iwriter = new IndexWriter(directory, config);
        File file = new File("junto.txt");
        Scanner sc = new Scanner(file);
        String pid = "", title = "", titleMin = "", price = "", avScore = "", score = "", uid = "", uname = "", summary = "", text = "";
        String artist = "", artistMin = "", titlemb = "", mbRating = "", year = "", countryid = "", barcode = "", trackCount = "", label = "", lang = "", tagsMin = "", tags = "";
        int qtyOfTrainingSet = 1500;
        int startAt = 1503;
        int currentReview = 0;
        int itemsWritten = 0;
        String n = "\n";
        iteracionPorReviews:
        while (itemsWritten < qtyOfTrainingSet && sc.hasNextLine()) {
            if (startAt > currentReview) {
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();

                currentReview++;
                continue;
            }
            String a = sc.nextLine();
            pid = a;
            a = sc.nextLine();
            title = a;
            titleMin = title.toLowerCase();
            a = sc.nextLine();
            price = a;
            a = sc.nextLine();
            avScore = a;
            a = sc.nextLine();
            score = a;
            a = sc.nextLine();
            uid = a;
            a = sc.nextLine();
            uname = a;
            a = sc.nextLine();
            summary = a;
            a = sc.nextLine();
            text = a;
            File f = new File("MB/" + pid + ".txt");

            String comentariosMallet = mallet.comentariosToMallet(text);
            String sentimiento = mallet.cadenaSentimiento(clasificador,comentariosMallet );
            Double sentProm = mallet.promedioSentimiento(clasificador, comentariosMallet);
            //String sentimiento = "";
            //Double sentProm = 0.0;
            if (f.exists() && !f.isDirectory()) {
                Scanner scf = new Scanner(f);
                scf.nextLine();
                artist = scf.nextLine();
                artistMin = artist.toLowerCase();
                titlemb = scf.nextLine();
                mbRating = scf.nextLine();
                year = scf.nextLine();
                countryid = scf.nextLine();
                barcode = scf.nextLine();
                trackCount = scf.nextLine();
                label = scf.nextLine();
                lang = scf.nextLine();
                if (scf.hasNextLine()) {
                    tags = scf.nextLine();
                    tagsMin = tags.toLowerCase();
                }
            }
            System.out.println("###################################### Documento: " + itemsWritten);
            addDoc(iwriter, pid, title, titleMin, price, avScore, score, uid,
                    uname, summary, text, artist, artistMin, titlemb, mbRating, year, countryid,
                    barcode, trackCount, label, lang, tags, tagsMin, sentimiento, sentProm);
            itemsWritten++;
            currentReview++;           
        }
        iwriter.close();
    }

    private static void addDoc(IndexWriter w, String pid, String title, String titleMin, String price, 
            String avScore, String score, String uid, String uname, String summary, String text,
            String artist, String artistMin, String titlemb, String mbRating, String year, 
            String countryid, String barcode, String trackCount, String label, String lang, 
            String tags, String tagsMin,
            String sentimiento, double sentProm) throws IOException {
        Document doc = new Document();
        if(mbRating==""){
        mbRating="0.0";
        }
        doc.add(new StringField("productId", pid, Field.Store.YES));//
        doc.add(new StringField("title", title, Field.Store.YES));
        doc.add(new StringField("titleMin", titleMin, Field.Store.YES));
        doc.add(new StringField("price", price, Field.Store.YES));
        doc.add(new FloatField("avScore", Float.valueOf(avScore), Field.Store.YES));
        doc.add(new TextField("score",score, Field.Store.YES));
        doc.add(new TextField("UserId",uid, Field.Store.YES));
        doc.add(new TextField("UserName",uname, Field.Store.YES));
        doc.add(new TextField("summary",summary, Field.Store.YES));
        doc.add(new TextField("text",text, Field.Store.YES));
        doc.add(new StringField("artist",artist, Field.Store.YES));
        doc.add(new StringField("artistMin",artistMin, Field.Store.YES));
        doc.add(new StringField("titlemb",titlemb, Field.Store.YES));
        doc.add(new FloatField("mbRating",Float.valueOf(mbRating), Field.Store.YES));
        doc.add(new StringField("year",year, Field.Store.YES));
        doc.add(new StringField("countryid",countryid, Field.Store.YES));
        doc.add(new StringField("barcode",barcode, Field.Store.YES));
        doc.add(new StringField("trackCount",trackCount, Field.Store.YES));
        doc.add(new StringField("label",label, Field.Store.YES));
        doc.add(new StringField("lang",lang, Field.Store.YES));
        doc.add(new TextField("tags",tags, Field.Store.YES));
        doc.add(new TextField("tagsMin",tagsMin, Field.Store.YES));
        doc.add(new TextField("sentimiento",sentimiento, Field.Store.YES));
        doc.add(new DoubleField("sentProm",sentProm, Field.Store.YES));        
        w.addDocument(doc);
    }
    



}
