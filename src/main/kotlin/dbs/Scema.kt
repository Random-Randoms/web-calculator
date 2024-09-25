package dbs

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Users : IntIdTable("user", "user_id") {
    val login: Column<String> = varchar("login", 20).uniqueIndex()
    val passwordHash: Column<String> = varchar("password_hash", 257)
}

class User(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    val login by Users.login

    val passwordHash by Users.passwordHash

    val queries by Query referrersOn Queries.user
}

object Queries : IntIdTable("query", "query_id") {
    val user = reference("user_id", Users)
    val expression: Column<String> = text("expression")
    val result: Column<String> = text("result")
    val number: Column<ULong> = ulong("number")
}

class Query(
    id: EntityID<Int>,
) : IntEntity(id) {
    companion object : IntEntityClass<Query>(Queries)

    val expression by Queries.expression

    val result by Queries.result

    val number by Queries.number
}
