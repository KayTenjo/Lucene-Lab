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

    public static void main(String[] args) throws IOException, MBWS2Exception, InterruptedException, SQLException, ParseException, Exception {


        //ReaderClass reader = new ReaderClass();

        //TrainGenerator trainGenerator = new TrainGenerator();
        WritterClass escribe=new WritterClass();
        escribe.writeMB();

        
        //trainGenerator.generateTrain();
        //ReaderClass reader = new ReaderClass();
       
        IndexClass index = new IndexClass();

       // DB db= new DB();
       //db.updateRelease();

        //DB db= new DB();

        //index.oli();
        //db.getIdRange("RELEASE",3000,0);


        //db.isIn("RELEASE"," B000007SLB");
        //db.getIdRange("RELEASE",40, 0);
        

        /*

        while(true){
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduzca la consulta : ");
        String consulta=sc.nextLine();
        //System.out.print("Introduzca el campo : ");
        //String campo=sc.nextLine();
        reader.search(consulta,"oli");
        }*/
        //IndexClass index = new IndexClass();
        //index.Indexer();

        //org.apache.log4j.BasicConfigurator.configure();


        //MBSearch mbSearch = new MBSearch();
        //malletClass coso= new malletClass();

       // MBSearch mbSearch = new MBSearch();

        //mbSearch.releaseSearch("A Sunshine state of mind rock king");
        //MBSearch mbSearch = new MBSearch();
        //mbSearch.releaseSearch("A Sunshine state of mind rock king");
        //mbSearch.releaseSearchByASIN("B000058A81");
       // db.insertRelease();
        

    }
    
}
