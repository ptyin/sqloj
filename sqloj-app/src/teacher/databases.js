import React, {useEffect, useState} from "react";
import GuideTeacher from "../components/guideTeacher";
import axios from 'axios';
import {useHistory} from "react-router-dom";
import {Button, Layout, message, Space, Table} from 'antd';
import logo from '../common/images/logo.png';
import QueueAnim from 'rc-queue-anim';
import '../common/layout.css';

export default function Databases()
{
    const success_init = () =>
    {
        message.success('init successfully');
    };
    const success_delete = () =>
    {
        message.success('delete successfully');
    };
    // const [show] = useState(true)
    // const [operation_type, setOperation_type] = useState('query')
    const {Header, Content, Sider} = Layout;
    const [data, setData] = useState([])
    const history = useHistory();

    function queryDatabaseList()
    {
        axios.get('/api/teacher/DatabaseListQuery').then((response) =>
        {
            const temp = response.data
            setData(temp)
        })
    }

    const columns = [
        {
            title: 'Name',
            dataIndex: 'db_name',
            key: 'database_name'
        },
        {
            title: 'Description',
            dataIndex: 'db_description',
            key: 'database_description'
        },
        {
            title: 'Upload Time',
            dataIndex: 'upload_time',
            key: 'upload_time'
        },
        {
            title: 'Delete',
            key: 'delete',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={() =>
                    {
                        console.log(record.database_id)
                        axios.delete('/api/teacher/DatabaseDetail', {
                            params: {
                                db_id: record.db_id,
                            }
                        }).then((response) =>
                        {
                            if (response.data.success)
                            {
                                message.success('delete successfully.');
                                queryDatabaseList()
                            } else
                            {
                                message.error('Fail to delete, there are some questions dependent on this database.');
                            }
                        })
                    }}>delete</Button>
                </Space>
            ),
        },
    ];
    useEffect(() =>
    {
        function queryDatabaseList()
        {
            axios.get('/api/teacher/DatabaseListQuery').then((response) =>
            {
                const temp = response.data
                setData(temp)
            })
        }

        queryDatabaseList()
        const timer = setInterval(queryDatabaseList, 3000)

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
            <Sider style={{width: '200px'}} className="site-layout-content"><GuideTeacher item={"databases"}/></Sider>
            <Layout style={{padding: '0 24px 24px'}}>
                <Content className="default_font" style={{margin: '24px 0'}}>
                    <QueueAnim
                        key="demo"
                        type={['top', 'bottom']}
                        duration="1400"
                        ease={['easeOutQuart', 'easeInOutQuart']}>
                        <div key="databases">
                            <Button style={{width: "90px"}} type="primary" onClick={() =>
                            {
                                history.push('/addDatabase')
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

