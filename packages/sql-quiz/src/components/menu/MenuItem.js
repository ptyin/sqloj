import {useTranslation} from 'react-i18next';

export default function (props) {
  const {t} = useTranslation();
  return (
    <div key={props.key} className="w-80 h-full hover:border-b-8 hover:border-b-white">
      {t(props.key)}
    </div>
  );
}