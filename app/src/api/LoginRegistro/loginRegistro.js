import axios from "axios";
import { BaseUrl } from "../BaseUrl";

const http = axios.create({
  baseURL: BaseUrl,
  withCredentials: true,
});

export const useLoginRegister = () => {

  const registro = async (nome, senha) => {
    const response = await http.post("/register", {
      nome: nome,
      senha: senha,
    });

    return response.data;
  };

  const login = async (nome, senha) => {

    const authorization = btoa(nome + ":" + senha);

    const response = await http.post(
    "/login",
    {},
    { headers: { Authorization: "Basic " + authorization } }
    );
    return response.data;
  };

  const logout = async () => {

    await http.get("/logout")

  }

  return {login, logout, registro};
};