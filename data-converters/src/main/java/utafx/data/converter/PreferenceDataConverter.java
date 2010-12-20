package utafx.data.converter;

import java.io.OutputStream;

import utafx.data.exception.ConversionException;
import utafx.data.pref.jaxb.Preferences;

public interface PreferenceDataConverter extends DataConverter {
    void convert(Preferences preferences, OutputStream output) throws ConversionException;
}
