import { useEffect, useState } from "react";
import { useSala } from "../../../api/api";
import { useVerifySession } from "../../../api/verifySession";
import { EntrarSalaModal } from "./entrar-sala-modal/EntrarSalaModal.hook";
import "./EntrarSala.style.css";

export const EntrarSala = () => {
  const [salaPesquisa, setSalaPesquisa] = useState([])
  const [salaSelecionada, setSalaSelecionada] = useState()

  const { buscarSala } = useSala();
  const { verifySessionUser } = useVerifySession();

  const [userData, setUserData] = useState({
    nome: "",
    senha: ""
  });

  useEffect(() => {

    buscarSalaService()

  }, [userData.nome])


  const buscarSalaService = async () => {

    try {

      const response = await buscarSala(userData.nome)

      setSalaPesquisa([...response])

    } catch (error) {
      verifySessionUser(error)
    }

  }

  const handlerValue = (event) => {
    const { value, name } = event.target;
    setUserData({ ...userData, [name]: value });
  };

  return (
    <>
      <EntrarSalaModal sala={salaSelecionada} onClose={() => setSalaSelecionada(null)}/>
      <div className="EntrarSala-section">
        <div className="EntrarSala-container efeito-vidro">
          <div className="EntrarSala-input">
            <p> Sala: </p>
            <input name="nome" onChange={handlerValue}/>
          </div>
          <div className="EntrarSala-content">
            { salaPesquisa.map((sala, key) => (

              <div className="EntrarSala-sala-pesquisa" key={key} onClick={() => setSalaSelecionada(sala)}>

                <div className="EntraSala-sala-nome">
                  <p> {sala.nome} </p>
                </div>
                <div className="EntrarSala-sala-jogadores">

                  <p> {sala.numeroAtualDeJogadores} / {sala.numeroTotalDeJogadores} </p>

                </div>
              </div>
            )) }
          </div>
        </div>
      </div>
    </>
  );
};
