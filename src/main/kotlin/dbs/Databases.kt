@file:Suppress("ktlint:standard:no-wildcard-imports")

package dbs

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseBuilder {
    fun initialize() {
        @Suppress("ktlint:standard:max-line-length")
        Database.connect(
            url = "jdbc:postgresql://aws-0-eu-north-1.pooler.supabase.com:6543/postgres?prepareThreshold=0",
            driver = "org.postgresql.Driver",
            user = "postgres.mpyipwqrcouunxemswxl",
            password = "6@sA#KVbrS3FFLf",
        )
    }

    fun build() {
        transaction {
            SchemaUtils.create(Users, Queries)
        }
    }

    fun destroy() {
        transaction {
            SchemaUtils.drop(Queries, Users)
        }
    }
}

data class Equation(
    val expression: String,
    val result: String,
)

fun forEachEquationOfUser(
    id: EntityID<Int>,
    block: Equation.() -> Unit,
) {
    transaction {
        User[id]
            .queries
            .orderBy(Pair(Queries.number, SortOrder.DESC))
            .forEach { Equation(it.expression, it.result).block() }
    }
}

fun fullHistory(): List<Any> =
    transaction {
        User
            .all()
            .asSequence()
            .map { user -> user.queries.map { user to it } }
            .flatten()
            .sortedByDescending { it.second.id }
            .take(100)
            .map {
                object {
                    val user = it.first.login
                    val expression = it.second.expression
                    val result = it.second.result
                }
            }.toList()
    }

fun getUserIdOrNull(
    login: String,
    passwordHash: String,
): EntityID<Int>? {
    return transaction {
        val users = User.find((Users.login eq login) and (Users.passwordHash eq passwordHash)).limit(1)
        return@transaction users.firstOrNull()?.id
    }
}

fun isLoginExists(login: String): Boolean {
    return transaction {
        val users = User.find(Users.login eq login)
        return@transaction !users.empty()
    }
}

fun addUser(
    login: String,
    passwordHash: String,
): EntityID<Int> {
    return transaction {
        return@transaction Users.insert {
            it[Users.login] = login
            it[Users.passwordHash] = passwordHash
        }[Users.id]
    }
}

fun addQuery(
    id: EntityID<Int>,
    equation: Equation,
): EntityID<Int> =
    transaction {
        val newNumber: ULong =
            Query
                .all()
                .orderBy(Pair(Queries.number, SortOrder.DESC))
                .limit(1)
                .firstOrNull()
                ?.number
                ?.plus(1UL)
                ?: 0UL
        Queries.insert {
            it[user] = id
            it[expression] = equation.expression
            it[result] = equation.result
            it[number] = newNumber
        }[Queries.id]
    }
