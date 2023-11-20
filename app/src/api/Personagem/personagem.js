import axios from "axios";
import { BaseUrl } from "../BaseUrl";

const http = axios.create({
  baseURL: BaseUrl + "/personagem",
  withCredentials: true,
});



export const usePersonagem = () => {

    const criarPersonagem = async (classe, nome) => {

      const response = await http.post("/criar", { classe: classe, nome: nome });

      return response.data;
    }

    return { criarPersonagem }
}