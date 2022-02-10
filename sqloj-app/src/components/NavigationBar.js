import {Breadcrumb} from "antd";
import React from "react";
import {Link, useLocation} from "react-router-dom";


export default function NavigationBar(props)
{
    const location = useLocation();
    const pathSnippets = location.pathname.split('/').filter( i => i !== '');
    const notShowedItems = pathSnippets.slice(0, 2)  // remove '/dashboard' and '/dashboard/:role'
    const slicedPathSnippets = pathSnippets.slice(notShowedItems.length)
    const extraBreadcrumbItems = slicedPathSnippets.map((_, index) =>
    {
        const url = `/${notShowedItems.join('/')}/${slicedPathSnippets.slice(0, index + 1).join('/')}`;
        return (
            <Breadcrumb.Item key={url}>
                <Link to={url}>{slicedPathSnippets[index]}</Link>
            </Breadcrumb.Item>
        );
    })
    return (
        <Breadcrumb style={props.style}>
            {extraBreadcrumbItems}
        </Breadcrumb>
    )
}