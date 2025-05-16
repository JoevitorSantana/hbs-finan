import React, { useContext } from "react";
import { Link } from "react-router-dom";

import { ConfigContext } from "../../../../contexts/ConfigContext";
import * as actionType from "../../../../store/actions";
import { useParametros } from "hooks/useParametros";


const NavLogo = () => {

  const { parametros } = useParametros();

  /** Configuração de colapso do menu (context já existente) */
  const configContext = useContext(ConfigContext);
  const {
    state: { collapseMenu },
    dispatch,
  } = configContext;

  const toggleClass = ["mobile-menu", collapseMenu && "on"].filter(Boolean).join(" ");

  // Fallbacks caso o endpoint ainda não tenha respondido
  const logoUrl = parametros?.logoPequenaUrl || "/assets/images/default-logo.png";
  const empresaNome = parametros?.nomeEmpresa || "ONG";

  return (
    <div className="navbar-brand header-logo">
      {/* Logo + nome – redireciona para a raíz */}
      <Link to="/" className="b-brand d-flex align-items-center gap-2">
        <img src={logoUrl} alt="Logo" style={{ height: 30 }} />
        <span className="b-title">{empresaNome}</span>
      </Link>

      {/* Botão hamburguer para mobile */}
      <Link
        to="#"
        id="mobile-collapse"
        className={toggleClass}
        onClick={() => dispatch({ type: actionType.COLLAPSE_MENU })}
      >
        <span />
      </Link>
    </div>
  );
};

export default NavLogo;


/*AQUI QUE FICA O NOME DA EMPRESA */
