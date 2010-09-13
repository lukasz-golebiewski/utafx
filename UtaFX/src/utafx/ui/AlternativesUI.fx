package utafx.ui;

import javafx.scene.CustomNode;
import javax.swing.JTable;
import javafx.ext.swing.SwingComponent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;
import java.awt.Dimension;
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
import java.lang.Integer;
import utafx.ui.CriteriaUI.TableCell;
import utafx.ui.CriteriaUI.TableColumn;
import utafx.ui.CriteriaUI.TableRow;
import java.util.Arrays;

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
    var columnNames: String[];

    init {
        insert "Name" into columnNames;
        for(c in criteriaPOJO){
            insert c.getName() into columnNames
        }
    }
    
    var table = AlternativesTable {
                columns: bind for (c in columnNames){
                    CriteriaUI.TableColumn{
                        text: c
                    }
                }
    }
    
    public function add() {
        var row = CriteriaUI.TableRow{
            cells: for (c in columnNames){
                TableCell{
                    text: if ((indexof c) == 0){
                        "Alternative {sizeof table.rows + 1}"
                    } else {
                        "{sizeof table.rows +1}"
                    }
                }
            }
        }
        insert row into table.rows
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

    public function getPOJO(): uta.Alternative[] {
        println("Executed alternativesUI.getPOJO()");
        var alternativesPOJO: Alternative[];

        for(row in table.rows){
            var i = indexof row;
            var name = "{table.model.getValueAt(i, 0)}";
            var values: Double[] =
            for(j in [1.. sizeof criteriaPOJO]){
                
                Double.parseDouble("{table.model.getValueAt(i, j)}");
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
                    content: [
                        Rectangle {
                            fill: outerBorderFill
                            width: 600
                            height: 20
                        }

                        Label {
                            textAlignment: TextAlignment.CENTER
                            text: "Define your possible choices"
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
                            width: 600
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


package class AlternativesTable extends SwingComponent {

    var table: JTable;
    var model: javax.swing.table.DefaultTableModel;
    var tHeight = 200;
    var tWidth = 600;
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