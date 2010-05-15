/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * Copyright 2009 Sun Microsystems, Inc. All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *   * Neither the name of Sun Microsystems nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package lesson;
var result   ;

function add(arg1: Integer, arg2: Integer) {
    result = arg1 + arg2;
    println("{arg1} + {arg2} = {result}");
}

function subtract(arg1: Integer, arg2: Integer) {
    result = arg1 + arg2;
    println("{arg1} + {arg2} = {result}");
}

function multiply(arg1: Integer, arg2: Integer) {
    result = arg1 * arg2;
    println("{arg1} * {arg2} = {result}");
}

function divide(arg1: Integer, arg2: Integer) {
    result = arg1 + arg2;
    println("{arg1} + {arg2} = {result}");
}

function doNothing(): Float {
    java.lang.System.out.println("Dupa");
    return 1.0;
}

function run(        args :
        String  [])


        {






             if(sizeof args > 1){
        def numOne = Integer.parseInt(args[0]);
        def numTwo = Integer.parseInt(args[1]);
        add(numOne, numTwo);
    }


    var adr = Address {
        city: ""
        state: ""
        street: ""
        zip: ""
    }
    println(ala());
}


function ala() : Number {
    var nums = [5, 7, 3, 9];
    var total = {
     var sum = 0;
     for (a in nums) { sum += a };
     sum;
     }
     println("Total is {total}.");
     var squares = for (i in [1..10]) i*i;
     println(squares);
     var dupa = for(a in ["ala", "ma", "kota"]) a.toUpperCase();
     println(dupa);
     return 1;
}
