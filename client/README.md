# client

> A Vue.js project

## Build Setup

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report

# run unit tests
npm run unit

# run e2e tests
npm run e2e

# run all tests
npm test
```

For detailed explanation on how things work, checkout the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).

## 开发中引进来的所有插件的用法	

### api插件

- 插件的意义：重新封装了vueResource的插件，使ajax的写法对前端开发更加友好
- 插件的用处：使用来进行http请求。
- 用法： 

1. 首先在api/api_map.js文件夹中填写相应的映射，例如

```javascript
'test': [POST, '']
```

2. 然后就可以在vue中使用啦

```javascript
	//在Vue实例中，单纯的http访问，不需要理会服务器的response的话：
	this.$api({method: 'test'})
	//需要处理response时
	this.$api({method: 'test'}).then((data) => {
          console.log(data)
        })
        
   //上传参数时的三种参数上传的写法
   //直接写在请求体中
   	this.$api({method: 'test', body: {object}})
   	//作为arg传上去的例如http://110.110.110.2:3000/page/:page中的page
   	this.$api({method: 'test', arg: {page: 1}})
   	//作为query传上去的例如'http://110.110.110.2:3000/team/delete_job/?jobId=1'中的jobId
   	this.$api({method: 'test', query: {jobId: 1}})
```

### Hint插件

- 插件的意义： 封装了一个常用的提示框的插件，可以统一对一些不需要停留过久自动消失的提示进行处理
- 插件的用处： 节省大家分开写提示框的时间，并且提示框样式更加统一
- 用法：

```javascript
//接受一个以上参数，第一个是提示的文字，类型为字符串，第二个是提示的类型，分别有"success", "warn", "error"类型也是字符串
//在Vue实例中
this.$hint('test','success')
```

### Modal插件

- 插件的意义： 封装了一个常用的弹出框的插件，同时把弹出框的一般操作做了封装
- 插件的用法： 节省大家分开写弹出框的时间，并且不用在多余的样式上浪费时间
- 用法：

1. 在componnets/components-plugins/modal/boxes文件夹中新建一个vue组件，负责写弹出框的主题部分样式和逻辑操作
2. 在新建的组件中引入BaseBox.js
3. 把BaseBox的操作写进该组件的mixins中

```javascript
<template>
  <div>
    测试弹出框,key值为{{data.key}}
    <button @click="back"></button>
  </div>
</template>
<style lang="less" scoped rel="stylesheet/less">

</style>
<script>
  import BaseBox from './BaseBox'
  export default{
    mixins: [BaseBox],
    methods: {
      back(){
        this.commit(true)
//        浏览器测试：this.$modal({type:'alert',data: {key: 'q'}}).then((a)=>{console.log(a)})
      }
    }
  }
</script>
```

4. 然后在componnets/components-plugins/modal下的Modal.vue文件中引入你的组件，同时把引入的组件写在components属性下

```javascript
import Alert from './boxes/Alert.vue'
...
export default {
    components: {
      Alert
    },
    ...
}
```

5. 调用

```javascript
this.$modal({type:'alert',data: {key: 'q'}}).then((a)=>{console.log(a)})
//点击Alert中的按钮后，弹出框消失，控制台输出true
//then回调函数中的参数a其实是Alert组件中的commit函数中参数
```