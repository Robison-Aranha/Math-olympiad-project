import { useState } from "react";
import { useSala } from "../../../../api/api";
import { useVerifySession } from "../../../../api/verifySession";
import { useNavigate } from "react-router-dom";
import { userGlobalState, useGlobalModal } from "../../../../globalState/globalSate";
import "./EntrarSalaModal.style.css";

export const EntrarSalaModal = ({ sala, onClose }) => {

  const [userGlobal, setUserGlobal] = userGlobalState()
  const [globalModal, setGlobalModal] = useGlobalModal()

  const [userData, setUserData] = useState({
    senha: "",
  });

  const navigate = useNavigate();
  const { verifySessionUser } = useVerifySession();
  const { entrarSalaPrivada, entrarSalaPublica } = useSala();

  const closeModal = (event) => {
    const element = document.getElementById("modal-container");

    if (!element.contains(event.target)) {
      onClose();
    }
  };

  const handlerValue = (event) => {
    const { value, name } = event.target;
    setUserData({ ...userData, [name]: value });
  };

  const entrarSalaPrivadaService = async () => {
    try {
      const response = await entrarSalaPrivada(userData.senha);

      const userInfo = {
        ...userGlobal,
        sala: {
          webSocketKey: response.webSocketKey,
          numeroJogadores: response.numeroJogadores,
        },
      };

      setUserGlobal({ ...userInfo });

      localStorage.setItem("user", JSON.stringify({ ...userInfo }));

      navigate("/sala", { state: "entrar" });
    } catch (error) {
      verifySessionUser(error);

      if (error.response.status == 404) {
        setGlobalModal([...globalModal, { message: "Senha Invalida!" }])
      }

    }
  };

  const entrarSalaPublicaService = async () => {
    try {
      const response = await entrarSalaPublica(sala.id);

      const userInfo = {
        ...userGlobal,
        sala: {
          webSocketKey: response.webSocketKey,
          numeroJogadores: response.numeroJogadores,
        },
      };

      setUserGlobal({ ...userInfo });

      localStorage.setItem("user", JSON.stringify({ ...userInfo }));

      navigate("/sala", { state: "entrar" });
    } catch (error) {
      console.log(error)
      verifySessionUser(error);
    }
  };

  const returnState = () => {
    if (sala?.salaEPrivada) {
      return (
        <div className="EntrarSalaModal-privada-content">
          <p> Senha: </p>
          <input
            name="senha"
            placeholder="coloque a senha.."
            value={userData.senha}
            onChange={handlerValue}
          />
          <div className="EntrarSalaModal-buttons">
            <button className="button-1" onClick={entrarSalaPrivadaService}>Entrar</button>
          </div>
        </div>
      );
    } else {
      return (
        <div className="EntrarSalaModal-publica-content">
          <p>
            {" "}
            Deseja realmente entrar em: <span> {sala?.nome} </span> ?{" "}
          </p>

          <div className="EntrarSalaModal-buttons">
            <button onClick={entrarSalaPublicaService}>Entrar</button>
          </div>
        </div>
      );
    }
  };

  return (
    <div
      className="EntrarSalaModal-section"
      style={{ display: sala ? "flex" : "none" }}
      onClick={closeModal}
    >
      <div className="EntrarSalaModal-container" id="modal-container">
        {returnState()}
      </div>
    </div>
  );
};
