/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utafx.control;

/**
 * @author Pawcik
 */

var seq:Integer[] on replace oldSeq[i..j]=newSeq {

    for (n in oldSeq[i..j]) println("{n} was removed from the sequence");
    for (n in newSeq) println("{n} was added to the sequence");

}
