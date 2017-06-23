import Vue from "vue"
import Router from "vue-router"
import Hello from "@/components/Hello"
import FilesManager from "components/home/FilesManager"
import LogMerge from "components/home/Merge"
import MergeResult from "components/home/MergeResult"
import ProcessMining from "components/home/ProcessMining"


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
      component: FilesManager
    },
    {
      path: "/merge",
      name: "merge",
      component: LogMerge
    },
    {
      path: "/mergeResult",
      name: "mergeResult",
      component: MergeResult
    },
    {
      path: "/mining",
      name: "mining",
      component: ProcessMining
    },
    {
      path: "/miningResult",
      name: "miningResult",
      component: ProcessMining
    }
  
  ]
})
