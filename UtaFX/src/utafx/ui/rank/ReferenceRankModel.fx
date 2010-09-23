/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utafx.ui.rank;

import javafx.util.Sequences;

/**
 * @author Pawcik
 */

public class ReferenceRankModel {
    public var availNames:String[];
    public var alternativeNames: String[] on replace old[start..end]=newValues{
        for(oldValue in old){
            if(Sequences.indexOf(availNames, oldValue)!=-1){
                delete oldValue from availNames;
            }
        }
    }
}
