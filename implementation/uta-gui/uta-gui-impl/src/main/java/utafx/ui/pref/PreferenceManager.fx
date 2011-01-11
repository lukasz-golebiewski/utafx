/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.ui.pref;

import java.io.File;
import utafx.data.pref.jaxb.Preferences;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.lang.Exception;
import utafx.ui.pref.jaxb.ObjectFactory;
import javax.xml.bind.JAXBElement;
import java.io.IOException;
import javax.xml.bind.Marshaller;
import utafx.data.converter.ConversionManager;

/**
 * @author Pawcik
 */
public class PreferenceManager {
    
    public var importLastDir:File;
    public var exportLastDir:File;

    public function doImport(inFile: File): Preferences {
       var converter: ConversionManager = new ConversionManager();
       converter.read(inFile.getAbsolutePath());
    }

    public function export(prefs: Preferences, outFile: String): Void {
       var converter: ConversionManager = new ConversionManager();
       converter.write(prefs, outFile);
    }
}
