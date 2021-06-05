import React,{useState,useEffect} from "react";
import Guide from "../components/guide";
import axios from 'axios';
import {useHistory} from "react-router-dom";
import {Table, Space, Button, Layout, Tag} from 'antd';
import logo from '../common/images/logo.png';
import '../common/layout.css';
import QueueAnim from "rc-queue-anim";

export default function Questions() {
    const [timer,setTimer] = useState(0)
    const [show] = useState(true)
    const { Header,Content,Sider }= Layout;
    const [data,setData] = useState([])
    const history = useHistory();
    const columns = [
        // {
        //     title: 'Index',
        //     dataIndex: 'question_index',
        //     key: 'index'
        // },
        {
            title: 'Name',
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
                if (tag === 'F')
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
                    <Button className='button' onClick={()=>
                    {
                        window.sessionStorage.question_ID = record.question_id;
                        history.push('submit');
                    }}>details</Button>
                </Space>
            ),
        }
    ];


    const assignment_id = window.sessionStorage.assignment_ID;
    useEffect(()=>
    {
        const now = Date.now()
        console.log(now, timer)
        if (timer===0||now-timer>3000)
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
                        temp[i].is_finished = 'T'
                    } else
                    {
                        temp[i].is_finished = 'F'
                    }
                }
                setData(response.data)
                setTimer(now)
            })
        }
    })
    return<Layout>
        <Header className="header" >
            <img src={logo} style={{height:'45px'}} alt= "" />
        </Header>
        <Layout>
            <Sider width={200} className="site-layout-content"><Guide/></Sider>
            <Layout style={{padding:'0 24px 24px'}}>
                <Content className="default_font" style={{ height:'600px', margin: '24px 0' }}>
                    <QueueAnim
                        key="demo"
                        type={['top', 'bottom']}
                        duration = "1400"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        {show ? [
                            <Table columns={columns} key="demo1" dataSource={data} />,
                            <div style={{height:'20px'}} />,
                        ] : null}
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}