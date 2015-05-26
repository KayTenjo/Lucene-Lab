/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Thread.sleep;
import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.model.searchresult.ReleaseResultWs2;

/**
 *
 * @author Valeria
 */
public class DB {

    public static Connection getConnection() throws Exception {
        String driver = "org.postgresql.Driver";
        String url = "jdbc:postgresql://localhost:5432/music";
        String username = "postgres";
        String password = "123";
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, username, password);
        //System.out.println("Conectado!!");
        return conn;
    }

    public ArrayList<String> selectRelease() {
        ArrayList<String> datos = new ArrayList();
        ResultSet rs = null;
        PreparedStatement stat = null;
        Connection c;
        try {
            c = getConnection();
            String sql = "SELECT * FROM release";
            stat = c.prepareStatement(sql);
        } catch (Exception e) {
        }
        return datos;
    }

   // public void updateRelease(int n, int m) throws SQLException, Exception {
    public void updateRelease() throws SQLException, Exception {
        //ArrayList<String> productId = getIdRange("RELEASE", n, m);
        ArrayList<String> productId = new ArrayList();
        String ID = " B000S08YUK";
        productId.add(ID);
        Connection c = null;
        PreparedStatement pst = null;
        try {
            c = getConnection();
            int i=1;
            for (String productId1 : productId) {
                ArrayList<String> datosRelease = getFromMusic(productId1);
                String query = "update release set userid=?, profilename=?, help = ?, score=?, summary=?, review=? " + "where productid = ? ";
                pst = c.prepareStatement(query);
                pst.setString(1, datosRelease.get(0));
                pst.setString(2, datosRelease.get(1));
                pst.setString(3, datosRelease.get(2));
                pst.setFloat(4, Float.parseFloat(datosRelease.get(3)));
                pst.setString(5, datosRelease.get(4));
                pst.setString(6, datosRelease.get(5));
                pst.setString(7, productId1);
                pst.executeUpdate();
                System.out.println(i+". Actualizados datos del ID: " + productId1);
                i++;
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        c.close();
    }

    public ArrayList<String> getFromMusic(String productId) throws Exception {
        ArrayList<String> datos = new ArrayList();
        ResultSet rs = null;
        Connection c;
        PreparedStatement pst = null;
        String textos = "";
        String help = "";
        String summary = "";
        String userID = "";
        String profileName = "";
        float prom = 0;
        try {
            c=getConnection();
            String sql = "SELECT * FROM MUSIC WHERE PRODUCTID=?";
            pst = c.prepareStatement(sql);
            pst.setString(1, productId);
            rs = pst.executeQuery();
            int count = 0;
            while (rs.next()) {
                textos += rs.getString("TEXT") + " ";
                help += rs.getString("HELPFULNESS") + " ";
                summary += rs.getString("SUMMARY") + " ";
                prom += Float.parseFloat(rs.getString("SCORE"));
                userID += rs.getString("USERID") + " ";
                profileName += rs.getString("PROFILENAME") + " ";
                count = count + 1;
            }
            prom = prom / count;
            datos.add(userID);
            datos.add(profileName);
            datos.add(help);
            datos.add(Float.toString(prom));
            datos.add(summary);
            datos.add(textos);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("excepcion den getFromMusic ");
            System.err.println(e.getMessage());
        }
        return datos;
    }

    public ArrayList<String> getFromRelease(String productId) throws Exception {
        ArrayList<String> datos = new ArrayList();
        ResultSet rs = null;
        Connection c = null;
        PreparedStatement pst = null;
        try {
            c=getConnection();
            String sql = "SELECT * FROM RELEASE WHERE PRODUCTID=?";
            pst = c.prepareStatement(sql);
            pst.setString(1, productId);
            rs = pst.executeQuery();
            while (rs.next()) {
                datos.add(rs.getString("ARTIST"));
                datos.add(rs.getString("TITLE"));
                datos.add(rs.getString("DATE"));
                datos.add(rs.getString("COUNTRY"));
                datos.add(rs.getString("BARCODE"));
                datos.add(String.valueOf(rs.getInt("TRACKCOUNT")));
                datos.add(rs.getString("LABEL"));
                datos.add(rs.getString("LANGUAGE"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Got an exception oh dog! ");
            System.err.println(e.getMessage());
        }
        return datos;
    }

    public ArrayList<String> getId(String tabla) throws Exception {
        ArrayList<String> aidis = new ArrayList();
        
        Connection c;
        Statement stmt = null;
        try {
            c =getConnection();
            stmt = c.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT DISTINCT PRODUCTID FROM "+tabla);
            while (rs.next()) {
                String title = rs.getString("PRODUCTID");
                aidis.add(title);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Got an exception oh dog! ");
            System.err.println(e.getMessage());
        }
        System.out.println("productID capturados desde la tabla enriquecida");
        return aidis;
    }

    public void insertRelease() throws MBWS2Exception, InterruptedException, SQLException, Exception {
        MBSearch mbSearch=new MBSearch();
        int n=3000;
        int m=0;
        ArrayList<String> productId = getIdRange("MUSIC",n,m);
        Connection c=null;
        try {
            c=getConnection();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        PreparedStatement pst = null;
        int i = 1;
        for (String productId1 : productId) {
            if (isIn("RELEASE", productId1)) {
                System.out.println("está: "+productId1);
                continue;
            }            
            List<ReleaseResultWs2> datos = mbSearch.releaseSearchByASIN(productId1);
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
                c.commit();
                sql = null;
                sleep(1001);
            } else
                System.out.println(i + ". No hay datos del:" + productId1);
            i++;
        }
        c.close();
        System.out.println("Este mensaje no lo voy a ver jamás :c");
    }
    
    public ArrayList<String> getIdRange(String tabla,int n, int m){
        ArrayList<String> idis=new ArrayList();
        Connection c;
        //System.out.println("ola");
        Statement stm=null;
        int i=1;
        try {
            c=getConnection();
            String sql="SELECT * FROM "+tabla+" LIMIT "+n+" OFFSET "+m+";";
            stm=c.createStatement();
            ResultSet rs;
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                System.out.println(i+" "+rs.getString("PRODUCTID"));
                idis.add(rs.getString("PRODUCTID"));
                i++;
                
            }
        } catch (Exception e) {
        }
        return idis;    
    }
    
    public boolean isIn(String tabla,String id){
        Connection c;
        PreparedStatement pst;
        ResultSet rs;
        try {
            c = getConnection();
            String sql = "SELECT * FROM "+tabla+" WHERE PRODUCTID=?";
            pst = c.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (!rs.isBeforeFirst()) {
                //System.out.println("false");
            return false;
            }
            
        } catch (Exception e) {
        }
        //System.out.println("true");
        return true;

    }
    

    
    

    
}
