package utafx.data.converter.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import utafx.data.converter.PreferenceDataReader;
import utafx.data.pref.jaxb.Preferences;

public class XmlPreferenceDataReader implements PreferenceDataReader {

    public Preferences read(InputStream is) throws IOException {
	try {
	    JAXBContext context = JAXBContext
		    .newInstance("utafx.data.pref.jaxb");
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    Preferences pref = (Preferences) unmarshaller.unmarshal(is);
	    return pref;
	} catch (JAXBException e) {
	    throw new IOException("Problem with unmarshaling objects", e);
	}
    }

}
