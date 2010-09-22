/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.ui.solution;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import uta.LinearFunction;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.part.NumberAxis;
import uta.Alternative;
import utafx.ui.rank.FinalRankUI;
import utafx.ui.alternative.AlternativesModel;

/**
 * @author Pawcik
 */
public class SolutionUI extends CustomNode {

    public var functions: LinearFunction[];
    public var alternatives: Alternative[];
    public var columnNames: String[];
    var charts: LineChart[] = [];

    public override function create(): Node {
        VBox {
            spacing: 10;
            content: [VBox {
                    content: for (f in functions) {
                        var currentFun = f;
                        var c = f.getCriterion();
                            charts[sizeof f] = LineChart {
                                    xAxis: NumberAxis {
                                        lowerBound: if (c.isGain()) c.getWorstValue() else c.getBestValue()
                                        upperBound: if (c.isGain()) c.getBestValue() else c.getWorstValue()
                                        label: "CharPoints"
                                        labelTickGap: 0.3
                                        visible: true
                                        axisStrokeWidth: 1
                                        tickUnit: 1
                                    }
                                    yAxis: NumberAxis {
                                        lowerBound: 0
                                        upperBound: 1
                                        tickUnit: 1
                                        label: "Values"
                                        visible: true
                                    }
                                    data: [
                                        LineChart.Series {
                                            data: for (i in [0..currentFun.getNoOfPoints() - 1]) {
                                                LineChart.Data {
                                                    xValue: currentFun.getCharacteristicPoints()[i];
                                                    yValue: currentFun.getValues()[i];
                                                }
                                            }
                                        }
                                    ]
                                    scaleX: 0.8
                                    scaleY: 0.8
                                    title: currentFun.getCriterion().getName()
                                }
                    }
                },
                FinalRankUI {
                    alterns: alternatives;
                    functions: functions;
                    model: AlternativesModel{
                        columnNames: bind columnNames;
                    }
                }
            ]
        }
    }

}
