package component.singleGroup

import common.Item
import config.Config
import data.Group
import data.Stream
import data.groupId
import js.core.get
import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.dom.html.ReactHTML.span
import react.router.useParams
import react.useState
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json

val CGroupContainer = FC<Props>("GroupContainer") {
    val param = useParams()["groupId"] as groupId
    val queryClient = useQueryClient()
    val myQueryKey = arrayOf("group").unsafeCast<QueryKey>()
    val myQueryKey2 = arrayOf("streamsWithGroup").unsafeCast<QueryKey>()
    var editedIndex by useState<Int?>(null)

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = myQueryKey,
        queryFn = {
            fetchText(Config.groupsPath + param)
        }
    )

    val query2 = useQuery<String, QueryError, String, QueryKey>(
        queryKey = myQueryKey2,
        queryFn = {
            fetchText(Config.groupsPath + "streams/" + param)
        }
    )

    val updateMutation = useMutation<HTTPResult, Any, Item<Group>, Any>(
        mutationFn = { item: Item<Group> ->
            fetch(
                Config.groupsPath,
                jso {
                    method = "PUT"
                    headers = json(
                        "Content-Type" to "application/json"
                    )
                    body = Json.encodeToString(item)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(myQueryKey)
            }
        }
    )

    if (query.isLoading) div { +"Loading.." }
    else if (query.isError) div { +"Error" }
    else {
        val group = Json.decodeFromString<Item<Group>>(query.data ?: "")
        if (editedIndex != null)
            CEditSubGroup {
                this.index = editedIndex
                this.group = group
                addSubGroup = {
                    updateMutation.mutateAsync(it, null)
                }
            }
        div {
            ol {
                group.elem.subgroups.forEachIndexed { index, item ->
                    li {
                        +item
                        span {
                            +" ✂ "
                            onClick = {
                                updateMutation.mutateAsync(
                                    Item(
                                        group.elem.deleteSubGroup(item),
                                        group.id,
                                        group.version
                                    ), null
                                )
                            }
                        }
                        span {
                            +" ✎ "
                            onClick = {
                                editedIndex = index
                            }
                        }
                    }
                }
            }
        }
        CAddSubGroup {
            this.group = group
            addSubGroup = {
                updateMutation.mutateAsync(it, null)
            }
        }
    }
    div {
        +"Потоки группы: "
    }
    if (query2.isLoading) div { +"Loading.." }
    else if (query2.isError) div { +"Error" }
    else {
        val streams = Json.decodeFromString<List<Stream>>(query2.data ?: "")
        div {
            ol {
                streams.forEach {
                    li {
                        +it.name
                    }
                }
            }
        }
    }
}