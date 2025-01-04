// src/api/axiosInstance.js
import axios from 'axios';
import { useAuth } from '../context/AuthContext';

const useAxios = () => {
    const { token } = useAuth();

    const instance = axios.create({
        baseURL: 'http://localhost:8080',
    });

    instance.interceptors.request.use(
        (config) => {
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
        },
        (error) => Promise.reject(error)
    );

    return instance;
};

export default useAxios;
