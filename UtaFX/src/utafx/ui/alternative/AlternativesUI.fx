package utafx.ui.alternative;

import javafx.scene.CustomNode;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import uta.api.Alternative;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Container;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import java.util.Arrays;
import uta.api.Criterion;
import utafx.ui.generic.table.TableCell;
import utafx.ui.generic.table.TableColumn;
import utafx.ui.generic.table.TableRow;
import utafx.ui.generic.table.TableUI;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import java.util.Date;

def outerBorderFill = LinearGradient {
            startX: 0.0 startY: 0.0 endX: 0.0 endY: 1.0
            stops: [
                Stop { offset: 0.0 color: Color.web("#F4F4F4") },
                Stop { offset: 0.5 color: Color.web("#BCBCBC") }
            ]
        };

/**
 * @author Pawcik
 */
public class AlternativesUI extends CustomNode {

    public var model: AlternativesModel;
    var showLogs = true;
    public-read var table = TableUI {
                columns: bind for (cname in model.columnNames) {
                    TableColumn { text: cname };
                }
                rows: bind model.rows
            }

    postinit {
        table.addTableModelListener(TableModelListener {
            public override function tableChanged(e: TableModelEvent): Void {
                var tm = e.getSource() as TableModel;
                var row = e.getFirstRow();
                var lastRow = e.getLastRow();
                var col = e.getColumn();
                var type = e.getType();
                var sType = if (type == e.INSERT) "INSERT" else if (type == e.DELETE) "DELETE" else if (type == e.UPDATE) "UPDATE" else "TYPE-{type}";

                if (showLogs) println("{new Date()}: Table changed: firstrow={row} lastRow={lastRow} column={col} type={sType}");

                if (type == e.INSERT) {
                    for (i in [row..lastRow]) {
                        var rowsCount = tm.getRowCount();
                        var columnCount = tm.getColumnCount();
                        var cellValue = "{tm.getValueAt(i, 0)}";
                        insert cellValue into model.alternativeNames;
                    }
                } else if (type == e.DELETE) {
                    for (i in [row..lastRow]) {
                        delete model.alternativeNames[i] from model.alternativeNames;
                    }
                } else {
                    for (i in [row..lastRow]) {
                        var value = tm.getValueAt(i, col);
                        model.rows[i].cells[col].text = "{value}";
                        if (col == 0) {
                            if (value != model.alternativeNames[i]) {
                                model.alternativeNames[i] = "{value}";
                            } }
                    }
                }
                if (showLogs) println("AlternativeNames: {model.alternativeNames}");

                if (row > -1 and col > -1) {
                    var value = tm.getValueAt(row, col);
                    if (showLogs) println("{new Date()}: Table changed at [{row}, {col}] = {value}");

                }
            }
        });
        if (showLogs) println("{new Date()} TableModel listener registered");
    }

    public function add() {
        var row = TableRow {
                    cells: for (c in model.columnNames) {
                        TableCell {
                            text: if ((indexof c) == 0) {
                                "Alternative {sizeof model.rows + 1}"
                            } else {
                                "{sizeof model.rows + 1}"
                            }
                        }
                    }
                }
        insert row into model.rows
    }

    /**
    Removes the selected criterion from the table
     */
    public function remove() {
        if (table.selectedRow > -1) {
            //delete criteria[table.selection];
            delete  model.rows[table.selectedRow];
            //table.table.getSelectionModel().setLeadSelectionIndex((sizeof table.rows)-1);
            table.selectedRow = -1;
        }
    }

    public function getPOJO(): uta.api.Alternative[] {
        return model.getPOJO();
    }

    override function create(): Node {
        //table.table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(typesCombo));
        //table.table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(segmentsSlider));
        //table.table.setPreferredSize(new Dimension(200, 200));
        VBox {
            spacing: 0
            content: [
                Container {
                    var rect: Rectangle
                    var label: Label;
                    content: [
                        rect = Rectangle {
                                    fill: outerBorderFill
                                    width: bind table.width
                                    height: 20
                                }

                        label = Label {
                                    textAlignment: TextAlignment.CENTER
                                    text: "Define alternatives"
                                    vpos: VPos.CENTER
                                    hpos: HPos.CENTER
                                    layoutX: bind (rect.boundsInLocal.width - label.boundsInLocal.width) / 2
                                    layoutY: 5
                                }
                    ]
                }

                HBox {
                    hpos: HPos.CENTER
                    height: 200
                    content: bind table
                },
                /*table = TableView {
                tableModel: model = new DefaultTableModel(["Name", "Type", "Segments"]);
                layoutInfo: LayoutInfo {
                width: 400
                height: 400
                hpos: HPos.CENTER
                }
                },*/
                Container {
                    content: [
                        Rectangle {
                            width: bind table.width
                            height: 30
                            fill: outerBorderFill
                        }

                        HBox {
                            var addButton: Button;;
                            var removeButton: Button;
                            padding: Insets { top: 5, bottom: 5, left: 0, right: 0 }
                            spacing: 10
                            //layoutX: 100
                            hpos: HPos.CENTER
                            content: [
                                addButton = Button {
                                            text: "Add"
                                            id: "Add alternative"
                                            action: add
                                        //layoutX: 200
                                        }
                                removeButton = Button {
                                            text: "Remove"
                                            id: "Remove alternative"
                                            action: remove
                                        //layoutX: 350
                                        }
                            //                                Button {
                            //                                    text: "POJO"
                            //                                    action: function() {
                            //                                        getPOJO()
                            //                                    }
                            //                                }
                            ]
                            layoutX: bind (table.width - addButton.boundsInLocal.width - removeButton.boundsInLocal.width) / 2;
                        }
                    ]
                }
            ]
        }
    }

}
