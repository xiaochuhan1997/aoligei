<template>
<div class="container" id="app">
<table class="table table-striped">
  <caption>文章列表</caption>
  <thead>
  <tr>
    <th align="center">编号</th>
    <th align="center">标题</th>
    <th align="center">描述</th>
    <th align="center">发布时间</th>
    <th align="center">操作</th>
  </tr>
  </thead>
  <tbody>
  <tr v-for="art in articleList">
    <td>{{art.id}}</td>
    <td>{{art.title}}</td>
    <td>{{art.descn}}</td>
    <td>{{art.createTime}}</td>
    <td align="center">
      <button class="btn btn-link" style="margin-right: 10px;">修改</button>
      <button class="btn btn-link">删除</button>
    </td>
  </tr>
  </tbody>
</table>
<ul class="pagination" v-for="p in pageCnt">
  <li v-if="p == pageIndex" class="active"><a href="#" @click="doGo(p)">{{p}}</a></li>
  <li v-else="p == pageIndex"><a href="#" @click="doGo(p)">{{p}}</a></li>
</ul>
</div>
</template>
<script>
new Vue({
  el: '#app',
  data: {
    articleList: null,
    //用于分页
    pageIndex: 1, //页码
    pageSize: 3, //每页显示的条数
    pageTotal: 0, //总条数
    pageCnt: 0 //总页数
  },
  methods: {
    requestArticleList(){
      axios.get("http://localhost:8081/list?pageIndex="+this.pageIndex+"&pageSize="+this.pageSize).then(res => {
        console.log(res.data)
        this.articleList = res.data.records
        this.pageCnt = res.data.pages
        this.pageTotal = res.data.total
        this.pageIndex = res.data.current
        this.pageSize = res.data.size
      })
    },
    doGo(p){
      this.pageIndex = p
      this.requestArticleList()
    }
  },
  created: function () {
    this.requestArticleList()
  }
})
</script>
