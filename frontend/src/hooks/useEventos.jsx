import { useEffect, useState } from "react";

export function useEventos() {
    
    const token = localStorage.getItem("site");

    const [eventos, setEventos] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/eventos/listar', {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setEventos(res))
    }, []);
    
    return { eventos };
};

export function useEvento(id)
{
    const token = localStorage.getItem("site");

    const [evento, setEvento] = useState(null);
    
    useEffect(() => {
        fetch('http://localhost:8080/eventos/' + id, {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setEvento(res))
    }, []);

    return { evento };
}