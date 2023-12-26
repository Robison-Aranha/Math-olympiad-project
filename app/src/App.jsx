import { Route, Routes } from "react-router-dom"
import { LoginRegistro } from "./hooks/hooks"
import { Protegido, Notification, Loading } from "./hooks/hooks"



function App() {


  return (
    <>
      <Notification />
      <Loading />
      <Routes>

        <Route path="/" element={ <LoginRegistro /> } />

        <Route path="*" element={ <Protegido /> } />

      </Routes>
    </>
  )
}

export default App
