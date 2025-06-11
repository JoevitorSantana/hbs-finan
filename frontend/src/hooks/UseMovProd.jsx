import { useState, useEffect } from "react";

// Função auxiliar para formatar data, exemplo simples:
export function formatarData(dataString) {
  if (!dataString) return "";
  const data = new Date(dataString);
  return data.toLocaleDateString("pt-BR");
}



export function useDoacoesL() {
  const [doacoes, setDoacoes] = useState([]);

  useEffect(() => {
    const fetchDoacoes = async () => {
      try {
        const token = localStorage.getItem("site");
        const response = await fetch("http://localhost:8080/api/doacoes", {
          headers: {
            Authorization: "Bearer " + token,
          },
        });
        if (response.ok) {
          const data = await response.json();
          setDoacoes(data);
        } else {
          console.error("Erro ao carregar doações");
        }
      } catch (error) {
        console.error("Erro na requisição de doações:", error);
      }
    };

    fetchDoacoes();
  }, []);

  return { doacoes };
}
