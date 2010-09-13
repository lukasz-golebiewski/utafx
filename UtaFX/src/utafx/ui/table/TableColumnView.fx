/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utafx.ui.table;

import javafx.scene.control.ListView;
import com.sun.javafx.scene.control.skin.SkinAdapter;

/**
 * @author Pawcik
 */

public class TableColumnView extends ListView {

    public var onVScrollVisibleChange : function (visible : Boolean);
    public var onVScrollMinChange : function (value : Float);
    public var onVScrollMaxChange : function (value : Float);
    public var onVScrollValueChange : function (value : Float);
    public var onVScrollVisibleAmountChange : function (value : Float);

    var tableColumnViewSkin = TableColumnViewSkin {
        onVScrollMinChange: bind onVScrollMinChange
        onVScrollMaxChange: bind onVScrollMaxChange
        onVScrollValueChange: bind onVScrollValueChange
        onVScrollVisibleAmountChange: bind onVScrollVisibleAmountChange
        onVScrollVisibleChange: bind onVScrollVisibleChange
    };

    postinit {
        skin = SkinAdapter {
            rootRegion: tableColumnViewSkin
        };
    }

    public function updateVScrollValue(value : Float) : Void {
        tableColumnViewSkin.updateVScrollValue(value);
    }

    /**
     * @treatasprivate
     */
    public function onScrollPageUp():Void {
        tableColumnViewSkin.onScrollPageUp();
    }

    /**
     * @treatasprivate
     */
    public function onScrollPageDown():Void {
        tableColumnViewSkin.onScrollPageDown();
    }

    /**
     * @treatasprivate
     */
    public function onSelectPreviousRow():Void {
        tableColumnViewSkin.onSelectPreviousRow();
    }

    /**
     * @treatasprivate
     */
    public function onSelectNextRow():Void {
        tableColumnViewSkin.onSelectNextRow();
    }

    /**
     * @treatasprivate
     */
    public function onSelectFirstRow():Void {
        tableColumnViewSkin.onSelectFirstRow();
    }

    /**
     * @treatasprivate
     */
    public function onSelectLastRow():Void {
        tableColumnViewSkin.onSelectLastRow();
    }
}
