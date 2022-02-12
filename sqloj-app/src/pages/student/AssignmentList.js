import React, {useEffect, useState} from "react";
import axios from 'axios';
import {useHistory} from "react-router-dom";
import {Button, Layout, Space, Table} from 'antd';
import logo from '../../common/images/logo.png';
import '../../common/css/layout.css';
import GuideStudent from "../../components/guideStudent";
import EaseAnim from "../../components/anim/EaseAnim";


export default function AssignmentList()
{
    const {Header, Content, Sider} = Layout;
    const [data, setData] = useState([])
    const history = useHistory();
    const columns = [
        {
            title: 'Assignment',
            dataIndex: 'name',
            key: 'name',
            // render: text => <a>{text}</a>,
        },
        {
            title: 'Start',
            dataIndex: 'startedAt',
            key: 'start',
        },
        {
            title: 'End',
            dataIndex: 'endedAt',
            key: 'deadline',
        },
        {
            title: 'Action',
            key: 'action',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={() =>
                    {
                        window.sessionStorage.assignment_ID = record.assignment_id;
                        // window.localStorage.assignment_ID = record.assignment_name;
                        history.push("/questions");
                    }}>{"questions"}</Button>
                </Space>
            ),
        }
    ];

    useEffect(() =>
    {
        // const now = Date.now()

        // if (timer === 0 || now - timer > 3000)
        function query_assignment_list()
        {
            axios.defaults.withCredentials = true;
            axios.get('/api/student/queryAssignmentList').then((response) =>
            {
                const temp = response.data
                for (let i = 0; i < temp.length; i++)
                {
                    temp[i].key = temp[i].assignment_id;
                    // let newTime = new Date(temp[i].assignment_start_time);
                    // temp[i].assignment_start_time = strftime("%y/%m/%d %H:%M:%S", newTime)
                    // newTime = new Date(temp[i].assignment_end_time);
                    // temp[i].assignment_end_time = strftime("%y/%m/%d %H:%M:%S", newTime)
                }
                for (let k = 0; k < temp.length; k++)
                {
                    for (let j = k; j < temp.length; j++)
                    {
                        if (Date.parse(temp[k].assignment_end_time) > Date.parse(temp[j].assignment_end_time))
                        {
                            const op = temp[k];
                            temp[k] = temp[j];
                            temp[j] = op;
                        }
                    }
                }
                setData(temp)
            })
        }

        query_assignment_list()
        const timer = setInterval(query_assignment_list, 3000)

        return () =>
        {
            clearInterval(timer)
        }
    }, [])
    return <Layout>
        <Header className="header">
            <img src={logo} style={{height: '45px'}} alt=""/>
        </Header>
        <Layout>
            <Sider style={{width: '200px'}} className="site-layout-content">
                {/*{props.guide}*/}
                <GuideStudent item="assignments"/>
            </Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{margin: '24px 0'}}>
                    <EaseAnim>
                        <div key="assignments">
                            <Table columns={columns} dataSource={data}/>
                        </div>
                    </EaseAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}


// import React from 'react';
// import Guide from "../components/guide";
// import Assignments from "../components/assignments";
//
// export default function()
// {
//     return <Assignments action='questions' guide={Guide({item: 'assignments'})} />
// }