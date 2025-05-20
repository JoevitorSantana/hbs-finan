import { useEffect, useState } from "react";
import Funcionarios from "views/funcionarios";

export function useFuncionarios() {
    
    const token = localStorage.getItem("site");

    const [funcionarios, setFuncionarios] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/funcionarios/listar', {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setFuncionarios(res))
    }, []);
    
    return { Funcionarios };
};

export function useFuncionario(id)
{
    const token = localStorage.getItem("site");

    const [funcionario, setFuncionario] = useState(null);
    
    useEffect(() => {
        fetch('http://localhost:8080/funcionarios/' + id, {
            method: 'GET',
            headers: {
                'Authorization' : 'Bearer ' + token, 
            }
        })
        .then((response) => response.json())
        .then((res) => setFuncionario(res))
    }, []);

    return { funcionario };
}