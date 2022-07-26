package com.hectorfortuna.marvelproject.data.mockdatabase

import com.hectorfortuna.marvelproject.data.model.User

object MockDatabase {
   private var user = hashSetOf<User>()

    init {
       mockUser()
   }

    private fun mockUser() =
        user.run {
            add(User("hectorsuarez@gmail.com","Hector Suárez", "12345678",null, listOf()))
            add(User("emersonsoares@gmail.com","Emerson Soares", "12345678",null, listOf()))
            add(User("amandaluz@gmail.com","Amanda Cristina", "12345678",null, listOf()))
            add(User("fernandaoliveira@gmail.com","Fernanda Oliveira", "12345678",null, listOf()))
        }
    @Throws(Throwable::class)
    fun mockLogin(email:String, password: String): User? {
        return this.user.firstOrNull { email == it.email && password == it.password }
            ?: throw IllegalArgumentException("email e/ou senha inválido")
    }
}