<template>
  <!--   面包导航区-->
  <el-breadcrumb separator-icon="ArrowRight">
    <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>控制台</el-breadcrumb-item>
    <el-breadcrumb-item>接口管理</el-breadcrumb-item>
  </el-breadcrumb>
<!--  <el-card>-->
<!--    <el-form :model="paraForm" ref="paraFormRef" label-width="150px" :inline="true">-->
<!--      &lt;!&ndash; url &ndash;&gt;-->
<!--&lt;!&ndash;      <el-form-item style='width: 60%' label="swagger API 地址">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input v-model="paraForm.url" placeholder="请输入API 地址(http(s)://)">&ndash;&gt;-->
<!--&lt;!&ndash;          <template #append>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-button type="primary" icon="Promotion" @click="sent" />&ndash;&gt;-->
<!--&lt;!&ndash;          </template>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-input>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-form-item style='width: 60%' label="登录的Cookie">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-input v-model="paraForm.cookie" placeholder="请参考 'Cookie'：'JSESSIONID=***************'">&ndash;&gt;-->
<!--&lt;!&ndash;        </el-input>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-form-item>&ndash;&gt;-->
<!--      <el-form-item style='width: 60%' label="发送请求">-->

<!--        <el-button>发送到ATP</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->
<!--  </el-card>-->
  <el-card>
    <el-button>导入ATP</el-button>
    <el-table :data="myData" :style="{ width: '100%'}" :row-key="(row)=>{return row.id;}">
      <el-table-column type="expand">
        <template #default="props">
          <div m="4">
            <el-form :inline="true" ref="apiFormRef" :model="apiForm" :rules="apiFormRules"
                     label-width="120px" status-icon :style="{ marginLeft: '20px'}">
<!--              <el-form-item label="案例号" prop="caseNum">-->
<!--                <el-input v-model="apiForm.caseNum"/>-->
<!--              </el-form-item>-->
<!--              <el-form-item label="案例描述" prop="caseName">-->
<!--                <el-input v-model="apiForm.caseName"/>-->
<!--              </el-form-item>-->
<!--              <el-form-item>-->
<!--&lt;!&ndash;                <el-button type="">测试连通性</el-button>&ndash;&gt;-->
<!--                <el-button type="">保存案例</el-button>-->
<!--                <el-button @click="sendJson(props.row.id)">发送JSON请求</el-button>-->
<!--              </el-form-item>-->
              <el-form-item>
                <codemirror v-model=props.row.inputParam placeholder="Code goes here..."
                            :style="{ height: '300px',width: '1300px'}" :autofocus="true"
                            :indent-with-tab="true" :tab-size="2" :extensions="extensions"
                            @ready="handleReady" @blur="formatJson(props.row)"/>
              </el-form-item>
              <el-form-item>
                <codemirror v-model=props.row.outputParam placeholder="Code goes here..."
                            :style="{ height: '300px',width: '1300px'}" :autofocus="true"
                            :indent-with-tab="true" :tab-size="2" :extensions="extensions"
                            @ready="handleReady" @blur="formatJson(props.row)"/>
              </el-form-item>
            </el-form>
          </div>
        </template>
      </el-table-column>

      <el-table-column v-if="myData.length > 0" type="selection" width="40px"></el-table-column>
      <el-table-column label="案例号" prop="id" width="360px"/>
      <el-table-column label="案例描述" prop="describe" width="370px"/>
      <el-table-column label="接口地址" prop="apiUrl" width="400px"/>
      <el-table-column label="请求方式" prop="method" width="90px"/>
<!--      <el-table-column label="接口描述" prop="summary"/>-->
<!--      <el-table-column label="接口所属分组" prop="tag"/>-->

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
import data from '../1.json';
import {defineComponent, ref, shallowRef} from 'vue';
import {Codemirror} from 'vue-codemirror';
import {json} from '@codemirror/lang-json';
import {oneDark} from '@codemirror/theme-one-dark';

