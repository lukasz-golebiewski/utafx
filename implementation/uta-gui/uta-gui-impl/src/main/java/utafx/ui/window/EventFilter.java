/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utafx.ui.window;

/**
 *
 * @author Pawcik
 */
import java.awt.AWTEvent;

public interface EventFilter {
    public boolean accept(AWTEvent event);
}
