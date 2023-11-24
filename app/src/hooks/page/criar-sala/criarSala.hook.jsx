
import { useState } from "react"
import { TEMAS } from "../../../consts/temas"
import "./criarSala.style.css"


export const CriarSala = () => {

    const [userData, setUserData] = useState({
        nome: "",
        senha: "",
        numeroRodadas: 0,
        numeroJogadores: 0,
        tempoRodada: 0,
        temas: [],
        privado: false
    })

    const handlerValue = (event) => {
        const { value, name } = event.target;

        if (name == "tema") {

            if (userData.temas.find((tema) => tema == value)) {
                userData.temas = userData.temas.filter((tema) => tema != value)
            } else {
                userData.temas.push(value)
            }

            setUserData({...userData})

        } else if (name == "privado") {

            setUserData({ ...userData, privado : !userData.privado })
        
        } else {
            setUserData({ ...userData, [name]: value });
        }

        console.log(userData)
    };


    return (
        <div className="CriarSala-section">


            <label className="label-checkbox" >
                Privado
                <input name="privado" type="checkbox" onChange={handlerValue} />
                <span className="checkmark"></span>  
            </label>

            <div className="CriarSala-container efeito-vidro">

                { !userData.privado ? ( 
                    <div className="CriarSala-input">
                        <label>Nome da sala</label>
                        <input name="nome" value={userData.nome} onChange={handlerValue}/>
                    </div>
                ): null}
                <div className="CriarSala-input">
                    <label>Senha</label>
                    <input name="senha" value={userData.senha} onChange={handlerValue} />
                </div>
                <div className="CriarSala-input">
                    <label>Quantidade de jogadores:  <span>{userData.numeroJogadores}</span> </label>
                    <input name="numeroJogadores" type="range" min="0" max="10" value={userData.numeroJogadores} onChange={handlerValue} />
                </div>
                <div className="CriarSala-input">
                    <label>Quantidade de rodadas:  <span>{userData.numeroRodadas}</span> </label>
                    <input name="numeroRodadas" type="range" min="0" max="15" value={userData.numeroRodadas} onChange={handlerValue} />
                </div>
                <div className="CriarSala-input">
                    <label>tempo por rodadas:  <span>{userData.tempoRodada}s</span> </label>
                    <input name="tempoRodada" type="range" min="10" max="30" value={userData.tempoRodada} onChange={handlerValue} />
                </div>
                <div className="CriarSala-input">
                    
                    <p> Temas </p>
                    {TEMAS.map((tema, key) => ( 

                        <label className="label-checkbox" key={key}>
                            {tema.replaceAll("_", " ")}
                            <input name="tema" type="checkbox" onChange={handlerValue} value={tema}/>
                            <span className="checkmark"></span>  
                        </label>
                    ))}

                </div>
                <div className="CriarSala-input">
                    <button>
                        Criar
                    </button>
                </div>

            </div>


        </div>
    )

}