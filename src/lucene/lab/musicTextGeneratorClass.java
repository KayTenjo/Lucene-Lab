/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Rodrigo
 */
public class musicTextGeneratorClass {
    
    
    public void generate() throws IOException{
    
        
        File file = new File("Music.txt");
        BufferedWriter out = new BufferedWriter(new FileWriter("trainingData.txt",true));
        Scanner sc = new Scanner(file);
        
        while ( sc.hasNextLine() ) {
        
            String id_aux = (sc.nextLine()).split(": ")[1];
            String title_aux = (sc.nextLine()).split(": ")[1];
            sc.nextLine();
            sc.nextLine();
            sc.nextLine();
            String helpfuness_aux = (sc.nextLine()).split(": ")[1];
        
        }
        
        
        
        
    }
    
}
