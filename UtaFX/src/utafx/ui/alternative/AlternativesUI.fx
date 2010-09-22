package utafx.ui.alternative;

import javafx.scene.CustomNode;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import uta.Alternative;
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
import uta.Criterion;
import utafx.ui.generic.table.TableCell;
import utafx.ui.generic.table.TableColumn;
import utafx.ui.generic.table.TableRow;
import utafx.ui.generic.table.TableUI;

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

    public var criteriaPOJO: uta.Criterion[];

    public var model: AlternativesModel;
    
    var table = TableUI {
           columns: bind for (cname in model.columnNames) {
                 TableColumn { text: cname };
           }
           rows: bind model.rows
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

    public function getPOJO(): uta.Alternative[] {
        println("Executed alternativesUI.getPOJO()");
        var alternativesPOJO: Alternative[];

        for (row in model.rows) {
            var i = indexof row;
            var name = "{table.getValueAt(i, 0)}";
            var values: Double[] =
                    for (j in [1..<sizeof model.columnNames]) {
                        Double.parseDouble("{table.getValueAt(i, j)}");
                    }
            var a = new uta.Alternative();
            a.setName(name);
            a.setValues(values);
            a.setCriteria(criteriaPOJO);
            println("{a.getName()} {Arrays.toString(a.getValues())}");
            insert a into alternativesPOJO;
        }
        return alternativesPOJO;
    }

    override function create(): Node {
        //table.table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(typesCombo));
        //table.table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(segmentsSlider));
        //table.table.setPreferredSize(new Dimension(200, 200));
        VBox {
            hpos: HPos.CENTER
            spacing: 0
            content: [
                Container {
                    var rect:Rectangle
                    var label:Label;
                    content: [
                        rect = Rectangle {
                            fill: outerBorderFill
                            width: bind table.width
                            height: 20
                        }

                        label = Label {
                            textAlignment: TextAlignment.CENTER
                            text: "Define your possible choices"
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
                            padding: Insets { top: 5, bottom: 5, left: 0, right: 0 }
                            spacing: 10
                            layoutX: 100
                            hpos: HPos.CENTER
                            content: [
                                Button {
                                    text: "Add"
                                    action: add
                                    layoutX: 200
                                }
                                Button {
                                    text: "Remove"
                                    action: remove
                                    layoutX: 350
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