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

const useAuthenticationStore = defineStore('authentication', {
  state: () => {
    return {
      /** @type any */
      _jsonToken: null,
    }
  },

  getters: {
    jsonToken(state) {
      return state._jsonToken
    },
    isAuthenticated(state) {
      return state._jsonToken !== null
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
    },
  },
})

export default useAuthenticationStore
