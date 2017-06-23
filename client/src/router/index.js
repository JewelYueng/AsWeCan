import Vue from "vue"
import Router from "vue-router"
import Hello from "@/components/Hello"
import FilesManager from "components/home/FilesManager"
import LogMerge from "components/home/Merge"
import MergeResult from "components/home/MergeResult"
import ProcessMining from "components/home/ProcessMining"
import Login from "components/login/index"
import Register from "components/register/index"


Vue.use(Router)

export default new Router({
  routes: [
    // {
    //   path: '/',
    //   name: 'Hello',
    //   component: Hello
    // },
    {
      path: "/",
      name: "files",
      components: {
        home: FilesManager
      }
    },
    {
      path: "/login",
      name: "login",
      component: Login
    },
    {
      path: "/register",
      name: "register",
      component: Register
    },
    {
      path: "/merge",
      name: "merge",
      components: {
        home: LogMerge
      }
    },
    {
      path: "/mergeResult",
      name: "mergeResult",
      components: {
        home: MergeResult
      }
    },
    {
      path: "/mining",
      name: "mining",
      components: {
        home: ProcessMining
      }
    },
    {
      path: "/miningResult",
      name: "miningResult",
      components: {
        home: ProcessMining
      }
    }

  ]
})
