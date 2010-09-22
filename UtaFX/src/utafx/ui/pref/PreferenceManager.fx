/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.ui.pref;

import java.io.File;
import utafx.ui.pref.jaxb.Preferences;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.lang.Exception;
import utafx.ui.pref.jaxb.ObjectFactory;
import javax.xml.bind.JAXBElement;
import java.io.IOException;
import javax.xml.bind.Marshaller;

/**
 * @author Pawcik
 */
public class PreferenceManager {
    
    public var importLastDir:File;
    public var exportLastDir:File;

    public function doImport(inFile: File): Preferences {
        var context: JAXBContext = JAXBContext.newInstance("utafx.ui.pref.jaxb");
        var unmarshaller: Unmarshaller = context.createUnmarshaller();
        try {
            var prefs: JAXBElement = unmarshaller.unmarshal(inFile) as JAXBElement;
            return prefs.getValue() as Preferences;
        } catch (e: Exception) {
            println("Could not import preferences: {e.getMessage()}");
            e.printStackTrace();
        }
        return new ObjectFactory().createPreferences();
    }

    public function export(prefs: Preferences, outFile: String): Void {
        var context: JAXBContext = JAXBContext.newInstance("utafx.ui.pref.jaxb");
        var marshaller = context.createMarshaller();
        try {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(prefs, new File(outFile));
        } catch (e: Exception) {
            e.printStackTrace();
            throw new IOException("Marshalling error: {e.getMessage()}");
        }
    }

}
