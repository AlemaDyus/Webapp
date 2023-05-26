package component.singleStream

import common.Item
import config.Config
import data.Group
import data.Stream
import data.streamId
import js.core.get
import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.span
import react.router.dom.Link
import react.router.useParams
import react.useRef
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import web.html.HTMLSelectElement
import kotlin.js.json

val CStreamContainer = FC<Props>("test") {
    val param = useParams()["streamId"] as streamId
    val myQueryKey = arrayOf("stream").unsafeCast<QueryKey>()
    val myQueryKey2 = arrayOf("allGroups").unsafeCast<QueryKey>()
    val queryClient = useQueryClient()
    val selectRef = useRef<HTMLSelectElement>()

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = myQueryKey,
        queryFn = {
            fetchText(Config.streamsPath + param)
        }
    )

    val queryGroups = useQuery<String, QueryError, String, QueryKey>(
        queryKey = myQueryKey2,
        queryFn = {
            fetchText(Config.groupsPath)
        }
    )

    val updateMutation = useMutation<HTTPResult, Any, Item<Stream>, Any>(
        mutationFn = { item: Item<Stream> ->
            fetch(
                Config.streamsPath,
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
                queryClient.invalidateQueries<Any>(myQueryKey2)
            }
        }
    )

    if (query.isLoading) ReactHTML.div { +"Loading.." }
    else if (query.isError) ReactHTML.div { +"Error" }
    else if (queryGroups.isLoading) ReactHTML.div { +"Loading.." }
    else if (queryGroups.isError) ReactHTML.div { +"Error" }
    else {
        val currentStream = Json.decodeFromString<Item<Stream>>(query.data ?: "")
        val currentGroups = Json.decodeFromString<List<Item<Group>>>(queryGroups.data ?: "")
        val groups = currentGroups.mapNotNull {
            if (currentStream.elem.groupIds.contains(it.id))
                it
            else
                null
        }
        val freeGroups = currentGroups.map { it } - groups.toSet()
        div {
            select {
                ref = selectRef
                freeGroups.map {
                    option {
                        +it.elem.name
                        value = it.id
                    }
                }
            }
            button {
                +"add"
                onClick = {
                    updateMutation.mutateAsync(
                        Item(
                            currentStream.elem.addGroup(selectRef.current!!.value),
                            currentStream.id,
                            currentStream.version
                        ), null)
                }
            }
        }
        groups.forEach { group ->
            ol {
                li {
                    span {
                        Link {
                            +group.elem.name
                            replace = true
                            to = "/${Config.groupsPath}${group.id}"
                        }
                    }
                    span {
                        +" ✂ "
                        onClick = {
                            updateMutation.mutateAsync(
                                Item(
                                    currentStream.elem.deleteGroup(group.id),
                                    currentStream.id,
                                    currentStream.version
                                ), null
                            )
                        }
                    }
                }
            }
        }
    }
}