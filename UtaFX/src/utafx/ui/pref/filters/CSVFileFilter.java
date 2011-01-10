package utafx.ui.pref.filters;

import java.io.File;

public class CSVFileFilter extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File pathname) {	
	String name = pathname.getName().toLowerCase().trim();
	return pathname.isDirectory() || name.endsWith(".csv");
    }

    @Override
    public String getDescription() {
	return "CSV files (*.csv)";
    }

}
