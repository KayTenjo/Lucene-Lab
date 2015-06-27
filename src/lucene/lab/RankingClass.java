/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

/**
 *
 * @author Rodrigo
 */
public class RankingClass {
    
    
    
    public Double formulaRanking(Double maxScoreLucene, Double luceneScore, Double sentimiento, 
            String lista_sentimiento, String lista_estrellas, Double max_cant_comentarios, 
            Double cant_comentarios, Double musicBrainz){
    
        float ponderacion_lucene=0;
        float ponderacion_sentimiento=0;
        float ponderacion_calidad=0;
        float ponderacion_popularidad=0;
        float ponderacion_musicBrainz=0;

        Double ranking = 0.0;
        
        luceneScore = luceneScore(luceneScore, maxScoreLucene);
        //Double sentimiento;
        Double calidad = estrellas(lista_sentimiento, lista_estrellas);
        Double popularidad = cant_comentarios/ max_cant_comentarios;
        //Double musicBrainz = calidadMB(lista_musicbrai);

        ranking = luceneScore * ponderacion_lucene + sentimiento * ponderacion_sentimiento +
                calidad * ponderacion_calidad + popularidad * ponderacion_popularidad +
                musicBrainz * ponderacion_musicBrainz;
        
        return ranking;
    
    }
    
    
    
    public Double luceneScore(Double score, Double max){
    
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
        
        
        return calidad / cont;
    
    }
    
}
