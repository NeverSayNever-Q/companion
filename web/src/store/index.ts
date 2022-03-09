import { createStore } from 'vuex'

export default createStore({
  state: {
    noteDrawerVisible: false,
  },
  mutations: {
    showNoteDrawer: (state, visible) => {
      state.noteDrawerVisible = visible ?? state.noteDrawerVisible;
    }
  },
  actions: {
  },
  modules: {
  }
})
