package data

import domain.ProductOut
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.eq
import java.util.UUID

@Serializable
data class ProductDoc(
    @Contextual
    val _id: UUID,
    @Contextual
    val shopId: UUID,
    val name: String,
    val description: String?
)

class MongoMenuRepository {

    private val client = KMongo.createClient(
        System.getenv("MONGO_URL") ?: "mongodb://localhost:27017"
    ).coroutine
    private val database = client.getDatabase(
        System.getenv("MONGO_DB") ?: "coffeeshop"
    )
    private val collection = database.getCollection<ProductDoc>("products")

    suspend fun listProducts(shopId: UUID): List<ProductOut> {
        return collection.find(ProductDoc::shopId eq shopId).toList().map {
            ProductOut(
                id = it._id.toString(),
                name = it.name,
                description = it.description
            )
        }
    }

    suspend fun createProduct(shopId: UUID, name: String, description: String?): ProductOut {
        val id = UUID.randomUUID()
        val doc = ProductDoc(id, shopId, name, description)
        collection.insertOne(doc)
        return ProductOut(id.toString(), name, description)
    }
}
