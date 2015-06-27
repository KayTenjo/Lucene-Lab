/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.io.File;
import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.controller.Controller;
import org.musicbrainz.controller.Release;
import org.musicbrainz.model.searchresult.ReleaseResultWs2;

/**
 *
 * @author Rodrigo
 */
public class MBSearch {

    public void releaseSearch(String release_name) throws MBWS2Exception {

        Release releases = new Release();
        Long limite = (long) 80;
        releases.getSearchFilter().setLimit(limite);
        releases.search(release_name);
        List<ReleaseResultWs2> firstSearchResultPage = releases.getFirstSearchResultPage();

        for (ReleaseResultWs2 release : firstSearchResultPage) {

            String title = release.getRelease().getTitle();
            System.out.println(title);
        }

    }



  


    public List<ReleaseResultWs2> releaseSearchByASIN(String ASIN) throws MBWS2Exception {

        Release releases = new Release();
        Long limite = (long) 1;
        releases.getSearchFilter().setLimit(limite);
        releases.search("asin:" + ASIN);
        List<ReleaseResultWs2> firstSearchResultPage = releases.getFullSearchResultList();

       
        return firstSearchResultPage;
        
       
         

    }
}
