@file:Suppress("ktlint:standard:no-wildcard-imports")

package dbs

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.text.insert

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
}

fun forEachExpressionOfUser(
    id: EntityID<Int>,
    block: String.() -> Unit,
) {
    transaction {
        User[id].queries.orderBy(Pair(Queries.number, SortOrder.DESC)).forEach { it.expression.block() }
    }
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
            it[Users.passwordHash] = login
        }[Users.id]
    }
}

fun addQuery(
    id: EntityID<Int>,
    expression: String,
) {
    transaction {
        val newNumber: ULong =
            User[id].queries.orderBy(Pair(Queries.number, SortOrder.DESC)).limit(1).firstOrNull()?.number?.plus(1UL)
                ?: 0UL
        Queries.insert {
            it[user] = id
            it[Queries.expression] = expression
            it[number] = newNumber
        }
    }
}
