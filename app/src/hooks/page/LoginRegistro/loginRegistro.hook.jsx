import "./loginRegistro.style.css";
import { useState, useEffect } from "react";
import { useLoginRegister } from "../../../api/api";
import { userGlobalState } from "../../../globalState/globalSate";
import { useGlobalLoading } from "../../../globalState/globalSate";
import { useGlobalModal } from "../../../globalState/globalSate";
import { useNavigate, useLocation } from "react-router-dom";

export const LoginRegistro = () => {
  const [state, setState] = useState(true);

  const { login, registro } = useLoginRegister();

  const [userGlobal, setUserGlobal] = userGlobalState();
  const [, setLoading] = useGlobalLoading();
  const [globalModal, setGlobalModal] = useGlobalModal();

  const navigate = useNavigate();
  const location = useLocation();

  const [userData, setUserData] = useState({
    nome: "",
    senha: "",
    confirmeSenha: "",
  });

  const handlerValue = (event) => {
    const { value, name } = event.target;
    setUserData({ ...userData, [name]: value });
  };

  useEffect(() => {
    if (location.state == "expired") {
      setGlobalModal([...globalModal, { message: "Sua sessão expirou!!" }]);
    } else if (location.state == "logout") {
      setGlobalModal([...globalModal, { message: "Sessão encerrada!!" }]);
    }

    localStorage.removeItem("user");
    setUserGlobal({});
  }, []);

  const handleCommit = () => {
    const value = verifyCredentials();

    if (value) {
      if (value == 2) {
        setGlobalModal([...globalModal, { message: "Senhas não são iguais!" }]);
      } else {
        if (state) {
          loginService();
        } else {
          registerService();
        }
      }
    } else {
      setGlobalModal([...globalModal, { message: "Credenciais Invalidas!" }]);
    }
  };

  const loginService = async () => {
    setLoading(true);
    try {
      const response = await login(userData.nome, userData.senha);

      const userInfo = {
        ...userGlobal,
        loged: true,
        nome: response.nome,
        imagemPerfil: response.imagemPerfil,
        token: response.token,
        id: response.id,
      };

      setUserGlobal({ ...userInfo });

      localStorage.setItem("user", JSON.stringify({ ...userInfo }));

      navigate("/criar-sala");
    } catch (response) {
      console.log(response);
      setGlobalModal([...globalModal, { message: "Login falhou!" }]);
    }
    setLoading(false);
  };

  const registerService = async () => {
    setLoading(true);
    try {
      await registro(userData.nome, userData.senha);

      setState(true);
      setGlobalModal([
        ...globalModal,
        { message: "Conta criada com sucesso!" },
      ]);
    } catch (error) {
      if (error.response.data.fields) {
        const decodedErros = JSON.parse(error.response.data.fields);

        decodedErros.forEach((error) => globalModal.push({ message: error }));
      } else {
        if (error.response.data.status == 302) {
          globalModal.push({ message: error.response.data.message });
        }
      }

      console.log(error);

      setGlobalModal([...globalModal]);
    }

    setLoading(false);
  };

  const verifyCredentials = () => {
    if (!state) {
      if (userData.nome == "" || userData.nome == null) {
        return false;
      } else if (
        userData.confirmeSenha != "" &&
        userData.confirmeSenha != null
      ) {
        if (userData.senha != userData.confirmeSenha) {
          return 2;
        }
      } else {
        return false;
      }
    }

    if (userData.nome == "" || userData.nome == null) {
      return false;
    } else if (userData.senha == "" || userData.senha == null) {
      return false;
    }

    return true;
  };

  return (
    <>
      <div className="LoginRegistro-section">
        <div className="LoginRegistro-container">
          <div className="LoginRegistro-content">
            <div className="LoginRegistro-input">
              <label> Id Usuario </label>
              <input
                name="nome"
                type="text"
                value={userData.nome}
                placeholder="Nome do usuario..."
                onChange={handlerValue}
              />
            </div>
            <div className="LoginRegistro-input">
              <label> Senha </label>
              <input
                name="senha"
                type="password"
                value={userData.senha}
                placeholder="Senha do usuario..."
                onChange={handlerValue}
              />
            </div>
            {!state ? (
              <div className="LoginRegistro-input">
                <label> Confirmar Senha </label>
                <input
                  name="confirmeSenha"
                  type="password"
                  value={userData.confirmeSenha}
                  placeholder="Confirme a senha..."
                  onChange={handlerValue}
                />
              </div>
            ) : null}
          </div>
          <div className="LoginRegistro-button">
            <p>
              {" "}
              {state ? "Não Possui uma conta?" : "Ja Possui uma conta?"}{" "}
              <a onClick={() => setState(!state)}>
                {" "}
                {state ? "Registrar" : "Logar"}{" "}
              </a>{" "}
            </p>
            <button className="button-1" onClick={handleCommit}>
              {state ? "Logar" : "Registar"}
            </button>
          </div>
        </div>
      </div>
    </>
  );
};
