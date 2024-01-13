import { userGlobalState } from "../../globalState/globalSate";
import { Route, Routes } from "react-router-dom";
import { TopBar, CriarSala, EntrarSala, Sala } from "../hooks";
import { Navigate } from "react-router-dom";

export const Protegido = () => {
  const [userGlobal] = userGlobalState();

  return userGlobal.loged ? (
    userGlobal.sala ? (
      <Routes>
        <Route path="*" element={<Sala />} />
      </Routes>
    ) : (
      <>
        <TopBar />
        <Routes>
          <Route path="/criar-sala" element={<CriarSala />} />
          <Route path="/entrar-sala" element={<EntrarSala />} />
          <Route path="*" element={<Navigate to="/criar-sala" />} />
        </Routes>
      </>
    )
  ) : (
    <Navigate to="/" />
  );
};
