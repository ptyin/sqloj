import {message, Popconfirm} from "antd";


/**
 * @callback OnConfirmCallback
 * @return Promise
 */
/**
 * @param props {{thing: string, onConfirm: OnConfirmCallback}}
 * @returns {JSX.Element}
 * @constructor
 */
export default function DeletePop(props)
{

    const confirm = () => {
        if (props.onConfirm)
            props.onConfirm().then(message.success('Delete successfully'))
    }

    const cancel = () => {
      message.warn('Cancel deletion.')
    }

    return (
        <Popconfirm
            title={`Are you sure to delete this ${props.thing}?`}
            onConfirm={confirm}
            onCancel={cancel}
            okText="Yes"
            cancelText="No"
        >
            {props.children}
        </Popconfirm>
    )
}