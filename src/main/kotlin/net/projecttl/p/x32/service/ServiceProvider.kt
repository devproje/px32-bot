package net.projecttl.p.x32.service

import org.jetbrains.exposed.sql.transactions.transaction

interface ServiceProvider<P, T> {
    fun <T> dbQuery(block: () -> T): T =
        transaction { block() }

    fun create(data: T)

    fun read(id: P): T?

    fun update(id: P, data: T) {}

    fun delete(id: P) {}
}