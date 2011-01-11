/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx;

import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Container;
import javafx.stage.Stage;
import java.lang.String;
import utafx.Constants;
import javafx.scene.layout.LayoutInfo;
import utafx.ui.MainView;
import org.apache.log4j.Logger;

/**
 * @author Pawcik
 */
var scene: Scene;
var APP_PADDING = 2;
var CONTENT_PADDING = 2;

var lazyLoad:Container;

var mainView:MainView;

def LOG: Logger = Logger.getLogger("utafx.Main");

public function run() {
    LOG.info("==============================================");
    LOG.debug("Starting UtaFX");
    Stage {
        title: "UtaFX - UTA methods in JavaFX"
        scene: scene = Scene {
                    width: 1024
                    height: 768
                    stylesheets: [Constants.STYLESHEET_APP, Constants.STYLESHEET_PATH[0]]
                    content: [
                Rectangle{
                    styleClass: "app-background"
                    width: bind scene.width, height: bind scene.height
                },
//                VBox{
//                    layoutX: APP_PADDING, layoutY: APP_PADDING
//                    layoutInfo: LayoutInfo{
//                        width: bind scene.width - APP_PADDING*2;
//                        height: bind scene.height - APP_PADDING*2;
//                    }
//                    spacing: CONTENT_PADDING
//                    content:[
//                        lazyLoad = VBox{
//                            spacing: CONTENT_PADDING
//                            content: [ ]
//                        },
//                    ]
//                }
            ]
        }
    }
    LOG.debug("Creating main view in background thread");
    FX.deferAction(function(){
       mainView = MainView{
            layoutX: APP_PADDING, layoutY: APP_PADDING
                    layoutInfo: LayoutInfo{
                        width: bind scene.width - APP_PADDING*2;
                        height: bind scene.height - APP_PADDING*2;
                    }
       }       
       insert mainView into scene.content;
       LOG.debug("Main view created and added to container");
    });
    LOG.debug("UtaFX started and running");
}