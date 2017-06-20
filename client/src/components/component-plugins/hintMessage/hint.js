'use strict'

let vm = null

const register = (_vm) => {
  vm = _vm
}

const HintMessage = (Vue) => {
  function hint (...args) {
    if (vm && typeof vm.hint === 'function') {
      vm.hint(...args)
    }else {
      throw Error ('Cannot using notify before register')
    }
  }
  Vue.prototype.$hint = hint
}

export { register, HintMessage }
