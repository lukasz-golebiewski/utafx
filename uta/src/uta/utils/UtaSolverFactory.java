/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uta.utils;

import uta.api.IUtaSolver;
import uta.star.UtaStarSolver;

/**
 * 
 * @author LG
 */
public class UtaSolverFactory {

    public IUtaSolver createSolver() {
        // stub:
        return new UtaStarSolver();
    }
}
