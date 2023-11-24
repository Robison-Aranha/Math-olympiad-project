import "./sala.style.css"
import { useState } from "react"


export const Sala = () => {


    const [userData, setUserData] = useState({
        mensage: "",
        resposta: ""
    })


    return (
        <div className="Sala-section">

            <div className="Sala-container efeito-vidro">

                <div className="Sala-header">
                    <button>
                        voltar
                    </button>
                </div>
                <div className="Sala-content">
                    <div className="Sala-perfils">

                    </div>
                    <div className="Sala-pergunta">

                    </div>
                    <div className="Sala-chat">
                        <div className="Sala-chat-content">

                        </div>
                        <div className="Sala-chat-input">
                            <input name="mensagem" placeholder="comente aqui..." />
                        </div>
                    </div>
                </div>
            </div>

        </div>
    )

}