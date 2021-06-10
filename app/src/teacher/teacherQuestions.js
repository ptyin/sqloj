import React, {useState, useEffect} from "react";
import GuideTeacher from "../components/guideTeacher";
import axios from 'axios';
import {useHistory} from "react-router-dom";
import {Table, Space, Button, Layout, Card, message} from 'antd';
import logo from '../common/images/logo.png';
import '../common/layout.css';
import QueueAnim from "rc-queue-anim";

export default function Questions()
{
    // const [timer,setTimer] = useState(0)
    // const [show] = useState(true)
    const {Header, Content, Sider} = Layout;
    const [data, setData] = useState([])
    const history = useHistory();
    const assignment_id = window.sessionStorage.assignment_id;
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
            title: 'Detail',
            key: 'detail',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={() =>
                    {
                        window.sessionStorage.detail_question_id = record.question_id;
                        history.push('/questionDetail');
                    }}>detail</Button>
                </Space>
            ),
        },
        {
            title: 'Update',
            key: 'update',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={() =>
                    {
                        window.sessionStorage.update_question_id = record.question_id;
                        history.push('/updateQuestion');
                    }}>update</Button>
                </Space>
            ),
        },
        {
            title: 'Delete',
            key: 'delete',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={() =>
                    {
                        axios.delete('api/teacher/QuestionsDetail', {
                            params: {
                                question_id: record.question_id,
                            }
                        }).then((response) =>
                        {
                            if (response.data.success)
                            {
                                message.success('Delete successfully.');
                                select_questions_by_assignment()
                            } else
                            {
                                message.error('Fail to delete, please retry.');
                            }
                        })
                    }}>delete</Button>
                </Space>
            ),
        }
        ,
        // {
        //     title: 'copycat',
        //     key: 'copycat',
        //     render: (record) => (
        //         <Space size="middle">
        //             <Button className='button' onClick={()=>{
        //                 window.localStorage.question_id = record.question_id
        //                 history.push('/copycat');
        //             }}>copycat</Button>
        //         </Space>
        //     ),
        // }
    ];

    function select_questions_by_assignment()
    {

        axios.defaults.withCredentials = true;
        axios.get('/api/teacher/QuestionList', {
            params:
                {
                    assignment_id: assignment_id
                }
        }).then((response) =>
        {
            setData(response.data)
        })
    }

    useEffect(() =>
    {
        select_questions_by_assignment()
        const timer = setInterval(select_questions_by_assignment, 3000)

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
            <Sider width={200} className="site-layout-content"><GuideTeacher item="assignments"/></Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{margin: '24px 0'}}>
                    <QueueAnim
                        key="demo"
                        type={['top', 'bottom']}
                        duration="1400"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        <div key="questions">
                            <Button type="primary" style={{width: "90px", margin: "0 10px"}} onClick={() =>
                            {
                                history.push('/AddQuestion')
                            }}>Add</Button>
                            <div style={{height: '20px'}}/>
                            <Table columns={columns} key="demo1" dataSource={data}/>
                        </div>
                    </QueueAnim>
                </Content>
            </Layout>
        </Layout>
    </Layout>
}