/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.ui.rank;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import uta.Alternative;
import uta.LinearFunction;
import uta.UtaStarSolver;
import javafx.geometry.HPos;
import javafx.scene.layout.Container;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Stop;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;
import javafx.scene.layout.HBox;
import utafx.ui.generic.table.TableCell;
import utafx.ui.generic.table.TableRow;
import utafx.ui.generic.table.TableUI;
import utafx.ui.generic.table.TableColumn;
import javafx.scene.control.TextBox;
import javafx.scene.control.CheckBox;
import uta.RankingUtils;
import utafx.ui.alternative.AlternativesModel;

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
public class FinalRankUI extends CustomNode {

    public var functions: LinearFunction[];
    public var alterns: Alternative[];
    //public var columnNames: String[];

    public var model: AlternativesModel;

    var solver: UtaStarSolver;
    var table: TableUI;
    var kendallRate: Container;
    var rankUtils: RankingUtils;
    var kendallValue: Double;
    var kendallCheckBox: CheckBox;
    var preserveKendallRate = bind kendallCheckBox.selected;

    postinit {
        //insert "Name" before columnNames[0];
        //insert "Value" into columnNames;
        rankUtils = new RankingUtils();
        solver = new UtaStarSolver();
        var sortedRank = rankUtils.buildRank(functions, alterns);
        table = TableUI {
                    columns: for (c in model.columnNames) {
                        TableColumn {
                            text: c
                        }
                    }
                    rows: for (a in sortedRank.getAlternatives()) {
                        TableRow {
                            var i=-1;
                            cells: for (c in model.columnNames) {
                                println("Column name is {c} index of c is {indexof c}");
                                var index = indexof c;
                                var size = sizeof model.columnNames;
                                i++;
                                var dupa = i-1;
                                TableCell {
                                    text: if (i==0) {
                                        println("{a.getName()}");
                                        "{a.getName()}";
                                    } else if (i==(size-1)) {
                                        println("Last");
                                        "{solver.getGeneralUtil(functions, a)}";
                                    } else {
                                        println("{a.getValues()[dupa]}");
                                        "{a.getValues()[dupa]}";
                                    }
                                }
                            }
                        }
                    }
                };
                kendallRate = VBox{
                    spacing:5;
                    content:[
                        HBox{
                            spacing: 5
                            content:[
                                Label{
                                    text: "Kendall Rate"
                                }
                                TextBox{
                                    lines:1
                                    multiline:false
                                    editable: false
                                    text: bind "{kendallValue}"
                                }

                            ]
                        }
                        kendallCheckBox = CheckBox{
                           selected: false
                           text: "Preserve Kendall Rate"
                        }
                    ]
                };

    }

    public override function create(): Node {
        VBox {
            //hpos: HPos.CENTER
            spacing: 0
            content: [
                Container {
                    content: [
                        Rectangle {
                            fill: outerBorderFill
                            width: bind table.width
                            height: 20
                        }

                        Label {
                            textAlignment: TextAlignment.CENTER
                            text: "Final Ranking"
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
                    content: bind [table,
                    kendallRate]
                }]
        }
    }

}
    


