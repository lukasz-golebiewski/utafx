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
import javafx.scene.layout.Container;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.util.Math;
import javafx.scene.paint.Paint;
import javafx.scene.chart.part.PlotSymbol;
import javafx.scene.control.Button;
import javafx.scene.chart.part.ValueAxis;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.Line;

/**
 * @author Pawcik
 */
public class ChartUI extends CustomNode {

    def INITIAL_FACTOR_VALUE = 0;
    public var name:String;
    public-read var chart: LineChart;

    public-init var fun: LinearFunction;
    public-init var data: ChartUIData;
    
    public-init var xAxisMin:Number=0.0;
    public-init var xAxisMax:Number;
    public-init var yAxisMin:Number=0.0;
    public-init var yAxisMax:Number;

    public-init var xAxisMinAuto:Boolean=false;
    public-init var xAxisMaxAuto:Boolean=false;
    public-init var yAxisMinAuto:Boolean=false;
    public-init var yAxisMaxAuto:Boolean=false;

    var chartX:Number;
    var chartY:Number;

    var factorOld:Double = INITIAL_FACTOR_VALUE;
    var factor:Double = INITIAL_FACTOR_VALUE;
    var distanceToXAxis:Double;

    init {

//        if(fun != null){
//            data.x = fun.getCharacteristicPoints();
//            data.y = fun.getValues();
//        }
    
        chart = LineChart {

                    xAxis: NumberAxis {
                        lowerBound: bind if(xAxisMinAuto) getMin(data.x) else xAxisMin;
                        upperBound: bind if(xAxisMaxAuto) getMax(data.x) else xAxisMax;
                        label: "CharPoints"
                        labelTickGap: 0.3
                        visible: true
                        axisStrokeWidth: 1
                        tickUnit: bind (chart.xAxis.upperBound - chart.xAxis.lowerBound) / (sizeof data.x - 1)
                    }
                    yAxis: NumberAxis {
                        lowerBound: bind if(yAxisMinAuto) getMin(data.y) else yAxisMin;
                        upperBound: bind if(yAxisMaxAuto) getMax(data.y) else yAxisMax;
                        tickUnit: bind (chart.yAxis.upperBound - chart.yAxis.lowerBound) / (sizeof data.x - 1)
                        label: "Values"
                        //tickMarkLength: 10
                        visible: true
                    }
                    data: [
                        LineChart.Series {
                            var cpd:CharPointData;
                            var symbol: CharPointSymbol;
                            name: "Characteristic points"
                            data: for (i in [0..<sizeof data.x]) {
                                //println("Processing index {i}");
                                cpd = CharPointData {
                                    xValue: bind data.x[i]
                                    yValue: bind data.y[i]
                                    lowerBound: bind (data.lowers[i])
                                    upperBound: bind (data.uppers[i])
                                    index: i
                                    valueUpdated: function(){
                                        println("Data changed in item {i} on chart {name}");
                                    }
                                }
                                symbol = CharPointSymbol{
                                    item:cpd;
                                }
                                cpd.symbol=symbol;
                                cpd;
                            }
                            fill: Color.RED;                            
                        }
                    ]
                    title: bind name;
                    onMouseEntered: function(e:MouseEvent){
                        if(factor == INITIAL_FACTOR_VALUE){
                            getEstimatedScaleFactor(chart.xAxis, chart.insets.top, true);
                            factor = getEstimatedScaleFactor(chart.yAxis, chart.insets.top, false);
                        }
                    }
                    onMouseMoved: function(e:MouseEvent){
                        chartX = e.x;
                        chartY = e.y;
                    }
                }
    }    

