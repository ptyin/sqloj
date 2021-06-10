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
import TeacherAssignments from './teacher/teacherAssignments'
import UpdateAssignment from "./teacher/updateAssignment";
import AddAssignment from "./teacher/addAssignment";

import TeacherQuestions from "./teacher/teacherQuestions"
import QuestionDetail from "./teacher/questionDetail";
import AddQuestion from "./teacher/addQuestion";

import Databases from "./teacher/databases";
import AddDatabase from "./teacher/addDatabase";
// import Change_pwd_teacher from './teacher_pages/change_pwd_teacher'
// import Admin_users from "./admin_pages/admin_users";
// import Admin_chet from "./admin_pages/admin_chet";
// import RenewQuestion from "./teacher_pages/renewQuestion";
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
            <Route path="/updateAssignment" component={UpdateAssignment}/>
            <Route path="/addAssignment" component={AddAssignment}/>

            <Route path="/teacherQuestions" component={TeacherQuestions}/>
            <Route path="/questionDetail" component={QuestionDetail}/>
            {/*<Route path="/change_pwd_teacher" component={Change_pwd_teacher}/>}*/}
            <Route path="/addQuestion" component={AddQuestion}/>
            <Route path="/databases" component={Databases}/>
            <Route path="/addDatabase" component={AddDatabase}/>
            {/*<Route path="/admin_users" component={Admin_users}/>*/}
            {/*<Route path="/admin_chet" component={Admin_chet}/>*/}
            {/*<Route path="/renewQuestion" component={RenewQuestion}/>*/}
            {/*<Route path="/copycat" component={Copycat}/>*/}
            {/*<Route path="/add_trigger" component={Add_trigger}/>*/}
            <Route path="/" component={App}/>
        </Switch>
    </Router>
}