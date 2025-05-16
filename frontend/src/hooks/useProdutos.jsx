import { useEffect, useState } from "react";

export function useProdutos() {
    
    const token = localStorage.getItem("site");

    const [produtos, setProdutos] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/produtos/listar', {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setProdutos(res))
    }, []);
    
    return { produtos };
};

export function useProduto(id)
{
    const token = localStorage.getItem("site");

    const [produto, setProduto] = useState(null);
    
    useEffect(() => {
        fetch('http://localhost:8080/produtos/' + id, {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setProduto(res))
    }, []);

    return { produto };
}