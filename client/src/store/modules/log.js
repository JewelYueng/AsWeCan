export default {
  state: {
    selected: {
      type: -1,
      id: null
    }
    // type: 日志类型 0原始日志 1规范化日志 2事件日志
    // id: 日志id
  },
  mutations:{
    SET_SELECTED(state, {item}) {
      if(typeof item !== 'object'){
        throw new TypeError(JSON.stringify(item))
      }
      state.selected = item
    }
  },
  actions: {
    selectLog({commit}, item) {
      commit('SET_SELECTED', { item })
    }
  },
  getters: {
    selectedLog: state => state.selected
  }
}