    override function create(): Node {
        //printNodeBounds("Chart in create()", chart);
        //chart.chartBackgroundStrokeWidth = 50;
        var c:Container = Container{
            content:[
                chart,
//                Label{
//                    text: bind "X: {chartX} Y: {chartY}";
//                    layoutX: 30
//                    layoutY: 400
//                },
//                Button{
//                    text: "Draw X axis min/max"
//                    action: function(){
//
//                        var X = chart.xAxis;
//                        var Y = chart.yAxis;
//                        var xBoundsInScene = X.localToScene(X.boundsInLocal);
//                        var yBoundsInScene = Y.localToScene(Y.boundsInLocal);
//
//                        insert Circle {
//                            centerX: yBoundsInScene.maxX;
//                            centerY: xBoundsInScene.minX;
//                            fill: Color.DARKBLUE;
//                            radius:3
//                        } into c.content;
//                        insert Circle {
//                            centerX: chart.xAxis.boundsInParent.maxX;
//                            centerY: chart.xAxis.boundsInParent.maxY;
//                            fill: Color.DARKBLUE;
//                            radius:3
//                        } into c.content;
//                        insert Circle {
//                            centerX: chart.yAxis.boundsInParent.minX;
//                            centerY: chart.yAxis.boundsInParent.minY;
//                            fill: Color.ORANGE;
//                            radius:3
//                        } into c.content;
//                        insert Circle {
//                            centerX: chart.yAxis.boundsInParent.maxX;
//                            centerY: chart.yAxis.boundsInParent.maxY;
//                            fill: Color.ORANGE;
//                            radius:3
//                        } into c.content;
//
//                    }
//                    layoutX: 30
//                    layoutY: 450
//                }

            ]
        }
    }

}

package class ChartUIData {
    public var x: Double[];
    public var y: Double[];
    public var lowers: Double[];
    public var uppers: Double[];
}

package class CharPointData extends LineChart.Data     {
    public  var upperBound: Double;
    public  var lowerBound: Double;
    public-init var index: Integer;

    protected var valueUpdated: function():Void;

    postinit{
        //println("CharPointData created with index {index}");
    }



}

function printNodeBounds(name:String, node: Node): Void {
    //println("==== {name} bounds info ====");
    //println("Node.scene [x,y] = [{node.scene.x}, {node.scene.y}]");
    //println("Node class: {node.getClass()}");
    //println("Bounds in local: {node.boundsInLocal}");
    //println("Layout bounds: {node.layoutBounds}");
    //println("Bounds in parent: {node.boundsInParent}");
    //println("Bounds in scene: {node.localToScene(node.boundsInLocal)}\n");
}

