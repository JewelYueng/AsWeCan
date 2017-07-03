export default {
  state: {
    selected: {
      type: -1,
      id: null,
      page: -1
    }
    // type: 日志类型 0原始日志 1规范化日志 2事件日志 3分享的原始日志 4分享的规范化日志 5分享的事件日志
    // id: 日志id
  //  page：日志所在的页码
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
