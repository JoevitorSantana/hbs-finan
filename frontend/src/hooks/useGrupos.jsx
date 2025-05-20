import { useEffect, useState } from "react";

export function useGrupos() {
    
    const token = localStorage.getItem("site");

    const [grupos, setGrupos] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/grupos/listar', {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setGrupos(res))
    }, []);
    
    return { grupos };
};

export function useGrupo(id)
{
    const token = localStorage.getItem("site");

    const [grupo, setGrupo] = useState(null);
    
    useEffect(() => {
        fetch('http://localhost:8080/grupos/' + id, {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setGrupo(res))
    }, []);

    return { grupo };
}