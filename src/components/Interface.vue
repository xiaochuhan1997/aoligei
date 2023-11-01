<template>
  <!--   面包导航区-->
  <el-breadcrumb separator-icon="ArrowRight">
    <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>控制台</el-breadcrumb-item>
    <el-breadcrumb-item>接口管理</el-breadcrumb-item>
  </el-breadcrumb>

  <el-card>
    <el-table :data="swaggerData" :style="{ width: '100%'}" :row-key="(row)=>{return row.id;}">
      <el-table-column type="expand">
        <template #default="props">
          <div m="4">
            <el-form :inline="true" ref="apiFormRef" :model="apiForm" :rules="apiFormRules"
                     label-width="120px" status-icon :style="{ marginLeft: '20px'}">
              <el-row>
                <el-col>
              <el-form-item>
<!--                <el-button type="" @click="save(props.row.serverUrl,props.row.apiUrl,props.row.method,props.row.tag,props.row.summary,props.row.inputParam,props.row.outputParam,)">保存案例</el-button>-->
                <el-button @click="sendJson(props.row.id,props.row.method,props.row.inputParam,props.row.outputParam,props.row.apiUrl,props.row.serverUrl,props.row.inputParamDec)">发送JSON请求</el-button>
              </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col>
              <el-form-item>
                <codemirror v-model=props.row.inputParam placeholder="Code goes here..."
                            :style="{ height: '300px',width: '1300px'}" :autofocus="true"
                            :indent-with-tab="true" :tab-size="2" :extensions="extensions"
                            @ready="handleReady" @blur="formatJson(props.row)"/>
              </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col>
              <el-form-item>
                <codemirror v-model=props.row.outputParam placeholder="Code goes here..."
                            :style="{ height: '300px',width: '1300px'}" :autofocus="true"
                            :indent-with-tab="true" :tab-size="2" :extensions="extensions"
                            @ready="handleReady" @blur="formatJson(props.row)"/>
              </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="案例号" prop="caseNo"/>
      <el-table-column label="案例描述" prop="caseDec"/>
      <el-table-column label="接口地址" prop="serverUrl"/>
      <el-table-column label="接口地址" prop="apiUrl"/>
      <el-table-column label="请求方式" prop="method"/>
      <el-table-column label="接口描述" prop="summary"/>
      <el-table-column label="接口所属分组" prop="tag"/>
    </el-table>
    <el-pagination class="fy"
                   layout="total, sizes, prev, pager, next, jumper"
                   :total="total"
                   :page-sizes="[5, 10]"
                   :page-size="pageSize"
                   v-show="total>5"
                   @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"
                   background style="margin-top:10px">
    </el-pagination>

  </el-card>

</template>
<script>

import {Codemirror} from 'vue-codemirror';
import {json} from '@codemirror/lang-json';
import {oneDark} from '@codemirror/theme-one-dark';
import {ref, shallowRef} from 'vue';
export default {

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

      swaggerData: [],
      apiForm: {},
      apiFormRules: [],
    };
  },
  mounted() {
    this.sent();
  },
  components: {
    Codemirror,
  },
  setup() {
    const code = ref(`console.log('Hello, world!')`);
    const extensions = [json(), oneDark];
    const view = shallowRef();
    const handleReady = (payload) => {
      view.value = payload.view;
    };
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
      const {data: res} = await this.$http.post('/swagger/query', this.paraForm);
      console.log(res);
      if (res.code !== 200) return this.$message.error(res.msg);
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
      this.swaggerData = res.data;
      console.log(this.swaggerData);
    },
    sendJson(id,method,inputParam,outputParam,apiUrl,serverUrl,inputParamDec) {
      // 利用传递的ID进行处理，比如构建请求数据
      const requestData = {
        id: id,
        methods: method,
        inputParam: inputParam,
        inputParamDec: inputParamDec,
        outputParam: outputParam,
        apiUrl: apiUrl,
        serverUrl: serverUrl
        // 根据你的需求构建其他请求数据
      };
      console.log(id,method,apiUrl,inputParamDec)
      if (method === 'get') {
        this.$http.get('/api/getAll', { params: requestData })
          .then(response => {
            // 处理成功回调
            console.log(response);
          })
          .catch(error => {
            // 处理错误回调
            console.error(error);
          });
      } else if (method === 'post') {
        this.$http.post('/api/sendPostRequest', requestData)
          .then(response => {
            // 处理成功回调
            console.log(response);
          })
          .catch(error => {
            // 处理错误回调
            console.error(error);
          });
      } else {
        console.error('Invalid HTTP method:', method);
      }
    },
  },
};
</script>
<style lang="less" scoped>
.el-card {
  margin: 15px;
}
</style>

