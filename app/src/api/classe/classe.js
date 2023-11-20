import axios from "axios";
import { BaseUrl } from "../BaseUrl";

const http = axios.create({
  baseURL: BaseUrl + "/classe",
  withCredentials: true,
});


export const useClasse = () => {


    const listarClasses = async (cla) => {

        const response = await http.get("/" + cla)


        return response.data
    }


    return { listarClasses }
}