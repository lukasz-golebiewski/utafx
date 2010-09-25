/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.ui.solution;

import uta.LinearFunction;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.part.NumberAxis;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Container;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.util.Math;
import javafx.scene.paint.Paint;
import javafx.scene.chart.part.PlotSymbol;
import utafx.ui.solution.ChartUI.CharPointData;

/**
 * @author Pawcik
 */
public class ChartUI extends CustomNode {

    var fun: LinearFunction;
    var chart: LineChart;
    var slider: Rectangle;
    var data: LineChartData;

    init {
        chart = LineChart {
                    xAxis: NumberAxis {
                        lowerBound: getMin(data.x);
                        upperBound: getMax(data.x);
                        label: "CharPoints"
                        labelTickGap: 0.3
                        visible: true
                        axisStrokeWidth: 1
                        tickUnit: 1
                    }
                    yAxis: NumberAxis {
                        lowerBound: getMin(data.y);
                        upperBound: getMax(data.y);
                        tickUnit: 1
                        label: "Values"
                        visible: true
                    }
                    data: [
                        LineChart.Series {
                            name: "Characteristic points"
                            data: for (i in [0..<sizeof data.x]) {
                                CharPointData {
                                    xValue: data.x[i];
                                    yValue: data.y[i];
                                }
                            }
                            symbolCreator: function(series: LineChart.Series, seriesIndex: Integer, item: LineChart.Data, itemIndex: Integer, fill: Paint) {
                                //Rectangle {
//                            width: 10
//                            height: 20
//                            arcWidth: 5
//                            arcHeight: 5
//                            translateX:-5
//                            translateY:-10
//                            fill: Color.CORAL;}
                                Container {
                                    content: [
                                        //                                        PlotSymbol.Square {
                                        //                                            fill: Color.GREEN
                                        //                                            layoutX: -5
                                        //                                            layoutY: -10
                                        //                                        }
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
                            }
                        }
                    ]
                    title: "Some name"
                    onMouseDragged: function(me: MouseEvent): Void {
                    //println(me);
                    }
                    onMouseMoved: function(me: MouseEvent): Void {
                    //println(me);
                    }
                }
    }

    function getMin(array: Number[]): Number {
        var min = array[0];
        for (i in [1..<sizeof array]) {
            min = Math.min(min, array[i]);
        }
        return min;
    }

    function getMax(array: Number[]): Number {
        var max = array[0];
        for (i in [1..<sizeof array]) {
            max = Math.max(max, array[i]);
        }
        return max;
    }

    override function create(): Node {
        Container {
            content: bind chart
        }
    }
}

package class LineChartData {

    public var x: Double[];
    public var y: Double[];
}

function run() {
    Stage {
        width: 300
        height: 300
        scene: Scene {
            var range = 5;
            content: [
                ChartUI {
                    data: LineChartData {
                        x: bind [-range..range]
                        y: for (i in [-range..range])
                            i * i
                    }
                }
            ]
        }
    }
}

package class CharPointData extends LineChart



.Data{
    public var upperBound:Integer;
    public var lowerBound:Integer;
}

