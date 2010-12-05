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
import javafx.scene.chart.part.ValueAxis;
import uta.ConstraintsManager;
import java.util.Date;
import uta.Point;
import javafx.util.Sequences;

/**
 * @author Pawcik
 */
public class ChartUI extends CustomNode {

    def INITIAL_FACTOR_VALUE = 0;
    var showLogs = false;

    public var name:String;
    public-read var chart: LineChart;

    public-init var fun: LinearFunction;
    public var data: ChartUIData;
    
    public-init var xAxisMin:Number=0.0;
    public-init var xAxisMax:Number;
    public-init var yAxisMin:Number=0.0;
    public-init var yAxisMax:Number;

    public-init var xAxisMinAuto:Boolean=false;
    public-init var xAxisMaxAuto:Boolean=false;
    public-init var yAxisMinAuto:Boolean=false;
    public-init var yAxisMaxAuto:Boolean=false;

    public var constraintsManager: ConstraintsManager on replace {
        update();
    }

    var chartX:Number;
    var chartY:Number;

    var factor:Double = INITIAL_FACTOR_VALUE;



//    var calcFactorReady:Boolean = bind chart.visible on replace {
//        if(calcFactorReady and factor==INITIAL_FACTOR_VALUE){
//            if(showLogs) println("Chart is now visible, calculating factor...");
//            factor = getEstimatedScaleFactor(chart.yAxis, chart.insets.top, false);
//            if(showLogs) println("Calculated factor: {factor}");
//            if(factor<0){
//                factor = INITIAL_FACTOR_VALUE;
//            }
//        }
//    };

    

    //var distanceToXAxis:Double;

    init {
        update();
        if(showLogs) println("Updated bounds of {name} function");
    
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
                                //if(showLogs) println("Processing index {i}");
                                cpd = CharPointData {
                                    xValue: bind data.x[i]
                                    yValue: bind data.y[i]
                                    lowerBound: bind (data.lowers[i])
                                    upperBound: bind (data.uppers[i])
                                    index: i
                                    valueUpdated: function(index:Integer, value:Double){
                                        var p = fun.getPoints().get(index);
                                        p.setY(data.y[index]);
                                        constraintsManager.update(fun, p);
                                        data.fireChartDataChanged(i, index, value);
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
                            getEstimatedScaleFactor(chart.xAxis, chart.insets.left, true);
                            factor = getEstimatedScaleFactor(chart.yAxis, chart.insets.top, false);
                        }
                    }
                    onMouseMoved: function(e:MouseEvent){
                        chartX = e.x;
                        chartY = e.y;
                    }
                }
    }

    public function update(){
        for(i in [0..<sizeof data.x]){
            updatePoint(i);
            updateLocalBound(i);
        }
        if(showLogs) println("Updated local bounds of {name}");
    }


    public function updatePoint(index:Integer) {
        var p:Point = fun.getPoints().get(index);
        data.y[index] = p.getY();
    }


    public function updateLocalBound(index:Integer):Void{
          var p:Point = fun.getPoints().get(index);
//          data.lowers[index] -= 0.1;
//          data.uppers[index] += 0.05;
          data.lowers[index] = constraintsManager.getConstraintFor(p).getLowerBound();
          data.uppers[index] = constraintsManager.getConstraintFor(p).getUpperBound();
//        data.lowers = for(p in fun.getPoints()){
//            (constraintsManager.getConstraintFor(p) as Constraint).getLowerBound();
//        }
//        data.uppers = for(p in fun.getPoints()){
//            (constraintsManager.getConstraintFor(p) as Constraint).getUpperBound();
//        }
    }


    override function create(): Node {
        //printNodeBounds("Chart in create()", chart);
        //chart.chartBackgroundStrokeWidth = 50;
        var c:Container = Container{
            content: bind chart
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
            
        }
    }

}

public class ChartUIData {
    public var x: Double[];
    public var y: Double[];
    public var lowers: Double[];
    public var uppers: Double[];

    var chartListeners: ChartListener[];

    public function fireChartDataChanged(funIndex:Integer, pointIndex:Integer, value:Double){
        for(listener in chartListeners){
            listener.dataChanged(
            ChartEvent{
                functionIndex:funIndex
                pointIndex: pointIndex
                newValue:value
            });
        }
    }

    public function addChartListener(listener:ChartListener){
        if(Sequences.indexOf(chartListeners, listener)==-1){
            insert listener into chartListeners;
        }
    }

    public function removeChartListener(listener:ChartListener){
           delete listener from chartListeners;
    }

    public function getChartListeners(): ChartListener[]{
        return chartListeners;
    }
}

package class CharPointData extends LineChart.Data     {
    public  var upperBound: Double;
    public  var lowerBound: Double;
    public-init var index: Integer;

    protected var valueUpdated: function(itemIndex:Integer, value:Double):Void;

    postinit{
        //if(showLogs) println("CharPointData created with index {index}");
    }



}

function printNodeBounds(name:String, node: Node): Void {
    //if(showLogs) println("==== {name} bounds info ====");
    //if(showLogs) println("Node.scene [x,y] = [{node.scene.x}, {node.scene.y}]");
    //if(showLogs) println("Node class: {node.getClass()}");
    //if(showLogs) println("Bounds in local: {node.boundsInLocal}");
    //if(showLogs) println("Layout bounds: {node.layoutBounds}");
    //if(showLogs) println("Bounds in parent: {node.boundsInParent}");
    //if(showLogs) println("Bounds in scene: {node.localToScene(node.boundsInLocal)}\n");
}

