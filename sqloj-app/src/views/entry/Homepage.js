import HomepageAnim from "../../components/anim/HomepageAnim";
import {Button} from "antd"
import React from "react";
import './Homepage.css'
import {LoginOutlined} from "@ant-design/icons";
import {Link} from "react-router-dom";


export default function Homepage()
{
    return <HomepageAnim title="SQLOJ" description="An open judge platform for Database System.">
        <Link to="/login">
            <Button className="jump"
                    type="primary"
                    icon={<LoginOutlined />}>
                Log in
            </Button>
        </Link>
    </HomepageAnim>
}