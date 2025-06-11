import { useState, useEffect } from "react";

export function useDoacoes() {
  const [doacoes, setDoacoes] = useState([]);

  useEffect(() => {
    const fetchDoacoes = async () => {
      try {
        const token = localStorage.getItem("site");
        const response = await fetch("http://localhost:8080/api/doacoesmaterial", {
          headers: {
            Authorization: "Bearer " + token,
          },
        });
        if (response.ok) {
          const data = await response.json();
          setDoacoes(data);
        } else {
          console.error("Erro ao carregar doações materiais");
        }
      } catch (error) {
        console.error("Erro na requisição de doações materiais:", error);
      }
    };

    fetchDoacoes();
  }, []);

  return { doacoes };
}

// Função auxiliar para formatar data
export function formatarData(dataString) {
  if (!dataString) return "";
  const data = new Date(dataString);
  return data.toLocaleDateString("pt-BR");
}

export function useDoacao(id) {
  const token = localStorage.getItem("site");
  const [doacao, setDoacao] = useState(null);

  useEffect(() => {
    if (!id) return; // evita requisição sem id

    fetch(`http://localhost:8080/api/doacoesmaterial/${id}`, {
      method: "GET",
      headers: {
        Authorization: "Bearer " + token,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Erro na resposta da API");
        }
        return response.json();
      })
      .then(setDoacao)
      .catch((err) => console.error("Erro ao buscar doação:", err));
  }, [id, token]);

  return { doacao };
}
