/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utafx.control;

import uta.Criterion;
import uta.Alternative;
import uta.Ranking;
import java.lang.Exception;

/**
 * @author Pawcik
 */

public class ReferenceLinker {

    /**
        Sets the criterias array on each alternative from second array        
    */
    package function interconnectReferences(criterias: Criterion[], alterns: Alternative[]): Alternative[]{
        for(a in alterns){
            a.setCriteria(criterias);
        }
        return alterns;
    }

    /**
        Converts the given ranking, which must be a String->Number mapping,
        into the Alternative->Number mapping.
    */
    package function interconnectReferences(alterns: Alternative[], refRank: uta.Ranking): uta.Ranking {
        var newRank = new Ranking();
        var index;
        for(name in refRank.getAlternatives()){
             index = getIndexOf(name as String, alterns);
             if(index!=-1){
                 newRank.add(alterns[index], refRank.getRank(name));
             } else {
                 throw new Exception("Cannot find alternative for given name: {name}");
             }
        }
        return newRank;
    }

    function getIndexOf(s:String, alterns:Alternative[]){
        for(a in alterns){
            if(a.getName()==s){
                return indexof a;
            }
        }
        return -1;
    }

}
