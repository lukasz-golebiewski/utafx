/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utafx.control;

/**
 * This class is responsible for cosuming update events from
   GUI layer that have incfluence on other widgets and delegating
   changes to guiController. It ease decoupling between the GUI
   components.

 */

public class UpdateManager {
    var controller: GUIController;

    
    //Should be fired when criteria has been added/removed/updated
    public function fireCriteriaUpdated(){

    }

    //Should be fired when alternatives has been added/removed/updated
    public function fireAlternativesUpdated(){

    }

    public function fireReferenceRankUpdated(){

    }


}
