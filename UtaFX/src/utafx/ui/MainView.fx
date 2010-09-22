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

/**
 * This class represents the the user screen. 
 */
public class MainView extends VBox {

    def MAIN_VIEW_VSPACING = 10;
    def DYNAMIC_CONTENT_VSPACING = 10;
    
    var controller: GUIController;
    //container for criterias, alternatives, etc.
    var dynamicContent: Container;
    public-read var criteriaPanel: CriteriaUI;
    public-read var alternativesPanel: AlternativesUI;
    public-read var referenceRankPanel: ReferenceRankUI;
    public-read var solutionPanel: SolutionUI;
    public-read var criteriaAdded = bind (criteriaPanel != null);
    public-read var alternativesAdded = bind (alternativesPanel != null);
    public-read var referenceRankAdded = bind (referenceRankPanel != null);

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
            //effect: Reflection {fraction: 0.7};
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
                        node: bind dynamicContent
                    },
                ];
        dynamicContent = VBox {
                    spacing: DYNAMIC_CONTENT_VSPACING;
        }
    }

    public function addCriteria(node: CriteriaUI) {
        criteriaPanel = node;
        insert HBox {
            //padding: Insets {left:10, top:10}
            //hpos: HPos.CENTER
            content: bind node
        } into dynamicContent.content;
        //criteriaAdded = true;
    }

    public function addAlternatives(node: AlternativesUI) {
        alternativesPanel = node;
        insert HBox {
            content: bind node
        } into dynamicContent.content;
    }

    public function addReferenceRank(node: ReferenceRankUI) {
        referenceRankPanel = node;
        insert HBox {
            //hpos: HPos.CENTER
            content: bind node
        } into dynamicContent.content;
    }

    public function addSolutionUI(node: SolutionUI) {
        insert HBox {
            //hpos: HPos.CENTER
            content: bind node
        } into dynamicContent.content;
    }

}
