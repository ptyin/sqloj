import HomepageAnim from "../../components/anim/HomepageAnim";
import Button from "antd/lib/button";
import React from "react";
import './Homepage.css'
import {LoginOutlined} from "@ant-design/icons";


export default function Homepage(props)
{
    const onClick = () =>
    {
        props.history.push("/login")
    }

    return <HomepageAnim title="SQLOJ" description="An open judge platform for Database System.">
        <Button className="jump"
                type="primary"
                icon={<LoginOutlined />} onClick={onClick}>
            Log in
        </Button>
    </HomepageAnim>
}