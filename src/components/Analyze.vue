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
        <el-input v-model="paraForm.url" placeholder="请输入API 地址(http(s)://)">d
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
      <el-table-column type="expand">
        <template #default="props">
          <div m="4">
            <el-form :inline="true" ref="apiFormRef" :model="apiForm" :rules="apiFormRules"
                     label-width="120px" status-icon :style="{ marginLeft: '20px'}">
              <el-form-item label="案例号" prop="caseNo">
                <el-input v-model="formData.caseNo" maxlength="30"/>
              </el-form-item>
              <el-form-item label="案例描述" prop="caseDec">
                <el-input v-model="formData.caseDec" maxlength="100"/>
              </el-form-item>
              <el-form-item>
                <el-button type="" @click="saveData(props.row)">保存案例</el-button>
              </el-form-item>
              <el-form-item>
                <codemirror v-model=props.row.inputParam placeholder="Code goes here..."
                            :style="{ height: '300px',width: '1300px'}" :autofocus="true"
                            :indent-with-tab="true" :tab-size="2" :extensions="extensions"
                            @ready="handleReady" @blur="formatJson(props.row)" />
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
      <el-table-column label="接口地址" prop="apiUrl" width="400px"/>
      <el-table-column label="请求方式" prop="method"/>
      <el-table-column label="接口描述" prop="summary" />
      <el-table-column label="接口所属分组" prop="tag" />
    </el-table>
  </el-card>
</template>
<script>
import { Codemirror } from 'vue-codemirror';
import { json } from '@codemirror/lang-json';
import { oneDark } from '@codemirror/theme-one-dark';
import {ref, shallowRef} from 'vue';
export default {
  // components: {
  //   Codemirror,
  // },
  mounted() {
    // debugger;
  },
  data() {
    return {
      code1: '11111111',
      paraForm: {
        url: '',
      },formData: {
        caseNo: '',
        caseDec: '',
      },
      swaggerData: [],
      apiForm: {},
      apiFormRules: []
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
      const { data: res } = await this.$http.post('/swagger/analyze', this.paraForm);
      console.log(res);
      if (res.code !== 200) return this.$message.error(res.msg);
      this.$message.success('解析成功');
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
        res.data[i].inputParamDec = JSON.stringify(
          JSON.parse(res.data[i].inputParamDec),
          null,
          '\t'
        );
      }
      this.swaggerData = res.data;
    },
    saveData(data) {
      // 利用传递的ID进行处理，比如构建请求数据
      const requestData = {
        method: data.method,
        inputParam: data.inputParam,
        inputParamDec: data.inputParamDec,
        outputParam: data.outputParam,
        apiUrl: data.apiUrl,
        serverUrl: data.serverUrl,
        tag: data.tag,
        summary: data.summary,
        outputParamDec: data.outputParamDec,
        caseNo: this.formData.caseNo,
        caseDec: this.formData.caseDec
        // 根据你的需求构建其他请求数据
      };
      console.log(data.summary,data.inputParamDec)
      if (!this.formData.caseNo) {
        this.$message.error('案例号不能为空');
        return; // 阻止继续执行
      }
      // 判断caseDec是否为空
      if (!this.formData.caseDec) {
        this.$message.error('案例描述不能为空');
        return; // 阻止继续执行
      }
      this.$http.post('/swagger/saveData', requestData)
        .then(response => {
          // 处理成功回调
          console.log(response);
          this.$message.success('保存成功');
        })
        .catch(error => {
          // 处理错误回调
          console.error(error);
          this.$message.error('保存异常，请检查错误日志');
        });
    }
  },
};
</script>
<style lang="less" scoped>
.el-card {
  margin: 15px;
}
.hidden-column {
  display: none;
}
</style>
