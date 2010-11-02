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
import uta.ConstraintsManager;
import utafx.ui.solution.ChartEvent;
import utafx.ui.solution.ChartUI.ChartUIData;
import utafx.control.GUIController;

/**
 * @author Pawcik
 */
public class SolutionUI extends CustomNode {

    public var functions: LinearFunction[];
    public var alternatives: Alternative[];
    public var columnNames: String[];
    public var constraintManager: ConstraintsManager;
    public var freezedKendall:Boolean = false;
    public var charts: ChartUI[];
    public var refRank: Ranking;
    public var guiController: GUIController;

    var showLogs = false;

    public-read var finalRank: FinalRankUI;

    public override function create(): Node {
        VBox {
            spacing: 10;
            content: [VBox {
                    content: for(f in functions) {
                        var currentFun = f;
                        var c = f.getCriterion();
                        var chartUI: ChartUI;
                        var chartData = ChartUIData {
                                x: currentFun.getCharacteristicPoints()
                                y: currentFun.getValues();
                        }
                        chartData.addChartListener(ChartListener{
                           override public function dataChanged (e : ChartEvent) : Void {
                               //update bounds of all charts
                               if (showLogs) println("Updating bounds of {sizeof charts}");
                               for(chart in charts){
                                    chart.update();                                    
                               }
                           }
                        });
                        chartData.addChartListener(ChartListener{
                           override public function dataChanged (e : ChartEvent) : Void {
                               //update reference rank od final rank
                               if (showLogs) println("Updating bounds of {sizeof charts} charts");
                               finalRank.refRank = guiController.getReferenceRankData();
                           }
                        });
                        //insert chartData into charts;
                        chartUI = ChartUI {
                            fun: currentFun;
                            data: bind chartData;
                            xAxisMinAuto: true
                            xAxisMaxAuto: true
                            yAxisMin: 0.0
                            yAxisMax: 1.0                            
                            name: c.getName()
                            constraintsManager: bind constraintManager;                            
                        }
                        insert chartUI into charts;
                        chartUI;
                     }                    
                },
                finalRank = FinalRankUI {
                    alterns: bind alternatives;
                    functions: bind functions;
                    refRank: refRank;
                    model: AlternativesModel {
                        columnNames: bind ["Name", columnNames, "Utillity"]
                    }
                }
            ]
        }
    }
}
