import React, {useState, useEffect} from "react";
import GuideTeacher from "../components/guideTeacher";
import axios from 'axios';
import {useHistory} from "react-router-dom";
import {Table, Space, Button, Layout, Radio, message} from 'antd';
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
    const columns = [
        {
            title: 'Name',
            dataIndex: 'database_name',
            key: 'database_name'
        },
        {
            title: 'Description',
            dataIndex: 'database_description',
            key: 'database_description'
        },
        // {
        //     title: 'Enabled',
        //     dataIndex: 'is_enabled',
        //     key: 'is_enabled',
        // },
        {
            title: 'Delete',
            key: 'delete',
            render: (record) => (
                <Space size="middle">
                    <Button className='button' onClick={() =>
                    {
                        console.log(record.database_id)
                        axios.get('/api/teacher/deleteDatabaseById', {
                            params: {
                                database_id: record.database_id,
                            }
                        })
                        success_delete()
                        history.push('/database')
                    }}>delete</Button>
                </Space>
            ),
        },
        // {
        //     title: 'Init_type',
        //     key: 'init_type',
        //     render: (record) => (
        //         <Space size="middle">
        //             <Radio.Group value={operation_type} onChange={e =>
        //             {
        //                 setOperation_type(e.target.value);
        //             }}>
        //                 <Radio.Button value="query">query</Radio.Button>
        //                 <Radio.Button value="trigger">trigger</Radio.Button>
        //             </Radio.Group>
        //         </Space>
        //     ),
        // },

        // {
        //     title: 'Init_Docker',
        //     key: 'Init',
        //     render: (record) => (
        //         <Space size="middle">
        //             <Button className='button' onClick={() =>
        //             {
        //                 axios.get('/api/admin/initDatabaseDocker', {
        //                     params: {
        //                         database_id: record.id,
        //                         operation_type: operation_type
        //                     }
        //                 })
        //                 success_init();
        //             }}>start</Button>
        //         </Space>
        //     ),
        // }
    ];
    useEffect(() =>
    {
        function queryDatabaseList()
        {
            axios.get('/api/teacher/queryDatabaseList').then((response) =>
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
                                history.push('/AddDatabase')
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

