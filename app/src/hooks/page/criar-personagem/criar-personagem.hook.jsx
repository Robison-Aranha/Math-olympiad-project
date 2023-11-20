import "./criar-personagem.style.css";
import { CLAS } from "../../../consts/clas";
import { useState, useEffect } from "react";
import { useClasse } from "../../../api/api";
import { useGlobalModal } from "../../../globalState/globalSate";


export const CriarPersonagem = () => {

    const [userData, setUserData] = useState({
        nome: "",
        classe: "",
        cla: ""
    })

    useEffect(() => {

        const element = document.getElementsByName("cla")
        element[0].checked = true
        setUserData({ ...userData, cla: element[0].value })

    }, [])

    useEffect(() => {

        listarClassesService()

    }, [userData.cla])

    const [classes, setClasses] = useState([])


    const [globalModal, setGlobalModal] = useGlobalModal()
    const { listarClasses } = useClasse()

    const handlerValue = (event) => {
        const { value, name } = event.target;

        if (name == "classe") {

            const classe = classes.find(c => c.nome == value) 

            setUserData({ ...userData, classe: classe })

        } else {

            setUserData({ ...userData, [name]: value });

        }

      };

    

    const listarClassesService = async () => {
        try {

            const response = await listarClasses(userData.cla)
            
            setUserData({ ...userData, classe: response.classes[0] })

            setClasses([...response.classes])
        } catch {
            setGlobalModal([...globalModal, { message: "Erro ao listar as classes!" }])
        }

    }

    
    return (
        <div className="CriarPersonagens-section">

            <div className="CriarPersonagens-personagem">

                <div className="CriarPersonagens-personagem-content">

                    <div className="CriarPersonagens-personagem-imagem efeito-vidro">
                        <img src={userData.classe.imagem} />
                    </div>
                    <div className="CriarPersonagens-personagem-stats">

                    </div>
                </div>

            </div>
            <div className="CriarPersonagens-inputs">

                <div className="CriarPersonagens-inputs-content efeito-vidro" id="vidro-preto">
                    <div className="CriarPersonagens-inputs-container">
                        <div className="CriarPersonagens-inputs-input">
                            <label>Nome</label>
                            <input name="nome" type="text" value={userData.nome} onChange={handlerValue}/>
                        </div>
                        <div className="CriarPersonagens-inputs-input select-style">
                            <label>Classe</label>
                            <select name="classe" value={userData.classe.nome} onChange={handlerValue}>
                                <option selected disabled>
                                    Selecione uma classe
                                </option>
                                {classes?.map((classe, key) => (
                                    <option key={key}>
                                        {classe.nome}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="CriarPersonagens-inputs-input">
                            {CLAS.map((cla, key) => (
                                <label className="label-radio" key={key}>
                                    {cla}
                                    <input name="cla" type="radio" value={cla} onChange={handlerValue} />
                                    <span className="checkmark"></span>
                                </label>
                            ))}
                        </div>
                    </div>
                    <div className="CriarPersonagens-buttons-container">
                        <button>
                            Criar
                        </button>
                    </div>
                </div>

            </div>

        </div>
    )


}