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
import uta.api.Ranking;
import uta.api.Criterion;
import uta.api.Alternative;
import uta.api.LinearFunction;
import uta.constraint.ConstraintsManager;
import uta.constraint.ConstraintsManagerFactory;
import utafx.ui.solution.FinalRankUI;
import utafx.control.ReferenceLinker;
import javafx.scene.layout.Flow;
import utafx.ui.solution.ChartUI;
import utafx.ui.solution.ChartUI.ChartUIData;
import utafx.ui.solution.ChartListener;
import utafx.ui.solution.ChartEvent;
import com.sun.javafx.runtime.sequence.Sequences;
import javafx.scene.layout.HBox;
import uta.api.IUtaSolver;
import uta.UtaSolverFactory;
import utafx.ui.pref.FileChooser;
import utafx.ui.pref.PreferenceManager;
import utafx.ui.pref.ImportUI;
import utafx.ui.pref.jaxb.Preferences;
import utafx.ui.generic.table.TableRow;
import utafx.ui.generic.table.TableCell;
import javax.swing.filechooser.FileFilter;
import java.lang.UnsupportedOperationException;
import java.lang.String;
import java.io.File;

/**
 * @author LG
 */
public class MainSceneController {

    def solverFactory = new UtaSolverFactory();
    def prefManager = PreferenceManager {};

    package var criteriaUI: CriteriaUI;
    package var alternativesUI: AlternativesUI;
    package var referenceRankUI: ReferenceRankUI;
    package var finalRankUI: FinalRankUI;
    package var chartsHBox: HBox;
    var charts: ChartUI[];
    var constraintsManager: ConstraintsManager;
    var freezedKendall = bind finalRankUI.preserveKendallRate on replace {
                constraintsManager = new ConstraintsManagerFactory(freezedKendall).createConstraintsManager(finalRankUI.functions, getReferenceRankData(),
                        finalRankUI.sortedRank);
            }

    init {
        alternativesUI.model = AlternativesModel {
                    columnNames: bind ["Name", criteriaUI.model.criteriaNames];
                    criteriaPOJO: bind criteriaUI.getPOJO();
                }
        referenceRankUI.model = ReferenceRankModel {
                    alternativeNames: bind alternativesUI.model.alternativeNames
                }
    }

    public function onNextState(currentState: Integer) {
        if (currentState == 2) {
            solve();
        }
    }

    public function solve() {
        delete  chartsHBox.content;
        delete  charts;
        var refRank: Ranking = referenceRankUI.getPOJO();
        var criterias: Criterion[] = criteriaUI.getPOJO();
        var alterns: Alternative[] = alternativesUI.getPOJO();

        var linker = ReferenceLinker {};
        alterns = linker.interconnectReferences(criterias, alterns);
        refRank = linker.interconnectReferences(alterns, refRank);

        var solver: IUtaSolver = solverFactory.createSolver();
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

        constraintsManager = new ConstraintsManagerFactory(false).createConstraintsManager(finalRankUI.functions, getReferenceRankData(),
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
                        constraintsManager: bind constraintsManager;
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

    function clearDataControls() {
        delete  criteriaUI.model.criteriaNames;
    }

    public function importPreferences() {
        var fc = FileChooser {
                    title: "Import Preferences"
                    command: "Import"
                    workingDir: prefManager.importLastDir
                    open: true
                    show: true
                }
        if (fc.selectedFile != null) {
            clearDataControls();
            prefManager.importLastDir = fc.selectedFile.getParentFile();
            var preferences: Preferences = prefManager.doImport(fc.selectedFile);
            var importedCriteria = preferences.getCriteria();

            criteriaUI.model.rows =
                    for (c in importedCriteria.getCriterion()) {
                        TableRow {
                            var type = "{c.getType().toString().charAt(0)}{c.getType().toString().toLowerCase().substring(1)}";
                            cells: [
                                TableCell { text: c.getName() },
                                TableCell { text: type },
                                TableCell { text: "{c.getSegments()}" }
                            ]
                        }
                    }

            alternativesUI.model.rows =
                    for (a in preferences.getAlternatives().getAlternative()) {
                        TableRow {
                            var values = a.getValues().getValue();
                            cells: [
                                TableCell { text: a.getName() },
                                for (v in values) {
                                    TableCell { text: "{v.getValue()}" }
                                }
                            ]
                        }
                    }

            var items = preferences.getRefRank().getItem();

            for (rr in items) {
                var name = alternativesUI.model.alternativeNames[rr.getId()];
                var r = rr.getRank();
                referenceRankUI.insertToTreeView2(name, r)
            }
        }
    }

    public function changeSolverImpl() {
        var fc = FileChooser {
                    title: "Choose a JAR archive"
                    command: "Choose"
                    workingDir: prefManager.importLastDir
                    open: true
                //show: true
                }
        fc.nativeFC.setAcceptAllFileFilterUsed(false);
        fc.nativeFC.setFileFilter(FileFilter {
            override public function getDescription(): String {
                "JAR archive";
            }

            override public function accept(arg0: File): Boolean {
                return arg0.isDirectory() or arg0.getName().endsWith(".jar");
            }
        });
        fc.show = true;
        if (fc.selectedFile != null) {
            solverFactory.setSolverJarPath(fc.selectedFile.getAbsolutePath());
        }

    }

}
