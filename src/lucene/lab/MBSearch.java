/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.controller.Controller;
import org.musicbrainz.controller.Release;
import org.musicbrainz.model.searchresult.ReleaseResultWs2;






/**
 *
 * @author Rodrigo
 */
public class MBSearch {
    
    
    
    public void releaseSearch(String release_name) throws MBWS2Exception{
    
        
        Release releases = new Release();
        Long limite = (long)80;
        releases.getSearchFilter().setLimit(limite);
        releases.search(release_name);
        List<ReleaseResultWs2> firstSearchResultPage = releases.getFirstSearchResultPage();
        
        
        for (ReleaseResultWs2 release : firstSearchResultPage){
        
            String title = release.getRelease().getTitle();
            System.out.println(title);
        }
        
       
        
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


    
    public void releaseSearchByASIN(String ASIN) throws MBWS2Exception{
    
        
        Release releases = new Release();
        Long limite = (long)1;
        releases.getSearchFilter().setLimit(limite);
        releases.search("asin:"+ASIN);
        List<ReleaseResultWs2> firstSearchResultPage = releases.getFullSearchResultList();
        
        for (ReleaseResultWs2 release : firstSearchResultPage){
        
            String title = release.getRelease().getTitle();
            System.out.println(title);
        }
         
    }
}   
