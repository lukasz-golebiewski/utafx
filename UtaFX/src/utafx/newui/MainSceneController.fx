/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.newui;

import utafx.ui.alternative.AlternativesUI;
import utafx.ui.criteria.CriteriaUI;
import utafx.ui.rank.ReferenceRankUI;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import utafx.ui.alternative.AlternativesModel;
import utafx.ui.rank.ReferenceRankModel;

/**
 * @author masklin
 */
public class MainSceneController {

    public var criteriaUI: CriteriaUI;
    public var alternativesUI: AlternativesUI;
    public var referenceRankUI: ReferenceRankUI;

    init {
        alternativesUI.model = AlternativesModel {
                    columnNames: bind ["Name", criteriaUI.model.criteriaNames];
                    criteriaPOJO: bind criteriaUI.getPOJO();
                }
        referenceRankUI.model = ReferenceRankModel {
                alternativeNames: bind alternativesUI.model.alternativeNames
            }
                

//        criteriaUI.table.addTableModelListener(TableModelListener {
//            public override function tableChanged(e: TableModelEvent): Void {
//                alternativesUI.update();
//            } });
    }

}
