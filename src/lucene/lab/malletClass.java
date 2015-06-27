/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.LabelAlphabet;

import cc.mallet.types.Labeling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Rodrigo
 */
public class malletClass {

    public void createTextForMallet() throws IOException {

        String fileName = "music_for_mallet.txt";
        FileWriter fileWriter
                = new FileWriter(fileName);

        BufferedWriter bufferedWriter
                = new BufferedWriter(fileWriter);

            // Note that write() does not automatically
        // append a newline character.
        ArrayList<String> datos = new ArrayList();
        ResultSet rs = null;
        ResultSet rs_id = null;
        Connection c = null;
        PreparedStatement pst = null;
        PreparedStatement pst_id = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/music",
                            "postgres", "123");

            String sql = "SELECT DISTINCT PRODUCTID FROM MUSIC";
            pst = c.prepareStatement(sql);
            //pst.setString(1, productId);
            rs = pst.executeQuery();
            while (rs.next()) {

                String productID = rs.getString("PRODUCTID");

                String query_id = "SELECT * FROM MUSIC WHERE PRODUCTID = ?";

                pst_id = c.prepareStatement(query_id);
                pst_id.setString(1, productID);

                rs_id = pst_id.executeQuery();

                int cont_id = 0;

                while (rs_id.next()) {
                    
                    String comentario = rs_id.getString("TEXT");
                    Float score = rs_id.getFloat("SCORE");
                    String sentimiento = "";
                    if (score > 3) {
                        sentimiento = "POSITIVO";
                    } else if (score < 3) {
                        sentimiento = "NEGATIVO";
                    } else {
                        sentimiento = "NEUTRO";
                    }

                    String id = rs_id.getString("PRODUCTID") + "-" + cont_id;

                    String resultado = id + " " + sentimiento + " " + comentario;

                    bufferedWriter.write(resultado);
                    bufferedWriter.newLine();

                    cont_id++;
                    System.out.println("Agregado a la lista " + id);

                }

            }
            fileWriter.close();

