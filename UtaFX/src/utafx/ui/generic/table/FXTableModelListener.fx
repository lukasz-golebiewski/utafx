/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utafx.ui.generic.table;

import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

/**
 * @author Pawcik
 */

public class FXTableModelListener extends TableModelListener{
    override public function tableChanged (e : TableModelEvent) : Void {
        var row = e.getFirstRow();
        var column = e.getColumn();
        var model = e.getSource() as TableModel;
        var columnName = model.getColumnName(column);
        var data = model.getValueAt(row, column);
        println("Data changed at [{row}, {column}]={data}")
    }
}
