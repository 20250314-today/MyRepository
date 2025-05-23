import { createStore } from 'vuex'

export default createStore({ // ✅ 使用 createStore 替代 new Vuex.Store
    state: {
        HomeWork: {},
        NotHomework: [],
    },
    mutations: {
        SET_HOMEWORK(state, value) {
            state.HomeWork = value
        },
        SET_NOT_HOMEWORK(state, value) {
            state.NotHomework = value
        }
    },
    actions: {
        // 直接调用根 Action
        updateHomework({ commit }, value) {
            commit('SET_HOMEWORK', value)
        },
        updateNotHomework({ commit }, value) {
            commit('SET_NOT_HOMEWORK', value)
        }
    },
    modules: {}
})