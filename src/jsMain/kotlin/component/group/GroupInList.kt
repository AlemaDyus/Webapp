package component.group

import common.Item
import component.template.ElementInListProps
import config.Config
import data.Group
import data.Stream
import data.streamId
import js.core.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.span
import react.router.dom.Link
import react.router.useParams
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText


val CGroupInList = FC<ElementInListProps<Item<Group>>>("GroupInList") { props ->
    span {
        Link {
            +props.element.elem.name
            to = props.element.id
        }
    }
}