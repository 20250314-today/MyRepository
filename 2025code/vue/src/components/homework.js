// store/modules/homework.js
const state = {
    homework: []
};

const mutations = {
    ADD_HOMEWORK(state, payload) {
        state.homework.push(payload);
    }
};

const actions = {
    add({ commit }, homework) {
        commit('ADD_HOMEWORK', homework);
    }
};

export default {
    namespaced: true, // 启用命名空间
    state,
    mutations,
    actions
};