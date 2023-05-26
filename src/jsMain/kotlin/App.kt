import component.group.*
import component.singleGroup.CGroupContainer
import component.singleStream.CStreamContainer
import component.stream.*
import component.template.RestContainerChildProps
import component.template.restContainer
import component.template.restList
import config.Config
import data.Group
import data.Stream
import react.FC
import react.Props
import react.create
import react.createContext
import react.dom.client.createRoot
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter
import react.router.dom.Link
import tanstack.query.core.QueryClient
import tanstack.query.core.QueryKey
import tanstack.react.query.QueryClientProvider
import tanstack.react.query.devtools.ReactQueryDevtools
import web.dom.document

val invalidateRepoKey = createContext<QueryKey>()

fun main() {
    val container = document.getElementById("root")!!
    createRoot(container).render(app.create())
}

val app = FC<Props>("App") {
    HashRouter {
        QueryClientProvider {
            client = QueryClient()
            ul {
                listOf("Groups", "Streams").map { tag ->
                    li {
                        Link {
                            +tag
                            to = tag.lowercase()
                        }
                    }
                }
            }
            Routes {
                Route {
                    path = "streams"
                    val list: FC<RestContainerChildProps<Stream>> =
                        restList(
                            CStreamInList,
                            CStreamAdd,
                            CStreamEdit
                        )
                    element = restContainer(
                        Config.streamsPath,
                        list,
                        "streams"
                    ).create()
                }
                Route {
                    path = "groups"
                    val list: FC<RestContainerChildProps<Group>> =
                        restList(
                            CGroupInList,
                            CGroupAdd,
                            CGroupEdit
                        )
                    element = restContainer(
                        Config.groupsPath,
                        list,
                        "groups"
                    ).create()
                }
                Route {
                    path = "groups/" + ":groupId"
                    element = CGroupContainer.create()
                }
                Route {
                    path = "streams/" + ":streamId"
                    element = CStreamContainer.create()
                }
            }
            ReactQueryDevtools { }
        }
    }
}