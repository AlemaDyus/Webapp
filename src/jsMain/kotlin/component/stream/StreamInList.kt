package component.stream

import common.Item
import component.template.ElementInListProps
import data.Stream
import react.FC
import react.dom.html.ReactHTML.span
import react.router.dom.Link


val CStreamInList = FC<ElementInListProps<Item<Stream>>>("StreamInList") { props ->
    span {
        Link {
            +props.element.elem.name
            to = props.element.id
        }
    }
}