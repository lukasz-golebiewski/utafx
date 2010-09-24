/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.control;

import utafx.ui.alternative.AlternativesUI;
import utafx.ui.criteria.CriteriaUI;
import utafx.ui.MainView;
import utafx.ui.rank.ReferenceRankUI;
import uta.UtaStarSolver;
import uta.Ranking;
import uta.LinearFunction;
import uta.Alternative;
import uta.Criterion;
import utafx.ui.solution.SolutionUI;
import java.util.Arrays;
import utafx.ui.pref.ImportUI;
import utafx.ui.pref.PreferenceManager;
import utafx.ui.pref.FileChooser;
import utafx.ui.pref.jaxb.Preferences;
import utafx.ui.alternative.AlternativesModel;
import utafx.ui.generic.table.TableRow;
import utafx.ui.generic.table.TableCell;
import utafx.ui.pref.JAXBSupport;
import utafx.ui.pref.jaxb.ObjectFactory;
import java.io.IOException;
import utafx.ui.rank.ReferenceRankModel;

/**
 * @author Pawcik
 */
public class GUIController {

    public var view: MainView;
    var prefManager = PreferenceManager{};
    

    public function createCriteria(): CriteriaUI {
        return CriteriaUI {}
    }

    public function createAlternatives(): AlternativesUI {
        var criterias = view.criteriaPanel.getPOJO();
        AlternativesUI {
            model: AlternativesModel {
                columnNames: bind ["Name", view.criteriaPanel.model.criteriaNames]
                criteriaPOJO: bind criterias;
            }            
        }
    }

    public function createReferenceRank(): ReferenceRankUI {
        var alternatives = view.alternativesPanel.getPOJO();
        ReferenceRankUI {
            allItems: bind alternatives;
            model: ReferenceRankModel{
                alternativeNames: bind view.alternativesPanel.model.alternativeNames
            }

        }
    }

    public function solve(): Void {
        
        var refRank: Ranking = view.referenceRankPanel.getPOJO();
        var criterias: Criterion[] = view.criteriaPanel.getPOJO();
        var alterns: Alternative[] = view.alternativesPanel.getPOJO();
        var linker = ReferenceLinker{};
        
        alterns = linker.interconnectReferences(criterias, alterns);
        refRank = linker.interconnectReferences(alterns, refRank);
        
        //for (a in rank.getAlternatives()) {
        //    (a as uta.Alternative).setCriteria(criterias);
        //}
        var solver: UtaStarSolver = new UtaStarSolver();
        var functs: LinearFunction[] = solver.solve(refRank, criterias, alterns);
        //functs = solver.solve(rank, criterias, alterns);
        for (f in functs) {
            println("CharPoints: {Arrays.toString(f.getCharacteristicPoints())} \nand values: {Arrays.toString(f.getValues())}");
        }
        var solution = SolutionUI {
                    functions: functs;
                    alternatives: alterns;
                    columnNames: ["Name", view.criteriaPanel.getCriteriaNames(), "Value"];
                }
        view.addSolutionUI(solution);
    }

    public function importPreferences(): ImportUI {        
        var fc = FileChooser {
                    title: "Import Preferences"
                    command: "Import"
                    workingDir: prefManager.importLastDir
                    open: true
                    show: true
                }
        if (fc.selectedFile != null) {
            prefManager.importLastDir = fc.selectedFile.getParentFile();
            var preferences: Preferences = prefManager.doImport(fc.selectedFile);
            var importedCriteria = preferences.getCriteria();
            if (not view.criteriaAdded) {
                view.addCriteria(createCriteria());
            }
            view.criteriaPanel.model.rows =
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
            if (not view.alternativesAdded) {
                view.addAlternatives(createAlternatives());
            }
            view.alternativesPanel.model.rows =
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
            if (not view.referenceRankAdded) {
                view.addReferenceRank(createReferenceRank());
            }
            view.referenceRankPanel.reset();

            var items = preferences.getRefRank().getItem();
            for (rr in items) {
                var name = view.alternativesPanel.model.alternativeNames[rr.getId()];
                var r = rr.getRank();
                view.referenceRankPanel.insertToTreeView2(name, r)
            }
        }
        null;
    }


    public function exportPreferences(){
        var prefManager = PreferenceManager{};
        var fc = FileChooser {
                    title: "Export Preferences"
                    command: "Export"
                    workingDir: prefManager.exportLastDir
                    open: false;
                    show: true
                }
        if (fc.selectedFile != null) {
            prefManager.exportLastDir = fc.selectedFile.getParentFile();
            var converter = JAXBSupport{};
            var prefs = new ObjectFactory().createPreferences();
            var criteria = converter.convert(view.criteriaPanel.getPOJO());
            var altPojo = view.alternativesPanel.getPOJO();
            var altrns = converter.convert(altPojo);
            var refRank = converter.convert(view.referenceRankPanel.getPOJO(), altPojo);

            prefs.setCriteria(criteria);
            prefs.setAlternatives(altrns);
            prefs.setRefRank(refRank);
            try{
                prefManager.export(prefs, fc.selectedFile.getAbsolutePath());
                println("File {fc.selectedFile.getAbsolutePath()} saved.")
            }catch(e:IOException){
                println("Could not eport preferences: {e.getMessage()}")
            }
        }
    }
}
