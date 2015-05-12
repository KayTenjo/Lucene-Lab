/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.musicbrainz.MBWS2Exception;



/**
 *
 * @author Rodrigo
 */
public class LuceneLab {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, MBWS2Exception, InterruptedException, SQLException, ParseException {
        
        ReaderClass reader = new ReaderClass();
        
        while(true){
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduzca la consulta : ");
        String consulta=sc.nextLine();
        //System.out.print("Introduzca el campo : ");
        //String campo=sc.nextLine();
        reader.search(consulta,"oli");
        }
        //IndexClass index = new IndexClass();
        //index.Indexer();
        
        //org.apache.log4j.BasicConfigurator.configure();


        //MBSearch mbSearch = new MBSearch();
        //mbSearch.releaseSearch("A Sunshine state of mind rock king");
        //MBSearch mbSearch = new MBSearch();
        //mbSearch.releaseSearch("A Sunshine state of mind rock king");
        //mbSearch.releaseSearchByASIN("B000058A81");
        //mbSearch.insertRelease();
        

    }
    
}
