package utafx.data.converter;

import java.io.IOException;
import java.io.OutputStream;

import utafx.data.pref.jaxb.Preferences;

public interface PreferenceDataWriter {
    void write(Preferences preferences, OutputStream output) throws IOException;
}
