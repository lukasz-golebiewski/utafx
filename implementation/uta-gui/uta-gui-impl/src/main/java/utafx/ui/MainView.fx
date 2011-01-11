package utafx.ui;

import javafx.scene.layout.VBox;
import utafx.control.GUIController;
import utafx.ui.menu.UtaMenuBar;
import javafx.scene.layout.Container;
import javafx.scene.control.ScrollView;
import javafx.scene.control.ScrollBarPolicy;
import utafx.Constants;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import utafx.ui.criteria.CriteriaUI;
import utafx.ui.alternative.AlternativesUI;
import utafx.ui.rank.ReferenceRankUI;
import utafx.ui.solution.SolutionUI;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.BlurType;

/**
 * This class represents the the user screen. 
 */
public class MainView extends VBox {

    def MAIN_VIEW_VSPACING = 10;
    def DYNAMIC_CONTENT_VSPACING = 10;
    
    var controller: GUIController;
    //container for criterias, alternatives, etc.
    public var userDataContent: Container;
    public var criteriaPanel: CriteriaUI;
    public var alternativesPanel: AlternativesUI;
    public var referenceRankPanel: ReferenceRankUI;
    public var solutionPanel: SolutionUI;
    public var criteriaAdded = bind (criteriaPanel != null);
    public var alternativesAdded = bind (alternativesPanel != null);
    public var referenceRankAdded = bind (referenceRankPanel != null);

    init {
        spacing = MAIN_VIEW_VSPACING;
        controller = GUIController {
                    view: MainView.this;
        }
        def menu = UtaMenuBar {
                    guiController: controller;
        }
        var label:Text = Text {
            content: "Welcome to the UTA methods"
            font: Font{ name: "Amble Cn" size:24 };
            effect: DropShadow{blurType:BlurType.GAUSSIAN};
            translateX: bind (width - label.layoutBounds.width)/2.0-label.layoutBounds.minX;
        }
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
                        node: bind userDataContent
                    },
                ];
        userDataContent = VBox {
                    spacing: DYNAMIC_CONTENT_VSPACING;
        }
    }

    //postinit{
    //    controller.importPreferences();
    //}

    public function addCriteria(node: CriteriaUI) {
        criteriaPanel = node;
        insert HBox {
            //padding: Insets {left:10, top:10}
            //hpos: HPos.CENTER
            content: bind node
        } into userDataContent.content;
        //criteriaAdded = true;
    }

    public function addAlternatives(node: AlternativesUI) {
        alternativesPanel = node;
        insert HBox {
            content: bind node
        } into userDataContent.content;
    }

    public function addReferenceRank(node: ReferenceRankUI) {
        referenceRankPanel = node;
        insert HBox {
            //hpos: HPos.CENTER
            content: bind node
        } into userDataContent.content;
    }

    public function addSolutionUI(node: SolutionUI) {
        insert node
        //insert HBox {
            //hpos: HPos.CENTER
            //content: bind node}
        into userDataContent.content;
    }

}
