<template>
  <div class="ucen-menubar menu">
    <div class="sider">
      <el-menu class="el-menu-vertical-demo siderBar" background-color="#2A2E3C" text-color="#fff">
        <el-submenu :index="''+index" v-for="(item,index) in menus" :key="index"
          :class="item.class">
          <template v-slot:title>
            <span class="titleIcon"></span>
            <span>{{item.menuName}}</span>
          </template>
          <el-menu-item :index="index+'-'+num" v-for="(data,num) in item.data" :key="num"
            @click="addTab(data)">
            <span :class="data.class"></span>
            <span>{{data.name}}</span>
          </el-menu-item>
        </el-submenu>
      </el-menu>
    </div>
    <div class="content">
      <el-tabs v-model="TabsValue" type="card" closable @tab-remove="removeTab">
        <el-tab-pane v-for="item in Tabs" :key="item.name" :label="item.title" :name="item.name">
          <tab-component :is="item.content" :name="item.name"></tab-component>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
import Vue from 'vue';
// import Bus from 'bus.js';
export default {
  name: 'UcenMenubar',
  props: ['gradfrom', 'menus'],
  // components: { appMenu },
  data() {
    return {
      show: false,
      TabsValue: '',
      value: '',
      input: '',
      Tabs: [],
    };
  },
  created() {
    let that = this;
    // Bus.$on('openNewTab', function (name) {
    //   that.openTab(name);
    // });
    //   Bus.$on('NewTab', function (name) {
    //     that.openTab(name);
    //   });
    // Bus.$on('NewManyTab', function (name) {
    //   that.openTabMany(name);
    // });
  },
  beforeDestroy() {
    // Bus.$off('openNewTab', name);
    // Bus.$off('NewTab', name);
    // Bus.$off('NewManyTab', name);
  },
  methods: {
    // 打开多个页面
    openTabMany(name) {
      for (var m = 0; m < this.menus.length; m++) {
        for (var n = 0; n < this.menus[m].data.length; n++) {
          var ser = this.menus[m].data;
          if (ser[n].name == name.name) {
            this.addTabMany(ser[n], name.tabname);
          }
        }
      }
    },
    addTabMany(menu, tabname) {
      var exist = false;
      for (var i = 0; i < this.Tabs.length; i++) {
        if (tabname == this.Tabs[i].title) {
          exist = true;
          break;
        }
      }
      if (exist == true) {
        this.TabsValue = tabname;
        return;
      }
      this.Tabs.push({
        title: tabname,
        name: tabname,
        showItem: menu.showItem,
        content: menu.component,
      });
      this.TabsValue = tabname;
    },
    // 跳转页面
    openTab(name) {
      for (var m = 0; m < this.menus.length; m++) {
        for (var n = 0; n < this.menus[m].data.length; n++) {
          var ser = this.menus[m].data;
          if (ser[n].name == name) {
            this.addTab(ser[n]);
          }
        }
      }
    },
    // 菜单打开页面
    addTab(menu) {
      var exist = false;
      for (var i = 0; i < this.Tabs.length; i++) {
        if (menu.name == this.Tabs[i].name) {
          exist = true;
          break;
        }
      }
      if (exist == true) {
        this.TabsValue = menu.name;
        return;
      }
      this.Tabs.push({
        title: menu.name,
        name: menu.name,
        showItem: menu.showItem,
        content: menu.component,
      });
      this.TabsValue = menu.name;
    },
    removeTab(targetName) {
      let tabs = this.Tabs;
      let activeName = this.TabsValue;
      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.name;
            }
          }
        });
      }

      this.TabsValue = activeName;
      this.Tabs = tabs.filter((tab) => tab.name !== targetName);
    },
  },
};
</script>
