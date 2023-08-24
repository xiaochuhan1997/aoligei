<template>
  <div class="common-layout home-container">
    <el-container class="home-container">
      <el-header>
        <div><img src="">
          <span>接口管理平台</span>
        </div>
        <el-button type="info" @click="logout">退出</el-button>
      </el-header>
      <el-container>
        <el-aside :width="isCollapse ? '64px':'200px'">
          <!-- 侧边栏菜单 -->
          <div class="toggle-button" @click="toggleCollapse">|||</div>
          <el-menu active-text-color="#409EFF" background-color="#333744"
            class="el-menu-vertical-demo" :default-active="activePath" text-color="#fff"
            :unique-opened='true' :collapse="isCollapse" :collapse-transition="false" router>
            <el-sub-menu index="0">
              <!-- 一级模板 -->
              <template #title>
                <el-icon>
                  <Menu />
                </el-icon>
                <span>操作中心</span>
              </template>
              <el-menu-item index="/analyze" @click="saveNavState('/analyze')">
                <template #title>
                  <el-icon>
                    <Menu />
                  </el-icon>
                  <span>swagger解析</span>
                </template>
              </el-menu-item>
              <el-menu-item index="/view" @click="saveNavState('/view')">
                <template #title>
                  <el-icon>
                    <Menu />
                  </el-icon>
                  <span>接口管理</span>
                </template>
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
          <!-- </el-col> -->
        </el-aside>
        <el-main>
          <router-view></router-view>

        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
export default {
  data() {
    return {
      isCollapse: false,
      activePath: '',
    };
  },
  created() {
    this.activePath = window.sessionStorage.getItem('activePath');
  },
  methods: {
    async logout() {
      const { data: res } = await this.$http.post('/user/logout');
      localStorage.removeItem('userInfo');
      this.$router.push('/login');
    },
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
    },
    saveNavState(activePath) {
      window.sessionStorage.setItem('activePath', activePath);
      this.activePath = activePath;
    },
  },
};
</script>
<style lang="less" scoped>
.el-header {
  background-color: #373d41;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  color: #fff;
  font-size: 20px;
  div {
    display: flex;
    align-items: center;
    span {
      margin-left: 15px;
    }
  }
}
.el-button {
  margin: auto 0;
}
.el-aside {
  background-color: #333744;
}
.el-main {
  background-color: #eaedf1;
}
.home-container {
  height: 100%;
}
.iconfont {
  margin-right: 10px;
}
.toggle-button {
  background-color: #4a5064;
  font-size: 10px;
  line-height: 24px;
  color: #fff;
  text-align: center;
  letter-spacing: 0.2rem;
  cursor: pointer;
}
</style>