            c.close();
            System.out.println("Creación archivo mallet finalizada");

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Got an exception oh dog! ");
            System.err.println(e.getMessage());
        }

    }

    public Classifier loadClassifier(File serializedFile)
            throws FileNotFoundException, IOException, ClassNotFoundException {

        // The standard way to save classifiers and Mallet data                                            
        //  for repeated use is through Java serialization.                                                
        // Here we load a serialized classifier from a file.                                               
        Classifier classifier;

        ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream(serializedFile));
        classifier = (Classifier) ois.readObject();
        ois.close();

        return classifier;
    }

    public Double promedioSentimiento(Classifier classifier, String datos) throws IOException {

        double valor_sentimiento = 0;

        int contador_sentimiento = 0;

        StringReader stringReader = new StringReader(datos);

        CsvIterator reader
                = new CsvIterator(stringReader,
                        "(\\w+)\\s+(\\w+)\\s+(.*)",
                        3, 2, 1);

        Iterator instances = classifier.getInstancePipe().newIteratorFrom(reader);

        while (instances.hasNext()) {

            double positivo = 0;
            double negativo = 0;
            double neutro = 0;
            Labeling labeling = classifier.classify(instances.next()).getLabeling();

            // print the labels with their weights in descending order (ie best first)
            //LabelAlphabet label_list = labeling.getLabelAlphabet();
            for (int rank = 0; rank < labeling.numLocations(); rank++) {

                if ("NEUTRO".equals(labeling.getLabelAtRank(rank).toString())) {

                    neutro = labeling.getValueAtRank(rank);

                }
                if ("POSITIVO".equals(labeling.getLabelAtRank(rank).toString())) {

                    positivo = labeling.getValueAtRank(rank);

                }

                if ("NEGATIVO".equals(labeling.getLabelAtRank(rank).toString())) {

                    negativo = labeling.getValueAtRank(rank);

                }

            }

            valor_sentimiento = valor_sentimiento + ((positivo + (1 - negativo) + (neutro / 2)) / 2);

            contador_sentimiento++;

        }

        return valor_sentimiento / contador_sentimiento;
    }

    public String cadenaSentimiento(Classifier classifier, String datos) throws IOException {

        double valor_sentimiento = 0;

        int contador_sentimiento = 0;
        String cadena_final = "";
        // Create a new iterator that will read raw instance data from                                     
        //  the lines of a file.                                                                           
        // Lines should be formatted as:                                                                   
        //                                                                                                 
        //   [name] [label] [data ... ]                                                                    
        //                                                                                                 
        //  in this case, "label" is ignored.                                                              

        StringReader stringReader = new StringReader(datos);
        // Create a new iterator that will read raw instance data from                                     
        //  the lines of a file.                                                                           
        // Lines should be formatted as:                                                                   
        //                                                                                                 
        //   [name] [label] [data ... ]                                                                    
        //                                                                                                 
        //  in this case, "label" is ignored.                                                              

        CsvIterator reader
                = new CsvIterator(stringReader,
                        "(\\w+)\\s+(\\w+)\\s+(.*)",
                        3, 2, 1);  // (data, label, name) field indices               

        // Create an iterator that will pass each instance through                                         
        //  the same pipe that was used to create the training data                                        
        //  for the classifier.                                                                            
        Iterator instances = classifier.getInstancePipe().newIteratorFrom(reader);

        // Classifier.classify() returns a Classification object                                           
        //  that includes the instance, the classifier, and the                                            
        //  classification results (the labeling). Here we only                                            
        //  care about the Labeling.                                                                       
        while (instances.hasNext()) {

            double positivo = 0;
            double negativo = 0;
            double neutro = 0;
            instances.next();
            //Classification clasification = classifier.classify(instances.next());
            //Labeling labeling = clasification.getLabeling();
            //Labeling labeling = classifier.classify(instances.next()).getLabeling();

            // print the labels with their weights in descending order (ie best first)
            //LabelAlphabet label_list = labeling.getLabelAlphabet();
            /*for (int rank = 0; rank < labeling.numLocations(); rank++){
                       
             if ("NEUTRO".equals(labeling.getLabelAtRank(rank).toString())){
                    
             neutro = labeling.getValueAtRank(rank);
             }
             if ("POSITIVO".equals(labeling.getLabelAtRank(rank).toString())) {
                    
             positivo = labeling.getValueAtRank(rank);
                
             }
                
             if ("NEGATIVO".equals(labeling.getLabelAtRank(rank).toString())){
                
             negativo = labeling.getValueAtRank(rank);
    
             }
                
             }*/
            valor_sentimiento = ((positivo + (1 - negativo) + (neutro / 2)) / 2);
            cadena_final = cadena_final + "##";

            contador_sentimiento++;

        }

        return cadena_final;
    }

    public String comentariosToMallet(String cadena_larga) {

        String[] cadena_split = cadena_larga.split("##");

        String cadena_final="";
        String salto_linea= "\r\n ";
        int cont =0;
        String label= "a";
        for (String string : cadena_split){
            
            cadena_final = cadena_final + cont + " " + label + " " + string;
            cont++;
            if (cont != cadena_split.length) {

                cadena_final = cadena_final + salto_linea;

            }

        }

        return cadena_final;


    }

      
     
     public void printLabelings(Classifier classifier, String cadena) throws IOException {

        // Create a new iterator that will read raw instance data from                                     
        //  the lines of a file.                                                                           
        // Lines should be formatted as:                                                                   
        //                                                                                                 
        //   [name] [label] [data ... ]                                                                    
        //                                                                                                 
        //  in this case, "label" is ignored.                                                              
        
        //StringReader sr = new StringReader(cadena);
         
        CsvIterator reader =
            new CsvIterator(new StringReader(cadena),
                            "(\\w+)\\s+(\\w+)\\s+(.*)",
                            3, 2, 1);  // (data, label, name) field indices               

        // Create an iterator that will pass each instance through                                         
        //  the same pipe that was used to create the training data                                        
        //  for the classifier.                                                                            
        Iterator instances =
            classifier.getInstancePipe().newIteratorFrom(reader);

        // Classifier.classify() returns a Classification object                                           
        //  that includes the instance, the classifier, and the                                            
        //  classification results (the labeling). Here we only                                            
        //  care about the Labeling.                                                                       
        while (instances.hasNext()) {
            
           instances.next();
           /* Labeling labeling = classifier.classify(instances.next()).getLabeling();

            // print the labels with their weights in descending order (ie best first)                     

            for (int rank = 0; rank < labeling.numLocations(); rank++){
                System.out.print(labeling.getLabelAtRank(rank) + ":" +
                                 labeling.getValueAtRank(rank) + " ");
            }
            System.out.println();
                   */
        }
    }
     
     

    }



