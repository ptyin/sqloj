import {Badge, Button, Descriptions, message, PageHeader} from "antd";
import AssignmentList from "./assignment/AssignmentList";
import {useEffect, useState} from "react";
import {LikeOutlined} from "@ant-design/icons";

export default function Course(props)
{

    const [course, setCourse] = useState(null)
    useEffect(() =>
    {
        const demo = {
            uuid: 'abc',
            name: `Course abc`,
            description: `This is a mock course`,
            createdAt: new Date().toDateString(),
            startedAt: new Date().toDateString(),
            endedAt: new Date().toDateString(),
        }
        setCourse(demo)
    }, [])

    return (
        <div>
            <PageHeader
                ghost={false}
                onBack={() => window.history.back()}
                title={props.match.params.courseUuid}
                subTitle={course?.description}
                extra={[
                        <Badge count={0}>
                            <Button key="like" onClick={() => message.warn("not developed yet.")}>
                                <LikeOutlined />
                            </Button>
                        </Badge>
                ]}
            >
                <Descriptions size="small" column={3}>
                    <Descriptions.Item label="Created At">{course?.createdAt}</Descriptions.Item>
                    <Descriptions.Item label="Started At">{course?.startedAt}</Descriptions.Item>
                    <Descriptions.Item label="Ended At">{course?.endedAt}</Descriptions.Item>
                </Descriptions>
            </PageHeader>
            <AssignmentList/>
        </div>
    )
}