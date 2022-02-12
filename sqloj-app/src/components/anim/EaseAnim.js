import QueueAnim from "rc-queue-anim";

export default function EaseAnim(props)
{
    return <QueueAnim
    className="anime"
    key="ease"
    type={['top', 'bottom']}
    duration="1400"
    ease={['easeOutQuart', 'easeInOutQuart']}>
        {props.children}
    </QueueAnim>
}