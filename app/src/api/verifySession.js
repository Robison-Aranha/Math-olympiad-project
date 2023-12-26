import { useNavigate } from "react-router-dom";



export const useVerifySession = () => {


    const navigate = useNavigate()


    const verifySessionUser = (data) => {
        
        if (data.response.status == 401) {
            navigate("/", {state: "expired"});
        }
    }



    return { verifySessionUser }
}