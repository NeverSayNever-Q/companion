import { createStore } from 'vuex'

export default createStore({
  state: {
    noteDrawerVisible: true,
    searchDrawerVisible: false
  },
  mutations: {
    showNoteDrawer: (state, visible) => {
      state.noteDrawerVisible = visible ?? state.noteDrawerVisible;
    },
    showSearchDrawer: (state, visible) => {
      state.searchDrawerVisible = visible ?? state.searchDrawerVisible;
    }
  },
  actions: {
  },
  modules: {
  }
})
