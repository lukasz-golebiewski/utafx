package utafx.ui.pref.filters;

import java.io.File;
import java.io.FileFilter;

public class XLSXFileFilter extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File pathname) {	
	String name = pathname.getName().toLowerCase().trim();
	return pathname.isDirectory() || name.endsWith(".xlsx");
    }
    
    @Override
    public String getDescription() {
	return "Microsoft Excel 2007 files (*.xlsx)";
    }

}
