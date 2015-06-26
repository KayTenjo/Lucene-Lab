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
import static java.lang.Thread.sleep;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.model.TagWs2;
import org.musicbrainz.model.searchresult.ReleaseResultWs2;

/**
 *
 * @author Valeria
 */
public class WritterClass {
    
    public void generateFile() throws IOException{
        File file = new File("Music.txt");
        BufferedWriter out = new BufferedWriter(new FileWriter("junto.txt"));
        Scanner sc = new Scanner(file);
        String pid, title, price, uid, pname, hness, score, time, summ, text;
        String currentID = "";
        String currentText = "";
        String currentUID = "";
        String currentUName = "";
        String currentSummary = "";
        String currentScore="";
        String currentTitle="";
        String currentPrice="";
        float averageScore=0;
        int div=0;
        String n="\n";
        String sep="##";
        while (sc.hasNext()){
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
            
            if(currentID==null && currentID.equals("")){
            // es el primero
                currentID=pid;
            }

            if ( !pid.equals(currentID) ) {
                //hay cambio de album
                //escribo todo el webeo xD
                out.write(currentID+n);
                out.write(currentTitle + n);
                out.write(currentPrice + n);
                //escribo los juntos de todo
                averageScore = averageScore / div;
                out.write(String.valueOf(averageScore) + n);
                out.write(currentScore + n);
                out.write(currentUID + n);
                out.write(currentUName + n);
                out.write(currentSummary + n);
                out.write(currentText + n);
                //System.out.println(pid+" "+currentID+" prom: "+averageScore+" divisor: "+div);
                //System.out.println("##############################");
                //asigno los nuevos valores <3
                currentScore = score + sep;
                currentSummary = summ + sep;
                currentText = text + sep;
                currentUID = uid + sep;
                currentUName = pname + sep;
                averageScore = Float.valueOf(score);
                //nuevos valores de los cosos que no acumulan <3
                currentPrice = price;
                currentTitle = title;
                currentID = pid;
                div = 1;
                //System.out.println(pid + " " + currentID + "  score: " + score + "  suma: " + averageScore + "  " + div);

            } else {
                //si no, almaceno todo el webeo 
                currentText = currentText + text + sep;
                currentSummary = currentSummary + summ + sep;
                currentUID = currentUID + uid + sep;
                currentUName = currentUName + pname + sep;
                currentScore = currentScore + score + sep;
                averageScore = averageScore + Float.valueOf(score);
                div = div + 1;
                //System.out.println(pid + " " + currentID + "  score: " + score + "  suma: " + averageScore + "  " + div);

            }

        }
        //out.write("pico");
        out.close();

    }
    
    public void writeMB() throws IOException, InterruptedException, MBWS2Exception{
        File file = new File("junto.txt");
        Scanner sc = new Scanner(file);
        String pid;
        String line;
        int qtyOfTrainingSet = 10000;
        int startAt = 5000;

        int currentReview = 0;
        int itemsWritten = 0;
        String n="\n";
        MBSearch mbSearch=new MBSearch();
        int i=0;
        iteracionPorReviews:
        while (itemsWritten < qtyOfTrainingSet && sc.hasNextLine()) {
            if (startAt > currentReview) {
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();
                sc.nextLine();

                currentReview++;
                continue;
            }
            String a = sc.nextLine();
            pid = a;
            sc.nextLine();
            sc.nextLine();
            sc.nextLine();
            sc.nextLine();
            sc.nextLine();
            sc.nextLine();
            sc.nextLine();
            sc.nextLine();
            List<ReleaseResultWs2> datos = mbSearch.releaseSearchByASIN(pid);
            if (!datos.isEmpty()) {
                try (BufferedWriter out = new BufferedWriter(new FileWriter(pid + ".txt", false))) {
                    out.write(pid + n);
                    //escribir los demás datos de interés <3
                    out.write(datos.get(0).getRelease().getArtistCreditString() + n);
                    out.write(datos.get(0).getRelease().getTitle() + n);
                    out.write(String.valueOf(datos.get(0).getRelease().getRating().getAverageRating())+n);
                    out.write(datos.get(0).getRelease().getYear()+ n);
                    out.write(datos.get(0).getRelease().getCountryId() + n);
                    out.write(datos.get(0).getRelease().getBarcode() + n);
                    out.write(datos.get(0).getRelease().getTracksCount() + n);
                    out.write(datos.get(0).getRelease().getLabelInfoString() + n);
                    out.write(datos.get(0).getRelease().getTextLanguage() + n);
                    List<TagWs2> tags = datos.get(0).getRelease().getTags();
                    if (!tags.isEmpty()) {
                        for (TagWs2 next : tags) {
                            out.write(next.getName() + " ");
                            //System.out.println(next);
                        }
                        out.write(n);
                    }
                }
                i++;
                System.out.println(i+"/"+itemsWritten+": "+pid);
                
            }
            itemsWritten++;
            currentReview++;
            sleep(1051);
        }
    }
}
