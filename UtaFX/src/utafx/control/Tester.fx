/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utafx.control;


/**
 * @author Pawcik
 */
var seq = ["a", "b", "c"];

public function run(args: String[]) {

    var cells:TableCell[] = for (c in seq) {
        println("Column name is {c} index of c is {indexof c}");
        var index = indexof c;
        var size = sizeof seq;
        TableCell {
            text: if (index == 0) {
                println("Zero");
                "Zero";
            } else if (index == (size - 1)) {
                println("Last");
                "Last";
            } else {
                println({indexof c});
                "{indexof c}";
            }
        }
    }
}

package class TableCell {

    public var text: String;
}

