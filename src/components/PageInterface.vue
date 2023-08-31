<template>
<!--   面包导航区-->
  <el-breadcrumb separator-icon="ArrowRight">
    <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>控制台</el-breadcrumb-item>
    <el-breadcrumb-item>接口管理（分页）</el-breadcrumb-item>
  </el-breadcrumb>

<!--  <el-card>-->
<!--    <el-form :model="paraForm" ref="paraFormRef" label-width="150px" :inline="true">-->
<!--      &lt;!&ndash; url &ndash;&gt;-->
<!--      <el-form-item style='width: 60%' label="swagger API 地址">-->
<!--        <el-input v-model="paraForm.url" placeholder="请输入API 地址(http(s)://)">-->
<!--          <template #append>-->
<!--            <el-button type="primary" icon="Promotion" @click="sent" />-->
<!--          </template>-->
<!--        </el-input>-->
<!--      </el-form-item>-->
<!--&lt;!&ndash;      <el-form-item style='width: 60%' label="登录的Cookie">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input v-model="paraForm.cookie" placeholder="请参考 'Cookie'：'JSESSIONID=***************'">&ndash;&gt;-->
<!--&lt;!&ndash;        </el-input>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--    </el-form>-->
<!--  </el-card>-->
  <el-card>
    <el-table :data="swaggerData" :style="{ width: '100%'}" :row-key="(row)=>{return row.id;}">
      <!-- :row-key="(row)=>{return row;}" -->
      <el-table-column type="expand">
        <template #default="props">
          <div m="4">
            <el-form :inline="true" ref="apiFormRef" :model="apiForm" :rules="apiFormRules"
                     label-width="120px" status-icon :style="{ marginLeft: '20px'}">

              <el-form-item label="案例号" prop="caseNum">
                <el-input v-model="apiForm.caseNum" />
              </el-form-item>
              <el-form-item label="案例描述" prop="caseName">
                <el-input v-model="apiForm.caseName" />
              </el-form-item>
              <el-form-item> <el-button type="">测试连通性</el-button>
                <el-button type="">保存案例</el-button></el-form-item>

              <el-form-item>
                <!-- <el-form-item label="Activity zone" prop="ssss"> -->
                <codemirror v-model=props.row.inputParam placeholder="Code goes here..."
                            :style="{ height: '300px',width: '1300px'}" :autofocus="true"
                            :indent-with-tab="true" :tab-size="2" :extensions="extensions"
                            @ready="handleReady" @blur="formatJson(props.row)" />
                <!-- @change="resetData('change', $event)"  @focus="log('focus', $event)" @blur="test(props.row)" -->
              </el-form-item>
              <el-form-item>
                <codemirror v-model=props.row.outputParam placeholder="Code goes here..."
                            :style="{ height: '300px',width: '1300px'}" :autofocus="true"
                            :indent-with-tab="true" :tab-size="2" :extensions="extensions"
                            @ready="handleReady" @blur="formatJson(props.row)" />
              </el-form-item>
            </el-form>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="接口地址" prop="apiUrl" />
      <el-table-column label="请求方式" prop="method" />
      <el-table-column label="接口描述" prop="summary" />
      <el-table-column label="接口所属分组" prop="tag" />
<!--      <el-table-column align="center" label="操作">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>  &lt;!&ndash;scope.row当前行的对象&ndash;&gt;-->
<!--          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->

    </el-table>
    <el-pagination class="fy"
                   layout="total, sizes, prev, pager, next, jumper"
                   :total="total"
                   :page-sizes="[5, 10]"
                   :page-size= "pageSize"
                   v-show="total>5"
                   @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"
                   background style="margin-top:10px">
    </el-pagination>


<!--    <div class="block">-->
<!--      <el-pagination-->
<!--        hide-on-single-page   当数据只有一页时,自动隐藏分页菜单-->
<!--        @size-change="handleSizeChange"        当每页显示的数据数目发生改变生的动作,需要按新的pageSize查询数据-->
<!--        @current-change="handleCurrentChange"  当改变当前页时产生的动作-->
<!--        :current-page="pageNo"                 绑定当前页-->
<!--        :page-sizes="[5, 10, 30, 50]"          显示pageSize的选项-->
<!--        :page-size="pageSize"				  绑定当前页数-->
<!--        layout="total, sizes, prev, pager, next, jumper"-->
<!--        :total="totalCount">				  绑定当前总数-->
<!--      </el-pagination>-->
<!--    </div>-->

  </el-card>

