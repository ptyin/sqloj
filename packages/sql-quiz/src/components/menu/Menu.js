import {useTranslation} from 'react-i18next';
import MenuItem from './MenuItem';

export default function () {
  // const {t} = useTranslation();
  // const items = [
  //   {key: 'menu-question-bank'},
  //   {key: 'menu-databases'},
  //   {
  //     key: 'menu-quizzes',
  //     children: [
  //       {key: 'submenu-student'},
  //       {key: 'submenu-teacher'}
  //     ]
  //   },
  //   {key: 'menu-settings'},
  // ];
  // const addLabel = items => items.forEach(item => {
  //   item.label = t(item.key);
  //   item.children && addLabel(item.children);
  // });
  // addLabel(items);
  const items = ['menu-question-bank', 'menu-databases', 'menu-quizzes', 'menu-settings'];
  const menuItems = items.map(item => <MenuItem key={item} />);

  return (
    <div>
      <div className="absolute w-screen h-24 bg-cardboard bg-contain bg-repeat">
        {menuItems}
      </div>
      <div id="logo" className="absolute left-8 top-8 w-32 h-32 bg-logo bg-contain"/>
    </div>
  );
}