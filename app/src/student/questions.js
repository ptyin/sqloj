import React, {useEffect, useState} from "react";
import Guide from "../components/guide";
import axios from 'axios';
import {useHistory} from "react-router-dom";
import {Button, Layout, Space, Table, Tag} from 'antd';
import logo from '../common/images/logo.png';
import '../common/layout.css';
import QueueAnim from "rc-queue-anim";

export default function Questions()
{
    // window.sessionStorage.current = 'assignments'
    // const [timer, setTimer] = useState(0)
    const [show] = useState(true)
    const {Header, Content, Sider} = Layout;
    const [data, setData] = useState([])
    const history = useHistory();
    const columns = [
        // {
        //     title: 'Index',
        //     dataIndex: 'question_index',
        //     key: 'index'
        // },
        {
            title: 'Question Name',
            dataIndex: 'question_name',
            key: 'question_name',
            // render: text => <a>{text}</a>
        },
        {
            title: 'Finished',
            dataIndex: 'is_finished',
            key: 'is_finished',
            render: tag =>
            {
                let color = 'green';
                if (tag === 'No')
                {
                    color = 'volcano';
                }
                return (
                    <Tag color={color} key={tag}>
                        {tag.toUpperCase()}
                    </Tag>);
            }
        },
        {
            title: 'Action',
            key: 'action',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={() =>
                    {
                        window.sessionStorage.question_ID = record.question_id;
                        history.push('submit');
                    }}>details</Button>
                </Space>
            ),
        }
    ];


    const assignment_id = window.sessionStorage.assignment_ID;
    useEffect(() =>
    {
        function select_questions_by_assignment()
        {

            axios.defaults.withCredentials = true;
            axios.get('/api/student/selectQuestionsByAssignment', {
                params:
                    {
                        assignment_id: assignment_id
                    }
            }).then((response) =>
            {
                const temp = response.data
                for (let i = 0; i < temp.length; i++)
                {
                    temp[i].key = temp[i].question_id
                    if (temp[i].is_finished === true)
                    {
                        temp[i].is_finished = 'Yes'
                    } else
                    {
                        temp[i].is_finished = 'No'
                    }
                }
                setData(response.data)
            })
        }

        select_questions_by_assignment()
        const timer = setInterval(select_questions_by_assignment, 3000)

        return () =>
        {
            clearInterval(timer)
        }
    }, [assignment_id])
    return <Layout>
        <Header className="header">
            <img src={logo} style={{height: '45px'}} alt=""/>
        </Header>
        <Layout>
            <Sider width={200} className="site-layout-content"><Guide item="assignments"/></Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{margin: '24px 0'}}>
                    <QueueAnim
                        key="demo"
                        type={['top', 'bottom']}
                        duration="1400"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        {show ? [
                            <Table columns={columns} key="demo1" dataSource={data}/>,
                            <div style={{height: '20px'}}/>,
                        ] : null}
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}