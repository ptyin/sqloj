import {Menu} from 'antd';
import {useTranslation} from 'react-i18next';

export default function () {
  const {t} = useTranslation();
  // const items = [
  //   { label: '菜单项一', key: 'question-bank' },
  //   { label: '菜单项二', key: 'item-2' },
  //   {
  //     label: '子菜单',
  //     key: 'submenu',
  //     children: [{ label: '子菜单项', key: 'submenu-item-1' }],
  //   },
  // ];
  const items = ['question-bank', 'databases', 'quizzes', 'settings'].map(item => ({ key: item, label: t(item)}));
  return (
    <div>
      <div className='absolute w-screen h-24 bg-cardboard bg-contain bg-repeat'>
        <Menu className='bg-transparent border-0 h-full' items={items} mode='horizontal'/>
      </div>
      <div id='logo' className='absolute left-8 top-8 w-32 h-32 bg-logo bg-contain'/>
    </div>
  );
}