import VUe from "vue"

const state = {
  normal_log: [],
  raw_log: [],
  event_log: []
}

const getters = {

  normal_log: state => state.normal_log,
  raw_log: state => state.raw_log,
  event_log: state =>state.event_log
}

const actions = {

}

const mutations = {

}
export default {state, getters, actions, mutations}
