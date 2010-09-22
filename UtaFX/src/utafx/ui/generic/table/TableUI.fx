/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.ui.generic.table;

import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javafx.ext.swing.SwingComponent;
import java.lang.Math;

/**
 * @author Pawcik
 */
public class TableUI extends SwingComponent {

    var table: JTable;
    var model: javax.swing.table.DefaultTableModel;
    def COLUMN_WIDTH = 80;
    
    public var tHeight:Integer = 200;
    public var tWidth:Integer = 300 on replace {
        println("Table width replaced with: {tWidth}");
        pane.setPreferredSize(new Dimension(tWidth, tHeight));
    }
    override var width = bind tWidth;
    override var height = bind tHeight;

    public var pane:JScrollPane;
    
    public var selectedRow: Integer;
    public var columns: TableColumn[] on replace {
                model = new javax.swing.table.DefaultTableModel(for (column in columns) column.text, 0);
                for (row in rows) {
                    model.addRow(for (cell in [row.cells, TableCell{}]) cell.text);
                }
                table.setModel(model);
                tWidth = (sizeof columns)*COLUMN_WIDTH;
            };
    public var rows: TableRow[] on replace oldValue[lo..hi] = newVals {
                for (index in [hi..lo step -1]) {
                    model.removeRow(index);
                    println("TableUI: removed row {index}")
                }

                for (row in newVals) {
                    model.addRow(for (cell in row.cells) cell.text);
                    println("TableUI: added row")
                }
                println("TableUI: rows on replace completed")
            };

    public function getValueAt(row: Integer, col: Integer): Object {
        return model.getValueAt(row, col);
    };

    public function getColumnModel(): TableColumnModel {
        return table.getColumnModel();
    };

    public function tableUpdated(row:Integer, col:Integer):Void{
        
    }

    public override function createJComponent() {          
        table = new JTable();
        model = table.getModel() as javax.swing.table.DefaultTableModel;
        
        var selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(
        ListSelectionListener {
            public override function valueChanged(e: ListSelectionEvent) {
                selectedRow = table.getSelectedRow();            
            }
        });        
        pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(tWidth, tHeight));
        return pane;
    }

}