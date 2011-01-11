package utafx.ui.pref.filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class XLSFileFilter extends FileFilter {

    @Override
    public boolean accept(File pathname) {	
	String name = pathname.getName().toLowerCase().trim();
	return pathname.isDirectory() || name.endsWith(".xls");
    }

    @Override
    public String getDescription() {
	return "Microsoft Excel 97-2003 files (*.xls)";
    }
}
