import {Button} from "antd";
import React from "react";


export default function ListHeader(props)
{
    const buttonList = props.actions.map(action => <Button className={'sqloj-header-button'}>{action}</Button>)
    return (
        <div className={'sqloj-header'}>
            {buttonList}
        </div>
    )
}