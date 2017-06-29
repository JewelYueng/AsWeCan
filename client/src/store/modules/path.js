export default {
  state: {
    home_path: '/',
    file_path: '1-1',
    diagram_path: '1',
  },
  mutations: {
    SET_HOME_PATH (state, {path}){
      if(typeof path !== 'string') {
        throw new TypeError(JSON.stringify(path))
      }
      state.home_path = path
    },
    SET_FILE_PATH (state, {path}) {
      if(typeof path !== 'string') {
        throw new TypeError(JSON.stringify(path))
      }
      state.file_path = path
    },
    SET_DIAGRAM_PATH(state, {path}) {
      if(typeof path !== 'string') {
        throw new TypeError(JSON.stringify(path))
      }
      state.diagram_path = path
    }
  },
  actions: {
    changeHomePath({commit}, path){
      commit('SET_HOME_PATH', { path })
    },
    changeFilePath({commit}, path){
      commit('SET_FILE_PATH', { path })
    },
    changeDiagramPath({commit}, path){
      commit('SET_DIAGRAM_PATH', {path})
    }
  },
  getters: {
    home_path: state => state.home_path,
    file_path: state => state.file_path,
    diagram_path: state => state.diagram_path
  }
}
