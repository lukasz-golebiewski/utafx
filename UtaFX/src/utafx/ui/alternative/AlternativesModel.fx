/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utafx.ui.alternative;

import utafx.ui.generic.table.TableRow;
import uta.api.Alternative;
import uta.api.Criterion;

/**
 * @author Pawcik
 */

public class AlternativesModel {
    public var columnNames: String[];
    public var rows: TableRow[];
    public var alternatives:uta.api.Alternative[];
    public var criteriaPOJO: uta.api.Criterion[];
    //new approach
    public var alternativeNames:String[];

}
