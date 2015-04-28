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
    
    
    
    public void releaseSearch(String release_name) throws MBWS2Exception{
    
        
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

    public ArrayList<String> getId() {
        ArrayList<String> aidis = new ArrayList();;
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/music",
                            "postgres", "123");
            stmt = c.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT DISTINCT PRODUCTID FROM RELEASE ");

            while (rs.next()) {
                String title = rs.getString("PRODUCTID");
                aidis.add(title);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Got an exception oh dog! ");
            System.err.println(e.getMessage());
        }
        System.out.println("ya tengo los aidis");
        return aidis;

    }

    public void insertRelease() throws MBWS2Exception, InterruptedException, SQLException {
        ArrayList<String> productId = getId();
        //System.out.println("ya tengo todos los ID");
        Connection c = null;
        try {

            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/music",
                            "postgres", "123");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        Statement stmt = null;
        PreparedStatement pst = null;

        for (String productId1 : productId) {

            List<ReleaseResultWs2> datos = releaseSearchByASIN(productId1);
            if (!datos.isEmpty()) {
                String artista = datos.get(0).getRelease().getArtistCreditString();
                String title = datos.get(0).getRelease().getTitle();
                String date = datos.get(0).getRelease().getDateStr();
                String country = datos.get(0).getRelease().getCountryId();
                String barCode = datos.get(0).getRelease().getBarcode();
                int trackCount = datos.get(0).getRelease().getTracksCount();
                String label = datos.get(0).getRelease().getLabelInfoString();
                String language = datos.get(0).getRelease().getTextLanguage();
                System.out.println(title);

                stmt = c.createStatement();
                String sql = "INSERT INTO RELEASE  " + "VALUES(?,?,?,?,?,?,?,?,?)";
                pst = c.prepareStatement(sql);
                pst.setString(1, productId1);
                pst.setString(2, artista);
                pst.setString(3, title);
                pst.setString(4, date);
                pst.setString(5, country);
                pst.setString(6, barCode);
                pst.setInt(7, trackCount);
                pst.setString(8, label);
                pst.setString(9, language);
                pst.executeUpdate();

                stmt.close();
                c.commit();
                sql = null;

                sleep(1001);
            }

        }
        c.close();
        System.out.println("Este mensaje no lo voy a ver jamás :c");
    }

    public List<ReleaseResultWs2> releaseSearchByASIN(String ASIN) throws MBWS2Exception {

        Release releases = new Release();
        Long limite = (long)1;
        releases.getSearchFilter().setLimit(limite);
        releases.search("asin:"+ASIN);
        List<ReleaseResultWs2> firstSearchResultPage = releases.getFullSearchResultList();
        
        return firstSearchResultPage;
       
         
    }
}   
