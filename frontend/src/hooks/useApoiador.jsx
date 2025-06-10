import { useEffect, useState } from "react";

export function useApoiadores() {
    
    const token = localStorage.getItem("site");

    const [apoiadores, setApoiadores] = useState([]); 

    useEffect(() => {
        fetch('http://localhost:8080/apoiador/listar', {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setApoiadores(res))
    }, []);
    
    return { apoiadores };
};


export function useApoiador(id)
{
    const token = localStorage.getItem("site");

    const [apoiador, setApoiador] = useState(null);
    
    useEffect(() => {
        fetch('http://localhost:8080/apoiador/' + id, {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setApoiador(res))
    }, []);

    return { apoiador };
}

export function validarCPF(cpf) {
    cpf = cpf.replace(/[^\d]+/g, '');

    if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;

    let soma = 0;
    for (let i = 0; i < 9; i++) soma += parseInt(cpf.charAt(i)) * (10 - i);
    let resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    if (resto !== parseInt(cpf.charAt(9))) return false;

    soma = 0;
    for (let i = 0; i < 10; i++) soma += parseInt(cpf.charAt(i)) * (11 - i);
    resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    return resto === parseInt(cpf.charAt(10));
}

export function formatarCPF(cpf) {
  cpf = cpf.replace(/[^\d]+/g, '');

  if (cpf.length !== 11) return 'CPF inválido';

  return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
}


export function validarTelefone(telefone) {
  const regex = /^\(?\d{2}\)?\s?9?\d{4}-?\d{4}$/;
  return regex.test(telefone);
}

export const formatarData = (dataCompleta) => {
  const data = new Date(dataCompleta);
  const ano = data.getFullYear();
  const mes = String(data.getMonth() + 1).padStart(2, '0');
  const dia = String(data.getDate()).padStart(2, '0');
  return `${dia}/${mes}/${ano}`;
};
export const carregarGrupos = async () => {
  const token = localStorage.getItem("site");

  try {
    const response = await fetch("http://localhost:8080/grupos/listar", {
      headers: {
        'Authorization': 'Bearer ' + token
      }
    });
    if (response.ok) {
      const data = await response.json();
      return data;  // retorna os dados aqui
    } else {
      console.error("Erro ao carregar grupos");
      return [];
    }
  } catch (error) {
    console.error("Erro na requisição de grupos:", error);
    return [];
  }
};