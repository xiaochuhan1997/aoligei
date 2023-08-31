<template>
  <!-- 面包导航区 -->
  <el-breadcrumb separator-icon="ArrowRight">
    <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>控制台</el-breadcrumb-item>
    <el-breadcrumb-item>swagger解析</el-breadcrumb-item>
  </el-breadcrumb>

  <el-card>
    <el-form :model="paraForm" ref="paraFormRef" label-width="150px" :inline="true">
      <!-- url -->
      <el-form-item style='width: 60%' label="swagger API 地址">
        <el-input v-model="paraForm.url" placeholder="请输入API 地址(http(s)://)">
          <template #append>
            <el-button type="primary" icon="Promotion" @click="sent" />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item style='width: 60%' label="登录的Cookie">
        <el-input v-model="paraForm.cookie" placeholder="请参考 'Cookie'：'JSESSIONID=***************'">
        </el-input>
      </el-form-item>
    </el-form>
  </el-card>
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
    </el-table>
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
      const { data: res } = await this.$http.post('/swagger', this.paraForm);
      console.log(res);
      if (res.code !== 200) return this.$message.error(res.msg);
      this.$message.success('查询成功');
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
  },
};
</script>
<style lang="less" scoped>
.el-card {
  margin: 15px;
}
</style>
