package org.example

import org.example.model.ExpressionParser

fun main() {
    println(ExpressionParser("(1 + 2/3) * (4/5 - 0.2)").parseString())
}
