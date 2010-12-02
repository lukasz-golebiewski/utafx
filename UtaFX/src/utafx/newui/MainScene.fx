/*
 * MainScene.fx
 *
 * Created on Nov 30, 2010, 10:58:34 PM
 */

package utafx.newui;

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
    
    def __layoutInfo_customNode2: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
    }
    public-read def customNode2: utafx.ui.criteria.CriteriaUI = utafx.ui.criteria.CriteriaUI {
        visible: false
        layoutInfo: __layoutInfo_customNode2
    }
    
    def __layoutInfo_customNode: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
    }
    public-read def customNode: utafx.ui.rank.ReferenceRankUI = utafx.ui.rank.ReferenceRankUI {
        visible: false
        layoutX: 55.0
        layoutY: 485.0
        layoutInfo: __layoutInfo_customNode
    }
    
    def __layoutInfo_criteriaAndRefRankFlow: javafx.scene.layout.LayoutInfo = javafx.scene.layout.LayoutInfo {
    }
    public-read def criteriaAndRefRankFlow: javafx.scene.layout.Flow = javafx.scene.layout.Flow {
        visible: false
        layoutInfo: __layoutInfo_criteriaAndRefRankFlow
        content: [ customNode2, customNode, ]
        hgap: 6.0
        vgap: 6.0
    }
    
    public-read def customNode3: utafx.ui.alternative.AlternativesUI = utafx.ui.alternative.AlternativesUI {
        visible: false
        layoutX: 511.0
        layoutY: 32.0
    }
    
    public-read def scene: javafx.scene.Scene = javafx.scene.Scene {
        width: 1024.0
        height: 768.0
        content: getDesignRootNodes ()
        camera: null
        cursor: null
        fill: javafx.scene.paint.Color.WHITE
    }
    
    public-read def currentState: org.netbeans.javafx.design.DesignState = org.netbeans.javafx.design.DesignState {
        names: [ "State1", "State 2", ]
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
                            customNode2.visible = false;
                            customNode2.managed = true;
                            customNode.visible = false;
                            customNode.layoutX = 55.0;
                            customNode.layoutY = 485.0;
                            criteriaAndRefRankFlow.visible = false;
                            criteriaAndRefRankFlow.layoutX = 0.0;
                            criteriaAndRefRankFlow.layoutY = 0.0;
                            __layoutInfo_criteriaAndRefRankFlow.width = 389.0;
                            __layoutInfo_criteriaAndRefRankFlow.height = 684.0;
                            criteriaAndRefRankFlow.vertical = false;
                            customNode3.visible = false;
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
                            customNode2.visible = true;
                            customNode2.managed = true;
                            __layoutInfo_customNode2.hpos = javafx.geometry.HPos.LEFT;
                            __layoutInfo_customNode2.vpos = javafx.geometry.VPos.TOP;
                            customNode.visible = true;
                            customNode.layoutX = 0.0;
                            customNode.layoutY = 281.0;
                            __layoutInfo_customNode.hpos = javafx.geometry.HPos.LEFT;
                            __layoutInfo_customNode.vpos = javafx.geometry.VPos.BOTTOM;
                            criteriaAndRefRankFlow.visible = true;
                            criteriaAndRefRankFlow.layoutX = 18.0;
                            criteriaAndRefRankFlow.layoutY = 32.0;
                            __layoutInfo_criteriaAndRefRankFlow.width = 447.0;
                            __layoutInfo_criteriaAndRefRankFlow.height = 641.0;
                            criteriaAndRefRankFlow.vertical = true;
                            customNode3.visible = true;
                        }
                    }
                ]
            }
        ]
    }
    
    public function getDesignRootNodes (): javafx.scene.Node[] {
        [ flow, criteriaAndRefRankFlow, customNode3, ]
    }
    
    public function getDesignScene (): javafx.scene.Scene {
        scene
    }
    // </editor-fold>//GEN-END:main

}

function run (): Void {
    var design = MainScene {};

    javafx.stage.Stage {
        title: "MainScene"
        scene: design.getDesignScene ()
    }
}
