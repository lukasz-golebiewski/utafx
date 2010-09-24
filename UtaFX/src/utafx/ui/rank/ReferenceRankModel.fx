/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.ui.rank;

import javafx.util.Sequences;
import com.javafx.preview.control.TreeItemBase;
import utafx.ui.rank.ReferenceRankUI.RRTreeItem;

/**
 * @author Pawcik
 */
public class ReferenceRankModel {

    public var rankings: RRTreeItem[];
    public var availNames: String[];
    public var alternativeNames: String[] on replace old[start..end] = newValues {
                for (oldValue in old[start..end]) {
                    if (Sequences.indexOf(availNames, oldValue) != -1) {
                        delete oldValue from availNames;
                    } else {
                        var item = findItemWithValue(oldValue, rankings);
                        removeItem(item as RRTreeItem, true);
                    }
                }
                for (nv in newValues) {
                    insert nv into availNames;
                }
            }

    public function removeFromTreeModel(s: String) {
        var item = findItemWithValue(s, rankings);
        removeItem(item as RRTreeItem);
    }

    function findItemWithValue(value: Object, items: TreeItemBase[]): TreeItemBase {
        for (item in items) {
            if (item.data == value) {
                return item;
            }
        }
        for (item in items) {
            var result = findItemWithValue(value, item.children);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public function removeItem(item: RRTreeItem) {
        removeItem(item, false);
    }

    function removeItem(item: RRTreeItem, persistRemove: Boolean) {
        var parent = item.parent as RRTreeItem;
        for (i in [item.position + 1..sizeof parent.children]) {
            (parent.children[i] as RRTreeItem).position--;
        }

        delete item from parent.children;

        //if last element, remove ranking, reindex rankings
//        if(parent.rank>0 and sizeof parent.children == 1){
//            var start = parent.rank+1;
//            var end = sizeof rankings;
//            for(r in [start..end]){
//                rankings[r-1].rank--;
//                println("rankings[{r-1}].rank={rankings[r-1].rank}");
//                println("rankings[{r-1}].data={rankings[r-1].data}");
//            }
//            delete parent from rankings;
//        } else {
//            delete item from parent.children;
//        }
        if (not persistRemove) insert item.altName into availNames;
    }

    public function removeItem(rank: Integer, position: Integer) {
        var item;
        if (rank > 0) {
            if (position > -1) {
                item = rankings[rank - 1].children[position];
                if (item != null) removeItem(item as RRTreeItem);
            } else if (rank == sizeof rankings) {
                item = rankings[rank - 1];
                for (c in item.children) {
                    insert "{c.data}" into availNames;
                }
                delete item as RRTreeItem from rankings;
            }
        }
    }

}
