package utafx.data.selection;

/**
 * Value object, storing area of excel sheet data for processing.
 * 
 * @author <a href="mailto:marzec12@poczta.onet.pl">Pawel Solarski</a>
 */
public class SelectionArea {
    private CellAddress start;
    private CellAddress end;

    /**
     * Creates new selection area with starting cell address and ending address,
     * taken from arguments
     * 
     * @param start
     *            starting address
     * @param end
     *            ending address
     */
    public SelectionArea(CellAddress start, CellAddress end) {
	this.start = start;
	this.end = end;
    }

    /**
     * Creates new selection area with starting cell address and no ending
     * address
     */
    public SelectionArea(CellAddress start) {
	this(start, null);
    }

    /**
     * Creates new selection area with starting cell address at [0,0] and no
     * ending address
     */
    public SelectionArea() {
	this(new CellAddress(0, 0), null);
    }

    public CellAddress getStart() {
	return start;
    }

    public CellAddress getEnd() {
	return end;
    }

    public void setStart(CellAddress start) {
	this.start = start;
    }

    public void setEnd(CellAddress end) {
	this.end = end;
    }

    // TODO: overwrite equals and hashcode if needed

}
