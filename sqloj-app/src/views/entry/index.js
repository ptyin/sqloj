import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import React from "react";
import LoginPanel from "./LoginPanel";
import Homepage from "./Homepage";
import Dashboard from "../Dashboard";


export default function Entry()
{
    return (
        <Router>
            {/*<div className="combined-wrapper">*/}
                <Switch>
                    <Route path="/login" component={LoginPanel}/>
                    <Route path="/dashboard/:role/" component={Dashboard}/>
                    <Route exact path="/" component={Homepage}/>
                </Switch>
            {/*</div>*/}
        </Router>
    )
}