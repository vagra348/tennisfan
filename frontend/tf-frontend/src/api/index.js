// src/api/index.js
import axios from 'axios'

const API_BASE_URL = 'http://localhost:8081'

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

export const adminApi = (adminUserId) => {
  return axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-Type': 'application/json',
      'X-User-Id': adminUserId
    }
  })
}