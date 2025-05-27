import { useEffect, useState } from "react";

// Hook para buscar todas as caixas
export function useCaixas() {
    const token = localStorage.getItem("site");
    const [caixas, setCaixas] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/caixa/listar', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            }
        })
            .then((response) => response.json())
            .then((res) => setCaixas(res))
            .catch((error) => console.error("Erro ao buscar caixas:", error));
    }, []);

    console.log(caixas);
    return { caixas };
}

// Hook para buscar uma caixa específica pelo ID
export function useCaixa(id) {
    const token = localStorage.getItem("site");
    const [caixa, setCaixa] = useState(null);

    useEffect(() => {
        fetch(`http://localhost:8080/caixa/${id}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            }
        })
            .then((response) => response.json())
            .then((res) => setCaixa(res))
            .catch((error) => console.error("Erro ao buscar caixa:", error));
    }, [id]);

    return { caixa };
}
