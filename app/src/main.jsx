import { BrowserRouter } from 'react-router-dom'
import { UserProvider, ModalProvider, LoadingProvider } from './globalState/globalSate.js'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import "./main.css"


ReactDOM.createRoot(document.getElementById('root')).render(
    <ModalProvider>
      <LoadingProvider>
        <UserProvider>
          <BrowserRouter>
            <App />
          </BrowserRouter>
        </UserProvider>
      </LoadingProvider>
    </ModalProvider>
)
