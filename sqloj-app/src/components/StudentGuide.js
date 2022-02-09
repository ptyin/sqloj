import Guide from "./Guide";
import {Route} from "react-router-dom";

export default function StudentGuide(props)
{
    return <Guide allItems={["courses", "records"]} selectedItem={props.selectedItem} callback={props.callback}/>
}