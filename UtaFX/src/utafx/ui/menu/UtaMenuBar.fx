package utafx.ui.menu;

import com.javafx.preview.control.MenuBar;
import com.javafx.preview.control.Menu;
import com.javafx.preview.control.MenuItem;
import java.lang.System;
import utafx.control.GUIController;
import utafx.ui.rank.ReferenceRankUI;

/**
 * @author Pawcik
 */
public class UtaMenuBar extends MenuBar {

    public var guiController: GUIController;

    init {
        menus = [
                    Menu {
                        text: "File"
                        items: [
                            MenuItem {
                                text: "Import Preferences..."
                                action: function() {
                                    guiController.importPreferences();
                                } },
                            MenuItem {
                                text: "Export Preferences..."
                                action: function() {
                                    guiController.exportPreferences();
                                }
                            }
                            MenuItem { text: "Exit"
                                action: function() {
                                    System.exit(0)
                                }
                            }
                        ]
                    },
                    Menu {
                        text: "Define"
                        items: [
                            MenuItem {
                                text: "Criteria"
                                action: function() {
                                    var criteria = guiController.createCriteria();
                                    guiController.view.addCriteria(criteria);
                                }
                                disable: bind guiController.view.criteriaAdded;
                            },
                            MenuItem {
                                text: "Alternatives"
                                action: function() {
                                    if (guiController.view.criteriaAdded) {
                                        var alterns = guiController.createAlternatives();
                                        guiController.view.addAlternatives(alterns);
                                    }
                                }
                                disable: bind (not guiController.view.criteriaAdded or guiController.view.alternativesAdded);
                            }
                            MenuItem {
                                text: "Reference Ranking";
                                action: function() {
                                    if (guiController.view.alternativesAdded) {
                                        var rr: ReferenceRankUI = guiController.createReferenceRank();
                                        guiController.view.addReferenceRank(rr);
                                    }
                                }
                                disable: bind (not guiController.view.alternativesAdded or guiController.view.referenceRankAdded);
                            }
                        ]
                    },
                    Menu {
                        text: "Tools"
                        items: [
                            MenuItem { text: "Solve"
                                action: function() {
                                    guiController.solve();
                                }
                            }
                            MenuItem { text: "Clear Criteria" }
                            MenuItem { text: "Clear Alternatives" }
                            MenuItem { text: "Clear Reference Ranking" }
                            MenuItem { text: "Clear All" }
                        ]
                    },
                    Menu {
                        text: "Help"
                        items: [
                            MenuItem { text: "Help" }
                            MenuItem { text: "About" }
                        ]
                    }
                ]
    }

}
/*
         ,
                            ]
    }

}
*/
