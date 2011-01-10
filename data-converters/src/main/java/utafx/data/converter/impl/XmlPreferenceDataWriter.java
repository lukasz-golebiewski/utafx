package utafx.data.converter.impl;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import utafx.data.converter.PreferenceDataWriter;
import utafx.data.converter.PreferenceManager;
import utafx.data.pref.jaxb.Preferences;

public class XmlPreferenceDataWriter implements PreferenceDataWriter {

    public void write(Preferences pref, OutputStream output) throws IOException {
	PreferenceManager writer = new PreferenceManager();
	try {
	    writer.write(pref, output);
	} catch (JAXBException e) {
	    throw new IOException("JAXB error occured", e);
	}
    }
}
