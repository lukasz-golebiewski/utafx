package utafx.ui;

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
import com.javafx.preview.control.TreeCell;

/**
 * @author Pawcik
 */
public class ReferenceRankUI extends CustomNode {

    public var allItems: uta.Alternative[];
    var selectedItems: uta.Alternative[];
    var rank: ReferenceRankItem[];
    var treeView: TreeView;
    var selectedRank = -1;
    var comboBox: ChoiceBox;
    var itemAdded = false;
    var addButton: Button;
    var acceptButton: Button;
    var cancelButton: Button;
    var selectedItemNames: String[];
    var maxRank = 0;

    
    //Adds new alternative to the currently selected ranking
    function add() {
        if (selectedRank == -1) {
            selectedRank = maxRank+1;
        } 
        maxRank = Math.max(maxRank, selectedRank);

        def cancel: function() =
                function() {
                    itemAdded = false;
                    window.hide();
                }

        var box:Container = VBox{
                   spacing: 20
                   content: [
                                comboBox = ChoiceBox {
                                            layoutX: 10
                                            layoutY: 10
                                            items: bind for(a in allItems){
                                                a.getName()
                                            }
                                        }
                                HBox {
                                    spacing: 30
                                    hpos: HPos.CENTER
                                    content: [
                                        acceptButton = Button {
                                                    text: "Accept";
                                                    action: function(){
                                                        var index = comboBox.selectedIndex;
                                                        insert allItems[index] into selectedItems;
                                                        insert ReferenceRankItem {
                                                            alternative: allItems[index];
                                                            rank: selectedRank;
                                                        } into rank;
                                                        insert allItems[index].getName() into selectedItemNames;
                                                        itemAdded = true;
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


        var window:Window = Window {
                    title: "Add alternative"
                    width: 200
                    height: 200
                    nodes: box;
        }
        window.show(addButton.scene);

    //MessageBox.showOptionsDialog(dialog, "Add", "Add Alternative", ["OK", "Cancel"], [accept, doNothing]);
    //var result = MessageBox.showInputDialog(dialog, "Dupa", "Pokaz dupe");
    //println("{result}");
    }

    bound function getChildren(): TreeItem[]{
        for(i in [1..maxRank]){
            RRTreeItem{
                data: "Rank {i}."
                children: getChildren(i);
                rank: i
            }
        }
    }

    function getChildren(rankIndex: Integer): TreeItem[]{
        var result:TreeItem[] = [];
        for(rri in rank){
            if(rri.rank == rankIndex){
                insert RRTreeItem{
                    data: rri.alternative.getName();
                    rank: rankIndex;
                } into result;
            }
        }
        result;
    }

    function createRoot(rankArray: ReferenceRankItem[]): TreeItem {
        null
    }

    override function create(): Node {
        VBox {
            spacing: 10
            content: [
                treeView = TreeView {
                            showRoot: true
                            height: 200
                            root: bind createRoot(rank);
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
                            action: function(){
                                
                            }

                        },
                        Button {
                            text: "Move Up"
                        },
                        Button {
                            text: "Move Down"
                        },
                    ]
                }
            ]
        }
    }

}

package class ReferenceRankItem {

    var alternative: uta.Alternative;
    var rank: Integer;
}

package class RRTreeItem extends TreeItem {
    protected var rank: Integer;
}
