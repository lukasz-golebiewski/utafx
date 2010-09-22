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
import javafx.util.Math;
import uta.Alternative;
import uta.Ranking;

/**
 * @author Pawcik
 */
public class ReferenceRankUI extends CustomNode {

    public var allItems: uta.Alternative[];
    public var available = allItems;
    var treeView: TreeView;
    var comboBox: ChoiceBox;
    var itemAdded = false;
    var addButton: Button;
    var acceptButton: Button;
    var cancelButton: Button;
    var rr: RRTreeItem[];
    var maxRank = 0;
    var selectedRank = 0 on replace old {
            //println("Old rank: {old},  new rank: {selectedRank} new position{selectedPosition}");
            }
    var selectedPosition = 0 on replace old {
            //println("Old position: {old},  new rank: {selectedRank} new position{selectedPosition}")
            }

    public function reset() {
        println("Reseting ReferenceRank...");
        rr = [];
        available = allItems;
        maxRank = 0;
        selectedRank = 0;
        selectedPosition = 0;
        println("Reseted ReferenceRank");
    }

    //Adds new alternative to the currently selected ranking
    function add() {
        //if the root is selected, then we are adding another subRefRank
        if (selectedRank == 0) {
            selectedRank = maxRank + 1;
        }
        maxRank = Math.max(maxRank, selectedRank);

        def cancel: function() =
                function() {
                    itemAdded = false;
                    window.hide();
                }

        var box: Container = VBox {
                    spacing: 20
                    content: [
                        comboBox = ChoiceBox {
                                    layoutX: 10
                                    layoutY: 10
                                    items: bind for (a in available) {
                                        a.getName()
                                    }
                                }

                        HBox {
                            spacing: 30
                            hpos: HPos.CENTER
                            content: [
                                acceptButton = Button {
                                            text: "Accept";
                                            action: function() {
                                                var availIndex = comboBox.selectedIndex;
                                                var selectedAltern = available[availIndex];
                                                insertToTreeView(selectedAltern, selectedRank);
                                                //switch back to root
                                                //selectedRank = 0;
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
                    width: 200
                    height: 200
                    nodes: box;
                }
        window.show(addButton.scene);

        //MessageBox.showOptionsDialog(dialog, "Add", "Add Alternative", ["OK", "Cancel"], [accept, doNothing]);
        //var result = MessageBox.showInputDialog(dialog, "Dupa", "Pokaz dupe");
        //println("{result}");
        treeView.requestFocus();
    }

    function remove() {
        var parent = rr[selectedRank - 1];
        var item = parent.children[selectedPosition] as RRTreeItem;
        insert item.alternative into available;
        delete item from parent.children;
    //        if(sizeof parent.children ==0){
    //            delete parent from rr;
    //        }

    }

    function moveUp() {
        var newRank = Math.max(selectedRank - 1, 1);
        if (newRank == selectedRank) {
            return;
        } else {
            var parent = rr[selectedRank - 1];
            var item = parent.children[selectedPosition] as RRTreeItem;
            var newParent = rr[newRank - 1];
            delete item from parent.children;
            item.position = sizeof newParent.children;
            item.rank = newRank;
            insert item into newParent.children;
        }
    }

    function moveDown() {
        var newRank = Math.min(selectedRank + 1, maxRank);
        if (newRank == maxRank) {
            return;
        } else {
            var parent = rr[selectedRank - 1];
            var item = parent.children[selectedPosition] as RRTreeItem;
            var newParent = rr[newRank - 1];
            delete item from parent.children;
            item.position = sizeof newParent.children;
            item.rank = newRank;
            insert item into newParent.children;
        }
    }

    public function insertToTreeView(a: Alternative, r: Integer) {
        var allRanks = sizeof treeView.root.children;
        if (allRanks < r) {
            insert RRTreeItem {
                rank: r;
                data: "{r}. Ranking"
                expanded: true;
            } into rr;
        }

        var currentParent = rr[r - 1] as ReferenceRankUI.RRTreeItem;
        var pos = sizeof rr[r - 1].children;
        insert RRTreeItem {
            rank: r;
            alternative: a;
            position: pos;
            expanded: true;
        } into currentParent.children;
        delete a from available;
        maxRank = Math.max(r, maxRank);
    }

    public function getPOJO(): Ranking {
        var rankPojo = new Ranking();
        for (i in [1..maxRank]) {
            var parent = rr[i - 1];
            for (c in parent.children) {
                rankPojo.add((c as RRTreeItem).alternative, i);
                println(rankPojo.toString());
            }
        }
        return rankPojo;
    }

    override function create(): Node {
        VBox {
            spacing: 10
            content: [
                treeView = TreeView {
                            showRoot: true
                            height: 200
                            //                            override var cellFactory = function() : TreeCell {
                            //                                def cell:TreeCell = TreeCell {
                            //                                    node: Label {
                            //                                        text: bind if (cell.item instanceof Alternative){
                            //                                            (cell.item as Alternative).getName()
                            //                                            } else {
                            //                                                cell.item.toString();
                            //                                            }
                            //                                        visible: bind (cell.item != null)
                            //                                    }
                            //                                }
                            //                            }
                            root: RRTreeItem {
                                rank: 0;
                                expanded: true;
                                data: "Reference Rank"
                                children: bind rr
                            }
                            layoutInfo: LayoutInfo { width: 400, hgrow: Priority.ALWAYS }
                        },
                HBox {
                    hpos: HPos.CENTER
                    spacing: 20
                    content: [
                        addButton = Button {
                                    text: "Add"
                                    action: add
                                },
                        Button {
                            text: "Remove"
                            action: remove
                        },
                        Button {
                            text: "Move Up"
                            action: moveUp
                        },
                        Button {
                            text: "Move Down"
                            action: moveDown
                        },
                    ]
                }
            ]
        }
    }

}

package class RRTreeItem extends TreeItem {

    protected var rank: Integer;
    protected var position: Integer;
    public override var expanded = true;
    public var alternative: Alternative on replace {
                //println("Rank = {rank}, position = {position}, Alternative changed");
                if (alternative != null) {
                    data = alternative.getName();
                }
            }
    public override var onSelected = function() {
                selectedRank = this.rank;
                selectedPosition = this.position;
            }
}
