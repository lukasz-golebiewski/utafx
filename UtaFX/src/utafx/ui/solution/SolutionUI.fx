/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.ui.solution;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import uta.LinearFunction;
import uta.Alternative;
import utafx.ui.alternative.AlternativesModel;
import uta.Ranking;
import uta.ConstraintsManagerFactory;
import uta.ConstraintsManager;
import uta.Constraint;
import utafx.ui.solution.ChartUI.ChartUIData;

/**
 * @author Pawcik
 */
public class SolutionUI extends CustomNode {

    public var functions: LinearFunction[];
    public var alternatives: Alternative[];
    public var columnNames: String[];
    public var refRank: Ranking;
    public var constraintManager: ConstraintsManager;
    public var freezedKendall:Boolean = false;

    init {
         constraintManager = new ConstraintsManagerFactory(freezedKendall).createConstraintsManager(functions, null, null);
    }


    public override function create(): Node {
        VBox {
            spacing: 10;
            content: [VBox {
                    content: for (f in functions) {
                        var currentFun = f;
                        var c = f.getCriterion();
//                        var chart = ChartUI{
//                            fun: currentFun
//                            name: c.getName()
//                        }
//                        insert chart into charts;
//                        chart;
                        ChartUI {                            
                            data: ChartUIData {
                                x: currentFun.getCharacteristicPoints()
                                y: currentFun.getValues();
                                lowers: for(p in currentFun.getPoints()){
                                    (constraintManager.getConstraintFor(p) as Constraint).getLowerBound();
                                }
                                uppers: for(p in currentFun.getPoints()){
                                    (constraintManager.getConstraintFor(p) as Constraint).getUpperBound();
                                }
                            }
                            xAxisMinAuto: true
                            xAxisMaxAuto: true
                            yAxisMin: 0.0
                            yAxisMax: 1.0                            
                            name: c.getName()
                        }
                     }
                    
                },
                FinalRankUI {
                    alterns: alternatives;
                    functions: functions;
                    refRank: refRank;
                    model: AlternativesModel {
                        columnNames: bind columnNames;
                    }
                }
            ]
        }

    }

}
