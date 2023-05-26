package component.singleGroup

import common.Item
import data.Group
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState
import web.html.InputType

external interface EditSubGroupProps : Props {
    var index: Int?
    var group: Item<Group>
    var addSubGroup: (Item<Group>) -> Unit
}

val CEditSubGroup = FC<EditSubGroupProps>("StudentEdit") { props ->
    var group by useState(props.group.elem.subgroups[props.index!!])
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
            props.addSubGroup(
                Item(
                    props.group.elem.renameSubGroup(props.index!!, group),
                    props.group.id,
                    props.group.version
                )
            )
        }
    }
}
