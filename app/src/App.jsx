import { Route, Routes } from "react-router-dom"
import { LoginRegistro } from "./hooks/hooks"
import { Protegido } from "./hooks/hooks"



function App() {


  return (
    
    <Routes>

      <Route path="/" element={ <LoginRegistro /> } />

      <Route path="*" element={ <Protegido /> } />

    </Routes>
    
  )
}

export default App
