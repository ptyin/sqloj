import Guide from "./Guide";

export default function TeacherGuide(props)
{
    return <Guide allItems={["courses", ]} selectedItem={props.selectedItem} callback={props.callback}>

    </Guide>
}