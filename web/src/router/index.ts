import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Note from '../views/Note.vue'
import Search from '../views/Search.vue'

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'Note',
        component: Note
    },
    {
        path: '/search',
        name: 'Search',
        component: Search
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
