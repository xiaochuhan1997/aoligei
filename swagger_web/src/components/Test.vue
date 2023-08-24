<template>
  <el-button type="" @click="a">aaaaaaaaaa</el-button>
  <el-button type="" @click="test">发送</el-button>

  <codemirror v-model=props.row.outputParam placeholder="Code goes here..."
    :style="{ height: '300px',width: '1300px'}" :autofocus="true" :indent-with-tab="true"
    :tab-size="2" :extensions="extensions" @ready="handleReady" @change="log('change', $event)"
    @focus="log('focus', $event)" @blur="log('blur', $event)" />
  <codemirror v-model="req" placeholder="Code goes here..." :style="{ height: '400px' }"
    :autofocus="true" :indent-with-tab="true" :tab-size="2" :extensions="extensions" />
</template>
<script>
import { defineComponent, ref, shallowRef } from 'vue';
import { Codemirror } from 'vue-codemirror';
import { json } from '@codemirror/lang-json';
import { oneDark } from '@codemirror/theme-one-dark';

export default defineComponent({
  components: {
    Codemirror,
  },
  data() {
    return {
      req: '',
      data: {
        url: 'http://localhost:8080/test',
        method: 'post',
        headers: null,
        params: '',
      },
      code1:
        '{"$ref":{"title":"QueryActivityUrlResponse","required":["retCode","retMsg"],"type":"object","properties":{"data":{"description":"数据","$ref":{"title":"ActivityUrlDTO","type":"object","properties":{"activityId":{"type":"string"},"brief":{"type":"string"},"id":{"type":"string"},"originCode":{"type":"string"},"url":{"type":"string"}}}},"retCode":{"type":"string","description":"返回码 0:成功，1:失败，2:需要登录认证"},"retMsg":{"type":"string","description":"返回信息"}}}}',
      code2: '',
      paraForm: {
        url: '1111',
      },
      tableData: [
        {
          date: '2016-05-07',
          name: 'Tom',
          state: 'California',
          city: 'San Francisco',
          address: '3650 21st St, San Francisco',
          zip: 'CA 94114',
          family: [
            {
              name: 'Jerry',
              state: 'California',
              city: 'San Francisco',
              address: '3650 21st St, San Francisco',
              zip: 'CA 94114',
            },
            {
              name: 'Spike',
              state: 'California',
              city: 'San Francisco',
              address: '3650 21st St, San Francisco',
              zip: 'CA 94114',
            },
            {
              name: 'Tyke',
              state: 'California',
              city: 'San Francisco',
              address: '3650 21st St, San Francisco',
              zip: 'CA 94114',
            },
          ],
        },
      ],
      swaggerData: [],
    };
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

    return {
      code,
      extensions,
      handleReady,
      log: console.log,
    };
  },
  methods: {
    a() {
      this.code2 = JSON.stringify(JSON.parse(this.code2), null, '\t');
    },
    async test() {
      // this.data.params = JSON.stringify(this.data.params);
      console.log('code2' + this.code2);
      this.data.params = this.code2
        .replace(/\r\n/g, '')
        .replace(/\n/g, '')
        .replace(/\s+/g, '');
      this.data.params = JSON.parse(this.data.params);
      console.log('data' + this.data);
      const { data: res } = await this.$http.post('/api', this.data);
      console.log('res' + res);

      console.log(res.data.params);
      // this.req = res.data.params;
      this.req = JSON.stringify(JSON.parse(res.data.params), null, '\t');

      if (res.code !== 200) return this.$message.error(res.msg);
      this.$message.success('查询成功');
    },
  },
});
</script>
