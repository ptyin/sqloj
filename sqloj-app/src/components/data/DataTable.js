import {Table} from "antd";
import React from "react";

export default function DataTable(props)
{
    return (
        <Table
            rowKey={"uuid"}
            columns={props.columns}
            dataSource={props.data}
            pagination={{pageSize: props.pageSize || 5}}
        />
    )
}