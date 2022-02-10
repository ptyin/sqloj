import React, {useEffect, useState} from "react";
import {Card, List} from "antd";
import {Link, Route, Switch, useRouteMatch} from "react-router-dom";
import {ArrowRightOutlined} from "@ant-design/icons";
import Course from "./Course";
import EaseAnim from "../../components/anim/EaseAnim";


export default function CourseList(props)
{
    const match = useRouteMatch()
    const [data, setData] = useState([])
    useEffect(() =>
    {
        const demo = []
        for(let i=0;i<40;i++)
            demo.push({
                uuid: `${i}`,
                name: `Course ${i}`,
                description: `Description ${i}`
            })
        setData(demo)
    }, [])

    return (
        <Switch>
            <Route path={`${match.path}/:courseUuid`} component={Course}/>
            <Route>
                <EaseAnim>
                    <List
                        grid={{ gutter: 16, column: 4 }}
                        pagination={{pageSize: 12}}
                        dataSource={data}
                        renderItem={item => (
                            <List.Item key={item.name}>
                                <Card title={item.name} extra={<Link to={`${match.url}/${item.uuid}`}><ArrowRightOutlined /></Link>}>{item.description}</Card>
                            </List.Item>
                        )}
                    />
                </EaseAnim>
            </Route>
        </Switch>
    )
}