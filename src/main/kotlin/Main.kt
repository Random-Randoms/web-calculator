package org.example

import org.example.model.ExpressionParser

fun main() {
    println(ExpressionParser("2+3*(0)+(((7)))-1").parseString())
}
