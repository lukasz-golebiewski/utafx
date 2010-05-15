/*
 * WebUTA.fx
 *
 * Created on 2010-04-19, 00:41:54
 */
package app.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.jfxtras.scene.XScene;
import org.jfxtras.scene.layout.XVBox;
import org.jfxtras.scene.menu.CheckedMenuItem;
import org.jfxtras.scene.menu.Menu;
import org.jfxtras.scene.menu.MenuItem;
import org.jfxtras.scene.menu.MenuSeparator;
import org.jfxtras.scene.menu.MenuBar;

var scene: Scene;
var buttonDisabled = false;
var menubar: MenuBar;

menubar = MenuBar {
    width: bind scene.width;
    menus: [
        // File Menu
        Menu {
            text: "File"
            items: [
                MenuItem {
                    text: "Import CSV..."
                    action: function () {
                        println("Importing CSV file")
                    }
                    graphic: ImageView {
                        image: Image { url: "{__DIR__}menu_img/Arrow_Up.png"; width:15, height:15}

                    }
                },
                MenuItem {
                    text: "Import UTX..."
                    action: function () {
                        println("Importing UTX file")
                    }
                    graphic: ImageView {
                        image: Image { url: "{__DIR__}menu_img/Arrow_Up.png"; width:12, height:12}

                    }
                },
                MenuSeparator { },
                MenuItem {
                    text: "Exit"
                    action: function () {
                        FX.exit();
                    }
                    graphic: ImageView {
                        image: Image {
                            url: "{__DIR__}menu_img/Door.png";
                            width: 15
                            height: 12
                        }
                    }
                }
            ]
        },
        // Actions Menu
        Menu {
            text: "Actions"
            items: [
                MenuItem {
                    text: "Resolve"
                    action: function () {
                        println("Resolving the UTA problem");
                    }
                }
            ]
        }
        Menu {
            text: "Help"
            items: [
                MenuItem {
                    text: "Help Contents"
                    action: function () {
                        println("help Contents");
                    }
                    graphic: ImageView {
                        image: Image {
                            url: "{__DIR__}menu_img/Help.tiff";
                            width: 12
                            height: 12
                        }
                    }
                },
                MenuItem {
                    text: "About"
                    action: function () {
                        println("{__DIR__}");
                    }
                    graphic: ImageView {
                        image: Image {
                            url: "{__DIR__}menu_img/about.png";
                            width: 15
                            height: 15
                        }
                    }
                }
            ]
        }
    ]
}

Stage {
    title: "MenuBar application"
    width: 800
    height: 200
    scene: scene = XScene {
        menuBar: menubar;
        
        content: Button { translateX: 10, translateY: 10, text: "button" }
    }
}
