/*
 * layouts.fx
 *
 * Created on 2010-04-18, 23:49:35
 */
package gui;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Pawcik
 */
def group = ToggleGroup { };
def choiceText = ["STOP", "READY", "GO"];
def choices = for (ch in choiceText)
            RadioButton {
                toggleGroup: group
                text: ch
            }
var colors = ["RED", "GOLD", "GREEN"];
var lights = for (color in colors)
    Circle {
        centerX: 12
        centerY: 12
        radius: 12
        stroke: Color.GRAY
        fill: bind RadialGradient {
            centerX: 8,
            centerY: 8,
            radius: 10,
            proportional: false
            stops: [
                Stop { offset: 0.0 color: Color.WHITE },
                Stop { offset: 1.0 color: if (choices[indexof color].selected) then Color.web(color) else Color.GRAY
                }//Stop
            ]
        }//RadialGradient

    }

Stage {
    title: "Traffic Lights"
    scene: Scene {
        width: 400
        height: 300
        content: [
            HBox {
                spacing: 20
                content: [
                    VBox {
                       spacing: 10
                       content: choices
                    }
                    HBox {
                        spacing: 15
                        content: lights
                        translateY: 25
                    }
                ]
            }
        ]
    }
}

