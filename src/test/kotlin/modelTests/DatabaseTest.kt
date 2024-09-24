@file:Suppress("ktlint:standard:no-wildcard-imports")

package modelTests

import dbs.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.Test

class DatabaseTest {
    init {
        DatabaseBuilder.initialize()
        DatabaseBuilder.build()
    }

    private fun generateLogin(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    @Test
    fun isLoginExistsTest() {
        var myTestLogin = "MyTestLogin"
        while (isLoginExists(myTestLogin)) {
            myTestLogin = generateLogin(20)
        }
        addUser(myTestLogin, "secretHash")
        assert(isLoginExists(myTestLogin))
        transaction {
            Users.deleteWhere { login eq myTestLogin }
        }
    }
}
