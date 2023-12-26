import axios from "axios";
import { BaseUrl } from "../BaseUrl";

const http = axios.create({
  baseURL: BaseUrl + "/sala",
  withCredentials: true,
});

export const useSala = () => {
  const criarSala = async (
    nome,
    senha,
    numeroRodadas,
    numeroJogadores,
    tempoRodada,
    temas,
    privado
  ) => {
    
    const reponse = await http.post("/criar", {
      nome: nome,
      senha: senha,
      numeroRodadas: numeroRodadas,
      numeroJogadores: numeroJogadores,
      tempoRodada: tempoRodada,
      temas: temas,
      privado: privado,
    });

    return reponse.data
  };

  const entrarSalaPrivada = async (senha) => {

    const response = await http.post("/entrar-privada", { senha: senha  })

    return response.data
  }

  const buscarSala = async (nome) => {

    const response = await http.post("/buscar-sala?nome=" + nome)

    return response.data
  }

  const entrarSalaPublica = async (salaId) => {

    const response = await http.post("/entrar-publica", { salaId: salaId })

    return response.data
  }

  const sairSala = async (id) => {

    const response = await http.post("/sair", { id: id })


    return response.data
  }

  const verificarUsuarioHost = async () => {

    const response = await http.get("/verificar-host")

    return response.data
  }

  return { criarSala, entrarSalaPrivada, entrarSalaPublica, buscarSala, sairSala, verificarUsuarioHost }
};
