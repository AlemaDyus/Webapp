package component.stream

import component.template.EditItemProps
import data.Stream
import react.FC
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState
import web.html.InputType

val CStreamEdit = FC<EditItemProps<Stream>>("StreamEdit") { props ->
    var stream by useState(props.item.elem.name)
    span {
        input {
            type = InputType.text
            value = stream
            onChange = { stream = it.target.value }
        }
    }
    button {
        +"✓"
        onClick = {
            props.saveElement(Stream(stream, props.item.elem.groupIds))
        }
    }
}
