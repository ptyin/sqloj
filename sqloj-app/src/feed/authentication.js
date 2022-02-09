import {message} from "antd";
import axios from "axios";


/**
 *
 * @param {string} username
 * @param {string} password
 * @param {boolean} remember
 * @returns {Promise<AxiosResponse<any>>}
 */
export function login(username, password, remember)
{

    const params = new URLSearchParams();
    params.append('username', username);
    params.append('password', password);
    axios.defaults.withCredentials = true;
    return axios.post('/api/login', params).then((response) =>
    {
        console.log(response.data)
        if (response.data.success === true)
        {
            window.sessionStorage.username = username
            window.localStorage.remember = remember
            console.log(remember)
            if (remember)
            {
                window.localStorage.username = username
                window.localStorage.password = password
            } else
            {
                window.localStorage.removeItem("username")
                window.localStorage.removeItem("password")
            }
        }
        return response
    })
}