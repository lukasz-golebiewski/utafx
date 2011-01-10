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

    public function getPOJO(): uta.api.Alternative[] {
        var alternativesPOJO: Alternative[] = [];

        for (row in this.rows) {
            var i = indexof row;
            var name = "{row.cells[0].text}";
            var values: Double[] =
                    for (j in [1..<sizeof this.columnNames]) {
                        Double.parseDouble("{row.cells[j].text}");
                    }
            var a = new uta.api.Alternative();
            a.setName(name);
            a.setValues(values);
            insert a into alternativesPOJO;
        }
        return alternativesPOJO;
    }

}
