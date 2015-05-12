/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.io.IOException;
import java.sql.SQLException;
import org.musicbrainz.MBWS2Exception;


/**
 *
 * @author Rodrigo
 */
public class LuceneLab {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, MBWS2Exception, InterruptedException, SQLException, Exception {
        
        
       // IndexClass index = new IndexClass();
        DB db= new DB();
        //index.Indexer();
        db.updateRelease();
        //org.apache.log4j.BasicConfigurator.configure();

        //MBSearch mbSearch = new MBSearch();
        //mbSearch.releaseSearch("A Sunshine state of mind rock king");
        //MBSearch mbSearch = new MBSearch();
        //mbSearch.releaseSearch("A Sunshine state of mind rock king");
        //mbSearch.releaseSearchByASIN("B000058A81");
    //    mbSearch.insertRelease();

    }
    
}
