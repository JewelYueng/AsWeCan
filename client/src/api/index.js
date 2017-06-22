"use strict"

const _ = require("lodash")

const api_map = require("./api_map").default
const base_url = api_map.__base_url__

const replaceArgs = (string, args) => {
  let result = string
  Object.keys(args).forEach(key => {
    const value = args[key]
    result = result
      .replace(new RegExp(`:${key}$`), encodeURIComponent(value))
      .replace(new RegExp(`:${key}/`), encodeURIComponent(value) + "/")
  })
  return result
}

class WrapResponse {
  constructor(response) {
    this.data = response.data
    this.error = this.data ? this.data.error : undefined
    this.status = response.status
    this._origin = response
  }
  
  handle(error_object) {
    if (this.data === undefined)
      new Error("attribute \"data\" not in response")
    const error = this.data.error
    if (error === undefined || typeof error !== "number")
      new Error("attribute \"error\" not in response.data")
    if (error !== 0) {
      if (error in error_object)
        error_object[error](this)
      else if ("default" in error_object)
        error_object["default"](this)
    }
    return this
  }
  
  success(fn) {
    if (this.data.error === 0)
      fn(this)
    return this
  }
}

const each_action = {}
each_action.trigger = function (key, ...args) {
  if (typeof this[key] === "function")
    this[key].call(null, ...args)
  return this
}.bind(each_action)


let httpPlugin = {}
httpPlugin.install = function (Vue, options) {
  
  Vue.prototype.$api = function ({method, args = {}, body = {}, query = {}, opts = {}}) {
    const headers = {}
    if (typeof this.$store.getters.getToken === "string" && this.$store.getters.getToken) {
      headers["Authorization"] = this.$store.getters.getToken
    }
    let promise
    const [http_method, uri] = api_map[method]

    if (["get", "head", "delete"].indexOf(http_method) >= 0) {
      promise = this.$http[http_method](base_url + replaceArgs(uri, args), {
        headers: headers,
        params: query,
        body: opts['body']
      })
    } else {
      promise = this.$http[http_method](base_url + replaceArgs(uri, args), JSON.stringify(body), {
        headers: _.merge({
          "Content-Type": "application/json"
        }, headers),
        params: query
      })
    }

    each_action.trigger("request", {method, args, body, query})

    // return promise
    return new Promise((resolve, reject) => {
      promise.then(response => {
        each_action.trigger("response", response)
        each_action.trigger("success", response)
        resolve(new WrapResponse(response))
      }, response => {
        each_action.trigger("response", response)
        each_action.trigger("failure", response)
        reject(new WrapResponse(response))
      })
    })
  }
  
  Vue.prototype.$api.onEach = function (event, action) {
    each_action[event] = action
  }
  
}

export default httpPlugin


