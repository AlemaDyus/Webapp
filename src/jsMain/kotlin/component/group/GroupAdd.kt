package component.group

import component.template.EditAddProps
import data.Group
import data.Stream
import react.FC
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState
import web.html.InputType

val CGroupAdd = FC<EditAddProps<Group>>("GroupAdd") { props ->
    var group by useState("")
    span {
        input {
            type = InputType.text
            value = group
            onChange = { group = it.target.value }
        }
        button {
            +"✓"
            onClick = {
                props.saveElement(Group(group))
            }
        }
    }
}

