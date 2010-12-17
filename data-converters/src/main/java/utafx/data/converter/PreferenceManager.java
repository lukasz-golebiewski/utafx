package utafx.data.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import utafx.data.pref.jaxb.Preferences;

public class PreferenceManager {

    public Preferences read(InputStream prefXml) throws JAXBException,
	    IOException {
	JAXBContext context = JAXBContext.newInstance("utafx.data.pref.jaxb");
	Unmarshaller unmarshaller = context.createUnmarshaller();
	Preferences pref = (Preferences) unmarshaller.unmarshal(prefXml);
	return pref;
    }

    public void write(Preferences pref, OutputStream prefXml)
	    throws JAXBException, IOException {
	JAXBContext context = JAXBContext.newInstance("utafx.data.pref.jaxb");
	Marshaller marshaller = context.createMarshaller();
	marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	marshaller.marshal(pref, prefXml);
    }
}