package class CharPointSymbol extends CustomNode {

    var rectangle:Rectangle;
    var circle: Node;    
    public-init var item: CharPointData;

    var x1: Number;
    var y1: Number;
    var x2: Number;
    var y2: Number;
    var delta:Number;

    var circleFill:Paint = Color.RED;
    var rectFill:Paint = Color.GREENYELLOW;

    var valueChanged:Boolean;

    override function create():Node{

        Container {
            content: [
                rectangle = Rectangle {
                    width: 10
                    height: bind (item.upperBound-item.lowerBound)/factor*1.0;
                    arcWidth: 10
                    arcHeight: 10
                    fill: bind rectFill;
                    translateX: bind -1.0 * rectangle.width / 2;
                    translateY: bind if(item.upperBound>data.y[item.index]){
                        -1.0 * rectangle.height * (item.upperBound-data.y[item.index])/(item.upperBound-item.lowerBound);
                    } else {
                       0.0;
                    }
                },
                circle = PlotSymbol.Circle {
                    fill: bind circleFill;

                    onMouseDragged: function(e: MouseEvent) {
                            x2 = e.x;
                            y2 = e.y;
                            delta = y2 - y1;
//                            //println("Circle dragged: {e}");
//                            //println("Current position: [{x2},{y2}]");
                            ////println("Item index: {item.index}");
                            //                            
                            var circleMovedBounds = circle.boundsInParent;
                            var circleMovedX = circleMovedBounds.minX + circleMovedBounds.width/2;
                            var circleMovedY = circleMovedBounds.minY + circleMovedBounds.height/2 + delta;

                            var valueBefore = data.y[item.index];
                            var valueAfter = valueBefore;
                            
                            if(rectangle.boundsInParent.contains(circleMovedX, circleMovedY)){
                                ////println("Rectangle contains: [{x2}, {y2}]");
                                delta = y2 - y1;
                                ////println("Delta: {delta}");
                                if (delta > 0) {
                                    ////println("We dragged circle down, lowering yValue");
                                    ////println("Value correction: {factor*delta}");
                                    //item.yValue -= factor * delta;
                                    data.y[item.index]-= factor * delta;
                                } else {
                                    ////println("We dragged circle up, highering yValue");
                                    ////println("Value correction: {factor*delta}");
                                    data.y[item.index]-= factor * delta;
                                    //item.yValue -= factor * delta;
                                }                                
                            } else {

                                ////println("Dragged out of costraints");
                                if(delta>0){
                                    data.y[item.index]= item.lowerBound;
                                } else {
                                    data.y[item.index]= item.upperBound;
                                }

                                ////println("Start[{x1}, {y1}]\nEnd  [{x2}, {y2}]");
                            }
                            valueAfter = data.y[item.index];
                            valueChanged = (valueBefore!=valueAfter);

                    }

                    onMouseReleased:function( e: MouseEvent) {
                        if(valueChanged){
                            item.valueUpdated()
                        }
                    }

                    onMouseEntered:function( e: MouseEvent) {                        
                    }

                    onMouseExited:function( e: MouseEvent) {
                        ////println("Circle exited {e}");
                    }

                    onMousePressed:function( e: MouseEvent) {
                        ////println("Circle {item.index} pressed {e}");
                        //printNodeBounds("Rectangle", rectangle);
                        //printNodeBounds("Circle", circle);
                        x1 = e.x;
                        y1 = e.y;
                        //if (factorOld == INITIAL_FACTOR_VALUE) {
                            //distanceToXAxis = chart.xAxis.boundsInParent.minY - (circle.parent.boundsInParent.minY + circle.parent.boundsInParent.height / 2);
                            //factorOld = Math.abs(item.yValue/(1.0 * distanceToXAxis));
                            ////println("      Old factor: {factorOld}");
                            //factor = getEstimatedScaleFactor(chart.yAxis, false);
                            ////println("Distance to X axis: {distanceToXAxis}");
                            //printNodeBounds("Symbol (circle+rectangle)", this);
                            //printNodeBounds("Y Axis", chart.yAxis);
                        //}
                        
                    }
               }                    
            ]
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

function printEstimatedScaleFactors(c:ChartUI){

    //println("======= Estimated factors =======");
    getEstimatedScaleFactor(c.chart.xAxis, c.chart.insets.left, true);
    getEstimatedScaleFactor(c.chart.yAxis, c.chart.insets.top, false);
}

function getEstimatedScaleFactor(va:ValueAxis, margin:Number, xAxis:Boolean): Number{
    var s = if(xAxis) " X Axis" else "Y axis";
    printNodeBounds("{s}", va);

    var width = va.boundsInLocal.width;
    var height = va.boundsInLocal.height;
    var minValue = va.lowerBound;
    var maxValue = va.upperBound;

    var valueDiff = maxValue - minValue;
    var f:Number;
    if(xAxis){
        //println("X Axis width: {width-margin}");
        //println("Value diff: {valueDiff}");
        f = valueDiff/(width-margin)*1.0;
        //println("Estimated factor: {f}");
    } else {
        //println("Y Axis height: {height-margin}");
        //println("Value diff: {valueDiff}");
        f = valueDiff/(height-margin)*1.0;
        //println("Estimated factor: {f}");
    }
    return f;
}



function printChartInfo(s:String, chart:LineChart){
    //println("Chart.width: {chart.width}, Chart.height: {chart.height}");
    //println("Chart.chartBackgroundStrokeWidth: {chart.chartBackgroundStrokeWidth}");
    //println("Chart.horizontalGridLineStrokeWidth: {chart.horizontalGridLineStrokeWidth}");
    //println("Chart.insets: {chart.insets}");
    //println("Chart.layoutInfo: {chart.layoutInfo}");
    printNodeBounds("Chart", chart);
    printNodeBounds("Chart.xAxis", chart.xAxis);
    printNodeBounds("Chart.yAxis", chart.yAxis);
}


//For testing purpose
function run() {
    Stage {
        width: 600
        height: 600
        scene: Scene {
            var range = 8;
            var c:ChartUI = ChartUI {
                    data: ChartUIData {
//                        x: bind [-range..range]
//                        y: for (i in [-range..range]) i * i
                          x: [21, 9, 5];
                          y: [0, 0.4, 0.6];
                          lowers: [0, 0, 0.4]
                          uppers: [0, 0.6, 0.7]
                    }
                    xAxisMinAuto: true;
                    xAxisMaxAuto: true;
                    yAxisMin: 0;
                    yAxisMax: 1;
            }

            content: [
                c,
//                Button{
//                    text: "Chart Bounds"
//                    action: function(){
//                        printChartInfo("LineChart", c.chart);
//                        printEstimatedScaleFactors(c);
//                    }
//                }
            ]
        }
    }
}