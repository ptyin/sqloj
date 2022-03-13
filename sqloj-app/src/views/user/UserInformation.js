import {Avatar, Form, Input, message, Tooltip, Upload} from "antd";
import {useEffect, useState} from "react";
import ImgCrop from 'antd-img-crop';


export default function UserInformation(props)
{
    const [information, setInformation] = useState({})
    const doubleLayout = {
        labelCol: { offset: 2, span: 6 },
        wrapperCol: { offset: 1, span: 8 },
    };

    const headLayout = {
        wrapperCol: {offset: 4, span: 8},
    };

    useEffect(() =>
    {
        const demo = {
            uuid: 'abc',
            id: '201805130154',
            name: '尹祥琨',
            major: '计算机科学与技术',
            grade: '2018级',
            remark: ''
        }
        setInformation(demo)
    }, [])

    return (
        <div className={'sqloj-user-information'}>
            <div className={'sqloj-user-avatar'} style={{float: 'left'}}>
                <ImgCrop rotate
                         modalTitle={'Upload avatar'}>
                    <Upload
                        action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
                        listType="picture-card"
                    >
                        <Tooltip title="upload avatar" placement="top">
                            <Avatar shape='square' size={102}
                                    src={'https://joeschmoe.io/api/v1/random'}/>
                        </Tooltip>
                    </Upload>
                </ImgCrop>
            </div>

            <Form name='information'
                  style={{backgroundColor: '#f0f2f5'}}
            >
                <Form.Item {...doubleLayout} label='id'>
                    {information.id}
                </Form.Item>
                <Form.Item {...doubleLayout} label='name'>
                    {information.name}
                </Form.Item>
                <Form.Item {...doubleLayout} label='major'>
                    {information.major}
                </Form.Item>
                <Form.Item {...doubleLayout} label='grade'>
                    {information.grade}
                </Form.Item>
                <Form.Item {...doubleLayout} label='remark'>
                    {information.remark}
                </Form.Item>
            </Form>
        </div>
    )
}