import React, {useState, useEffect} from "react";
import axios from 'axios';
import {useHistory} from "react-router-dom";
import {Table, Space, Button, Layout, message} from 'antd';
import logo from '../common/images/logo.png';
import QueueAnim from 'rc-queue-anim';
import '../common/layout.css';
import Guide from "../components/guideTeacher";

import strftime from "strftime";


export default function Assignments()
{
    // window.sessionStorage.current = 'assignments'
    // const [timer, setTimer] = useState(0)
    // const [show] = useState(true)
    const {Header, Content, Sider} = Layout;
    const [data, setData] = useState([])
    const history = useHistory();
    const columns = [
        {
            title: 'Assignment Name',
            dataIndex: 'assignment_name',
            key: 'name',
            // render: text => <a>{text}</a>,
        },
        {
            title: 'Start',
            dataIndex: 'assignment_start_time',
            key: 'start',
        },
        {
            title: 'DDL',
            dataIndex: 'assignment_end_time',
            key: 'deadline',
        },
        {
            title: 'Detail',
            key: 'detail',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={() =>
                    {
                        window.sessionStorage.assignment_id = record.assignment_id;
                        // window.localStorage.assignment_ID = record.assignment_name;
                        history.push("/teacherQuestions");
                    }}>detail</Button>
                </Space>
            ),
        },
        {
            title: 'Update',
            key: 'update',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={()=>{
                        window.sessionStorage.assignment_id = record.assignment_id;
                        window.sessionStorage.assignment_name = record.assignment_name;
                        window.sessionStorage.assignment_start_time = record.assignment_start_time;
                        window.sessionStorage.assignment_end_time = record.assignment_end_time;

                        history.push('/updateAssignment');
                    }}>update</Button>
                </Space>
            ),
        },
        {
            title: 'Delete',
            key: 'delete',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={()=>{
                        axios.delete('/api/teacher/AssignmentDetail', {
                            params: {
                                assignment_id: record.assignment_id,
                            }
                        }).then((response) =>
                        {
                            if (response.data.success)
                            {
                                message.success('Delete successfully.');
                                query_assignment_list()
                            }
                            else
                            {
                                message.error('Fail to delete, please retry.');
                            }
                        })
                    }}>delete</Button>
                </Space>
            ),
        }
    ];

    function query_assignment_list()
    {
        axios.defaults.withCredentials = true;
        axios.get('/api/teacher/AssignmentListQuery').then((response) =>
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

    useEffect(() =>
    {
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
                <Guide item="assignments"/>
            </Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{margin: '24px 0'}}>
                    <QueueAnim
                        key="demo"
                        type={['top', 'bottom']}
                        duration="1400"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        <div key="assignments">
                            <Button type="primary" style={{width: "90px", margin: '0 0 20px 0'}} onClick={()=>{
                                history.push('/addAssignment')
                            }}>Add</Button>
                            <Table columns={columns}  dataSource={data}/>
                        </div>
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}



// import React from 'react';
// import Guide from "../components/guideTeacher";
// import Assignments from "../components/assignments";
//
//
// export default function()
// {
//     return <Assignments action='addAssignment' guide={Guide({item: "assignments"})}/>
// }