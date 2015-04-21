/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.io.IOException;

/**
 *
 * @author Rodrigo
 */
public class LuceneLab {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        
        IndexClass index = new IndexClass();
        index.Indexer();
    }
    
}
