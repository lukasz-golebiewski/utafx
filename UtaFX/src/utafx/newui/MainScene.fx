/*
 * MainScene.fx
 *
 * Created on Nov 30, 2010, 10:58:34 PM
 */
package utafx.newui;

import java.lang.System;

/**
 * @author LG
 */
public class MainScene {

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:main
    def __layoutInfo_indexPreviousButton: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        hfill: true
    }
    public-read def indexPreviousButton: javafx.scene.control.Button = javafx.scene.control.Button {
        disable: bind currentState.isFirst()
        layoutInfo: __layoutInfo_indexPreviousButton
        text: "Previous"
        action: function ():Void { currentState.previous(); }
    }
    
    def __layoutInfo_indexNextButton: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        hfill: true
    }
    public-read def indexNextButton: javafx.scene.control.Button = javafx.scene.control.Button {
        disable: bind currentState.isLast()
        layoutInfo: __layoutInfo_indexNextButton
        text: "Next"
        action: function ():Void { currentState.next(); }
    }
    
    def __layoutInfo_tile: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
    }
    public-read def tile: javafx.scene.layout.Tile = javafx.scene.layout.Tile {
        visible: true
        managed: true
        layoutX: 417.0
        layoutY: 729.0
        layoutInfo: __layoutInfo_tile
        content: [ indexPreviousButton, indexNextButton, ]
        columns: 2
        hgap: 6.0
        vgap: 6.0
        autoSizeTiles: true
        hpos: javafx.geometry.HPos.CENTER
        vpos: javafx.geometry.VPos.PAGE_END
        nodeVPos: javafx.geometry.VPos.BOTTOM
    }
    
    def __layoutInfo_flow: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 978.0
        height: 55.0
    }
    public-read def flow: javafx.scene.layout.Flow = javafx.scene.layout.Flow {
        layoutX: 25.0
        layoutY: 686.0
        layoutInfo: __layoutInfo_flow
        content: [ tile, ]
        hgap: 6.0
        vgap: 6.0
        hpos: javafx.geometry.HPos.CENTER
        vpos: javafx.geometry.VPos.CENTER
    }
    
    def __layoutInfo_criteriaUI: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
    }
    public-read def criteriaUI: utafx.ui.criteria.CriteriaUI = utafx.ui.criteria.CriteriaUI {
        visible: false
        layoutInfo: __layoutInfo_criteriaUI
    }
    
    def __layoutInfo_referenceRankUI: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
    }
    public-read def referenceRankUI: utafx.ui.rank.ReferenceRankUI = utafx.ui.rank.ReferenceRankUI {
        visible: false
        layoutX: 55.0
        layoutY: 485.0
        layoutInfo: __layoutInfo_referenceRankUI
    }
    
    def __layoutInfo_criteriaAndRefRankFlow: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
    }
    public-read def criteriaAndRefRankFlow: javafx.scene.layout.Flow = javafx.scene.layout.Flow {
        visible: false
        layoutInfo: __layoutInfo_criteriaAndRefRankFlow
        content: [ criteriaUI, referenceRankUI, ]
        hgap: 6.0
        vgap: 6.0
    }
    
    public-read def alternativesUI: utafx.ui.alternative.AlternativesUI = utafx.ui.alternative.AlternativesUI {
        visible: false
        layoutX: 511.0
        layoutY: 32.0
    }
    
    public-read def finalRankUI: utafx.ui.solution.FinalRankUI = utafx.ui.solution.FinalRankUI {
        visible: false
        layoutX: 274.0
        layoutY: 322.0
    }
    
    public-read def chartsHBox: javafx.scene.layout.HBox = javafx.scene.layout.HBox {
        visible: false
        spacing: 6.0
    }
    
    def __layoutInfo_chartsScrollView: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
    }
    public-read def chartsScrollView: javafx.scene.control.ScrollView = javafx.scene.control.ScrollView {
        visible: false
        layoutInfo: __layoutInfo_chartsScrollView
        node: chartsHBox
    }
    
    public-read def importMenuItem: com.javafx.preview.control.MenuItem = com.javafx.preview.control.MenuItem {
        text: "Import"
    }
    
    public-read def exportMenuItem: com.javafx.preview.control.MenuItem = com.javafx.preview.control.MenuItem {
        text: "Export"
    }
    
    public-read def separator: javafx.scene.control.Separator = javafx.scene.control.Separator {
    }
    
    public-read def exitMenuItem: com.javafx.preview.control.MenuItem = com.javafx.preview.control.MenuItem {
        onMouseClicked: exitMenuItemOnMouseClicked
        text: "Exit"
    }
    
    public-read def fileMenu: com.javafx.preview.control.Menu = com.javafx.preview.control.Menu {
        text: "File"
        items: [ importMenuItem, exportMenuItem, separator, exitMenuItem, ]
    }
    
    public-read def utaImplMenuItem: com.javafx.preview.control.MenuItem = com.javafx.preview.control.MenuItem {
        visible: false
        onMouseClicked: utaImplMenuItemOnMouseClicked
        text: "Choose solver"
    }
    
    public-read def toolsMenu: com.javafx.preview.control.Menu = com.javafx.preview.control.Menu {
        text: "Edit"
        items: [ utaImplMenuItem, ]
    }
    
    public-read def aboutMenuItem: com.javafx.preview.control.MenuItem = com.javafx.preview.control.MenuItem {
        text: "About"
    }
    
    public-read def helpMenu: com.javafx.preview.control.Menu = com.javafx.preview.control.Menu {
        text: "Help"
        items: [ aboutMenuItem, ]
    }
    
    public-read def menuBar: com.javafx.preview.control.MenuBar = com.javafx.preview.control.MenuBar {
        visible: true
        menus: [ fileMenu, toolsMenu, helpMenu, ]
    }
    
    public-read def font: javafx.scene.text.Font = javafx.scene.text.Font {
        size: 22.0
        oblique: false
        embolden: false
        position: javafx.scene.text.FontPosition.REGULAR
    }
    
    def __layoutInfo_label: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
        width: 199.0
        height: 38.0
    }
    public-read def label: javafx.scene.control.Label = javafx.scene.control.Label {
        visible: true
        layoutX: 415.0
        layoutY: -66.0
        layoutInfo: __layoutInfo_label
        text: "Welcome to UtaFX"
        font: font
    }
    
    public-read def scene: javafx.scene.Scene = javafx.scene.Scene {
        width: 1024.0
        height: 768.0
        content: getDesignRootNodes ()
        camera: null
        cursor: null
        fill: null
    }
    
    public-read def currentState: org.netbeans.javafx.design.DesignState = org.netbeans.javafx.design.DesignState {
        names: [ "State1", "State 2", "State 3", ]
        onTransitionFinished: onNextState
        actual: 0
        timelines: [
            javafx.animation.Timeline {
                keyFrames: [
                    javafx.animation.KeyFrame {
                        time: 0ms
                        action: function() {
                            indexNextButton.text = "Next";
                            tile.visible = true;
                            tile.layoutX = 323.0;
                            tile.layoutY = 717.0;
                            __layoutInfo_tile.height = 20.0;
                            flow.managed = true;
                            criteriaUI.visible = false;
                            criteriaUI.managed = true;
                            referenceRankUI.visible = false;
                            referenceRankUI.layoutX = 55.0;
                            referenceRankUI.layoutY = 485.0;
                            criteriaAndRefRankFlow.visible = false;
                            criteriaAndRefRankFlow.layoutX = 0.0;
                            criteriaAndRefRankFlow.layoutY = 0.0;
                            __layoutInfo_criteriaAndRefRankFlow.width = 389.0;
                            __layoutInfo_criteriaAndRefRankFlow.height = 684.0;
                            criteriaAndRefRankFlow.vertical = false;
                            alternativesUI.visible = false;
                            finalRankUI.visible = false;
                            finalRankUI.layoutX = 274.0;
                            finalRankUI.layoutY = 322.0;
                            chartsHBox.visible = false;
                            chartsScrollView.visible = false;
                            chartsScrollView.disable = false;
                            chartsScrollView.opacity = 1.0;
                            chartsScrollView.layoutX = 0.0;
                            chartsScrollView.layoutY = 0.0;
                            chartsScrollView.hbarPolicy = javafx.scene.control.ScrollBarPolicy.AS_NEEDED;
                            chartsScrollView.vbarPolicy = javafx.scene.control.ScrollBarPolicy.AS_NEEDED;
                            chartsScrollView.pannable = false;
                            label.layoutX = 419.0;
                            label.layoutY = 208.0;
                            importMenuItem.disable = true;
                            importMenuItem.onKeyPressed = null;
                            importMenuItem.onMouseClicked = null;
                            importMenuItem.text = "Import";
                            exportMenuItem.disable = true;
                            exportMenuItem.text = "Export";
                            exitMenuItem.onMouseClicked = exitMenuItemOnMouseClicked;
                            fileMenu.visible = true;
                            fileMenu.disable = false;
                            utaImplMenuItem.visible = false;
                            utaImplMenuItem.disable = false;
                            utaImplMenuItem.text = "Choose solver";
                            utaImplMenuItem.font = null;
                            toolsMenu.disable = true;
                            toolsMenu.text = "Tools";
                            toolsMenu.font = null;
                            toolsMenu.accelerator = null;
                            helpMenu.text = "Help";
                            menuBar.visible = true;
                            scene.camera = null;
                            scene.cursor = null;
                        }
                    }
                ]
            }
            javafx.animation.Timeline {
                keyFrames: [
                    javafx.animation.KeyFrame {
                        time: 0ms
                        action: function() {
                            indexNextButton.text = "Solve";
                            tile.visible = true;
                            tile.layoutX = 417.0;
                            tile.layoutY = 729.0;
                            flow.managed = true;
                            criteriaUI.visible = true;
                            criteriaUI.managed = true;
                            __layoutInfo_criteriaUI.hpos = javafx.geometry.HPos.LEFT;
                            __layoutInfo_criteriaUI.vpos = javafx.geometry.VPos.TOP;
                            referenceRankUI.visible = true;
                            referenceRankUI.layoutX = 0.0;
                            referenceRankUI.layoutY = 281.0;
                            __layoutInfo_referenceRankUI.hpos = javafx.geometry.HPos.LEFT;
                            __layoutInfo_referenceRankUI.vpos = javafx.geometry.VPos.BOTTOM;
                            criteriaAndRefRankFlow.visible = true;
                            criteriaAndRefRankFlow.layoutX = 18.0;
                            criteriaAndRefRankFlow.layoutY = 32.0;
                            __layoutInfo_criteriaAndRefRankFlow.width = 447.0;
                            __layoutInfo_criteriaAndRefRankFlow.height = 641.0;
                            criteriaAndRefRankFlow.vertical = true;
                            alternativesUI.visible = true;
                            finalRankUI.visible = false;
                            finalRankUI.layoutX = 274.0;
                            finalRankUI.layoutY = 322.0;
                            chartsHBox.visible = false;
                            chartsScrollView.visible = false;
                            chartsScrollView.disable = false;
                            chartsScrollView.opacity = 1.0;
                            chartsScrollView.layoutX = 0.0;
                            chartsScrollView.layoutY = 0.0;
                            chartsScrollView.hbarPolicy = javafx.scene.control.ScrollBarPolicy.AS_NEEDED;
                            chartsScrollView.vbarPolicy = javafx.scene.control.ScrollBarPolicy.AS_NEEDED;
                            chartsScrollView.pannable = false;
                            label.layoutX = 415.0;
                            label.layoutY = -66.0;
                            importMenuItem.disable = false;
                            importMenuItem.onKeyPressed = null;
                            importMenuItem.onMouseClicked = importMenuItemOnMouseClicked;
                            importMenuItem.text = "Import";
                            exportMenuItem.disable = false;
                            exportMenuItem.text = "Export";
                            exitMenuItem.onMouseClicked = exitMenuItemOnMouseClicked;
                            fileMenu.visible = true;
                            fileMenu.disable = false;
                            utaImplMenuItem.visible = true;
                            utaImplMenuItem.disable = false;
                            utaImplMenuItem.text = "Choose solver";
                            utaImplMenuItem.font = null;
                            toolsMenu.disable = false;
                            toolsMenu.text = "Tools";
                            toolsMenu.font = null;
                            toolsMenu.accelerator = null;
                            helpMenu.text = "Help";
                            menuBar.visible = true;
                            scene.camera = null;
                            scene.cursor = null;
                        }
                    }
                ]
            }
            javafx.animation.Timeline {
                keyFrames: [
                    javafx.animation.KeyFrame {
                        time: 0ms
                        action: function() {
                            indexNextButton.text = "Next";
                            tile.visible = true;
                            tile.layoutX = 417.0;
                            tile.layoutY = 729.0;
                            flow.managed = true;
                            criteriaUI.visible = false;
                            criteriaUI.managed = true;
                            referenceRankUI.visible = false;
                            referenceRankUI.layoutX = 55.0;
                            referenceRankUI.layoutY = 485.0;
                            criteriaAndRefRankFlow.visible = false;
                            criteriaAndRefRankFlow.layoutX = 0.0;
                            criteriaAndRefRankFlow.layoutY = 0.0;
                            criteriaAndRefRankFlow.vertical = false;
                            alternativesUI.visible = false;
                            finalRankUI.visible = true;
                            finalRankUI.layoutX = 38.0;
                            finalRankUI.layoutY = 450.0;
                            chartsHBox.visible = true;
                            chartsScrollView.visible = true;
                            chartsScrollView.disable = false;
                            chartsScrollView.opacity = 1.0;
                            chartsScrollView.layoutX = 10.0;
                            chartsScrollView.layoutY = 30.0;
                            __layoutInfo_chartsScrollView.width = 1000.0;
                            __layoutInfo_chartsScrollView.height = 410.0;
                            chartsScrollView.hbarPolicy = javafx.scene.control.ScrollBarPolicy.AS_NEEDED;
                            chartsScrollView.vbarPolicy = javafx.scene.control.ScrollBarPolicy.AS_NEEDED;
                            chartsScrollView.pannable = false;
                            label.layoutX = 415.0;
                            label.layoutY = -66.0;
                            importMenuItem.disable = true;
                            importMenuItem.onKeyPressed = null;
                            importMenuItem.onMouseClicked = null;
                            importMenuItem.text = "Import";
                            exportMenuItem.disable = true;
                            exportMenuItem.text = "Export";
                            exitMenuItem.onMouseClicked = exitMenuItemOnMouseClicked;
                            fileMenu.visible = true;
                            fileMenu.disable = false;
                            utaImplMenuItem.visible = true;
                            utaImplMenuItem.disable = true;
                            utaImplMenuItem.text = "Choose solver";
                            utaImplMenuItem.font = null;
                            toolsMenu.disable = false;
                            toolsMenu.text = "Tools";
                            toolsMenu.font = null;
                            toolsMenu.accelerator = null;
                            helpMenu.text = "Help";
                            menuBar.visible = true;
                            scene.camera = null;
                            scene.cursor = null;
                        }
                    }
                ]
            }
        ]
    }
    
    public function getDesignRootNodes (): javafx.scene.Node[] {
        [ flow, criteriaAndRefRankFlow, alternativesUI, finalRankUI, chartsScrollView, label, menuBar, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        scene
    }
    // </editor-fold>//GEN-END:main

    function utaImplMenuItemOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
        controller.changeSolverImpl();
         }

    function exitMenuItemOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
        System.exit(0);
         }

    function importMenuItemOnMouseClicked(event: javafx.scene.input.MouseEvent): Void {
        controller.importPreferences();
         }

    function onNextState(finishedState: Integer): Void {
        controller.onNextState(currentState.actual);
         }

    var controller: MainSceneController;

    init { controller = MainSceneController {
                    criteriaUI: this.criteriaUI;
                    alternativesUI: this.alternativesUI;
                    referenceRankUI: this.referenceRankUI;
                    finalRankUI: this.finalRankUI;
                    chartsHBox: this.chartsHBox;
                }
         }

}


    
function run (): Void {
    var design = MainScene {};    

    javafx.stage.Stage {
        title: "MainScene"
        scene: design.getDesignScene ()
    }
}
