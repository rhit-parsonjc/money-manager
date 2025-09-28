import { defineStore } from 'pinia'
import axios from 'axios'

const baseUrl = 'http://localhost:8080/api/v1/auth'

/*
 * This authentication store is responsible for:
 * - Registering a user
 * - Logging in a user (recording the JSON token)
 * - Signing a user out (clearing the JSON token)
 */

/*
This function takes in a url to do a POST request to
And the data to include in the POST request
And it returns a promise that resolves when the request is successful
And rejects if the request is unsuccessful or an error occurs.
*/
function postAsync(url, data) {
  return new Promise((resolve, reject) => {
    axios
      .post(url, data)
      .then((response) => {
        if (response.data.success) {
          resolve(response.data.data)
        } else {
          const errorMessage = response.data.data
          reject(errorMessage)
        }
      })
      .catch((error) => reject(error))
  })
}

function getJsonToken() {
  const jsonToken = window.localStorage.getItem('token')
  if (jsonToken) {
    parseJsonToken(jsonToken)
    return jsonToken
  } else return null
}

function setJsonToken(jsonToken) {
  window.localStorage.setItem('token', jsonToken)
}

function clearJsonToken() {
  window.localStorage.clear()
}

function parseJsonToken(jsonToken) {
  const jsonTokenParts = jsonToken.split('.')
  const base64Url = jsonTokenParts[1]
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
  const base64Atob = window.atob(base64)
  const base64AtobChars = base64Atob.split('')
  const base64AtobCharsCodes = base64AtobChars.map(
    (c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2),
  )
  const jsonPayloadEncoded = base64AtobCharsCodes.join('')
  const jsonPayload = decodeURIComponent(jsonPayloadEncoded)
  const parsedJsonPayload = JSON.parse(jsonPayload)
  return parsedJsonPayload
}

const useAuthenticationStore = defineStore('authentication', {
  state: () => {
    return {
      /** @type any */
      _jsonToken: getJsonToken(),
    }
  },

  getters: {
    jsonToken(state) {
      return state._jsonToken
    },
    isAuthenticated(state) {
      return state._jsonToken !== null
    },
    username(state) {
      if (state._jsonToken) return parseJsonToken(state._jsonToken).sub
      else return null
    },
  },

  actions: {
    registerAsync(userData) {
      const registerPromise = postAsync(`${baseUrl}/register`, userData)
      return new Promise((resolve, reject) => {
        registerPromise
          .then(() => {
            console.log('Successfully registered user')
            resolve()
          })
          .catch((error) => {
            console.error(error)
            reject()
          })
      })
    },
    loginAsync(userData) {
      const loginPromise = postAsync(`${baseUrl}/login`, userData)
      return new Promise((resolve, reject) => {
        loginPromise
          .then((response) => {
            console.log('Successfully logged in')
            const jsonToken = response
            console.log({ jsonToken })
            this._jsonToken = jsonToken
            setJsonToken(jsonToken)
            resolve()
          })
          .catch((error) => {
            console.error(error)
            reject()
          })
      })
    },
    signOut() {
      console.log('Logged out')
      this._jsonToken = null
      clearJsonToken()
    },
  },
})

export default useAuthenticationStore
