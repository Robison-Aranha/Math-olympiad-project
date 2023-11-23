import { userGlobalState } from "../../globalState/globalSate";
import { Route, Routes } from "react-router-dom";
import { TopBar, CriarSala, EntrarSala, Sala } from "../hooks";
import { Navigate } from "react-router-dom";

export const Protegido = () => {
  const [userGlobal,] = userGlobalState();

  return userGlobal.loged ? (
    <>
      <TopBar />
      <Routes>
        <Route path="/criar-sala" element={ <CriarSala /> } />
        <Route path="/entrar-sala" element={ <EntrarSala /> } />
        { userGlobal.sala ? 
          <Route path="/sala" element={ <Sala /> } />
        : null}
        <Route path="*" element={"Página não encontrada"} />
      </Routes>
    </>
  ) : (
    <Navigate to="/" />
  );
};