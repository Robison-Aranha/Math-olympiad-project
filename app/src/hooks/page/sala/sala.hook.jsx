import './sala.style.css';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { DOMAIN_SOCK } from '../../../consts/sock';
import {
  userGlobalState,
  useGlobalLoading,
  useGlobalModal,
} from '../../../globalState/globalSate';
import { useVerifySession } from '../../../api/verifySession';
import { RESPOSTAS } from '../../../consts/respotas';
import { SUBSCRIBES } from '../../../consts/subscribes';
import { useSala } from '../../../api/api';

export const Sala = () => {
  const [loading, setLoading] = useGlobalLoading();
  const [, setGlobalModal] = useGlobalModal();

  const [useStomp, setUseStomp] = useState();
  const [useSock, setUseSock] = useState();

  const [useIsSocket, setUseIsSocket] = useState();
  const [subscribesToConnect, setSubscribesToConnect] = useState([
    ...SUBSCRIBES,
  ]);
  const [countSubscribes, setCountSubscribes] = useState(0);

  const [userGlobal, setUserGlobal] = userGlobalState();

  const [userIsHost, setUserIshost] = useState();
  const [gameIsEnd, setGameIsEnd] = useState();

  const [chat, setChat] = useState([]);
  const [perfils, setPerfils] = useState([]);
  const [pergunta, setPergunta] = useState(false);
  const [tempoPergunta, setTempoPergunta] = useState(0);
  const [votacaoPergunta, setVotacaoPergunta] = useState([]);
  const [respostaAvancar, setRespostaAvancar] = useState(0);
  const [contagemIniciar, setContagemIniciar] = useState();

  const [jaVotouAvancar, setJaVotouAvancar] = useState(false);

  const { verificarUsuarioHost } = useSala();
  const { verifySessionUser } = useVerifySession();

  const [userData, setUserData] = useState({
    mensagem: '',
    resposta: '',
  });

  const navigate = useNavigate();
  const location = useLocation();

  const handlerValue = (event) => {
    const { value, name } = event.target;
    setUserData({ ...userData, [name]: value });
  };

  useEffect(() => {
    setUseSock(new SockJS(DOMAIN_SOCK));
    setUseIsSocket(1);
  }, []);

  useEffect(() => {
    if (useIsSocket == 1) {
      setUseStomp(over(useSock));

      setUseIsSocket(2);
    } else if (useIsSocket == 2) {
      useStomp.debug = null;
      useStomp.connect(
        {},
        () => setUseIsSocket(3),
        () => disconnetSala(true)
      );
    }
  }, [useIsSocket]);

  useEffect(() => {
    if (useIsSocket == 3 && subscribesToConnect.length > 0) {
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
      if (location.state == 'entrar' && !userGlobal.exit) {
        const userInfo = {
          ...userGlobal,
          exit: true,
        };

        setUserGlobal({ ...userInfo });

        localStorage.setItem('user', JSON.stringify({ ...userInfo }));
      } else {
        disconnetSala(true);
      }

      useStomp.send(
        '/sala/join',
        {},
        JSON.stringify({ nome: userGlobal.nome })
      );

      setLoading(false);
    }
  }, [countSubscribes]);

  const doSubscribe = (subscribe) => {
    if (subscribe != 'principal') {
      let fn = subscribe;
      fn = 'on' + fn.charAt(0).toUpperCase() + fn.slice(1);
      fn = eval(fn);

      useStomp.subscribe(
        '/' + subscribe + '/' + userGlobal.sala.senha,
        (payload) => fn(payload),
        { id: subscribe }
      );
    }

    setCountSubscribes((c) => c + 1);
  };

  const verificarUsuarioHostService = async () => {
    try {
      const response = await verificarUsuarioHost();

      setUserIshost(response.usuarioEOHost);
    } catch (error) {
      verifySessionUser(error);
    }
  };

  const onRespostaAvancar = () => {
    setRespostaAvancar((r) => r + 1);
  };

  const onContagemIniciar = (payload) => {

    let payloadData = JSON.parse(payload.body);

    setContagemIniciar(payloadData);
  };

  const onNotification = (payload) => {
    let payloadData = JSON.parse(payload.body);
  

    setGlobalModal((m) => [...m, { message: payloadData.content }]);
  };

  const onTempoPergunta = (payload) => {
    let payloadData = JSON.parse(payload.body);

    setTempoPergunta(payloadData);
  };

  const onPergunta = (payload) => {
    let payloadData = JSON.parse(payload.body);

    setPergunta(payloadData);
  };

  const comecarRodada = () => {
    useStomp.send(
      '/sala/rodadas',
      {},
      JSON.stringify({ nome: userGlobal.nome })
    );
  };

  const onPerfil = (payload) => {
    let payloadData = JSON.parse(payload.body);

    setPerfils(() => [...payloadData]);

    verificarUsuarioHostService();
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

      setUserData({ ...userData, mensagem: '' });

      useStomp.send('/sala/message', {}, JSON.stringify(chatMessage));
    }
  };

  const scrollDownChat = () => {
    var element = document.getElementById('chat');
    element.scrollTop = element.scrollHeight;
  };

  const disconnetSala = (error) => {
    setLoading(false);

    const userInfo = {
      ...userGlobal,
      sala: null,
    };

    setUserGlobal({ ...userInfo });

    localStorage.setItem('user', JSON.stringify({ ...userInfo }));

    useStomp.disconnect();

    navigate('/criar-sala', { state: error ? 'error' : 'exit' });
  };

  const votarAvancar = () => {
    useStomp.send('/sala/rodada', {}, JSON.stringify({ nome: userGlobal.nome }));
    setJaVotouAvancar(true);
  };

  const returnSalaState = () => {
    if (!loading) {
      if (gameIsEnd) {
        return (
          <div className="Sala-placar">
            {perfils.slice(0, 2).map((perfil, key) => (
              <div className="Sala-placar-colocacao" key={key}>
                <div className="Sala-placar-content">
                  <p> Pontos: {perfil.pontos} </p>

                  <div className="Sala-placar-content-foto">
                    <img src={perfil.imagemPerfil} />
                  </div>

                  <p> {perfil.nome} </p>
                </div>
              </div>
            ))}
          </div>
        );
      } else {
        if (pergunta) {
          return (
            <div className="Sala-pergunta">
              <div className="Sala-pergunta-questao">
                <a href={pergunta.link}>Link questao</a>
                <img src={pergunta.questao} />
              </div>

              <div className="Sala-pergunta-respostas">
                {RESPOSTAS.map((resposta, key) => (
                  <div className="Sala-pergunta-resposta" key={key}>
                    {resposta}
                  </div>
                ))}
              </div>
            </div>
          );
        } else if (perfils.length > 0) {
          return (
            <div className="Sala-inicio">
              <div className="Sala-inicio-content">
                {respostaAvancar < perfils.length ? (
                  <>
                    <div className="Sala-inicio-text">
                      <p>
                        {respostaAvancar} / {perfils.length}
                      </p>

                      <p> Votação iniciar... </p>
                    </div>

                    <button disabled={jaVotouAvancar} onClick={votarAvancar}>
                      Votar
                    </button>
                  </>
                ) : (
                  <>
                    <p> O jogo vai começar em... </p>
                    <p> {contagemIniciar} </p>
                  </>
                )}
              </div>
            </div>
          );
        }
      }
    }
  };

  return (
    <div className="Sala-section">
      <div className="Sala-container efeito-vidro">
        <div className="Sala-header">
          <button onClick={() => disconnetSala(false)}>voltar</button>

          <div className="Sala-header-tempo-rodada">
            <p> {tempoPergunta} </p>
          </div>

          <div className="Sala-header-numero-jogadores">
            {perfils.length} / {userGlobal.sala.numeroJogadores}
          </div>
        </div>
        <div className="Sala-content">
          <div className="Sala-perfils">
            {perfils.length > 0
              ? perfils.map((perfil, key) => (
                  <div className="Sala-perfils-perfil" key={key}>
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
          </div>
          <div className="Sala-state">{returnSalaState()}</div>
          <div className="Sala-chat">
            <div className="Sala-chat-content" id="chat">
              {chat.length > 0
                ? chat.map((mensagem, key) => (
                    <div
                      className="Sala-chat-mensagem"
                      style={{
                        textDecoration:
                          mensagem.nome == ('Sala' ? 'underline' : 'none'),
                        backgroundColor:
                          mensagem.nome == userGlobal.nome
                            ? 'var(--main-quaternary-color)'
                            : '',
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
              <button onClick={sendMessage}>Enviar</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
