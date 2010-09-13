package utafx.ui;

import javafx.scene.layout.VBox;
import utafx.control.GUIController;
import com.javafx.preview.control.MenuBar;
import utafx.ui.menu.UtaMenuBar;
import javafx.scene.control.Label;
import javafx.scene.layout.Container;
import javafx.scene.control.ScrollView;
import javafx.scene.control.ScrollBarPolicy;
import javafx.scene.layout.LayoutInfoBase;
import utafx.Constants;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.geometry.HPos;
import javafx.scene.CustomNode;
import javafx.scene.layout.HBox;

/**
 * @author Pawcik
 */
public class MainView extends VBox {

    var menu: MenuBar;
    var controller: GUIController;
    var mainContent: Container;

    public-read var criteriaPanel: CriteriaUI;
    public-read var alternativesPanel:AlternativesUI;
    public-read var referenceRankPanel: ReferenceRankUI;
    public-read var criteriaAdded = false;
    public-read var alternativesAdded = false;
    public-read var referenceRankAdded = false;

    init {
        //padding = 10;
        spacing = 10;
        controller = GUIController {
                    override var view = MainView.this;
                }
        menu = UtaMenuBar {
                    override var guiController = controller;
                }
        var label = Label { text: "Welcome to the UTA methods" }
        content = [
                    menu,
                    label,
                    ScrollView {
                        hbarPolicy: ScrollBarPolicy.AS_NEEDED
                        vbarPolicy: ScrollBarPolicy.AS_NEEDED
                        layoutInfo: Constants.ALWAYS_RESIZE
                        fitToWidth: true
                        pannable: false
                        //cursor: Cursor.WAIT
                        node: bind mainContent
                    },
                ];
        mainContent = VBox {
            spacing: 10
        }
    }

    public function addCriteria(node:CriteriaUI){
        criteriaPanel = node;
        insert HBox{
            hpos: HPos.CENTER
            content: bind node
        } into mainContent.content;
        criteriaAdded = true;
    }

    public function addAlternatives(node:AlternativesUI){
        alternativesPanel = node;
        insert HBox{
            hpos: HPos.CENTER
            content: bind node
        } into mainContent.content;
        alternativesAdded = true;
    }

     public function addReferenceRank(node:ReferenceRankUI){
        referenceRankPanel = node;
        insert HBox{
            hpos: HPos.CENTER
            content: bind node
        } into mainContent.content;
        alternativesAdded = true;
    }
}
