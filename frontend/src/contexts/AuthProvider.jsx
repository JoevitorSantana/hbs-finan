import React, { useContext, createContext, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const AuthContext = createContext();

const AuthProvider = ({ children }) => {

    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem("site") || "");
    const navigate = useNavigate();

    const loginAction = async (email, senha) => {
        try {
            const response  = await fetch("http://localhost:8080/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: email,
                    senha: senha
                }),
            });

            const res = await response.json();

            if (res.token) {
                let user = {
                    nome: res.nome,
                    ultimoNome: res.ultimoNome,
                    email: res.email,
                    role: res.role
                };
                setUser(user);
                setToken(res.token);
                localStorage.setItem("user", JSON.stringify(user));
                localStorage.setItem("site", res.token);
                navigate("/");
                return res;
            }
            return res;
            // throw new Error("Erro ao autenticar!");
        } catch (err) {
            console.error(err);
        }
    }

    const logOut = () => {
        setUser(null);
        setToken("");
        localStorage.removeItem("site");
        navigate("/login");
    }

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
    }, []);

    return (
        <AuthContext.Provider value={{ token, user, loginAction, logOut }}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;

export const useAuth = () => {
    return useContext(AuthContext);
};