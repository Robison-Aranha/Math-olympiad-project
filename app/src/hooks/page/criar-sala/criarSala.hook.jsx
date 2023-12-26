import { useEffect, useState } from "react";
import { TEMAS } from "../../../consts/temas";
import { useSala } from "../../../api/api";
import { useVerifySession } from "../../../api/verifySession";
import {
  useGlobalModal,
  userGlobalState,
  useGlobalLoading,
} from "../../../globalState/globalSate";
import { useNavigate, useLocation } from "react-router-dom";
import "./criarSala.style.css";

export const CriarSala = () => {
  const [userData, setUserData] = useState({
    nome: "",
    senha: "",
    numeroRodadas: 5,
    numeroJogadores: 3,
    tempoRodada: 10,
    temas: [],
    privado: false,
  });
  const [globalModal, setGlobalModal] = useGlobalModal();
  const [userGlobal, setUserGlobal] = userGlobalState();
  const [, setLoading] = useGlobalLoading();

  const { criarSala } = useSala();
  const { verifySessionUser } = useVerifySession();
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {

    console.log(location)
    console.log(userGlobal)

    if (location.state == "error" && userGlobal.exit) {
      setGlobalModal([...globalModal, { message: "Erro! Disconectado..." }]);
      
      const userInfo = {
        ...userGlobal,
        exit: false,
      };
  
      setUserGlobal({ ...userInfo });
  
      localStorage.setItem("user", JSON.stringify({ ...userInfo }));
    }
  }, []);

  const criarSalaService = async () => {
    try {
      if (!verificarInputs()) {
        setGlobalModal([...globalModal, { message: "Credenciais Invalidas!" }]);
      } else {
        const response = await criarSala(
          userData.nome,
          userData.senha,
          userData.numeroRodadas,
          userData.numeroJogadores,
          userData.tempoRodada,
          userData.temas,
          userData.privado
        );

        const userInfo = {
          ...userGlobal,
          sala: {
            senha: response.senha,
            numeroJogadores: response.numeroJogadores,
          },
        };

        setUserGlobal({ ...userInfo });

        localStorage.setItem("user", JSON.stringify({ ...userInfo }));

        setLoading(true);

        navigate("/sala", { state: "entrar" });
      }
    } catch (error) {
      verifySessionUser(error);
    }
  };

  const verificarInputs = () => {
    if (userData.nome == "") {
      return false;
    }

    if (userData.senha == "" && userData.privado) {
      return false;
    }

    if (userData.temas.length == 0) {
      return false;
    }

    return true;
  };

  const handlerValue = (event) => {
    const { value, name } = event.target;

    if (name == "tema") {
      if (userData.temas.find((tema) => tema == value)) {
        userData.temas = userData.temas.filter((tema) => tema != value);
      } else {
        userData.temas.push(value);
      }

      setUserData({ ...userData });
    } else if (name == "privado") {
      if (!userData.privado) {
        userData.senha = "";
      }

      setUserData({ ...userData, privado: !userData.privado });
    } else {
      setUserData({ ...userData, [name]: value });
    }

    console.log(userData);
  };

  return (
    <div className="CriarSala-section">
      <label className="label-checkbox">
        Privado
        <input name="privado" type="checkbox" onChange={handlerValue} />
        <span className="checkmark"></span>
      </label>

      <div className="CriarSala-container efeito-vidro">
        <div className="CriarSala-input">
          <label>Nome da sala</label>
          <input name="nome" value={userData.nome} onChange={handlerValue} />
        </div>

        {userData.privado ? (
          <div className="CriarSala-input">
            <label>Senha</label>
            <input
              name="senha"
              value={userData.senha}
              onChange={handlerValue}
            />
          </div>
        ) : null}
        <div className="CriarSala-input">
          <label>
            Quantidade de jogadores: <span>{userData.numeroJogadores}</span>{" "}
          </label>
          <input
            name="numeroJogadores"
            type="range"
            min="3"
            max="10"
            value={userData.numeroJogadores}
            onChange={handlerValue}
          />
        </div>
        <div className="CriarSala-input">
          <label>
            Quantidade de rodadas: <span>{userData.numeroRodadas}</span>{" "}
          </label>
          <input
            name="numeroRodadas"
            type="range"
            min="5"
            max="15"
            value={userData.numeroRodadas}
            onChange={handlerValue}
          />
        </div>
        <div className="CriarSala-input">
          <label>
            tempo por rodada: <span>{userData.tempoRodada}s</span>{" "}
          </label>
          <input
            name="tempoRodada"
            type="range"
            min="10"
            max="30"
            value={userData.tempoRodada}
            onChange={handlerValue}
          />
        </div>
        <div className="CriarSala-input">
          <p> Temas </p>
          {TEMAS.map((tema, key) => (
            <label className="label-checkbox" key={key}>
              {tema.replaceAll("_", " ")}
              <input
                name="tema"
                type="checkbox"
                onChange={handlerValue}
                value={tema}
              />
              <span className="checkmark"></span>
            </label>
          ))}
        </div>
        <div className="CriarSala-input">
          <button onClick={criarSalaService}>Criar</button>
        </div>
      </div>
    </div>
  );
};
