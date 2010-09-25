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
import utafx.ui.alternative.AlternativesModel;
import uta.Ranking;
import javafx.scene.paint.Paint;
import javafx.scene.layout.Container;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.chart.part.PlotSymbol;

/**
 * @author Pawcik
 */
public class SolutionUI extends CustomNode {

    public var functions: LinearFunction[];
    public var alternatives: Alternative[];
    public var columnNames: String[];
    public var refRank: Ranking;
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
                                            name: "Characteristic points"
                                            data: for (i in [0..currentFun.getNoOfPoints() - 1]) {
                                                LineChart.Data {
                                                    xValue: currentFun.getCharacteristicPoints()[i];
                                                    yValue: currentFun.getValues()[i];
                                                }
                                            }
                                            symbolCreator: function(series: LineChart.Series, seriesIndex: Integer, item: LineChart.Data, itemIndex: Integer, fill: Paint) {
                                                Container {
                                                    content: [
                                                        Rectangle {
                                                            width: 10
                                                            height: 20
                                                            arcWidth: 10
                                                            arcHeight: 10
                                                            fill: Color.GREENYELLOW
                                                            layoutX: -5;
                                                            layoutY: -10;
                                                        }
                                                        PlotSymbol.Circle {
                                                            fill: fill
                                                            onMouseClicked: function(e: MouseEvent) {
                                                                println("Cirlce clicked: {e}");
                                                            }

                                                            onMouseDragged: function(e: MouseEvent) {
                                                                println("Cirlce clicked: {e}");
                                                            }
                                                            blocksMouse: true
                                                        }
                                                    ]
                                                //                                    onMouseDragged: function(e: MouseEvent) {
                                                //                                        println("Container dragged: {e}");
                                                //                                    }
                                                //                                    onMouseReleased: function(e: MouseEvent) {
                                                //                                        println("Container Released: {e}");
                                                //                                    }
                                                //                                    onMouseClicked: function(e: MouseEvent) {
                                                //                                        println("Container clicked: {e}");
                                                //                                    }
                                                }
                                            } }]
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
