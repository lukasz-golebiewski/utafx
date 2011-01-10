/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utafx.ui.criteria;

import utafx.ui.generic.table.TableRow;

/**
 * @author Pawcik
 */

public class CriteriaModel {
    public var columnNames: String[];
    public var rows: TableRow[];
    public var criteriaNames: String[];

    public function clear(){
        delete rows;
    }

    public function getPOJO(): uta.api.Criterion[] {
        var criteriaPOJO: uta.api.Criterion[];

        for (row in this.rows) {
            var i = indexof row;
            var name = "{row.cells[0].text}";
            var origType = "{row.cells[1].text}";
            var type: Integer =
                    if (origType == 'Cost') {
                        0
                    } else {
                        1
                    };

            var seg = Integer.parseInt("{row.cells[2].text}");
            var c: uta.api.Criterion = new uta.api.Criterion(name, type == 1, seg);
            insert c into criteriaPOJO;            
        }
        return criteriaPOJO;
    }
}
