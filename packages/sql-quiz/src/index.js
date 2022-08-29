import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import i18n from 'i18next';
import {initReactI18next} from 'react-i18next';
import 'antd/dist/antd.css';
import './assets/index.css';

i18n
  .use(initReactI18next) // passes i18n down to react-i18next
  .init({
    ns: ['common', 'validation'],
    backend: {
      loadPath: '/locales/{{lng}}/{{ns}}.json',
    },
    lng: 'en',
    fallbackLng: 'en',
    interpolation: {
      escapeValue: false
    }
  });

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App/>
  </React.StrictMode>
);
