/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene.lab;

import java.util.List;
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
        releases.search(release_name);
        List<ReleaseResultWs2> firstSearchResultPage = releases.getFirstSearchResultPage();
        
        
        for (ReleaseResultWs2 release : firstSearchResultPage){
        
            String title = release.getRelease().getTitle();
            System.out.println(title);
        }
        
       
        
    }
}
