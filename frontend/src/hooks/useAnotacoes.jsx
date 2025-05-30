import { useEffect, useState } from "react";

export function useAnotacoes() {
    
    const token = localStorage.getItem("site");

    const [anotacoes, setAnotacoes] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/anotacoes/listar', {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setAnotacoes(res))
    }, []);
    
    return { anotacoes };
};

export function useAnotacao(id)
{
    const token = localStorage.getItem("site");

    const [anotacao, setAnotacao] = useState(null);
    
    useEffect(() => {
        fetch('http://localhost:8080/anotacoes/' + id, {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setAnotacao(res))
    }, []);

    return { anotacao };
}