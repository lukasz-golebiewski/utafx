package utafx.ui.criteria;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.HPos;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.layout.Container;
import javafx.geometry.VPos;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import uta.api.Criterion;
import utafx.ui.generic.table.TableColumn;
import utafx.ui.generic.table.TableRow;
import utafx.ui.generic.table.TableCell;
import utafx.ui.generic.table.TableUI;
import java.util.Date;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

def outerBorderFill = LinearGradient {
            startX: 0.0 startY: 0.0 endX: 0.0 endY: 1.0
            stops: [
                Stop { offset: 0.0 color: Color.web("#F4F4F4") },
                Stop { offset: 0.5 color: Color.web("#BCBCBC") }
            ]
        };
def INFO_LABEL_HEIGHT = 20;

/**
 * @author Pawcik
 */
public class CriteriaUI extends CustomNode {

    var typesCombo = new JComboBox();
    var showLogs = false;
    public var model = CriteriaModel {
                columnNames: ["Name", "Type", "Segments"];
                rows: [];
                criteriaNames: []
            };
    public-read var table = TableUI {
                columns: bind for (cname in model.columnNames) {
                    TableColumn { text: cname };
                }
                rows: bind model.rows
            //width: bind layoutBounds.width;
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
                        insert cellValue into model.criteriaNames;
                    }
                } else if (type == e.DELETE) {
                    for (i in [row..lastRow]) {
                        delete model.criteriaNames[i] from model.criteriaNames;
                    }
                } else {
                    for (i in [row..lastRow]) {
                        var value = tm.getValueAt(i, col);
                        model.rows[i].cells[col].text = "{value}";
                        if (col == 0) {
                            if (value != model.criteriaNames[i]) {
                                model.criteriaNames[i] = "{value}";
                            }
                        }
                    }
                }
                //if (showLogs) println("CriteriaNames: {criteriaNames}");
                if (row > -1 and col > -1) {
                    var value = tm.getValueAt(row, col);
                    if (showLogs) println("{new Date()}: Table changed at [{row}, {col}] = {value}");

                }
            }
        });
        if (showLogs) println("{new Date()} TableModel listener registered");
    }

    /**
    Adds new criterion at the ending posistion, with default values
     */
    public function add() {
        var c = utafx.ui.criteria.Criterion {
                    name: "Criterion {sizeof model.rows + 1}";
                    type: "{typesCombo.getItemAt(0)}";
                    segments: 2;
                };
        insert TableRow {
            cells: [
                TableCell { text: c.name },
                TableCell { text: c.type },
                TableCell { text: "{c.segments}" },
            ]
        } into model.rows
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

    public function getPOJO(): uta.api.Criterion[] {
        if (showLogs) println("Executed getPOJO");
        var criteriaPOJO: uta.api.Criterion[];

        for (row in model.rows) {
            var i = indexof row;
            var name = "{table.getValueAt(i, 0)}";
            var origType = table.getValueAt(i, 1);
            var type: Integer =
                    if (origType == 'Cost') {
                        0
                    } else {
                        1
                    };

            var seg = Integer.parseInt("{table.getValueAt(i, 2)}");
            var c: uta.api.Criterion = new uta.api.Criterion(name, type == 1, seg);
            insert c into criteriaPOJO;
            if (showLogs) println("{c.getName()} {c.isGain()} {c.getNoOfSegments()}");
        }
        return criteriaPOJO;
    }

    public function getCriteriaNames(): String[] {
        for (r in table.rows) {
            table.getValueAt((indexof r), 0).toString();
        }
    }

    override function create(): Node {
        typesCombo.addItem("Gain");
        typesCombo.addItem("Cost");
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(typesCombo));
        VBox {
            spacing: 0
            //padding: Insets { left: 100 right:30}
            content: [
                Container {
                    var rect: Rectangle;
                    var label: Label;
                    content: [
                        rect = Rectangle {
                                    fill: outerBorderFill
                                    width: bind table.width
                                    height: bind INFO_LABEL_HEIGHT;
                                }

                        label = Label {
                                    textAlignment: TextAlignment.CENTER
                                    text: "Define criteria"
                                    vpos: VPos.CENTER
                                    hpos: HPos.CENTER
                                    layoutX: bind (rect.boundsInLocal.width - label.boundsInLocal.width) / 2
                                    layoutY: 5
                                }
                    ]
                }

                HBox {
                    height: 200
                    content: bind table
                },

                Container {
                    content: [
                        Rectangle {
                            width: bind table.width
                            height: 30
                            fill: outerBorderFill
                        }

                        HBox {
                            padding: Insets { top: 5, bottom: 5, left: 0, right: 0 }
                            spacing: 10
                            hpos: HPos.CENTER
                            def addButton = Button {
                                        text: "Add"
                                        id: "Add criterion"
                                        action: add
                                    }
                            def removeButton = Button {
                                        text: "Remove"
                                        id: "Remove criterion"
                                        action: remove
                                    }
                            //                            def pojoCheck = Button {
                            //                                        text: "Pojo"
                            //                                        action: function() {
                            //                                            getPOJO()
                            //                                        }
                            //                                    }
                            content: [
                                addButton,
                                removeButton,
                            //pojoCheck
                            ]
                            layoutX: bind (table.width - addButton.boundsInLocal.width - removeButton.boundsInLocal.width) / 2;// - pojoCheck.boundsInLocal.width - 10) / 2
                        }
                    ]
                }
            ]
        }
    }

}
