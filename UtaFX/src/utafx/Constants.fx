package utafx;

import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;

/*package def ALWAYS_RESIZE = LayoutInfo{
    hgrow: Priority.ALWAYS
    vgrow: Priority.ALWAYS
    hshrink: Priority.ALWAYS
    vshrink: Priority.ALWAYS
    hfill: true
    vfill: true
}
package def ALWAYS_RESIZE_VERTICALLY = LayoutInfo{
    vgrow: Priority.ALWAYS
    vshrink: Priority.ALWAYS
    vfill: true
}
package def ALWAYS_RESIZE_HORIZONTALLY = LayoutInfo{
    hgrow: Priority.ALWAYS
    hshrink: Priority.ALWAYS
    hfill: true
    vgrow: Priority.NEVER
}

package def APP_PADDING = 10;
package def CONTENT_PADDING = 10;
*/

protected def STYLESHEET_APP = "{__DIR__}css/application.css";
//package def STYLESHEET_NAME = ["Default", "Red", "Dark", "Desktop", "TV", "Custom" ];
//package def STYLESHEET_FILE = ["default.css", "red-rectangle.css", "dark.css", "caspian-desktop.css", "caspian-tv.css", "_fx_styleeditor.css" ];
protected def STYLESHEET_PATH = ["{__DIR__}css/default.css"];

public def ALWAYS_RESIZE = LayoutInfo{
    hgrow: Priority.ALWAYS
    vgrow: Priority.ALWAYS
    hshrink: Priority.ALWAYS
    vshrink: Priority.ALWAYS
    hfill: true
    vfill: true
};