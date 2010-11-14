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
import utafx.ui.window.MessageBox;
import uta.ConstraintsManagerFactory;
import uta.ConstraintsManager;
import org.apache.log4j.Logger;

/**
 * @author Pawcik
 */
public class GUIController {

    def LOG = Logger.getLogger(this.getClass());
    public var view: MainView;
    var prefManager = PreferenceManager{};
    var kendallFreezed = false;
    var showLogs = false;    

    public function createCriteria(): CriteriaUI {
        LOG.debug("Created criteriaUI");
        return CriteriaUI {}
    }

    public function createAlternatives(): AlternativesUI {
        LOG.debug("Started createAlternatives");
        var criterias = view.criteriaPanel.getPOJO();
        var altUI = AlternativesUI {            
            model: AlternativesModel {
                columnNames: bind ["Name", view.criteriaPanel.model.criteriaNames];
                criteriaPOJO: bind criterias;
            }            
        }
        LOG.info("Created alternatives model with \ncolumns: {altUI.model.columnNames}\ndata   :{altUI.model.criteriaPOJO}");
        LOG.debug("Finished createAlternatives");
        return altUI;
    }

    public function createReferenceRank(): ReferenceRankUI {
        LOG.debug("Started createAlternatives");
        var rr = ReferenceRankUI {
            model: ReferenceRankModel{
                alternativeNames: bind view.alternativesPanel.model.alternativeNames
            }
            guiController: bind this;
        }
        LOG.info("Created reference rank model with data: {rr.model.alternativeNames}");
        LOG.debug("Finished createAlternatives");
        return rr;
    }

    public function updateSolution(){
        LOG.debug("Started updateSolution");
        if(view.solutionPanel!=null){
            view.solutionPanel.refRank = getReferenceRankData();
            LOG.debug("Reference rank in solution panel updated. New reference rank data:\n{view.solutionPanel.refRank}");
        } else {
            LOG.debug("Could not update reference rank - solution panel is null");
        }
        LOG.debug("Finished updateSolution");
    }


    public function getReferenceRankData(): Ranking{
        LOG.debug("Started getReferenceRankData");
        var refRank: Ranking = view.referenceRankPanel.getPOJO();
        LOG.debug("Current reference rank data: {refRank}");
        var criterias: Criterion[] = view.criteriaPanel.getPOJO();
        LOG.debug("Current criteria data: {criterias}");
        var alterns: Alternative[] = view.alternativesPanel.getPOJO();
        LOG.debug("Current alternatives data: {alterns}");
        var linker = ReferenceLinker{};
        LOG.debug("Linking criterias with alternatives");
        alterns = linker.interconnectReferences(criterias, alterns);
        LOG.debug("Linking alternatives with reference rank");
        refRank = linker.interconnectReferences(alterns, refRank);
        LOG.debug("Finished getReferenceRankData. New data:\n{refRank}");
        return refRank;
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
            LOG.info("CharPoints: {Arrays.toString(f.getCharacteristicPoints())} \nand values: {Arrays.toString(f.getValues())}");
            //if (showLogs) println("CharPoints: {Arrays.toString(f.getCharacteristicPoints())} \nand values: {Arrays.toString(f.getValues())}");
        }

        var manager: ConstraintsManager = new ConstraintsManagerFactory(kendallFreezed).createConstraintsManager(functs, null, null);
        
        var solution = SolutionUI {
                    refRank: refRank;
                    functions: functs;
                    alternatives: alterns;
                    columnNames: view.criteriaPanel.getCriteriaNames()
                    constraintManager: manager;
                    guiController: bind this;
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
            clearDataControls();
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
                LOG.info("File {fc.selectedFile.getAbsolutePath()} saved");
                //if (showLogs) println("File {fc.selectedFile.getAbsolutePath()} saved.")
            }catch(e:IOException){
                LOG.info("Could not export preferences", e);
                //if (showLogs) println("Could not export preferences: {e.getMessage()}")
            }
        }
    }

    public function clearCriterias(){
        var options = ["OK", "Cancel"];
        var userOption = MessageBox.showConfirmDialog(view.scene, "This will clear criterias and all referenced data.\n Continue?", "Clear Criteria", options);
        if(userOption == options[0]){
            clearDataControls();
            //view.criteriaPanel.model.clear();
        }
    }

    function clearDataControls(){
        delete view.userDataContent.content;
        view.alternativesPanel = null;
        view.criteriaPanel = null;
        view.referenceRankPanel = null;
    }
}
