"use strict"

let vm = null

const register = (_vm) => {
  vm = _vm
}

const Modal = (Vue) => {
  function $modal(opt) {
    if (vm && typeof vm.modal === "function") {
      return vm.modal(opt)
    } else {
      throw Error("Cannot using notify before register")
    }
  }
  
  Vue.prototype.$modal = $modal
}

export {register, Modal}
