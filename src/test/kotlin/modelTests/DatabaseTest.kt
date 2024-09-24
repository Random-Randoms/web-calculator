@file:Suppress("ktlint:standard:no-wildcard-imports")

package modelTests

import dbs.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.Test

class DatabaseTest {
    init {
        DatabaseBuilder.initialize()
        DatabaseBuilder.build()
    }

    private fun generateString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun getUniqueLogin(): String {
        var login = "MyTestLogin"
        while (isLoginExists(login)) {
            login = generateString(20)
        }
        return login
    }

    @Test
    fun isLoginExistsTest() {
        val myTestLogin = getUniqueLogin()
        val id = addUser(myTestLogin, "secretHash")
        assert(isLoginExists(myTestLogin))
        transaction {
            User[id].delete()
        }
    }

    private fun getUserCount(): Long {
        return transaction {
            return@transaction User.count()
        }
    }

    @Test
    fun addUserTest() {
        val myTestLogin = getUniqueLogin()
        val count = getUserCount()
        val id = addUser(myTestLogin, "secretHash")
        assert(count + 1 == getUserCount())
        transaction {
            User[id].delete()
        }
        assert(count == getUserCount())
    }

    @Test
    fun getUserIdOrNullTest() {
        val myTestLogin = getUniqueLogin()
        val hash = "secretHash"
        val factId = addUser(myTestLogin, hash)
        val id = getUserIdOrNull(myTestLogin, hash)
        assert(id == factId)
        val testLogin = getUniqueLogin()
        assert(getUserIdOrNull(testLogin, hash) == null)
        transaction {
            User[factId].delete()
        }
        assert(getUserIdOrNull(myTestLogin, hash) == null)
    }

    private fun getQueryCount(id: EntityID<Int>): Long {
        return transaction {
            return@transaction User[id].queries.count()
        }
    }

    @Test
    fun addQueryTest() {
        val myTestLogin = getUniqueLogin()
        val id = addUser(myTestLogin, "secretHash")
        val count = getQueryCount(id)
        assert(count == 0L)
        val queryId = addQuery(id, Equation("ыыы", "ь|ь|ь|"))
        assert(count + 1 == getQueryCount(id))
        transaction {
            Query[queryId].delete()
        }
        assert(count == getQueryCount(id))
        transaction {
            User[id].delete()
        }
    }

    @Test
    fun forEquationsTest() {
        val myTestLogin = getUniqueLogin()
        val id = addUser(myTestLogin, "secretHash")
        val list: MutableList<Equation> = mutableListOf()
        repeat(10) {
            list.add(Equation(generateString(15), generateString(10)))
        }
        val ids = mutableListOf<EntityID<Int>>()
        transaction {
            list.forEach {
                ids.add(addQuery(id, it))
            }
        }
        val actual: MutableList<Equation> = mutableListOf()
        forEachEquationOfUser(id) {
            actual.add(this)
        }
        list.reverse()
        assert(actual == list)
        transaction {
            ids.forEach {
                Query[it].delete()
            }
        }
        transaction {
            User[id].delete()
        }
    }
}
