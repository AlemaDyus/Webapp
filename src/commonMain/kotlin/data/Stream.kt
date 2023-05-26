package data

import common.ItemId
import kotlinx.serialization.Serializable

typealias streamId = ItemId

@Serializable
class Stream(
    val name: String,
    val groupIds: List<groupId> = emptyList()
) {
    fun addGroup(id: groupId) =
        Stream(name, groupIds + id)
    fun deleteGroup(id: groupId) =
        Stream(name, groupIds - id)
}
