export default {
  state: {
    selected: {
    
    }
    // type: 日志类型
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
