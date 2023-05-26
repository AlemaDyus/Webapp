package component.singleGroup

import common.Item
import data.Group
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState
import web.html.InputType

external interface AddSubGroupProps: Props{
    var group: Item<Group>
    var addSubGroup: (Item<Group>) -> Unit
}

val CAddSubGroup = FC<AddSubGroupProps>("AddSubGroup") { props ->
    var groupName by useState("")

    div {
        if (props.group.elem.subgroups.size < 2) {
            span {
                +"Добавить подгруппу"
                input {
                    type = InputType.text
                    value = groupName
                    onChange = { groupName = it.target.value }
                }
                button {
                    +"add"
                    onClick = {
                        props.addSubGroup(
                            Item(
                                props.group.elem.addSubGroup(groupName),
                                props.group.id,
                                props.group.version
                            )
                        )
                    }
                }
            }
        } else {
            span {
                +"Максимум подгрупп"
            }
        }
    }
}