package utafx.ui.rank;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import com.javafx.preview.control.TreeView;
import com.javafx.preview.control.TreeItem;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.geometry.HPos;
import javafx.scene.control.ChoiceBox;
import utafx.ui.window.Window;
import javafx.scene.layout.Container;
import uta.Alternative;
import uta.Ranking;
import utafx.control.GUIController;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;
import javafx.geometry.Insets;

def outerBorderFill = LinearGradient {
            startX: 0.0 startY: 0.0 endX: 0.0 endY: 1.0
            stops: [
                Stop { offset: 0.0 color: Color.web("#F4F4F4") },
                Stop { offset: 0.5 color: Color.web("#BCBCBC") }
            ]
        };
def INFO_LABEL_HEIGHT = 20;

/**
 * @author Pawcik
 */
public class ReferenceRankUI extends CustomNode {

    public var allItems: uta.Alternative[];
    public var available = allItems;
    var showLogs = false;
    var treeView: TreeView;
    var itemAdded = false;
    var addButton: Button;
    var removeButton: Button;
    var moveUpBtn: Button;
    var moveDownBtn: Button;
    var acceptButton: Button;
    var cancelButton: Button;
    var rr: RRTreeItem[];
    //var maxRank = 0;
    var currentRank = 0 on replace old {
                if (showLogs) println("Old rank: {old},  new rank: {currentRank} position: {currentPosition}");
            }
    var currentPosition = 0 on replace old {
                if (showLogs) println("Old position: {old},  new position: {currentPosition}")
            }
    //new approach
    public var model: ReferenceRankModel;
    var availNames = model.alternativeNames;
    public var comboBox2: ChoiceBox;
    public var guiController: GUIController;

    public function reset() {
        if (showLogs) println("Reseting ReferenceRank...");
        model.rankings = [];
        model.availNames = model.alternativeNames;
        //available = allItems;
        //availNames = model.alternativeNames;
        //maxRank = 0;
        currentRank = 0;
        currentPosition = 0;
        if (showLogs) println("Reseted ReferenceRank");
    }

    //Adds new alternative to the currently selected ranking
    function add() {
        //if the root is selected, then we are adding another subRefRank
        //maxRank = Math.max(maxRank, currentRank);

        def cancel: function() =
                function() {
                    itemAdded = false;
                    window.hide();
                }

        var box: Container = VBox {
                    spacing: 20
                    content: [
                        //                        comboBox = ChoiceBox {
                        //                                    layoutX: 10
                        //                                    layoutY: 10
                        //                                    items: bind for (a in available) {
                        //                                        a.getName()
                        //                                    }
                        //                                }
                        comboBox2 = ChoiceBox {
                                    layoutX: 10
                                    layoutY: 10
                                    items: bind model.availNames
                                }

                        HBox {
                            spacing: 30
                            hpos: HPos.CENTER
                            content: [
                                acceptButton = Button {
                                            text: "Accept";
                                            action: function() {
//                                                var index = comboBox.selectedIndex;
//                                                var selectedAltern = available[index];
//                                                insertToTreeView(selectedAltern, currentRank);
//                                                //switch back to root
                                                //currentRank = 0;
                                                var index = comboBox2.selectedIndex;
                                                var selectedName = model.availNames[index];
                                                insertToTreeView2(selectedName, currentRank);
                                                guiController.updateSolution();
                                                window.hide();
                                            }
                                        },

                                cancelButton = Button {
                                            text: "Cancel"
                                            action: cancel
                                        }
                            ]
                        }]
                }

        var window: Window = Window {
                    title: "Add alternative"
                    width: 300
                    height: 400
                    nodes: box;
                }
        window.show(addButton.scene);

        //MessageBox.showOptionsDialog(dialog, "Add", "Add Alternative", ["OK", "Cancel"], [accept, doNothing]);
        //var result = MessageBox.showInputDialog(dialog, "Dupa", "Pokaz dupe");
        //if (showLogs) println("{result}");
        treeView.requestFocus();
    }

    function remove() {
        //        var parent = model.rankings[currentRank - 1];
        //        var item = parent.children[currentPosition] as RRTreeItem;
        //        //insert item.alternative into available;
        //        insert item.altName into availNames;
        //        delete item from parent.children;
        //    //        if(sizeof parent.children ==0){
        //    //            delete parent from rr;
        //    //        }
        //model.removeFromTreeModel(s);
        model.removeItem(currentRank, currentPosition);
        guiController.updateSolution();
    //last ranking was removed
    }

    function moveUp() {
        if (currentRank < 2) {
            return;
        }
        var item = model.rankings[currentRank - 1].children[currentPosition] as RRTreeItem;
        var newRank = currentRank - 1;
        var newPosition = sizeof model.rankings[newRank - 1].children;
        if (item != null) {
            model.removeItem(item);
            insertToTreeView2(item.altName, currentRank - 1);
            if (showLogs) println("moveUp: current rank: {newRank}");
            if (showLogs) println("moveUp: current position: {newPosition}");
            currentRank = newRank;
            currentPosition = newPosition;
            guiController.updateSolution();
        }
    }

