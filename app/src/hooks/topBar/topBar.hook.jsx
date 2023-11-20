import "./topBar.style.css"
import { userGlobalState } from "../../globalState/globalSate"
import { Link } from "react-router-dom"


export const TopBar = () => {

    const [userGlobal, ] = userGlobalState()


    return (
        <div className="TopBar-section">

        
            <div className="TopBar-perfil">

                <div className="TopBar-foto">
                    <img />
                </div>
                <p> <span> NOME: </span> { userGlobal.nome } </p>

            </div>
            <div className="TopBar-links">
                <Link to="/salas">Salas</Link>
                <Link to="/criar-sala">Criar Sala</Link>
            </div>
         
            

        </div>
    )

}