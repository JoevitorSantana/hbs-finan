import { useEffect, useState } from "react";

export function useParametros() {
    
    const token = localStorage.getItem("site");

    //const [usuarios, setUsuarios] = useState(null);
    const [parametros, setParametros] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/api/parametrizacao', {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token,
            }
        })
        .then((response) => response.json())
        .then((res) => {
            setParametros(res);
        })
    }, []);
    
    return { parametros };
}