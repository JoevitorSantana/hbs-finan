import { useEffect, useState } from "react";

// Lista todas as doações
export function useDoacoes() {
    const token = localStorage.getItem("site");
    const [doacoes, setDoacoes] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/doacao-monetaria/listar', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            }
        })
        .then(response => response.json())
        .then(setDoacoes)
        .catch((err) => console.error("Erro ao buscar doações:", err));
    }, []);

    return { doacoes };
}

// Busca uma doação por ID
export function useDoacao(id) {
    const token = localStorage.getItem("site");
    const [doacao, setDoacao] = useState(null);

    useEffect(() => {
        if (id == null) return;
        fetch(`http://localhost:8080/doacao-monetaria/${id}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            }
        })
        .then(response => response.json())
        .then(setDoacao)
        .catch((err) => console.error("Erro ao buscar doação:", err));
    }, [id]);

    return { doacao };
}

// Busca doações por apoiador
export function useDoacaoPorApoiador(apoiador) {
    const token = localStorage.getItem("site");
    const [doacao, setDoacao] = useState(null);

    useEffect(() => {
        if (!apoiador) return;
        fetch(`http://localhost:8080/doacao-monetaria?apoiador=${apoiador}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            }
        })
        .then(response => response.json())
        .then(setDoacao)
        .catch((err) => console.error("Erro ao buscar por apoiador:", err));
    }, [apoiador]);

    return { doacao };
}

// Busca doações por caixa
export function useDoacaoPorCaixa(idCaixa) {
    const token = localStorage.getItem("site");
    const [doacoes, setDoacoes] = useState(null);

    useEffect(() => {
        if (idCaixa == null) return;
        fetch(`http://localhost:8080/doacao-monetaria/caixa/${idCaixa}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            }
        })
        .then(response => response.json())
        .then(setDoacoes)
        .catch((err) => console.error("Erro ao buscar por caixa:", err));
    }, [idCaixa]);

    return { doacoes };
}