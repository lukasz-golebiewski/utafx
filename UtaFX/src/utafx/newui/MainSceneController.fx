/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.newui;

import utafx.ui.alternative.AlternativesUI;
import utafx.ui.criteria.CriteriaUI;
import utafx.ui.rank.ReferenceRankUI;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import utafx.ui.alternative.AlternativesModel;
import utafx.ui.rank.ReferenceRankModel;
import uta.Ranking;
import uta.Criterion;
import uta.Alternative;
import uta.UtaStarSolver;
import uta.LinearFunction;
import uta.ConstraintsManager;
import uta.ConstraintsManagerFactory;
import utafx.ui.solution.FinalRankUI;
import utafx.control.ReferenceLinker;
import javafx.scene.layout.Flow;
import utafx.ui.solution.ChartUI;
import utafx.ui.solution.ChartUI.ChartUIData;
import utafx.ui.solution.ChartListener;
import utafx.ui.solution.ChartEvent;
import com.sun.javafx.runtime.sequence.Sequences;
import javafx.scene.layout.HBox;

/**
 * @author LG
 */
public class MainSceneController {

    package var criteriaUI: CriteriaUI;
    package var alternativesUI: AlternativesUI;
    package var referenceRankUI: ReferenceRankUI;
    package var finalRankUI: FinalRankUI;
    package var chartsHBox: HBox;
    var charts: ChartUI[];
    var constraintManager: ConstraintsManager;

    init {
        alternativesUI.model = AlternativesModel {
                    columnNames: bind ["Name", criteriaUI.model.criteriaNames];
                    criteriaPOJO: bind criteriaUI.getPOJO();
                }
        referenceRankUI.model = ReferenceRankModel {
                    alternativeNames: bind alternativesUI.model.alternativeNames
                }
    }

    public var freezedKendall: Boolean = false on replace {
                constraintManager = new ConstraintsManagerFactory(freezedKendall).createConstraintsManager(finalRankUI.functions, getReferenceRankData(),
                        finalRankUI.sortedRank);
            }

    public function onNextState(currentState: Integer) {
        if (currentState == 2) {
            solve();
        }
    }

    public function solve() {
        delete chartsHBox.content;
        delete charts;
        var refRank: Ranking = referenceRankUI.getPOJO();
        var criterias: Criterion[] = criteriaUI.getPOJO();
        var alterns: Alternative[] = alternativesUI.getPOJO();

        var linker = ReferenceLinker {};
        alterns = linker.interconnectReferences(criterias, alterns);
        refRank = linker.interconnectReferences(alterns, refRank);

        var solver: UtaStarSolver = new UtaStarSolver();
        var functs: LinearFunction[] = solver.solve(refRank, criterias, alterns);

        var manager: ConstraintsManager = new ConstraintsManagerFactory(false).createConstraintsManager(functs, null, null);
        finalRankUI.alterns = alterns;
        finalRankUI.functions = functs;
        //finalRankUI.model = alternativesUI.model;
        finalRankUI.model = AlternativesModel {
                        columnNames: bind ["Name", criteriaUI.model.criteriaNames, "Utillity"]
                    };
        finalRankUI.refRank = refRank;
        finalRankUI.update();


        constraintManager = new ConstraintsManagerFactory(false).createConstraintsManager(finalRankUI.functions, getReferenceRankData(),
                finalRankUI.sortedRank);

        for (f in functs) {
            var currentFun = f;
            var c = f.getCriterion();
            var chartUI: ChartUI;
            var chartData = ChartUIData {
                        x: currentFun.getCharacteristicPoints()
                        y: currentFun.getValues();
                    }
            chartData.addChartListener(ChartListener {
                override public function dataChanged(e: ChartEvent): Void {
                    //update bounds of all charts
                    //if (showLogs) println("Updating bounds of {sizeof charts}");
                    for (chart in charts) {
                        chart.update();
                    }
                }
            });
            chartData.addChartListener(ChartListener {
                override public function dataChanged(e: ChartEvent): Void {
                    //update reference rank od final rank
                    //if (showLogs) println("Updating bounds of {sizeof charts} charts");
                    finalRankUI.refRank = getReferenceRankData();
                }
            });
            //insert chartData into charts;
            chartUI = ChartUI {
                        fun: currentFun;
                        data: bind chartData;
                        xAxisMinAuto: true
                        xAxisMaxAuto: true
                        yAxisMin: 0.0
                        yAxisMax: 1.0
                        name: c.getName()
                        constraintsManager: bind constraintManager;
                    }
            insert chartUI into charts;
            insert chartUI into chartsHBox.content;
        }
        chartsHBox.layout();
    }

    public function getReferenceRankData(): Ranking {
        //        LOG.debug("Started getReferenceRankData");
        var refRank: Ranking = referenceRankUI.getPOJO();
        //        LOG.debug("Current reference rank data: {refRank}");
        var criterias: Criterion[] = criteriaUI.getPOJO();
        //        LOG.debug("Current criteria data: {criterias}");
        var alterns: Alternative[] = alternativesUI.getPOJO();
        //        LOG.debug("Current alternatives data: {alterns}");
        var linker = ReferenceLinker {};
        //        LOG.debug("Linking criterias with alternatives");
        alterns = linker.interconnectReferences(criterias, alterns);
        //        LOG.debug("Linking alternatives with reference rank");
        refRank = linker.interconnectReferences(alterns, refRank);
        //        LOG.debug("Finished getReferenceRankData. New data:\n{refRank}");
        return refRank;
    }

}
