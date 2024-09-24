package org.example

import org.example.model.ExpressionParser

fun main() {
    val res = ExpressionParser("2+3*(4)").parseString()
    println(res)
}
