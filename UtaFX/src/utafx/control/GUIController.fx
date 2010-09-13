/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.control;

import utafx.ui.AlternativesUI;
import utafx.ui.CriteriaUI;
import utafx.ui.MainView;
import utafx.ui.ReferenceRankUI;

/**
 * @author Pawcik
 */
public class GUIController {

    public var view: MainView;

    public function createAlternatives(criteria: uta.Criterion[]): AlternativesUI {
        AlternativesUI {
            criteriaPOJO: bind criteria
        }
    }

    public function createCriteria(): CriteriaUI {
        CriteriaUI {}
    }

    public function createReferenceRank(alternatives: uta.Alternative[]): ReferenceRankUI {
        ReferenceRankUI {
            allItems: bind alternatives;
        }
    }
}
