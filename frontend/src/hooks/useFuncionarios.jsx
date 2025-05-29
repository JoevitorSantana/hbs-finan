import { useEffect, useState } from "react";

export function useFuncionarios() {
    
    const token = localStorage.getItem("site");

    const [funcionarios, setFuncionarios] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/funcionarios/listar', {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setFuncionarios(res))
    }, []);
    
    return { funcionarios };
};

export function useFuncionario(id)
{
    const token = localStorage.getItem("site");

    const [funcionario, setFuncionario] = useState(null);
    
    useEffect(() => {
        fetch('http://localhost:8080/funcionarios/' + id, {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setFuncionario(res))
    }, []);

    return { funcionario };
}
export const validarCPF = (cpf) => {
  cpf = cpf.replace(/\D/g, ''); // Remove todos os não-dígitos
  if (cpf.length !== 11 || /^(\d)\1{10}$/.test(cpf)) return false;

  // Cálculo do primeiro dígito verificador
  let soma = 0;
  for (let i = 0; i < 9; i++) {
    soma += parseInt(cpf.charAt(i)) * (10 - i);
  }
  let resto = (soma * 10) % 11;
  if (resto === 10 || resto === 11) resto = 0;
  if (resto !== parseInt(cpf.charAt(9))) return false;

  // Cálculo do segundo dígito verificador
  soma = 0;
  for (let i = 0; i < 10; i++) {
    soma += parseInt(cpf.charAt(i)) * (11 - i);
  }
  resto = (soma * 10) % 11;
  if (resto === 10 || resto === 11) resto = 0;

  return resto === parseInt(cpf.charAt(10));
};


// Máscara para CPF
    export  const aplicarMascaraCPF = (valor) => {
        return valor
            .replace(/\D/g, '')
            .replace(/(\d{3})(\d)/, '$1.$2')
            .replace(/(\d{3})(\d)/, '$1.$2')
            .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    };

export function validarTelefone(fone) {
  const telLimpo = fone.replace(/\D/g, '');
    return telLimpo.length === 11;
}

export function aplicarMascaraTelefone(valor) {
    return valor
        .replace(/\D/g, '')
        .replace(/^(\d{2})(\d)/g, '($1) $2')
        .replace(/(\d{5})(\d)/, '$1-$2')
        .slice(0, 15);
}

export function validarEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}

export const formatarData = (dataCompleta) => {
  const data = new Date(dataCompleta);
  const ano = data.getFullYear();
  const mes = String(data.getMonth() + 1).padStart(2, '0');
  const dia = String(data.getDate()).padStart(2, '0');
   return `${dia}/${mes}/${ano}`;
};