export default {

  data() {
    return {

      code1: '11111111',
      paraForm: {
        url: '1111',
      },
      tableData: [],
      myData: [{ id: 'TianGong-FSPP-ZHTGONGXXFSPP0013-000A-0001', describe: '统计单个流水线执行成功率', apiUrl: '/dataCountInfo/getSinglePlsExecuteSuccessRate', method: 'POST', summary: '描述1' ,
        inputParam: '{\n' +
          '\t"idSys": "7",\n' +
          '\t"idPr": "8a5b041896e7463d8d2cc735d3dde333",\n' +
          '\t"timeStart": "2023-05-05",\n' +
          '\t"timeEnd": "2023-06-04"\n' +
          '}',
        outputParam: '{\n' +
          '\t"success": true,\n' +
          '\t"code": "200",\n' +
          '\t"message": null,\n' +
          '\t"detail": null,\n' +
          '\t"data": {\n' +
          '\t\t"execFailedNum": 0,\n' +
          '\t\t"sucessRate": 0.0,\n' +
          '\t\t"buildSucessNum": 0,\n' +
          '\t\t"gitSucessNum": 0,\n' +
          '\t\t"buildFailedNum": 0,\n' +
          '\t\t"buildSucessRate": 0.0,\n' +
          '\t\t"execSucessNum": 0,\n' +
          '\t\t"result": {},\n' +
          '\t\t"gitPullSucessRate": 0.0,\n' +
          '\t\t"deployFailedNum": 0,\n' +
          '\t\t"gitFailedNum": 0,\n' +
          '\t\t"deploySucessRate": 0.0,\n' +
          '\t\t"deploySucessNum": 0,\n' +
          '\t\t"execSucessRate": 0.0\n' +
          '\t}\n' +
          '}' },
        { id: 'TianGong-FSPP-ZHTGONGXXFSPP0020-000A-0001', describe: '工作负载管理--查询所有', apiUrl: '/workload/getAll', method: 'POST', summary: '描述1' ,
          inputParam: '{\n' +
            '\t"data": {}\n' +
            '}',
          outputParam: '{\n' +
            '\t"success": true,\n' +
            '\t"code": "200",\n' +
            '\t"message": null,\n' +
            '\t"detail": null,\n' +
            '\t"data": [\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "0e8222a7d0cc4b8bbbdd8b6bd0367a0b",\n' +
            '\t\t\t"nameWorkLoad": "liping345",\n' +
            '\t\t\t"typeWorkLoad": "1",\n' +
            '\t\t\t"cpu": 4.0,\n' +
            '\t\t\t"memory": 8.0,\n' +
            '\t\t\t"disk": 1,\n' +
            '\t\t\t"customeValue": "1"\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "1b00a08678984cec9302a8756756ce92",\n' +
            '\t\t\t"nameWorkLoad": "liping1999",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 10.0,\n' +
            '\t\t\t"memory": 20.0,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": "1"\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "1fee5e0dbeec447c89ba1dca934248bd",\n' +
            '\t\t\t"nameWorkLoad": "0.20.2",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 0.2,\n' +
            '\t\t\t"memory": 0.2,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "2",\n' +
            '\t\t\t"nameWorkLoad": "2c2g",\n' +
            '\t\t\t"typeWorkLoad": "2",\n' +
            '\t\t\t"cpu": 2.0,\n' +
            '\t\t\t"memory": 2.0,\n' +
            '\t\t\t"disk": 2,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "24",\n' +
            '\t\t\t"nameWorkLoad": "2-4",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 2.0,\n' +
            '\t\t\t"memory": 4.0,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "2cc0d8f21d8c4057b1f7e4c16b368a43",\n' +
            '\t\t\t"nameWorkLoad": "0-1",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 0.001,\n' +
            '\t\t\t"memory": 1.0,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "3",\n' +
            '\t\t\t"nameWorkLoad": "3c3g",\n' +
            '\t\t\t"typeWorkLoad": "3",\n' +
            '\t\t\t"cpu": 3.0,\n' +
            '\t\t\t"memory": 3.0,\n' +
            '\t\t\t"disk": 3,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "3592b03db71f4a3f9d63d5589a762ab4",\n' +
            '\t\t\t"nameWorkLoad": "1.51.5",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 1.5,\n' +
            '\t\t\t"memory": 1.5,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "4",\n' +
            '\t\t\t"nameWorkLoad": "4-4",\n' +
            '\t\t\t"typeWorkLoad": "4",\n' +
            '\t\t\t"cpu": 4.0,\n' +
            '\t\t\t"memory": 4.0,\n' +
            '\t\t\t"disk": 4,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "56974a90e3024ddda8d5c4cbf8975e16",\n' +
            '\t\t\t"nameWorkLoad": "8-16",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 8.0,\n' +
            '\t\t\t"memory": 16.0,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "60103a977a5b4eecbc48a5557a98cdd5",\n' +
            '\t\t\t"nameWorkLoad": "1-2",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 1.0,\n' +
            '\t\t\t"memory": 2.0,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "6fc058ce261640218af3ae52e6c7f721",\n' +
            '\t\t\t"nameWorkLoad": "eslint-1-0.3",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 1.0,\n' +
            '\t\t\t"memory": 0.3,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "8b3f591d52714ed9a592aa6220f33635",\n' +
            '\t\t\t"nameWorkLoad": "0505",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 0.5,\n' +
            '\t\t\t"memory": 0.5,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "8df193bcf1184d0abd0fdaa23e8eece0",\n' +
            '\t\t\t"nameWorkLoad": "npm-6-6",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 6.0,\n' +
            '\t\t\t"memory": 6.0,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "98a103c3f7f0402e802cbb93b470534a",\n' +
            '\t\t\t"nameWorkLoad": "11",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 1.0,\n' +
            '\t\t\t"memory": 1.0,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "a157fe4986ba4c8e8f1d224ceb4aa866",\n' +
            '\t\t\t"nameWorkLoad": "0-1-1",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 0.1,\n' +
            '\t\t\t"memory": 1.0,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "af04b857a63b484bbda9efb897fec774",\n' +
            '\t\t\t"nameWorkLoad": "0.3c0.3g",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 0.3,\n' +
            '\t\t\t"memory": 0.3,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "bc2a590778b64c1986a3cef72805c87f",\n' +
            '\t\t\t"nameWorkLoad": "1-2.5",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 1.0,\n' +
            '\t\t\t"memory": 2.5,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "c26f8287e29b4a1d942ab038934b2e61",\n' +
            '\t\t\t"nameWorkLoad": "1.034-2.063",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 1.034,\n' +
            '\t\t\t"memory": 2.063,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "d122e06c2e824bb8bbffce64058b5450",\n' +
            '\t\t\t"nameWorkLoad": "0.1c0.1g",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 0.1,\n' +
            '\t\t\t"memory": 0.1,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "d1e2b50c8da3424e81396e313bc81f6c",\n' +
            '\t\t\t"nameWorkLoad": "新流水线",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 2.0,\n' +
            '\t\t\t"memory": 0.001,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "daf561915381427e8ca398e1099e7ba5",\n' +
            '\t\t\t"nameWorkLoad": "1-1",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 1.0,\n' +
            '\t\t\t"memory": 1.0,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "default",\n' +
            '\t\t\t"nameWorkLoad": "1(4c8g)",\n' +
            '\t\t\t"typeWorkLoad": "1",\n' +
            '\t\t\t"cpu": 4.0,\n' +
            '\t\t\t"memory": 8.0,\n' +
            '\t\t\t"disk": 1,\n' +
            '\t\t\t"customeValue": "1"\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "e91bad57d2f440b0986a6b80f7541550",\n' +
            '\t\t\t"nameWorkLoad": "nameWorkLoad",\n' +
            '\t\t\t"typeWorkLoad": "1",\n' +
            '\t\t\t"cpu": 1.0,\n' +
            '\t\t\t"memory": 1.0,\n' +
            '\t\t\t"disk": 1,\n' +
            '\t\t\t"customeValue": "11"\n' +
            '\t\t},\n' +
            '\t\t{\n' +
            '\t\t\t"idWorkLoad": "f07b614d8b394d9e9d638bf6f7c433c9",\n' +
            '\t\t\t"nameWorkLoad": "11-11",\n' +
            '\t\t\t"typeWorkLoad": "0",\n' +
            '\t\t\t"cpu": 11.0,\n' +
            '\t\t\t"memory": 11.0,\n' +
            '\t\t\t"disk": 0,\n' +
            '\t\t\t"customeValue": null\n' +
            '\t\t}\n' +
            '\t]\n' +
            '}' },
        { id: 'TianGong-FSPP-ZHTGONGXXFSPP0035-000A-0001', describe: '发布插件版本', apiUrl: '/pluginversion/publish', method: 'POST', summary: '描述1' ,
          inputParam: '{\n' +
            '\t"idPluginVersion": "1fdf8d2aef404ca9a4e2912e031d8991"\n' +
            '}',
          outputParam: '{\n' +
            '\t"success": true,\n' +
            '\t"code": "200",\n' +
            '\t"message": null,\n' +
            '\t"detail": null,\n' +
            '\t"data": "ok"\n' +
            '}' },
        { id: 'TianGong-FSPP-ZHTGONGXXFSPP0044-000A-0001', describe: '根据插件版本id更新插件状态为已下线', apiUrl: '/pluginversion/updatePluginVersionStateByKey', method: 'POST', summary: '描述1' ,
          inputParam: '{\n' +
            '\t"idPluginVersion": "4057132e1a794be9a2a9335c1c985b3b"\n' +
            '}',
          outputParam: '{\n' +
            '\t"success": true,\n' +
            '\t"code": "200",\n' +
            '\t"message": null,\n' +
            '\t"detail": null,\n' +
            '\t"data": "ok"\n' +
            '}' },
        { id: 'TianGong-FSPP-ZHTGONGXXFSPP0045-000A-0001', describe: '更新tg_pls_plg表中的ID_PLUGIN_VERSION字段为新版本', apiUrl: '/plugin/updateIdPluginVersionByKey', method: 'POST', summary: '描述1' ,
          inputParam: '{\n' +
            '\t"idPlugin": "b0d0ac6a3421479684b5df667e34e603",\n' +
            '\t"idPluginVersion": "4057132e1a794be9a2a9335c1c985b3b"\n' +
            '}',
          outputParam: '{\n' +
            '\t"success": true,\n' +
            '\t"code": "200",\n' +
            '\t"message": null,\n' +
            '\t"detail": null,\n' +
            '\t"data": "ok"\n' +
            '}' },
      ],
      showTooltip: false,
      tooltipContent: "",
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
    sendJson(id) {
      // 利用传递的ID进行处理，比如构建请求数据
      const requestData = {
        id: id,
        // 根据你的需求构建其他请求数据
      };
      this.$http.post('/api/sendJson', {id})
        .then(response => {
          // 处理成功回调
          console.log(response);
        })
        .catch(error => {
          // 处理错误回调
          console.error(error);
        });
    },
    showFullContent(row) {
      this.tooltipContent = row.describe;
      this.showTooltip = true;
    },

  },
};
</script>
<style lang="less" scoped>


.el-card {
  margin: 15px;
}
</style>

