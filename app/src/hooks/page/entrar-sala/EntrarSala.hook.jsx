import { useState } from "react"
import "./EntrarSala.style.css"



export const EntrarSala = () => {


    const [state, setState] = useState(false)



    return (
        <div className="EntrarSala-section">

            <div className="EntrarSala-choose">

                <div className="EntrarSala-choose-section grow-animation" onClick={() => setState(true)}>
                    Entrar em uma sala
                </div>
                <div className="EntrarSala-choose-section grow-animation" onClick={() => setState(false)}>
                    Procurar sala
                </div>

            </div>
            { state ? (
                <div className="EntrarSala-container-entrar efeito-vidro">
                    
                    <div className="EntrarSala-entrar-input">
                        <label>Senha</label>
                        <input />
                    </div>

                    <button>
                        Entrar
                    </button>

                </div>
            ) : (

                <div className="EntrarSala-container-procurar efeito-vidro">
                    
                    <div className="EntrarSala-procurar-input">
                        <p> Sala: </p>
                        <input  />
                    </div>                        
                    <div className="EntrarSala-procurar-content" >

                    </div>

                </div>

            ) }

        </div>
    )


}