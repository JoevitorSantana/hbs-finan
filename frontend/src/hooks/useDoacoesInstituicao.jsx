import { useEffect, useState } from "react";

// Lista todas as doações para instituição
export function useDoacoesInstituicao() {
    const token = localStorage.getItem("site");
    const [doacoes, setDoacoes] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/doacao-instituicao/listar', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            }
        })
        .then(response => response.json())
        .then(setDoacoes)
        .catch((err) => console.error("Erro ao buscar doações para instituição:", err));
    }, []);

    return { doacoes };
}

// Busca uma doação por ID
export function useDoacaoInstituicao(id) {
    const token = localStorage.getItem("site");
    const [doacao, setDoacao] = useState(null);

    useEffect(() => {
        if (id == null) return;
        fetch(`http://localhost:8080/doacao-instituicao/${id}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            }
        })
        .then(response => response.json())
        .then(setDoacao)
        .catch((err) => console.error("Erro ao buscar doação para instituição:", err));
    }, [id]);

    return { doacao };
}

// Busca doações por apoiador
export function useDoacaoInstituicaoPorApoiador(apoiador) {
    const token = localStorage.getItem("site");
    const [doacao, setDoacao] = useState(null);

    useEffect(() => {
        if (!apoiador) return;
        fetch(`http://localhost:8080/doacao-instituicao?apoiador=${apoiador}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            }
        })
        .then(response => response.json())
        .then(setDoacao)
        .catch((err) => console.error("Erro ao buscar por apoiador (instituição):", err));
    }, [apoiador]);

    return { doacao };
}

// Busca doações por caixa
export function useDoacaoInstituicaoPorCaixa(idCaixa) {
    const token = localStorage.getItem("site");
    const [doacoes, setDoacoes] = useState(null);

    useEffect(() => {
        if (idCaixa == null) return;
        fetch(`http://localhost:8080/doacao-instituicao/caixa/${idCaixa}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
            }
        })
        .then(response => response.json())
        .then(setDoacoes)
        .catch((err) => console.error("Erro ao buscar por caixa (instituição):", err));
    }, [idCaixa]);

    return { doacoes };
}