package utafx.ui.pref;

import javax.swing.JFileChooser;

public class PreviewFinalChooser extends JFileChooser {

    /**
     * 
     */
    private static final long serialVersionUID = -7634097676067115784L;
    
    public PreviewFinalChooser() {
	setAccessory(new FileViewer());
    }
     

}
