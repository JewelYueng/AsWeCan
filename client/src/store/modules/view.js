/**
 * Created by jewel on 2017/6/19.
 */
export default {
  state: {
    path: []
  },
  mutations: {
    CHANGE_VIEW_PATH(state, { path }) {
      if (typeof path !== 'string') {
        throw new TypeError(JSON.stringify(path))
      }
      state.path = path.split('/').filter( p => p !== '')
    }
  },
  actions: {
    jumpView( { commit }, path) {
      commit('CHANGE_VIEW_PATH', { path })
    }
  },
  getters: {
    view_level0: state => state.path[0],
    view_level1: state => state.path[1],
    view_level2: state => state.path[2]
  }
}
