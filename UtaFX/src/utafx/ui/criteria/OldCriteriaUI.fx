package utafx.ui.criteria;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javax.swing.JTable;
import javafx.geometry.HPos;
import javafx.ext.swing.SwingComponent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import java.awt.Dimension;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.layout.Container;
import javafx.geometry.VPos;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import uta.Alternative;
import uta.Criterion;
import javafx.stage.Stage;
import javafx.scene.Scene;

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
public class OldCriteriaUI extends CustomNode {

    /*var criteria: Criterion[];/* on replace{
    for(c in criteria){
    model.addRow([c.name, c.type, c.segments]);
    }
    };*/
    //var table: TableView;
    //var model: TableModel;
    def BOTTOM_PADDING = 40;
    var columnNames = ["Name", "Type", "Segments"];
    var typesCombo = new JComboBox();
    /* TODO: This is very wrong dependency!!! */
    /* TODO: Fix me */
    var alternativesPOJO: Alternative[];
    var table = CriteriaTable {
                columns: bind for (cname in columnNames) {
                    TableColumn { text: cname };
                }
            }

    /**
    Adds new criterion at the ending posistion, with default values
     */
    public function add() {
        var c = Criterion {
                    name: "Criterion {sizeof table.rows + 1}";
                    type: "{typesCombo.getItemAt(0)}";
                    segments: 2;
                };
        insert TableRow {
            cells: [
                OldCriteriaUI.TableCell { text: c.name },
                OldCriteriaUI.TableCell { text: c.type },
                OldCriteriaUI.TableCell { text: "{c.segments}" },
            ]
        } into table.rows
    //insert Criterion {} into criteria;
    }

    /**
    Removes the selected criterion from the table
     */
    public function remove() {
        if (table.selection > -1) {
            //delete criteria[table.selection];
            delete  table.rows[table.selection];
            //table.table.getSelectionModel().setLeadSelectionIndex((sizeof table.rows)-1);
            table.selection = -1;
        }
    }

    public function getPOJO(): uta.Criterion[] {
        println("Executed getPOJO");
        var criteriaPOJO: uta.Criterion[];

        for (row in table.rows) {
            var i = indexof row;
            var name = "{table.model.getValueAt(i, 0)}";
            var origType = table.model.getValueAt(i, 1);
            var type: Integer =
                    if (origType == 'Cost') {
                        0
                    } else {
                        1
                    };

            var seg = Integer.parseInt("{table.model.getValueAt(i, 2)}");
            var c: uta.Criterion = new uta.Criterion(name, true, seg);
            insert c into criteriaPOJO;
            println("{c.getName()} {c.isGain()} {c.getNoOfSegments()}");
        }
        return criteriaPOJO;
    }

    public function getCriteriaNames(): String[] {
        for (r in table.rows) {
            table.model.getValueAt((indexof r), 0).toString();
        }
    }

    override function create(): Node {
        typesCombo.addItem("Gain");
        typesCombo.addItem("Cost");
        table.table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(typesCombo));
        //table.table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(segmentsSlider));
        //table.table.setPreferredSize(new Dimension(200, 200));
        VBox {
            spacing: 0
            content: [
                Container {
                    content: [
                        Rectangle {
                            fill: outerBorderFill
                            width: 300
                            height: 20
                        }

                        Label {
                            textAlignment: TextAlignment.CENTER
                            text: "Define your criterias"
                            vpos: VPos.CENTER
                            hpos: HPos.CENTER
                            layoutX: 100
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
                            width: 300
                            height: 30
                            fill: outerBorderFill
                        }

                        HBox {
                            padding: Insets { top: 5, bottom: 5, left: 0, right: 0 }
                            spacing: 10
                            layoutX: 100
                            hpos: HPos.CENTER
                            content: [
                                Button {
                                    text: "Add"
                                    action: add
                                    layoutX: 50
                                }
                                Button {
                                    text: "Remove"
                                    action: remove
                                    layoutX: 150
                                }
                                Button {
                                    text: "POJO"
                                    action: function() {
                                        getPOJO()
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    }

}

package class TableColumn {

    public var text: String;
}

package class TableCell {

    public var text: String;
}

package class TableRow {

    public var cells: TableCell[];
}

public class Criterion {

    public-read var name: String;
    public-read var type: String;
    public-read var segments: Integer = 2;
}

package class CriteriaTable extends SwingComponent {

    var table: JTable;
    var model: javax.swing.table.DefaultTableModel;
    var tHeight = 200;
    var tWidth = 300;
    public var selection: Integer;
    public var columns: TableColumn[] on replace {
                model = new javax.swing.table.DefaultTableModel(for (column in columns) column.text, 0);
                table.setModel(model);
            };
    public var rows: TableRow[] on replace oldValue[lo..hi] = newVals {
                for (index in [hi..lo step -1]) {
                    model.removeRow(index);
                }

                for (row in newVals) {
                    model.addRow(for (cell in row.cells) cell.text);
                }
            };

    public override function createJComponent() {
        table = new JTable();
        model =
                table.getModel() as javax.swing.table.DefaultTableModel;

        var selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(
        ListSelectionListener {
            public override function valueChanged(e: ListSelectionEvent) {
                selection = table.getSelectedRow();
            //println("Selection = {selection}")
            }
        });
        //var columnModel = table.getColumnModel().getColumn(0) as DefaultTableColumnModel;
        var pane: JScrollPane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(tWidth, tHeight));
        return pane;
    }
}

function run(){
    Stage {
       scene: Scene {
           content: OldCriteriaUI{}
       }

    }

}
