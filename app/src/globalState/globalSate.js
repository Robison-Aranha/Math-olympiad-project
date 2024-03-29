import createGlobalState from "react-create-global-state";


export const [userGlobalState, UserProvider] = createGlobalState(
  localStorage.getItem("user")
    ? JSON.parse(localStorage.getItem("user"))
    : {
        id: "",
        nome: "",
        sala: null,
        token: null,
        loged: false,
      }
);

export const [useGlobalModal, ModalProvider] = createGlobalState([]);
export const [useGlobalLoading, LoadingProvider] = createGlobalState(false);