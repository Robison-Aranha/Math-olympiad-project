import { userGlobalState } from "../../globalState/globalSate";
import { Route, Routes } from "react-router-dom";
import { CriarPersonagem, TopBar, Personagens } from "../hooks";
import { Navigate } from "react-router-dom";

export const Protegido = () => {
  const [userGlobal,] = userGlobalState();

  return userGlobal.loged ? (
    <>
      <TopBar />
      <Routes>
        <Route path="/criar-personagem" element={ <CriarPersonagem /> } />
        <Route path="/personagens" element={ <Personagens /> } />
        <Route path="*" element={"Página não encontrada"} />
      </Routes>
    </>
  ) : (
    <Navigate to="/" />
  );
};