import {Menu} from 'antd';
import {useTranslation} from 'react-i18next';

export default function () {
  const { t } = useTranslation();
  const items = [
    { key: 'menu-question-bank' },
    { key: 'menu-databases' },
    {
      key: 'menu-quizzes',
      children: [
        { key: 'submenu-student' },
        { key: 'submenu-teacher' }
      ]
    },
    { key: 'menu-settings' },
  ];
  const addLabel = items => items.forEach(item => {
      item.label = t(item.key);
      item.children && addLabel(item.children);
    });
  addLabel(items);

  return (
    <div>
      <div className='absolute w-screen h-24 bg-cardboard bg-contain bg-repeat'>
        <Menu className='bg-transparent border-0 h-full' items={items} mode='horizontal'/>
      </div>
      <div id='logo' className='absolute left-8 top-8 w-32 h-32 bg-logo bg-contain'/>
    </div>
  );
}