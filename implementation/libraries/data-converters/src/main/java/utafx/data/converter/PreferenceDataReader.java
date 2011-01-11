package utafx.data.converter;

import java.io.IOException;
import java.io.InputStream;

import utafx.data.pref.jaxb.Preferences;

public interface PreferenceDataReader {
    Preferences read(InputStream is) throws IOException;
}
