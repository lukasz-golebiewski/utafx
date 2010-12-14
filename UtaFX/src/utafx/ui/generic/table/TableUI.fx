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

    def COLUMN_WIDTH = 110;
    public var showLogs = true;
    public var tHeight: Integer = 200;
    public var tWidth: Integer = 300 on replace {
                if(showLogs) println("{new Date()}: (tWidth on replace) Table width replaced with: {tWidth}");
                pane.setPreferredSize(new Dimension(tWidth, tHeight));
            }
    override var width = bind tWidth;
    override var height = bind tHeight;
    var table: JTable;
    var nativeModel: javax.swing.table.DefaultTableModel;
    var tmListeners: TableModelListener[];
    public var pane: JScrollPane;
    public var selectedRow: Integer;
    public var columns: TableColumn[] on replace {
                nativeModel = new javax.swing.table.DefaultTableModel(for (column in columns) column.text, 0);
                for (row in rows) {
                    nativeModel.addRow(for (cell in [row.cells, TableCell {}]) cell.text);
                }
                table.setModel(nativeModel);
                if(showLogs) println("{new Date()}: (columns on replace) New model has been set");
                reregisterModelChangeListeners();
                tWidth = (sizeof columns) * COLUMN_WIDTH;
            };
    public var rows: TableRow[] on replace oldValue[lo..hi] = newVals {
                for (index in [hi..lo step -1]) {
                    nativeModel.removeRow(index);
                    if(showLogs) println("{new Date()}: TableUI: removed row {index}")
                }

                for (row in newVals) {
                    nativeModel.addRow(for (cell in row.cells) cell.text);
                    if(showLogs) println("TableUI: added row")
                }
                if(showLogs) println("{new Date()}: TableUI: rows on replace completed")
            };

    public function getValueAt(row: Integer, col: Integer): Object {
        return nativeModel.getValueAt(row, col);
    };

    public function getColumnModel(): TableColumnModel {
        return table.getColumnModel();
    };

    function reregisterModelChangeListeners() {
        if(showLogs) println("Re-registering {sizeof tmListeners} table model listener");
        for (tml in tmListeners) {
            if(showLogs) println("Re-registering table model listener");            
            table.getModel().addTableModelListener(tml);
        }
    }

    public override function createJComponent() {
        if(showLogs) println("{new Date()} JComponent creation started");
        table = new JTable();
        registerSelectionListener();
        pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(tWidth, tHeight));
        if(showLogs) println("{new Date()} JComponent created");
        return pane;
    }

    function registerSelectionListener() {
        var selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(
        ListSelectionListener {
            public override function valueChanged(e: ListSelectionEvent) {
                selectedRow = table.getSelectedRow();
                if(showLogs) println("{new Date()}: Selected row: {selectedRow}");
            }
        });
        if(showLogs) println("{new Date()} Selection listener registered");
    }

    public function addSelectionListener(listener: ListSelectionListener) {
        if (listener != null) {
            table.getSelectionModel().addListSelectionListener(listener);
        }
    }

    public function addTableModelListener(listener: TableModelListener) {
        if (listener != null) {
            insert listener into tmListeners;
            table.getModel().addTableModelListener(listener);
        }
    }

    public function removeTableModelListener(listener: TableModelListener) {
        if (listener != null) {
            delete listener from tmListeners;
            table.getModel().removeTableModelListener(listener);
        }
    }

    public function getTableModelListeners(): TableModelListener[] {
        return tmListeners;
    }

}
