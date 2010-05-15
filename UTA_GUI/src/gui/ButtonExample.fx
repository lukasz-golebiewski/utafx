/*
 * ButtonExample.fx
 *
 * Created on 2010-04-18, 21:41:12
 */
package gui;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.LinearGradient; //required to create
import javafx.scene.paint.Stop;           //a linear gradient
import javafx.scene.effect.Reflection;

/**
 * @author Pawcik
 */
Stage {
    title: "Button Example"
    scene: Scene {
        width: 250
        height: 350
        fill: Color.WHITE
        content: [
            Group {
                effect:  Reflection {fraction: 0.5 topOpacity: 0.5 topOffset: 1.0}
                content: [
                    Rectangle {
                        x: 48,
                        y: 100,
                        width: 150,
                        height: 70
                        fill: LinearGradient {
                            startX: 0.0, startY: 1.0, endX: 0.0, endY: 0.0,
                            proportional: true
                            stops: [
                                Stop { offset: 0.0 color: Color.web("#99ddff") },
                                Stop { offset: 1.0 color: Color.web("#337799") }
                            ] //stops

                        } //fill

                    }//Rectangle1

                    //Begin Rectangle 2: bottom part of lower gradient area
                    Rectangle {
                        x: 48,
                        y: 168,
                        width: 150,
                        height: 25
                        fill: LinearGradient {
                            startX: 0.0,
                            startY: 1.0,
                            endX: 0.0,
                            endY: 0.0,
                            proportional: true
                            stops: [
                                Stop {
                                    offset: 0.0
                                    color: Color.web("#337799")
                                },
                                Stop {
                                    offset: 1.0
                                    color: Color.web("#99ddff")
                                }
                            ] //stops

                        } //fill

                    }//Rectangle2

                    // Begin Rectangle 3: upper gradient area
                    Rectangle {
                        x: 48,
                        y: 45,
                        width: 150,
                        height: 60
                        arcWidth: 0, arcHeight: 0, stroke: Color.GREEN
                        fill: LinearGradient {
                            startX: 0.0,
                            startY: 1.0,
                            endX: 0.0,
                            endY: 0.0,
                            proportional: true
                            stops: [
                                Stop {
                                    offset: 0.0
                                    color: Color.web("#aaeeff")
                                },
                                Stop {
                                    offset: 1.0
                                    color: Color.web("#66aacc")
                                }
                            ] //stops

                        } //fill

                    } //Rectangle3

                    //Begin Rectangle 4: stroked outline
                    Rectangle {
                        x: 48,
                        y: 45,
                        width: 150,
                        height: 150
                        arcWidth: 15,
                        arcHeight: 15,
                        stroke: Color.web("#337799")
                        fill: null
                        strokeWidth: 5
                    }, //Rectangle 4
                    Circle {
                        centerX: 122 centerY: 122 radius: 38
                        fill: LinearGradient { startX: 0.0, startY: 1.0, endX: 0.0, endY: 0.0,
                            proportional: true
                            stops: [
                                Stop { offset: 0.0 color: Color.web("#66aacc") },
                                Stop { offset: 1.0 color: Color.web("#99ddff") }
                            ] //stops
                        } //fill
                        stroke: Color.web("#66aacc")
                        strokeWidth: 2.0
                    },
                    //Polygon2: upper triangle
                    Polygon {
                        points: [142.0, 123.0, 110.0, 105.0, 110.0, 140.0]
                        fill: Color.web("#ffffff")
                        effect: DropShadow {
                            color: Color.web("#337799")
                            offsetY: 5
                            offsetX: 2
                        }
                    }//Polygon2
                ]
            }
        ]
    }
}
