<template>
  <div class="home">
    <button @click="login()">登录</button>
  </div>
</template>
<script>
  import * as arr from "element-ui";

  export default {
    name: 'Home',
    methods:{
      login(){
        this.axios({
          method: 'get',
          url: '/getBook',
          data: {
            // username: 'admin',
            // password: 123456,
            msg:'',
            arr:[]
          }
        }).then((res)=>{
          //var m=this.msg
          // m=res.data
         alert(this.msg)
          console.log(arr)
// res.data 是数据（参考个自的数据格式）


        });
      },
      find(){}
    }
  }
</script>
