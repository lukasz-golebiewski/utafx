/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.ui.check;

import utafx.ui.criteria.CriteriaModel;
import utafx.ui.alternative.AlternativesModel;
import utafx.ui.rank.ReferenceRankModel;
import javafx.util.Sequences;
import java.lang.NumberFormatException;
import java.util.logging.Logger;

/**
 * @author LG
 */
public class ModelCheck {

    public function doCheck(criteriaModel: CriteriaModel, alternativesModel: AlternativesModel, refRankModel: ReferenceRankModel): Boolean {
        var result = true;
        result = result and checkCriteria(criteriaModel);
        result = result and checkAlternatives(alternativesModel);
        result = result and checkReferenceRank(refRankModel);
        if(result == false) {
            Logger.getLogger(this.getClass().getName()).info("Warning, model is inconsistent");
        }

        return result;
    }

    function checkCriteria(criteriaModel: CriteriaModel): Boolean {
        var allNames: String[];
        for (row in criteriaModel.rows) {
            var name = row.cells.get(0).text;
            var type = row.cells.get(1).text;
            var segmentsString = row.cells.get(2).text;

            if (Sequences.indexOf(allNames, name) >= 0) {
                return false;
            }
            insert name into allNames;

            try {
                var parsedSegmentsString = Integer.parseInt(segmentsString);
                if (parsedSegmentsString < 1) {
                    return false;
                }
            } catch (ex : NumberFormatException) {
                return false;
            }
        }
        return true;
    }

    function checkAlternatives(alternativesModel: AlternativesModel): Boolean {
        var allNames: String[];
        for (row in alternativesModel.rows) {
            var altName = row.cells.get(0).text;

            if (Sequences.indexOf(allNames, altName) >= 0) {
                return false;
            }
            insert altName into allNames;

            for (cell in row.cells) {
                var i = indexof cell;
                if (i > 0) {
                    try {
                        var parsedValue = Double.parseDouble(cell.text);
                    } catch (ex : NumberFormatException) {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    function checkReferenceRank(refRankModel: ReferenceRankModel): Boolean {
        if(refRankModel.getPOJO().getAlternatives().size() < 2)
            return false;
        return true;
    }

}
