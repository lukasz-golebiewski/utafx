/*
 * data_bind.fx
 *
 * Created on 2010-04-18, 22:43:46
 */
package gui;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.control.Slider;

def slider = Slider{min: 0 max: 60 value: 0 translateX: 10 translateY: 110};


/**
 * @author Pawcik
 */
Stage {
    title: "Data Binding"
    scene: Scene {
        width: 220
        height: 170
        content: [
           slider,
           Circle {
                translateX: bind slider.value
                centerX: 50;
                centerY: 60
                radius: 50
                stroke: Color.YELLOW
                fill: RadialGradient {
                    centerX: 50
                    centerY: 60 radius: 50
                    focusX: 50 focusY: 30
                    proportional: false
                    stops: [
                        Stop { offset: 0 color: Color.RED },
                        Stop { offset: 1 color: Color.WHITE }
                    ]
                }//RadialGradient

            },//Circle
            
        ]
    }
}
    
