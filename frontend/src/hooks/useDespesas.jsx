import { useEffect, useState } from "react";

export function useDespesas() {
  const token = localStorage.getItem("site");
  const [despesas, setDespesas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState(null);

  useEffect(() => {
    fetch("http://localhost:8080/despesas/listar", {
      method: "GET",
      headers: {
        Authorization: "Bearer " + token,
      },
    })
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text || "Erro ao buscar despesas");
        }
        return response.json();
      })
      .then((res) => {
        if (Array.isArray(res)) {
          setDespesas(res);
        } else {
          setDespesas([]);
        }
      })
      .catch((error) => {
        console.error("Erro ao buscar despesas:", error);
        setErro(error.message);
        setDespesas([]);
      })
      .finally(() => setLoading(false));
  }, []);

  return { despesas, loading, erro };
}

export function useDespesa(id) {
  const token = localStorage.getItem("site");
  const [despesa, setDespesa] = useState(null);
  const [erro, setErro] = useState(null);

  useEffect(() => {
    fetch("http://localhost:8080/despesas/" + id, {
      method: "GET",
      headers: {
        Authorization: "Bearer " + token,
      },
    })
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text || "Erro ao buscar despesa");
        }
        return response.json();
      })
      .then((res) => setDespesa(res))
      .catch((error) => {
        console.error("Erro ao buscar despesa:", error);
        setErro(error.message);
        setDespesa(null);
      });
  }, [id]);

  return { despesa, erro };
}


export const formatarData = (dataCompleta) => {
  const data = new Date(dataCompleta);
  const ano = data.getFullYear();
  const mes = String(data.getMonth() + 1).padStart(2, '0');
  const dia = String(data.getDate()).padStart(2, '0');
   return `${dia}/${mes}/${ano}`;
};