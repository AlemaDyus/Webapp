package data

import common.ItemId
import kotlinx.serialization.Serializable

typealias groupId = ItemId

@Serializable
class Group(
    val name: String,
    val subgroups: Array<String> = emptyArray()
) {
    fun addSubGroup(subGroup: String) =
        Group(
            name,
            subgroups + subGroup
        )

    fun deleteSubGroup(subGroup: String) =
        Group(
            name,
            subgroups.filterNot { it == subGroup }.toTypedArray()
        )

    fun renameSubGroup(index: Int, subGroup: String): Group {
        val subgroups = subgroups
        subgroups[index] = subGroup
        return Group(
            name,
            subgroups
        )
    }
}