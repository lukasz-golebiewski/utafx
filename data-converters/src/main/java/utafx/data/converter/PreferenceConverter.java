package utafx.data.converter;

import java.io.IOException;

public class PreferenceConverter {

    private String[] extensions = {".xls", ".csv", ".ods", ".xml"};

    /**
     * Converts given input file to output file. This method performs
     * the bi-diectional conversion, thus file extensions will be checked
     * for conversion type. 
     * 
     * @param inputPath
     *            path to input file
     * @param outputPath
     *            path where to store file (can be directory)
     * @throws IOException
     *             if any I/O error occurs
     *             
     */
    public void convert(String inputPath, String outputPath) throws IOException {
	throw new UnsupportedOperationException("not implemented yet");
    }
    
    public String[] getSupportedFormats(){
	return extensions;
    }
}
