package component.stream

import component.template.EditAddProps
import data.Stream
import react.FC
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState
import web.html.InputType

val CStreamAdd = FC<EditAddProps<Stream>>("StreamAdd") { props ->
    var stream by useState("")
    span {
        input {
            type = InputType.text
            value = stream
            onChange = { stream = it.target.value }
        }
        button {
            +"✓"
            onClick = {
                props.saveElement(Stream(stream))
            }
        }
    }
}

