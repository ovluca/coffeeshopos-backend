package data

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp

object CategoriesTable : UUIDTable("categories") {
    val shopId = uuid("shop_id")
    val name = text("name")
    val position = integer("position").default(0)
}

object ProductsTable : UUIDTable("products") {
    val shopId = uuid("shop_id")
    val categoryId = reference("category_id", CategoriesTable).nullable()
    val name = text("name")
    val description = text("description").nullable()
    val active = bool("active").default(true)
}

object ProductVariantsTable : UUIDTable("product_variants") {
    val productId = reference("product_id", ProductsTable)
    val name = text("name")
    val priceCents = integer("price_cents")
}
