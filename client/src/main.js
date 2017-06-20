// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

import  Vuex from 'vuex'
Vue.use(Vuex)

import VueResource from 'vue-resource'
Vue.use(VueResource)

import httpPlugin from './api'
Vue.use(httpPlugin)

import { Modal } from './components/component-plugins/modal/modal'
Vue.use(Modal)

import { HintMessage } from './components/component-plugins/hintMessage/hint'
Vue.use(HintMessage)

import store from './store'

Vue.config.productionTip = false

/* eslint-disable no-new */
 new Vue({
  router,
  render: h => h(App),
  store: new Vuex.Store(store)
}).$mount('#app')
