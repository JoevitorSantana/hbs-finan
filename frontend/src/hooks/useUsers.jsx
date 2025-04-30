import { useEffect, useState } from "react";

export function useUsuarios() {
    
    const token = localStorage.getItem("site");

    const [usuarios, setUsuarios] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/usuarios/listar', {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setUsuarios(res))
    }, []);
    
    return { usuarios };
};

export function useUsuario(id)
{
    const token = localStorage.getItem("site");

    const [usuario, setUsuario] = useState(null);
    
    useEffect(() => {
        fetch('http://localhost:8080/usuarios/' + id, {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setUsuario(res))
    }, []);

    return { usuario };
}