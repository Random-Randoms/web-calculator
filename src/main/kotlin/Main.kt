package org.example

import org.example.model.ExpressionParser

fun main() {
    println(ExpressionParser("(12 + 22 * 7)/(33 + (12 * 3 - 8)) * 3").parseString())
}
