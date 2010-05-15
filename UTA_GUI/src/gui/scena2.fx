/*
 * scena2.fx
 *
 * Created on 2010-04-18, 20:50:00
 */
package gui;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Group;

/**
 * @author Pawcik
 */
Stage {
    title: "Application title"
    scene: Scene {
        width: 220
        height: 170
        fill: Color.LIGHTBLUE
        content: Group {
            translateX: 55
            translateY: 30
            content: [
                Circle {
                    centerX: 50, centerY: 50
                    fill: Color.WHITE
                    stroke: Color.YELLOW
                    radius: 50
                },
                Text {
                    content: "Duke"
                    transforms: Transform.rotate(33, 10, 100)
                },
                ImageView {
                    image: Image {
                        //url: "http://java.sun.com/javafx/1/tutorials/ui/nodes/dukewave.png"
                        url: "{__DIR__}dukewave.png"

                    }
                    x:20, y:20
                }
            ]
        }
    }
}
