/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.ui.pref;

import javafx.scene.CustomNode;
import com.javafx.preview.control.TreeView;
import com.javafx.preview.control.TreeItem;
import java.io.File;
import javafx.scene.Node;
import com.javafx.preview.control.TreeItemBase;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.ext.swing.SwingScrollableComponent;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import utafx.ui.pref.filters.*;

/**
 * @author Pawcik
 */
public class FileChooser extends SwingScrollableComponent {

    public var selectedFile: File;
    public var open = true;

    public var title: String on replace {
        nativeFC.setDialogTitle(title);
    }

    public var command: String on replace {
        nativeFC.setApproveButtonText(command);
    }

    public var workingDir: File on replace {
        nativeFC.setCurrentDirectory(workingDir);
    }

    public var show: Boolean = false on replace{
        if(show){
            var option = if(open) nativeFC.showOpenDialog(null) else nativeFC.showSaveDialog(null);
            if(option == nativeFC.APPROVE_OPTION){
                selectedFile = nativeFC.getSelectedFile();
                workingDir = selectedFile.getParentFile();
            }
        }
    }
    public-read var nativeFC: JFileChooser;

    override function createJComponent(): JComponent {
        nativeFC = new JFileChooser();
        nativeFC.addChoosableFileFilter(new XLSFileFilter());
        nativeFC.addChoosableFileFilter(new XLSXFileFilter());
        nativeFC.addChoosableFileFilter(new CSVFileFilter()); 
        return nativeFC;
    }
}

function createChildren(item: TreeItemBase): TreeItemBase[] {
    def file = item.data as File;
    if (file.isDirectory()) {
        return for (f in file.listFiles()) {
                    TreeItem {
                        data: f
                        createChildren: createChildren;
                        isLeaf: isLeaf;
                    }
                }
    }

    return [];
}

function isLeaf(item: TreeItemBase): Boolean {
    def file = item.data as File;
    return file.isFile();
}

function run() {
    Stage {
        scene: Scene {
            content: FileChooser{}
        }
    }
}
