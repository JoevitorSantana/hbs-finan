const menuItems = {
  items: [
    {
      id: 'navigation',
      title: 'Navegação',
      type: 'group',
      icon: 'icon-navigation',
      children: [
        {
          id: 'dashboard',
          title: 'Dashboard',
          type: 'item',
          icon: 'feather icon-home',
          url: '/'
        },
        {
          id: 'apoiador',
          title: 'Apoiador',
          type: 'item',
          icon: 'feather icon-home',
          url: '/apoiador'
        },
        {
          id: 'caixa',
          title: 'Caixa',
          type: 'item',
          icon: 'feather icon-monitor',
          url: '/caixa'
        },
        {
          id: 'despesas',
          title: 'Despesas',
          type: 'item',
          icon: 'feather icon-percent',
          url: '/despesas'
        },
        {
          id: 'doacoes',
          title: 'Doações',
          type: 'collapse',
          icon: 'feather icon-box',
          children: [
            {
              id: 'material',
              title: 'Doação Material',
              type: 'item',
              url: '/doacao/material'
            },
            {
              id: 'alimenticia',
              title: 'Doação Alimentícia',
              type: 'item',
              url: '/doacao/alimenticia'
            },
            {
              id: 'monetaria',
              title: 'Doação Monetária',
              type: 'item',
              url: '/doacao/monetaria'
            },
          ],
        },
        {
          id: 'eventos',
          title: 'Eventos',
          type: 'collapse',
          icon: 'feather icon-box',
          children: [
            {
              id: 'criar',
              title: 'Criar evento',
              type: 'item',
              url: '/eventos/criar'
            },
            {
              id: 'inscrever',
              title: 'Inscrever-se Evento',
              type: 'item',
              url: '/eventos/inscrever'
            },
          ],
        },
        {
          id: 'funcionarios',
          title: 'Funcionários',
          type: 'item',
          icon: 'feather icon-briefcase',
          url: '/funcionarios'
        },
        {
          id: 'produtos',
          title: 'Produtos',
          type: 'item',
          icon: 'feather icon-shopping-cart',
          url: '/produtos'
        },
        {
          id: 'usuarios',
          title: 'Usuários',
          type: 'item',
          icon: 'feather icon-users',
          url: '/usuarios'
        },
        {
          id: 'parametrizacao',
          title: 'Parametrização',
          type: 'item',
          icon: 'feather icon-users',
          url: '/parametrizacao'
        },
        {
          id: 'grupos',
          title: 'Grupos',
          type: 'item',
          icon: 'feather icon-users',
          url: '/grupos'
        }
      ]
    },
  ]
};

export default menuItems;
