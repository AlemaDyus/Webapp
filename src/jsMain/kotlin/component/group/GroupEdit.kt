package component.group

import component.template.EditItemProps
import data.Group
import react.FC
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState
import web.html.InputType



val CGroupEdit = FC<EditItemProps<Group>>("GroupEdit") { props ->
    var group by useState(props.item.elem.name)
    span {
        input {
            type = InputType.text
            value = group
            onChange = { group = it.target.value }
        }
    }
    button {
        +"✓"
        onClick = {
            props.saveElement(Group(group, props.item.elem.subgroups))
        }
    }
}
