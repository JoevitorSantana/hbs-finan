import { useEffect, useState } from "react";

export function useDespesas() {
    
    const token = localStorage.getItem("site");

    const [despesas, setDespesas] = useState([null]);

    useEffect(() => {
        fetch('http://localhost:8080/despesas/listar', {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setDespesas(res))
    }, []);
    
    return { despesas };
};

export function useDespesa(id)
{
    const token = localStorage.getItem("site");

    const [despesa, setDespesa] = useState(null);
    
    useEffect(() => {
        fetch('http://localhost:8080/despesas/' + id, {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setDespesa(res))
    }, []);

    return { despesa };
}

export const formatarData = (dataCompleta) => {
  const data = new Date(dataCompleta);
  const ano = data.getFullYear();
  const mes = String(data.getMonth() + 1).padStart(2, '0');
  const dia = String(data.getDate()).padStart(2, '0');
   return `${dia}/${mes}/${ano}`;
};