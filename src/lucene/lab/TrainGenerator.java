/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Rodrigo
 */
public class TrainGenerator {
    

     public void generateTrain() throws IOException{
     
        File file = new File("Music.txt");
        BufferedWriter out = new BufferedWriter(new FileWriter("trainingData.txt",true));
        Scanner sc = new Scanner(file);
        Scanner sc2 = new Scanner(System.in);
        String pid, title, price, uid, pname, hness, score, time, summ, text;
        String line;


        int qtyOfTrainingSet = 100;
        int startAt = 3300;

        int currentReview = 0;
        int itemsWritten = 0;
        int seleccion;
        String clase;
        iteracionPorReviews:
        while (itemsWritten < qtyOfTrainingSet && sc.hasNextLine()) {
            if (startAt > currentReview) {
                String a = sc.nextLine();
                a = sc.nextLine();
                a = sc.nextLine();
                a = sc.nextLine();
                a = sc.nextLine();
                a = sc.nextLine();
                a = sc.nextLine();
                a = sc.nextLine();
                a = sc.nextLine();
                a = sc.nextLine();
                a = sc.nextLine();
                currentReview++;
                continue;
            }
            String a = sc.nextLine();
            a = a.substring(a.indexOf(": ") + 2);
            pid = a;
            a = sc.nextLine();
            a = a.substring(a.indexOf(": ") + 2);
            title = a;
            a = sc.nextLine();
            a = a.substring(a.indexOf(": ") + 2);
            price = a;
            a = sc.nextLine();
            a = a.substring(a.indexOf(": ") + 2);
            uid = a;
            a = sc.nextLine();
            a = a.substring(a.indexOf(": ") + 2);
            pname = a;
            a = sc.nextLine();
            a = a.substring(a.indexOf(": ") + 2);
            hness = a;
            a = sc.nextLine();
            a = a.substring(a.indexOf(": ") + 2);
            score = a;
            a = sc.nextLine();
            a = a.substring(a.indexOf(": ") + 2);
            time = a;
            a = sc.nextLine();
            a = a.substring(a.indexOf(": ") + 2);
            summ = a;
            a = sc.nextLine();
            a = a.substring(a.indexOf(": ") + 2);
            text = a;
            a = sc.nextLine();
            if (Float.valueOf(score) > 3) {
                itemsWritten++;
                currentReview++;
                continue;

            }
            System.out.println("============================================");
            System.out.println(title);
            System.out.println(summ+" - Score: "+score);
            System.out.println(text);
            
            System.out.print("("+ itemsWritten + " de " +qtyOfTrainingSet +") 1 positivo, 2 neutro, 3 negativo?: ");
            seleccion = sc2.nextInt();
            switch(seleccion){
                case 1:
                    clase = "positivo";
                    break;
                case 2:
                    clase = "neutro";
                    break;
                case 3:
                    clase = "negativo";
                    break;
                default:
                    currentReview++;
                    continue iteracionPorReviews;
            }
            line = pid + time + " "+clase+" " + summ + " " + text + "\n";
            out.write(line);
            itemsWritten++;
            currentReview++;
        }
        out.close();
     
     }
    
    
}
