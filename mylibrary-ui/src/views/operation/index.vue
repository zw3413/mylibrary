<template>
  <div class="app-container" >
    <el-input v-model="postid" placeholder="请输入postid"></el-input><el-button @click="deletePost">删除</el-button>



  </div>
</template>

<script>
  import {doDeletePost} from '@/api/operation'
export default {
  name: 'Operation',
  methods: {
    deletePost: function() {


    }
  },
  data(){
    return {
      postid:'';
    }
  }
}
</script>

<style scoped>

</style>
