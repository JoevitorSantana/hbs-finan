import React from 'react';
import { BrowserRouter } from 'react-router-dom';

import routes, { renderRoutes } from './routes';
import AuthProvider from 'contexts/AuthProvider';

const App = () => {
  return (
    <BrowserRouter basename={import.meta.env.VITE_APP_BASE_NAME}>
      {renderRoutes(routes)}
    </BrowserRouter>
  );
};

export default App;


/*APP É A CABEÇA DE TUDO */