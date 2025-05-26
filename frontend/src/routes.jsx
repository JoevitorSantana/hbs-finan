import React, { Suspense, Fragment, lazy, Component } from 'react';
import { Routes, Route, Navigate, Outlet } from 'react-router-dom';

import Loader from './components/Loader/Loader';
import AdminLayout from './layouts/AdminLayout';

import { BASE_URL } from './config/constant';
import AuthProvider, { useAuth } from 'contexts/AuthProvider';

export const PrivateRoute = () => {
  const user = useAuth();
  if (!user.token) return <Navigate to="/login" />;
  return <Outlet />;
};

export const renderRoutes = (routes = []) => (
  <Suspense fallback={<Loader />}>
    <AuthProvider>
    <Routes>
        {routes.map((route, i) => {
          const Guard = route.guard || Fragment;
          const Layout = route.layout || Fragment;
          const Element = route.element;
          
          if (route.visibility == 'public') {
            return (
              <Route
                key={i}
                path={route.path}
                element={
                  <Guard>
                    <Layout>{route.routes ? renderRoutes(route.routes) : <Element props={true} />}</Layout>
                  </Guard>
                }
              />
            );
          } else {
            return (
              <Route key={i} element={<PrivateRoute />}>
              <Route
                path={route.path}
                element={
                  <Guard>
                    <Layout>{route.routes ? renderRoutes(route.routes) : <Element props={true} />}</Layout>
                  </Guard>
                }
              />
              </Route>
            );
          }
        })}
      </Routes>
    </AuthProvider>
  </Suspense>
);

const routes = [
  {
    exact: 'true',
    path: '/login',
    visibility: 'public',
    element: lazy(() => import('./views/auth/signin/SignIn1'))
  },
  {
    path: '*',
    layout: AdminLayout,
    routes: [
      {
        exact: 'true',
        path: '/',
        element: lazy(() => import('./views/dashboard'))
      },
      {
        exact: 'true',
        path: '/usuarios',
        element: lazy(() => import('./views/usuarios'))
      },
      {
        exact: 'true',
        path: '/usuarios/novo',
        element: lazy(() => import('./views/usuarios/novo'))
      },
      {
        exact: 'true',
        path: '/usuarios/editar/*',
        element: lazy(() => import('./views/usuarios/editar'))
      },
      {
        exact: 'true',
        path: '/caixa',
        element: lazy(() => import('./views/caixa'))
      },
      {
        exact: 'true',
        path: '/despesas',
        element: lazy(() => import('./views/despesas'))
      },
       {
        exact: 'true',
        path: '/despesas/novo',
        element: lazy(() => import('./views/despesas/novo'))
      },
      {
        exact: 'true',
        path: '/despesas/editar/*',
        element: lazy(() => import('./views/despesas/editar'))
      },
      {
        exact: 'true',
        path: '/doacao/alimenticia',
        element: lazy(() => import('./views/doacoes/alimenticia'))
      },
      {
        exact: 'true',
        path: '/doacao/material',
        element: lazy(() => import('./views/doacoes/material'))
      },
      {
        exact: 'true',
        path: '/doacao/monetaria',
        element: lazy(() => import('./views/doacoes/monetaria'))
      },
      {
        exact: 'true',
        path: '/eventos/criar',
        element: lazy(() => import('./views/eventos/criar'))
      },
      {
        exact: 'true',
        path: '/eventos/inscrever',
        element: lazy(() => import('./views/eventos/inscrever'))
      },
      {
        exact: 'true',
        path: '/funcionarios',
        element: lazy(() => import('./views/funcionarios'))
      },
      {
        exact: 'true',
        path: '/funcionarios/novo',
        element: lazy(() => import('./views/funcionarios/novo'))
      },
      {
        exact: 'true',
        path: '/funcionarios/editar/*',
        element: lazy(() => import('./views/funcionarios/editar'))
      },
      {
        exact: 'true',
        path: '/produtos',
        element: lazy(() => import('./views/produtos'))
      },
      {
        exact: 'true',
        path: '/apoiador',
        element: lazy(() => import('./views/apoiador'))
      },
      {
        exact: 'true',
        path: '/parametrizacao',
        element: lazy(() => import('./views/parametrizacao'))
      },
      {
        exact: 'true',
        path: '/parametrizacao/editar/*',
        element: lazy(() => import('./views/parametrizacao/editar'))
      },
      {
        exact: 'true',
        path: '/produtos/novo',
        element: lazy(() => import('./views/produtos/novo'))
      },
      {
        exact: 'true',
        path: '/produtos/editar/*',
        element: lazy(() => import('./views/produtos/editar'))
      },
      {
        exact: 'true',
        path: '/grupos',
        element: lazy(() => import('./views/grupos'))
      },
      {
        exact: 'true',
        path: '/grupos/novo',
        element: lazy(() => import('./views/grupos/novo'))
      },
      {
        exact: 'true',
        path: '/grupos/editar/*',
        element: lazy(() => import('./views/grupos/editar'))
      },
      {
        path: '*',
        exact: 'true',
        element: () => <Navigate to={BASE_URL} />
      }
    ]
  }
];

export default routes;
