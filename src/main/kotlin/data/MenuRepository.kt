package data

import domain.ProductOut
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class MenuRepository {
    fun listProducts(shopId: UUID): List<ProductOut> = transaction {
        ProductsTable.select { ProductsTable.shopId eq shopId }.map {
            ProductOut(
                id = it[ProductsTable.id].value.toString(),
                name = it[ProductsTable.name],
                description = it[ProductsTable.description]
            )
        }
    }

    fun createProduct(shopId: UUID, name: String, description: String?): ProductOut = transaction {
        val id = ProductsTable.insertAndGetId { row ->
            row[ProductsTable.shopId] = shopId
            row[ProductsTable.categoryId] = null
            row[ProductsTable.name] = name
            row[ProductsTable.description] = description
            row[ProductsTable.active] = true
        }
        ProductOut(id.value.toString(), name, description)
    }
}