</template>
<script>
import data from '../1.json';
import { defineComponent, ref, shallowRef } from 'vue';
import { Codemirror } from 'vue-codemirror';
import { json } from '@codemirror/lang-json';
import { oneDark } from '@codemirror/theme-one-dark';
export default {
  // components: {
  //   Codemirror,
  // },
  data() {
    return {
      code1: '11111111',
      paraForm: {
        url: '1111',
      },
      tableData: [],
      currentPage: {
        pageNo: 1,
        pageSize: 5,
        totalCount: 0
      },
      // tableData: [
      //   {
      //     date: '2016-05-07',
      //     name: 'Tom',
      //     state: 'California',
      //     city: 'San Francisco',
      //     address: '3650 21st St, San Francisco',
      //     zip: 'CA 94114',
      //     family: [
      //       {
      //         name: 'Jerry',
      //         state: 'California',
      //         city: 'San Francisco',
      //         address: '3650 21st St, San Francisco',
      //         zip: 'CA 94114',
      //       },
      //       {
      //         name: 'Spike',
      //         state: 'California',
      //         city: 'San Francisco',
      //         address: '3650 21st St, San Francisco',
      //         zip: 'CA 94114',
      //       },
      //       {
      //         name: 'Tyke',
      //         state: 'California',
      //         city: 'San Francisco',
      //         address: '3650 21st St, San Francisco',
      //         zip: 'CA 94114',
      //       },
      //     ],
      //   },
      // ],
      swaggerData: [],
      apiForm: {},
      apiFormRules: [],
    };
  },
  mounted() {
    // this.getCount();
    // this.getList();
    this.sent();
  },

  components: {
    Codemirror,
  },
  setup() {
    const code = ref(`console.log('Hello, world!')`);
    const extensions = [json(), oneDark];

    // Codemirror EditorView instance ref
    const view = shallowRef();
    const handleReady = (payload) => {
      view.value = payload.view;
    };

    // Status is available at all times via Codemirror EditorView
    const getCodemirrorStates = () => {
      const state = view.value.state;
      const ranges = state.selection.ranges;
      const selected = ranges.reduce(
        (r, range) => r + range.to - range.from,
        0
      );
      const cursor = ranges[0].anchor;
      const length = state.doc.length;
      const lines = state.doc.lines;
      // more state info ...
      // return ...
    };

    const formatJson = (data) => {
      data.outputParam = JSON.stringify(
        JSON.parse(data.outputParam),
        null,
        '\t'
      );
    };
    return {
      code,
      extensions,
      handleReady,
      log: console.log,
      formatJson,
    };
  },
  methods: {
    async sent() {
      const { data: res } = await this.$http.post('/queryinterface/query', this.paraForm);
      console.log(res);
      if (res.code !== 200) return this.$message.error(res.msg);
      // this.$message.success('查询成功');
      // for (var i = 0; i < res.data.length; i++) {
      //   res.data[i].inputParam = JSON.parse(res.data[i].inputParam);
      //   res.data[i].outputParam = JSON.parse(res.data[i].outputParam);
      // }
      for (var i = 0; i < res.data.length; i++) {
        res.data[i].inputParam = JSON.stringify(
          JSON.parse(res.data[i].inputParam),
          null,
          '\t'
        );
        res.data[i].outputParam = JSON.stringify(
          JSON.parse(res.data[i].outputParam),
          null,
          '\t'
        );
      }

      // console.log(res.data);

      this.swaggerData = res.data;
      console.log(this.swaggerData);
    },
    // getCount(){
    //   this.axios.post("/getCount").then(res=>{
    //     this.totalCount = res.data;
    //   })
    // },
    // getList(){
    //   let params = new FormData();
    //   params.append("pageNo",this.pageNo);
    //   params.append("pageSize",this.pageSize);
    //   this.axios.post("/getUserList",params).then(res=>{
    //     this.userList = res.data;
    //   })
    // },
    // handleEdit(row){    // 对该行数据进行更新
    //   this.$router.push({
    //     name:'update',
    //     params:{user:row}
    //   })
    // },
    // handleDelete(row){ // 对改行数据进行删除
    //   let params = new FormData();
    //   params.append("id",row.id);
    //   this.axios.post("/delete",params).then(res=>{
    //     if(res.data=="ok"){
    //       this.$notify.success({"title":"删除结果","message":"成功"});
    //       this.getCount();
    //       this.getList();
    //     }else {
    //       this.$notify.error({"title":"删除结果","message":"失败"});
    //     }
    //   })
    // },
    // handleSizeChange(val) { // 修改每页所存数据量的值所触发的函数
    //   this.pageSize = val;  // 修改页的大小
    //   this.getList();       // 按新的pageNo和pageSize进行查询
    // },
    // handleCurrentChange(val) { // 修改当前页所触发的函数
    //   this.pageNo = val;       // 更新当前的页
    //   this.getList();          // 按新的pageNo和pageSize进行查询
    // }




    // pageNoChange(){
    //   let that = this;
    //   that.search();
    // },
    // search(){
    //   let that = this;
    //   that.$axios.defaults.baseURL='http://localhost:8081'
    //   that.$axios({
    //     method: 'get',
    //     url: '/page/search?pageNo='+that.currentPage.pageNo,
    //   }).then((result) => {
    //     that.tableData = result.data.data;
    //     that.currentPage.total = result.data.count;
    //   });
    // }


  },
};
</script>
<style lang="less" scoped>
.el-card {
  margin: 15px;
}
</style>

