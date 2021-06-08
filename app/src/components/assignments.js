import React, {useState, useEffect} from "react";
import axios from 'axios';
import {useHistory} from "react-router-dom";
import {Table, Space, Button, Layout} from 'antd';
import logo from '../common/images/logo.png';
import QueueAnim from 'rc-queue-anim';
import '../common/layout.css';


export default function Assignments(props)
{
    // window.sessionStorage.current = 'assignments'
    // const [timer, setTimer] = useState(0)
    // const [show] = useState(true)
    const {Header, Content, Sider} = Layout;
    const [data, setData] = useState([])
    const history = useHistory();
    const columns = [
        {
            title: 'Name',
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
            title: 'Action',
            key: 'action',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={() =>
                    {
                        window.sessionStorage.assignment_ID = record.assignment_id;
                        // window.localStorage.assignment_ID = record.assignment_name;
                        history.push(props.action);
                    }}>{props.action}</Button>
                </Space>
            ),
        }
    ];


    useEffect(() =>
    {
        const now = Date.now()
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
                    let newTime = new Date(temp[i].assignment_start_time);
                    temp[i].assignment_start_time = newTime.getFullYear() + '/' + newTime.getMonth() + '/' + newTime.getDate() + ' ' + newTime.getHours() + ':' + newTime.getMinutes()
                    newTime = new Date(temp[i].assignment_end_time);
                    temp[i].assignment_end_time = newTime.getFullYear() + '/' + newTime.getMonth() + '/' + newTime.getDate() + ' ' + newTime.getHours() + ':' + newTime.getMinutes()
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
                {props.guide}
                {/*<Guide item="assignments"/>*/}
            </Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{height: '680px', margin: '24px 0'}}>
                    <QueueAnim
                        key="demo"
                        type={['top', 'bottom']}
                        duration="1400"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        <div key="assignments">
                            <Table columns={columns}  dataSource={data}/>
                        </div>
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}

