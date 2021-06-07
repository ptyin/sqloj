import React from 'react'
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import App from './homepage/app'
import Login from './homepage/login'
import Assignments from './student/assignments'
import Questions from './student/questions'
import Submit from './student/submit'
import Records from './student/records'
import RecordDetail from "./student/recordDetail";
// import Users from './homepage/user'
// import Change_pwd from './homepage/change_pwd'
// import Forget from './homepage/forget'
import './common/font.css'
import TeacherAssignments from './teacher/teacherAssignment'
// import Change_pwd_teacher from './teacher_pages/change_pwd_teacher'
// import AddAssignment from "./teacher_pages/AddAssignment";
// import Teacher_question from "./teacher_pages/teacher_question"
// import AddQuestion from "./teacher_pages/AddQuestion";
// import Detail from "./teacher_pages/detail";
// import Database from "./teacher_pages/database";
// import AddDatabase from "./teacher_pages/AddDatabase";
// import Admin_users from "./admin_pages/admin_users";
// import Admin_chet from "./admin_pages/admin_chet";
// import RenewQuestion from "./teacher_pages/renewQuestion";
// import UpdateAssignment from "./teacher_pages/updateAssignment";
// import Copycat from "./teacher_pages/copycat";
// import Add_trigger from "./teacher_pages/add_trigger";
// import AnimatedRouter from "react-animated-router";
// import 'react-animated-router/animate.css'

export default function IRouter()
{
    return <Router>
        <Switch>
            <Route path="/login" component={Login}/>

            <Route path="/assignments" component={Assignments}/>
            <Route path="/questions" component={Questions}/>}
            <Route path="/submit" component={Submit}/>}
            <Route path="/records" component={Records}/>
            <Route path="/recordDetail" component={RecordDetail}/>
            {/*<Route path="/users" component={Users}/>*/}
            {/*<Route path="/change_pwd" component={Change_pwd}/>}*/}
            {/*<Route path="/forget" component={Forget}/>}*/}
            <Route path="/teacherAssignments" component={TeacherAssignments}/>}
            {/*<Route path="/change_pwd_teacher" component={Change_pwd_teacher}/>}*/}
            {/*<Route path="/AddAssignment" component={AddAssignment}/>*/}
            {/*<Route path="/AddQuestion" component={AddQuestion}/>*/}
            {/*<Route path="/teacherQuestion" component={Teacher_question}/>*/}
            {/*<Route path="/detail" component={Detail}/>*/}
            {/*<Route path="/database" component={Database}/>*/}
            {/*<Route path="/AddDatabase" component={AddDatabase}/>*/}
            {/*<Route path="/admin_users" component={Admin_users}/>*/}
            {/*<Route path="/admin_chet" component={Admin_chet}/>*/}
            {/*<Route path="/renewQuestion" component={RenewQuestion}/>*/}
            {/*<Route path="/updateAssignment" component={UpdateAssignment}/>*/}
            {/*<Route path="/copycat" component={Copycat}/>*/}
            {/*<Route path="/add_trigger" component={Add_trigger}/>*/}
            <Route path="/" component={App}/>
        </Switch>
    </Router>
}