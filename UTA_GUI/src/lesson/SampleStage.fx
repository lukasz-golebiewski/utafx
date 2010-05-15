/*
 * SampleStage.fx
 *
 * Created on 2010-04-18, 13:52:03
 */
package lesson;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * @author Pawcik
 */
def imageViews = for (i in [0..9]) {
            Image { url: "{__DIR__}images/{i}.png" }
        };
var count = 0;
var currImg = imageViews[count] on replace oldValue {
    if(count < 10){
        if (count == 9){println("Max count ({count}) reached.");}
    }else{
        println("\tWarning: count has exceeded 9! ");
        println("\tThis would cause the screen to go black.");
        println("\tI'll roll back the change (which will fire the trigger again)....");
        count = 9;
        currImg = oldValue;
        println("Done. The counter should look OK now.");
    }
};


Stage {
    title: "Application title"
    scene: Scene {
        content: ImageView {
            image: bind currImg
            onMouseClicked: function (e: MouseEvent) {
                currImg = imageViews[++count mod (sizeof imageViews)];
            }
        }
    }
}