package class CharPointSymbol extends CustomNode {

    var rectangle:Rectangle;
    var circle: Node;    
    public-init var item: CharPointData;
    var showLogs = true;

    var x1: Number;
    var y1: Number;
    var x2: Number;
    var y2: Number;
    var delta:Number;

    var circleFill:Paint = Color.RED;
    var rectFill:Paint = Color.GREENYELLOW;

    var valueChanged:Boolean;
    var valueBefore:Double;
    var valueAfter:Double;

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
                            if(showLogs) println("start: [{x1},{y1}]  end: [{x2},{y2}]  delta: {delta}");

                            if(Math.abs(delta)>rectangle.height){
                                return;
                            }

//                            //if(showLogs) println("Circle dragged: {e}");
                            
                            ////if(showLogs) println("Item index: {item.index}");
                            //                            
                            var circleMovedBounds = circle.boundsInParent;
                            var circleMovedX = circleMovedBounds.minX + circleMovedBounds.width/2;
                            var circleMovedY = circleMovedBounds.minY + circleMovedBounds.height/2 + delta;                           
                            
                            if(rectangle.boundsInParent.contains(circleMovedX, circleMovedY)){
                                ////if(showLogs) println("Rectangle contains: [{x2}, {y2}]");
                                delta = y2 - y1;
                                ////if(showLogs) println("Delta: {delta}");
                                if (delta > 0) {
                                    ////if(showLogs) println("We dragged circle down, lowering yValue");
                                    ////if(showLogs) println("Value correction: {factor*delta}");
                                    //item.yValue -= factor * delta;
                                    data.y[item.index]-= factor * delta;
                                } else {
                                    ////if(showLogs) println("We dragged circle up, highering yValue");
                                    ////if(showLogs) println("Value correction: {factor*delta}");
                                    data.y[item.index]-= factor * delta;
                                    //item.yValue -= factor * delta;
                                }                                
                            } else {
                                if(showLogs) println("Dragged out of costraints");
                                if(delta>0){
                                    if(showLogs) println("Setting current value to lowerbound");
                                    data.y[item.index]= item.lowerBound;
                                } else {
                                    if(showLogs) println("Setting current value to upperBound");
                                    data.y[item.index]= item.upperBound;
                                }

                                ////if(showLogs) println("Start[{x1}, {y1}]\nEnd  [{x2}, {y2}]");
                            }                            
                    }

                    onMouseReleased:function( e: MouseEvent) {
                        valueAfter = data.y[item.index];
                        valueChanged = (valueBefore!=valueAfter);
                        if(valueChanged){
                            valueChanged = false;
                            if(showLogs) println("{new Date().toString()} Value changed in function {name}, calling item.valueUpdated()...");
                            item.valueUpdated(item.index, item.yValue);                            
                        }
                    }

                    onMouseEntered:function( e: MouseEvent) {
                        
                    }

                    onMouseExited:function( e: MouseEvent) {
                        ////if(showLogs) println("Circle exited {e}");
                    }

                    onMousePressed:function( e: MouseEvent) {
                        ////if(showLogs) println("Circle {item.index} pressed {e}");
                        //printNodeBounds("Rectangle", rectangle);
                        //printNodeBounds("Circle", circle);
                        valueBefore = data.y[item.index];
                        x1 = e.x;
                        y1 = e.y;
                        //if (factorOld == INITIAL_FACTOR_VALUE) {
                            //distanceToXAxis = chart.xAxis.boundsInParent.minY - (circle.parent.boundsInParent.minY + circle.parent.boundsInParent.height / 2);
                            //factorOld = Math.abs(item.yValue/(1.0 * distanceToXAxis));
                            ////if(showLogs) println("      Old factor: {factorOld}");
                            //factor = getEstimatedScaleFactor(chart.yAxis, false);
                            ////if(showLogs) println("Distance to X axis: {distanceToXAxis}");
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

    //if(showLogs) println("======= Estimated factors =======");
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
        //if(showLogs) println("X Axis width: {width-margin}");
        //if(showLogs) println("Value diff: {valueDiff}");
        f = valueDiff/(width-margin)*1.0;
        //if(showLogs) println("Estimated factor: {f}");
    } else {
        //if(showLogs) println("Y Axis height: {height-margin}");
        //if(showLogs) println("Value diff: {valueDiff}");
        f = valueDiff/(height-margin)*1.0;
        //if(showLogs) println("Estimated factor: {f}");
    }
    return f;
}



function printChartInfo(s:String, chart:LineChart){
    //if(showLogs) println("Chart.width: {chart.width}, Chart.height: {chart.height}");
    //if(showLogs) println("Chart.chartBackgroundStrokeWidth: {chart.chartBackgroundStrokeWidth}");
    //if(showLogs) println("Chart.horizontalGridLineStrokeWidth: {chart.horizontalGridLineStrokeWidth}");
    //if(showLogs) println("Chart.insets: {chart.insets}");
    //if(showLogs) println("Chart.layoutInfo: {chart.layoutInfo}");
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