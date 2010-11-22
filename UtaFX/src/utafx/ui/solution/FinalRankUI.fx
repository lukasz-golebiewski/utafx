/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.ui.solution;

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
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Tile;
import uta.Ranking;
import javafx.geometry.Insets;

def outerBorderFill = LinearGradient {
            startX: 0.0 startY: 0.0 endX: 0.0 endY: 1.0
            stops: [
                Stop { offset: 0.0 color: Color.web("#F4F4F4") },
                Stop { offset: 0.5 color: Color.web("#BCBCBC") }
            ]
        };

def KENDALL_LEFT_PADDING = 10;


/**
 * @author Pawcik
 */
public class FinalRankUI extends CustomNode {

    public var functions: LinearFunction[];
    public var alterns: Alternative[];
    //public var columnNames: String[];
    public var model: AlternativesModel;
    var table: TableUI;
    var kendallRate: Tile;
    var kendallCheckBox: CheckBox;
    var valueBox: HBox;
    public var sortedRank: Ranking;
    var kendallValue: Double;
    var solver: UtaStarSolver = new UtaStarSolver();
    var rankUtils = new RankingUtils();
    public var solutionUI: SolutionUI;

    var preserveKendallRate = bind kendallCheckBox.selected on replace {
           solutionUI.freezedKendall = kendallCheckBox.selected;
    }

    //each time this reference change, all related data will be updated
    public var refRank: Ranking on replace {
                update();
            }

    init {        
        kendallRate = Tile {
                    vertical: true

                    content: [
                        valueBox = HBox {
                                    spacing: 5
                                    //height: 50
                                    content: [
                                        Label {
                                            text: bind "Kendall Rate: {kendallValue}"
                                        }
//                                        TextBox {
//                                            lines: 1
//                                            multiline: false
//                                            editable: false
//                                            columns: 5
//                                            text: bind "{kendallValue}"
//                                        }
                                    ]
                                }
                        kendallCheckBox = CheckBox {
                                    selected: false
                                    text: "Preserve Kendall Rate"
                                },
                    ]
                };
        kendallRate.padding = Insets{left: KENDALL_LEFT_PADDING};
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
                    //hpos: HPos.CENTER
                    //height: 200
                    content: bind [table,
                        kendallRate]
                }]
        }
    }

    public function update() {
        sortedRank = rankUtils.buildRank(functions, alterns);
        updateTable();
        updateKendallRate();
    }

    function updateTable() {
        table = TableUI {
            		id : "finalRankTable"
                    columns: bind for (c in model.columnNames) {
                        TableColumn {
                            text: c
                        }
                    }
                    rows: for (a in sortedRank.getAlternatives()) {
                        var aCast = a as uta.Alternative;
                        var size = sizeof model.columnNames;
                        TableRow {
                            cells: for (c in model.columnNames) {
                                var index = indexof c;                                
                                TableCell {
                                    text: if (index == 0) {
                                        aCast.getName()
                                    } else if (index == (size - 1)) {
                                        "{solver.getGeneralUtil(functions, aCast)}";
                                    } else {
                                        "{aCast.getValues()[index-1]}";
                                    }
                                }
                            }
                        }
                    }
                };
    }

    function updateKendallRate() {
        kendallValue = rankUtils.getKendallsCoefficient(refRank, sortedRank);
    }

}

function run() {
    Stage {
        scene: Scene {
            content: FinalRankUI {
            }
        }
    }

}
