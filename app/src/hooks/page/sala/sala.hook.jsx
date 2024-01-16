import "./sala.style.css";
import { over } from "stompjs";
import SockJS from "sockjs-client";
import md5Hex from "md5-hex";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import {
  userGlobalState,
  useGlobalLoading,
} from "../../../globalState/globalSate";
import {
  DOMAIN_SOCK,
  SUBSCRIBES,
  RESPOSTAS,
  STOMP_TIPOS,
} from "../../../consts/consts";

export const Sala = () => {
  const [loading, setLoading] = useGlobalLoading();

  const [useStompPublic, setUseStompPublic] = useState();
  const [useStompPrivate, setUseStompPrivate] = useState();
  const [useSockPublic, setUseSockPublic] = useState();
  const [useSockPrivate, setUseSockPrivate] = useState();

  const [useIsSocket, setUseIsSocket] = useState();
  const [subscribesToConnect, setSubscribesToConnect] = useState([
    ...SUBSCRIBES,
  ]);
  const [countSubscribes, setCountSubscribes] = useState(0);

  const [userGlobal, setUserGlobal] = userGlobalState();

  const [fimDeJogo, setFimDeJogo] = useState();
  const [jogoIniciou, setJogoIniciou] = useState();

  const [chat, setChat] = useState([]);
  const [perfils, setPerfils] = useState([]);
  const [perfilsUpdate, setPerfilsUpdate] = useState();
  const [pergunta, setPergunta] = useState(false);
  const [jaVotouComecar, setJaVotouComecar] = useState(false);
  const [votacaoPergunta, setVotacaoPergunta] = useState([]);
  const [contagemIniciar, setContagemIniciar] = useState(false);
  const [respostaAvancar, setRespostaAvancar] = useState(0);
  const [respostaPergunta, setRespostaPergunta] = useState("");
  const [respostaUsuario, setRespostaUsuario] = useState("");

  const [userData, setUserData] = useState({
    mensagem: "",
    resposta: "",
  });

  const navigate = useNavigate();
  const location = useLocation();

  const handlerValue = (event) => {
    const { value, name } = event.target;
    setUserData({ ...userData, [name]: value });
  };

  useEffect(() => {
    setUseSockPublic(new SockJS(DOMAIN_SOCK));
    setUseSockPrivate(new SockJS(DOMAIN_SOCK));

    setUseIsSocket(1);
  }, []);

  useEffect(() => {
    if (useIsSocket == 1) {
      setUseStompPublic(over(useSockPublic));
      setUseStompPrivate(over(useSockPrivate));

      setUseIsSocket(2);
    } else if (useIsSocket == 2) {
      useStompPublic.debug = null;
      useStompPublic.connect(
        {},
        () => setUseIsSocket((r) => r + 1),
        () => disconnetSala(true)
      );

      useStompPrivate.debug = null;
      useStompPrivate.connect(
        {},
        () => setUseIsSocket((r) => r + 1),
        () => disconnetSala(true)
      );
    }
  }, [useIsSocket]);

  useEffect(() => {
    if (useIsSocket == 4 && subscribesToConnect.length > 0) {
      const subscribe = subscribesToConnect[0];

      doSubscribe(subscribe);

      subscribesToConnect.splice(0, 1);
      setSubscribesToConnect([...subscribesToConnect]);
    }
  }, [subscribesToConnect, useIsSocket]);

  useEffect(() => {
    scrollDownChat();
  }, [chat]);

  useEffect(() => {
    if (countSubscribes == SUBSCRIBES.length) {
      if (location.state == "entrar" && !userGlobal.exit) {
        const userInfo = {
          ...userGlobal,
          exit: true,
        };

        setUserGlobal({ ...userInfo });

        localStorage.setItem("user", JSON.stringify({ ...userInfo }));
      } else {
        disconnetSala(true);
      }

      useStompPublic.send(
        "/sala/join",
        {},
        JSON.stringify({ nome: userGlobal.nome })
      );

      setLoading(false);
    }
  }, [countSubscribes]);

  useEffect(() => {
    const perfilsChanged = document.getElementsByClassName("changed");

    if (perfilsChanged) {
      for (let perfil of perfilsChanged) {
        console.log(perfil);
        perfil.style.animation = "fadein 1.5s";
      }
    }
  }, [perfils]);

  useEffect(() => {
    const pergunta = document.getElementById("pergunta");
    const resposta = document.getElementById("resposta");

    if (pergunta) {
      pergunta.style.animation = "fadein 1s";
    }

    if (resposta) {
      resposta.style.animation = "fadein 1s";
    }
  }, [pergunta]);

  useEffect(() => {
    const respostaPerguntaUsuario = document.getElementById("resposta-usuario");

    if (respostaPerguntaUsuario) {
      respostaPerguntaUsuario.style.animation = "fadein 1s";
    }
  }, [respostaUsuario]);

  useEffect(() => {
    const fimDeJogo = document.getElementById("placar");

    if (fimDeJogo) {
      fimDeJogo.style.animation = "fadein 1s";
    }
  }, [fimDeJogo]);

  useEffect(() => {
    const textIniciar = document.getElementById("texto-votacao");

    if (textIniciar) {
      textIniciar.style.animation = "fadein 3s";
    }
  }, [contagemIniciar]);

  useEffect(() => {
    if (perfilsUpdate) {
      if (perfils.length == perfilsUpdate.length) {
        perfilsUpdate.forEach((perfilNovo) => {
          const perfilVelho = perfils.find((p) => p.nome == perfilNovo.nome);

          if (perfilVelho.pontos != perfilNovo.pontos) {
            perfilNovo.changed = true;
          } else {
            perfilNovo.changed = false;
          }
        });
      } else if (perfilsUpdate.length > perfils.length) {
        perfilsUpdate.forEach((perfil1) => {
          const achouPerfil = perfils.find(
            (perfil2) => perfil1.nome == perfil2.nome
          );

          if (!achouPerfil) {
            perfil1.changed = true;
          }
        });
      } else {
        perfilsUpdate.forEach((perfil) => (perfil.changed = true));
      }

      setTimeout(() => setPerfils([...perfilsUpdate]), 100);
    }
  }, [perfilsUpdate]);

  const doSubscribe = (subscribe) => {
    if (subscribe != "principal") {
      let fn = subscribe;
      fn = "on" + fn.charAt(0).toUpperCase() + fn.slice(1);
      fn = eval(fn);

      STOMP_TIPOS.forEach((stompTipo) => {
        let stomp;
        eval("stomp = useStomp" + stompTipo);

        stomp.subscribe(
          "/" +
            subscribe +
            "/" +
            (stompTipo == "Public"
              ? userGlobal.sala.webSocketKey
              : md5Hex(userGlobal.sala.webSocketKey + userGlobal.nome)),
          (payload) => fn(payload),
          { id: subscribe }
        );
      });
    }

    setCountSubscribes((c) => c + 1);
  };

  const onRespostaAvancar = (payload) => {
    let payloadData = JSON.parse(payload.body);

    setRespostaAvancar(payloadData);
  };

  const onRespostaPergunta = (payload) => {
    let payloadData = JSON.parse(payload.body);

    if (!respostaPergunta) {
      setRespostaPergunta(payloadData.resposta);
    }
  };

  const onTempoRespostaPergunta = (payload) => {
    let payloadData = JSON.parse(payload.body);

    const progressBar = document.querySelector(".progress-bar");
    progressBar.style.setProperty("--progress", payloadData);

    if (payloadData == 100) {
      setRespostaPergunta(null);
    }
  };

  const onContagemIniciar = (payload) => {
    let payloadData = JSON.parse(payload.body);

    const progressBar = document.querySelector(".progress-bar");
    progressBar.style.setProperty("--progress", payloadData);

    if (!contagemIniciar) {
      setContagemIniciar(true);
    }
  };

  const onFimDeJogo = (payload) => {
    let payloadData = JSON.parse(payload.body);

    setContagemIniciar(false)
    setJaVotouComecar(false)
    setJogoIniciou(false)
    setRespostaAvancar(0)
    setFimDeJogo(payloadData.fimDeJogo);
  };

  const onJogoIniciou = (payload) => {
    let payloadData = JSON.parse(payload.body);

    setFimDeJogo(false)
    setJogoIniciou(payloadData.jogoIniciou);
  };

  const onTempoPergunta = (payload) => {
    let payloadData = JSON.parse(payload.body);

    const progressBar = document.querySelector(".progress-bar");
    progressBar.style.setProperty("--progress", payloadData);

    if (payloadData == 100) {
      setPergunta(null);
    }
  };

  const onPergunta = (payload) => {
    let payloadData = JSON.parse(payload.body);

    const progressBar = document.querySelector(".progress-bar");
    progressBar.style.setProperty("--progress", 0);

    setPergunta(payloadData);
    setRespostaUsuario(null);
  };

  const onPerfil = (payload) => {
    let payloadData = JSON.parse(payload.body);

    setPerfilsUpdate([...payloadData]);
  };

  const onChat = (payload) => {
    let payloadData = JSON.parse(payload.body);

    setChat((r) => [...r, payloadData]);
  };

  const sendMessage = () => {
    if (userData.mensagem.length > 0) {
      let chatMessage = {
        content: userData.mensagem,
        nome: userGlobal.nome,
      };

      setUserData({ ...userData, mensagem: "" });

      useStompPublic.send("/sala/message", {}, JSON.stringify(chatMessage));
    }
  };

  const scrollDownChat = () => {
    var element = document.getElementById("chat");
    element.scrollTop = element.scrollHeight;
  };

  const disconnetSala = (error) => {
    setLoading(false);

    const userInfo = {
      ...userGlobal,
      sala: null,
      exit: true,
    };

    setUserGlobal({ ...userInfo });

    localStorage.setItem("user", JSON.stringify({ ...userInfo }));

    useStompPublic.disconnect();
    useStompPrivate.disconnect();

    navigate("/criar-sala", { state: error ? "error" : "exit" });
  };

  const votarAvancar = (state) => {

    useStompPublic.send(
      "/sala/" + (state ? "rodada" : "recomecar"),
      {},
      JSON.stringify({ nome: userGlobal.nome })
    );

    setJaVotouComecar(true);
  };

  const enviarRespostaUsuario = (resposta) => {
    if (!respostaUsuario) {
      const progressBar = document.querySelector(".progress-bar");
      const tempoPergunta = progressBar.style.getPropertyValue("--progress");

      setRespostaUsuario(resposta);

      useStompPublic.send(
        "/sala/resposta",
        {},
        JSON.stringify({
          nome: userGlobal.nome,
          resposta: resposta,
          contagem: tempoPergunta,
        })
      );
    }
  };

  const Votacao = ({ state }) => {

    return (
      <>
        {!contagemIniciar ? (
          <>
            <div className="Sala-votacao">
              <p>
                {respostaAvancar} / {perfils.length}
              </p>

              <p> { state ? "Votação iniciar..." : "Votação reiniciar..." } </p>
            </div>

            <button
              className="button-1"
              disabled={jaVotouComecar}
              style={{
                filter: jaVotouComecar ? "grayscale(70%)" : "",
                animation: jaVotouComecar ? "fadein 1s" : "",
              }}
              onClick={() => votarAvancar(state)}
            >
              Votar
            </button>
          </>
        ) : (
          <p id="texto-votacao"> { state ? "O jogo já vai começar..." : "Espere o jogo recomeçar..."} </p>
        )}
      </>
    );
  };

  const ReturnSalaState = () => {
    if (!loading) {
      if (fimDeJogo) {
        const placar = ["gold", "silver", "brown"];
        const valorYHorizontal = 1000;

        return (
          <div className="Sala-placar" id="placar">
            <div className="Sala-placar-colocacao">
              {perfils.slice(0, 3).map((perfil, key) => (
                <div
                  className="Sala-placar-content"
                  style={{
                    backgroundColor: placar[key],
                    marginBottom: valorYHorizontal / ((key + 1) * 10),
                  }}
                  key={key}
                >
                  <p> Pontos: {perfil.pontos} </p>

                  <div className="Sala-placar-content-foto">
                    <img src={perfil.imagemPerfil} />
                  </div>

                  <p> {perfil.nome} </p>
                </div>
              ))}
            </div>
            <div className="Sala-placar-recomecar">
              <Votacao state={false} />
            </div>
          </div>
        );
      } else if (jogoIniciou) {
        if (pergunta) {
          const corRespostas = ["red", "blue", "green", "yellow", "purple"];

          return (
            <div className="Sala-pergunta" id="pergunta">
              <div className="Sala-pergunta-questao">
                <img src={pergunta.questao} />
              </div>

              <div className="Sala-pergunta-respostas" id="resposta-usuario">
                {RESPOSTAS.map((resposta, key) => {
                  if (!respostaUsuario || resposta == respostaUsuario) {
                    return (
                      <button
                        className="button-2 Sala-pergunta-resposta"
                        onClick={() => enviarRespostaUsuario(resposta)}
                        disabled={respostaUsuario ? true : false}
                        style={{
                          backgroundColor: corRespostas[key],
                          filter: respostaUsuario != "" ? "grayscale(50%)" : "",
                        }}
                        key={key}
                      >
                        {resposta}
                      </button>
                    );
                  }
                })}
              </div>
            </div>
          );
        } else if (respostaPergunta) {
          return (
            <div className="Sala-resposta-pergunta" id="resposta">
              <div className="Sala-resposta-pergunta-content">
                <p> A resposta para a pergunta é... </p>

                <span> {respostaPergunta} </span>
              </div>
            </div>
          );
        }
      } else {
        return (
          <div className="Sala-inicio">
            <div className="Sala-inicio-content">
              <Votacao state={true} />
            </div>
          </div>
        );
      }
    }
  };

  const ReturnProfiles = () => {
    return (
      <>
        {perfils.length > 0
          ? perfils.map((perfil, key) => (
              <div
                className={
                  "Sala-perfils-perfil" + (perfil.changed ? " changed" : "")
                }
                key={key}
              >
                <div className="Sala-perfils-foto">
                  <img src={perfil.imagemPerfil} />
                </div>
                <div className="Sala-perfils-content">
                  <p> {perfil.nome} </p>
                  <p> Pontos: {perfil.pontos} </p>
                </div>
              </div>
            ))
          : null}
      </>
    );
  };

  return (
    <div className="Sala-section">
      <div className="Sala-container efeito-vidro">
        <div className="Sala-header">
          <button className="button-1" onClick={() => disconnetSala(false)}>
            voltar
          </button>

          <div className="Sala-header-numero-jogadores">
            {perfils.length} / {userGlobal.sala.numeroJogadores}
          </div>
        </div>
        <div className="Sala-content">
          <div className="Sala-perfils">
            <ReturnProfiles />
          </div>
          <div className="Sala-state">
            <div className="Sala-progress-bar">
              <div className="progress-bar"></div>
            </div>
            <div className="Sala-jogo">
              <ReturnSalaState />
            </div>
          </div>
          <div className="Sala-chat">
            <div className="Sala-chat-content" id="chat">
              {chat.length > 0
                ? chat.map((mensagem, key) => (
                    <div
                      className="Sala-chat-mensagem"
                      style={{
                        textDecoration:
                          mensagem.nome == ("Sala" ? "underline" : "none"),
                        backgroundColor:
                          mensagem.nome == userGlobal.nome
                            ? "var(--main-quaternary-color)"
                            : "",
                      }}
                      key={key}
                    >
                      {mensagem.nome}: {mensagem.content}
                    </div>
                  ))
                : null}
            </div>
            <div className="Sala-chat-input">
              <input
                name="mensagem"
                value={userData.mensagem}
                placeholder="comente aqui..."
                onChange={handlerValue}
              />
              <button className="button-1" onClick={sendMessage}>
                Enviar
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
