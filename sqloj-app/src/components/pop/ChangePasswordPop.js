import {Form, Input, Modal} from "antd";
import React, {useState} from "react";


export default function ChangePasswordPop(props)
{
    const [oldPassword, setOldPassword] = useState('')
    const [newPassword, setNewPassword] = useState('')
    const singleLayout = {
        // wrapperCol: {offset: 8, span: 16},
        wrapperCol: {span: 24},
    };
    const doubleLayout = {
        labelCol: {span: 8},
        wrapperCol: {span: 16},
    };
    return (
        <Modal
            title="Change Password"
            visible={props.visible}
            onCancel={() => props.setVisible(false)}
        >
            <Form className='change-form'
                  name="basic"
                  initialValues={{remember: true}}>
                <Form.Item {...singleLayout} style={{textAlign: "center"}}>
                    <p className="form_title">{"user: " + window.sessionStorage.username}</p>
                </Form.Item>
                <Form.Item  {...doubleLayout} label="old password">
                    <Input placeholder="Please enter your old password."
                           maxLength={30} type={"password"}
                           onChange={(event) =>
                           {
                               setOldPassword(event.target.value)
                           }}/>
                </Form.Item>
                <Form.Item  {...doubleLayout} label="new password">
                    <Input placeholder="Please enter your new password."
                           maxLength={30} type={"password"}
                           onChange={(event) =>
                           {
                               setNewPassword(event.target.value)
                           }}/>
                </Form.Item>
            </Form>
        </Modal>
    )
}