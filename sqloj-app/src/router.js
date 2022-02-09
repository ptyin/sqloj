import React from 'react'
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import App from './app'
import LoginPanel from './views/entry/LoginPanel'
import AssignmentList from './pages/student/AssignmentList'
import Questions from './pages/student/questions'
import Submit from './pages/student/submit'
import Records from './pages/student/records'
import RecordDetail from "./pages/student/recordDetail";
import './common/css/font.css'
import CourseList from "./views/course/CourseList";
import TeacherAssignments from './pages/teacher/teacherAssignments'
import UpdateAssignment from "./pages/teacher/updateAssignment";
import AddAssignment from "./pages/teacher/addAssignment";

import TeacherQuestions from "./pages/teacher/teacherQuestions"
import QuestionDetail from "./pages/teacher/questionDetail";
import TeacherRecordDetail from "./pages/teacher/teacherRecordDetail"
import AddQuestion from "./pages/teacher/addQuestion";
import UpdateQuestion from "./pages/teacher/updateQuestion";

import Databases from "./pages/teacher/databases";
import AddDatabase from "./pages/teacher/addDatabase";

export default function IRouter()
{
    return <Router>
        <Switch>
            <Route path="/login" component={LoginPanel}/>
            <Route path="/courses" component={CourseList}/>

            <Route path="/assignments" component={AssignmentList}/>
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
            <Route path="/teacherRecordDetail" component={TeacherRecordDetail}/>
            {/*<Route path="/change_pwd_teacher" component={Change_pwd_teacher}/>}*/}
            <Route path="/addQuestion" component={AddQuestion}/>
            <Route path="/databases" component={Databases}/>
            <Route path="/addDatabase" component={AddDatabase}/>
            <Route path="/updateQuestion" component={UpdateQuestion}/>
            {/*<Route path="/admin_users" component={Admin_users}/>*/}
            {/*<Route path="/admin_chet" component={Admin_chet}/>*/}
            {/*<Route path="/copycat" component={Copycat}/>*/}
            {/*<Route path="/add_trigger" component={Add_trigger}/>*/}
            <Route path="/" component={App}/>
            <Route component={App}/>
        </Switch>
    </Router>
}