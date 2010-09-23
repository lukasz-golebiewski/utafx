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
import javax.swing.event.TableModelListener;
import java.util.Date;

/**
 * @author Pawcik
 */
public class TableUI extends SwingComponent {

    def COLUMN_WIDTH = 80;

    public var tHeight: Integer = 200;
    public var tWidth: Integer = 300 on replace {
                println("{new Date()}: (tWidth on replace) Table width replaced with: {tWidth}");
                pane.setPreferredSize(new Dimension(tWidth, tHeight));
            }
    
    override var width = bind tWidth;
    override var height = bind tHeight;

    var table: JTable;
    var model: javax.swing.table.DefaultTableModel;

    var tmListeners: TableModelListener[] on replace old {
        for(tml in old){
            removeTableModelListener(tml);
        }
        reregisterModelChangeListeners();
    }

    public var pane: JScrollPane;
    public var selectedRow: Integer;
    public var columns: TableColumn[] on replace {
                model = new javax.swing.table.DefaultTableModel(for (column in columns) column.text, 0);
                for (row in rows) {
                    model.addRow(for (cell in [row.cells, TableCell {}]) cell.text);
                }
                table.setModel(model);
                println("{new Date()}: (columns on replace) New model has been set");
                reregisterModelChangeListeners();
                tWidth = (sizeof columns) * COLUMN_WIDTH;
            };
    public var rows: TableRow[] on replace oldValue[lo..hi] = newVals {
                for (index in [hi..lo step -1]) {
                    model.removeRow(index);
                    println("{new Date()}: TableUI: removed row {index}")
                }

                for (row in newVals) {
                    model.addRow(for (cell in row.cells) cell.text);
                    println("TableUI: added row")
                }
                println("{new Date()}: TableUI: rows on replace completed")
            };

    public function getValueAt(row: Integer, col: Integer): Object {
        return model.getValueAt(row, col);
    };

    public function getColumnModel(): TableColumnModel {
        return table.getColumnModel();
    };

    function reregisterModelChangeListeners() {
        for(tml in tmListeners){
            addTableModelListener(tml);
        }
    }

    public override function createJComponent() {
        println("{new Date()} JComponent creation started");
        table = new JTable();
        registerSelectionListener();
        pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(tWidth, tHeight));
        println("{new Date()} JComponent created");
        return pane;
    }

    function registerSelectionListener() {
        var selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(
        ListSelectionListener {
            public override function valueChanged(e: ListSelectionEvent) {
                selectedRow = table.getSelectedRow();
                println("{new Date()}: Selected row: {selectedRow}");
            }
        });
        println("{new Date()} Selection listener registered");
    }

    public function addSelectionListener(listener: ListSelectionListener) {
        if (listener != null) {
            table.getSelectionModel().addListSelectionListener(listener);
        }
    }

    public function addTableModelListener(listener: TableModelListener) {
        if (listener != null) {
            table.getModel().addTableModelListener(listener);
        }
    }

    public function removeTableModelListener(listener: TableModelListener) {
        if (listener != null) {
            table.getModel().removeTableModelListener(listener);
        }
    }

    public function getTableModelListeners(): TableModelListener[]{
        return tmListeners;
    }


}
