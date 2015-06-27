/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import org.apache.lucene.document.Document;

/**
 *
 * @author Rodrigo
 */
public class Album implements Comparable<Album>{
    Document d;
    Double score;    
    float luceneScore;

    @Override
    public int compareTo(Album o) {
        
        Double score_comp = o.score - this.score;
        
        if (this.score == o.score) {
            
            return 0;
        }
        
        else if (o.score > this.score){
        
            return 1;
        }
        
        else {
            
            return -1;
        }
    }
    
   
    public void formulaRanking(Float maxScoreLucene, 
            int max_cant_comentarios) {

        Double ponderacion_lucene = 0.3;
        Double ponderacion_sentimiento = 0.3;
        Double ponderacion_calidad=0.2;
        Double ponderacion_popularidad=0.15;
        Double ponderacion_musicBrainz =0.5;
        Double ranking = 0.0;
        int cant_comentarios = this.cant_comentarios();
        String lista_sentimiento = this.d.get("sentimiento");
        String lista_estrellas = this.d.get("score");
        Float luceneScore_rank = luceneScore(this.luceneScore, maxScoreLucene);
        Double sentimiento = Double.parseDouble(this.d.get("sentProm"));
        Double calidad = estrellas(lista_sentimiento, lista_estrellas);
        Double popularidad = (double)cant_comentarios/ max_cant_comentarios;
        Double musicBrainz = Double.parseDouble(this.d.get("mbRating")) / 5;
        //Double musicBrainz = calidadMB(lista_musicbrai);
        ranking = luceneScore_rank * ponderacion_lucene + sentimiento * ponderacion_sentimiento +
                calidad * ponderacion_calidad + popularidad * ponderacion_popularidad +
                musicBrainz * ponderacion_musicBrainz;        
        
        this.score = ranking;
    }
    
    
    
    public Float luceneScore(Float score, Float max){
    
        return score/max;
    }
    
    public Double estrellas(String lista_sentimiento, String puntaje_estrella){
    
        String[] sentimiento_split = lista_sentimiento.split("##");
        String[] estrellas_split = puntaje_estrella.split("##");
        int cont=0;
        int index=0;
        
        double calidad=0;
        
        for(String sentimiento: sentimiento_split){
        
            double valor_sentimiento = Double.parseDouble(sentimiento);
            double valor_estrella = Double.parseDouble(estrellas_split[index]);
            
            if (valor_sentimiento < 0.2){
                
                if(valor_estrella <=2){
                
                    calidad = calidad + valor_estrella/5;
                    cont++;
                }
            
            
            }
            
            else if ((valor_sentimiento >= 0.2) && (valor_sentimiento < 0.4)){
                
                if((valor_estrella >=1) && valor_estrella <=3){
                
                    calidad = calidad + valor_estrella/5;
                    cont++;
                }
            
            
            }
            
            else if ((valor_sentimiento >= 0.4) && (valor_sentimiento < 0.6)){
                
                if((valor_estrella >=2) && valor_estrella <=4){
                
                    calidad = calidad + valor_estrella/5;
                    cont++;
                }
            
            
            }
            
            else if ((valor_sentimiento >= 0.6) && (valor_sentimiento < 0.8)){
                
                if((valor_estrella >=3) && valor_estrella <=5){
                
                    calidad = calidad + valor_estrella/5;
                    cont++;
                }
            
            
            }
            
            else if ((valor_sentimiento >= 0.8) ){
                
                if((valor_estrella >=3) && valor_estrella <=5){
                
                    calidad = calidad + valor_estrella/5;
                    cont++;
                }
            
            
            }
            
            index++;
        }
        
        if(cont > 0){
            
            return calidad / cont;
        
        }
        
        else{
        
            return 0.5;
        }
        
        
    
    }
    
    public int cant_comentarios(){
    
        
        String[] comentarios = this.d.get("text").split("##");
        return comentarios.length;
        
    }
    
}
