import {Breadcrumb} from "antd";
import React from "react";
import {Link, useLocation} from "react-router-dom";


export default function NavigationBar(props)
{

    const location = useLocation();
    const pathSnippets = location.pathname.split('/').filter(i => i !== "dashboard" && i !== "student" && i !== 'teacher');
    const extraBreadcrumbItems = pathSnippets.map((_, index) =>
    {
        const url = `/${pathSnippets.slice(0, index + 1).join('/')}`;
        return (
            <Breadcrumb.Item key={url}>
                <Link to={url}>{pathSnippets[index]}</Link>
            </Breadcrumb.Item>
        );
    })
    return (
        <Breadcrumb style={props.style}>
            {extraBreadcrumbItems}
        </Breadcrumb>
    )
}