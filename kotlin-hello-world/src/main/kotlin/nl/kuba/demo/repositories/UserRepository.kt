package nl.kuba.demo.repositories

import nl.kuba.demo.entities.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByLogin(login: String): User?
}