    function moveDown() {
        if (currentRank >= sizeof model.rankings) {
            return;
        }
        var newRank = currentRank + 1;
        var newPosition = sizeof model.rankings[newRank - 1].children;
        var item = model.rankings[currentRank - 1].children[currentPosition] as RRTreeItem;
        if (item != null) {
            model.removeItem(item);
            insertToTreeView2(item.altName, newRank);
            if (showLogs) println("moveUp: current rank: {newRank}");
            if (showLogs) println("moveUp: current position: {newPosition}");
            currentRank = newRank;
            currentPosition = newPosition;
            guiController.updateSolution();
        }
    }

    public function insertToTreeView(a: Alternative, r: Integer) {
        var allRanks = sizeof rr;
        if (allRanks < r) {
            insert RRTreeItem {
                rank: r;
                data: bind "{r}. Ranking"
                expanded: true;
            } into rr;
        }

        var currentParent = rr[r - 1] as RRTreeItem;
        insert RRTreeItem {
            rank: bind (rr[r - 1] as RRTreeItem).rank;
            alternative: a;
            position: bind sizeof rr[r - 1].children
            expanded: true;
        } into currentParent.children;
        delete a from available;
    //maxRank = Math.max(r, maxRank);
    }

    public function insertToTreeView2(name: String, r: Integer): Void {
        insertToTreeView2(name, r, false);
    }

    public function insertToTreeView2(name: String, r: Integer, head: Boolean): Void {
        var allRanks = sizeof model.rankings;

        if (r == 0) {
            insertToTreeView2(name, allRanks + 1);
        }

        var inserted: RRTreeItem;

        if (allRanks < r) {
            insert inserted = RRTreeItem {
                        rank: r;
                        //-1 tells that this item is ranking
                        position: -1;
                        data: bind "{inserted.rank}. Ranking"
                        expanded: true;
                    } into model.rankings;
        }

        var currentParent = model.rankings[r - 1] as ReferenceRankUI.RRTreeItem;
        var pos = sizeof model.rankings[r - 1].children;
        insert RRTreeItem {
            rank: r;
            //alternative: a;
            altName: name
            position: pos;
            expanded: true;
        } into currentParent.children;
        delete name from model.availNames;
    //maxRank = Math.max(r, maxRank);
    }

    public function getPOJO(): Ranking {
        var rankPojo = new Ranking();
        var maxRank = sizeof model.rankings;
        for (i in [1..maxRank]) {
            var ranking = model.rankings[i - 1];
            for (child in ranking.children) {
                rankPojo.add(child.data, i);
                if (showLogs) println(rankPojo.toString());
            }
        }
        return rankPojo;
    }

    override function create(): Node {
        var rect: Rectangle;
        VBox {
            spacing: 0
            content: [
                Container {
                    var label: Label;
                    content: [
                        rect = Rectangle {
                                    fill: outerBorderFill
                                    width: bind treeView.width - treeView.boundsInLocal.minX;
                                    layoutX: bind treeView.boundsInLocal.minX;
                                    height: bind INFO_LABEL_HEIGHT;
                                }

                        label = Label {
                                    textAlignment: TextAlignment.CENTER
                                    text: "Define your criterias"
                                    vpos: VPos.CENTER
                                    hpos: HPos.CENTER
                                    layoutX: bind (rect.boundsInLocal.width - label.boundsInLocal.width) / 2
                                    layoutY: 5
                                }
                    ]
                }
                treeView = TreeView {
                            showRoot: true
                            root: RRTreeItem {
                                rank: 0;
                                expanded: true;
                                data: "Reference Rank"
                                children: bind model.rankings
                            }
                            pannable: true
                            layoutInfo: LayoutInfo { width: 400, hgrow: Priority.ALWAYS }
                        },
                Container {
                    content: [
                        Rectangle {
                            width: bind rect.width
                            height: 30
                            fill: outerBorderFill
                        },
                        HBox {
                            hpos: HPos.CENTER
                            spacing: 20
                            padding: Insets { top: 5, bottom: 5, left: 0, right: 0 }
                            //layoutY: 10
                            content: [
                                addButton = Button {
                                            text: "Add"
                                            action: add
                                        },
                                removeButton = Button {
                                            text: "Remove"
                                            action: remove
                                        },
                                moveUpBtn = Button {
                                            text: "Move Up"
                                            action: moveUp
                                        },
                                moveDownBtn = Button {
                                            text: "Move Down"
                                            action: moveDown
                                        },
                            ]
                            layoutX: bind (rect.width - addButton.boundsInLocal.width - removeButton.boundsInLocal.width - moveUpBtn.boundsInLocal.width - moveDownBtn.boundsInLocal.width -3*20) / 2;
                        }
                    ]
                }] }
    }

}

package class RRTreeItem extends TreeItem {

    protected var rank: Integer;
    protected var position: Integer;// = bind Sequences.indexOf(this, this.parent.children);
    public override var expanded = true;
    public var alternative: Alternative on replace {
                //if (showLogs) println("Rank = {rank}, position = {position}, Alternative changed");
                if (alternative != null) {
                    data = alternative.getName();
                }
            }
    public override var onSelected = function() {
                currentRank = this.rank;
                currentPosition = this.position;
            }
    //new approach
    public var altName: String;
    override var data = bind altName;
}
