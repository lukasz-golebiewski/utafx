/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.control;

import uta.api.Criterion;
import uta.api.Alternative;
import uta.api.Ranking;
import java.lang.Exception;

/**
 * @author Pawcik
 */
public class ReferenceLinker {

    /**
    Sets the criterias array on each alternative from second array
     */
    public function interconnectReferences(criterias: Criterion[], alterns: Alternative[]): Alternative[] {
        for (a in alterns) {
            a.setCriteria(criterias);
        }
        for (criterion in criterias) {
            var maxValue = Double.MIN_VALUE;
            var minValue = Double.MAX_VALUE;
            for (alt in alterns) {
                var val = alt.getValueOn(criterion);
                if (val > maxValue) {
                    maxValue = val;
                }
                if (val < minValue) {
                    minValue = val;
                }
            }
            if (criterion.isGain()) {
                criterion.setBestValue(maxValue);
                criterion.setWorstValue(minValue);
            } else {
                criterion.setBestValue(minValue);
                criterion.setWorstValue(maxValue);
            }
        }
        return alterns;
    }

    /**
    Converts the given ranking, which must be a String->Number mapping,
    into the Alternative->Number mapping.
     */
    public function interconnectReferences(alterns: Alternative[], refRank: uta.api.Ranking): uta.api.Ranking {
        var newRank = new Ranking();
        var index;
        for (name in refRank.getAlternatives()) {
            index = getIndexOf(name as String, alterns);
            if (index != -1) {
                newRank.add(alterns[index], refRank.getRank(name));
            } else {
                throw new Exception("Cannot find alternative for given name: {name}");
            }
        }
        return newRank;
    }

    function getIndexOf(s: String, alterns: Alternative[]) {
        for (a in alterns) {
            if (a.getName() == s) {
                return indexof a;
            }
        }
        return -1;
    }

}
