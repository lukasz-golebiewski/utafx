/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utafx.ui.pref;

import javafx.scene.CustomNode;
import javafx.scene.Node;

/**
 * @author Pawcik
 */

public class ImportUI extends CustomNode{
     
    var fileChooser:FileChooser;
    var prefManager: PreferenceManager;
     
    override function create(): Node {
        prefManager = PreferenceManager {};
        null;
    }
}
