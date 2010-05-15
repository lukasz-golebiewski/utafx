/*
 * scena.fx
 *
 * Created on 2010-04-18, 20:19:36
 */

package gui;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * @author Pawcik
 */

Stage {
    title: "JavaFX GUI is Easy!"
    scene: Scene {
        width: 200
        height: 200
        content: [
            Circle {
                centerX: 100
                centerY: 100
                radius: 80
                strokeWidth: 10
                fill: Color.RED
                stroke : Color.INDIANRED
            }   
            Rectangle {
                x: 10, y: 60
                width: 200-20, height: 80
                arcWidth: 15, arcHeight: 15
                fill: Color.web("#6699ff")
                stroke: Color.web("#003399")
                strokeWidth: 2.0
		//opacity: 0.33
            } //Rectangle
        ]
    }

}