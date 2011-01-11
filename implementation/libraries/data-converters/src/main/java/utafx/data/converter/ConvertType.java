package utafx.data.converter;

public class ConvertType {

    private String sourceFormat;
    private String destinationFormat;

    public ConvertType(String src, String dst) {
	this.sourceFormat = src;
	this.destinationFormat = dst;
    }

    public ConvertType(FileFormat src, FileFormat dst) {
	this(src.name().toLowerCase(), dst.name().toLowerCase());
    }

    public String getSourceFormat() {
	return sourceFormat;
    }

    public String getDestinationFormat() {
	return destinationFormat;
    }

    public void setSourceFormat(String sourceFormat) {
	this.sourceFormat = sourceFormat;
    }

    public void setDestinationFormat(String destinationFormat) {
	this.destinationFormat = destinationFormat;
    }

    @Override
    public int hashCode() {
	return String.format("%s%s",sourceFormat, destinationFormat).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof ConvertType) {
	    ConvertType other = (ConvertType) obj;
	    if (this.sourceFormat.equalsIgnoreCase(other.sourceFormat)
		    && this.destinationFormat.equalsIgnoreCase(other.destinationFormat)) {
		return true;
	    }
	}
	return false;
    }
}
