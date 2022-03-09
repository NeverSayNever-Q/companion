<template>
  <a-layout>
    <a-layout-sider
            breakpoint="lg"
            collapsed-width="0"
            width="60px"
    >
      <div class="logo" />
      <a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="inline">
        <a-menu-item key="home">
          <router-link to="/"><home-outlined /></router-link>
        </a-menu-item>
        <a-menu-item key="note"  @click="showDrawer">
          <router-link to="/note"><read-outlined /></router-link>
        </a-menu-item>
        <a-menu-item key="3">
          <search-outlined />
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header :style="{ backgroundColor: 'Ivory', padding: 0 }" />
      <a-layout-content ref>
        <router-view />
      </a-layout-content>
      <a-layout-footer style="text-align: center">
        Ant Design ©2018 Created by Ant UED
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { useStore } from 'vuex';

  export default defineComponent({
    setup() {
      const store = useStore();

      const onCollapse = (collapsed: boolean, type: string) => {
        console.log(collapsed, type);
      };

      const onBreakpoint = (broken: boolean) => {
        console.log(broken);
      };

      // 抽屉
      const showDrawer = () => {
        store.commit('showNoteDrawer', !store.state.noteDrawerVisible);
      }

      return {
        selectedKeys: ref<string[]>(['4']),
        onCollapse,
        onBreakpoint,
        showDrawer
      };
    },
  });
</script>
<style>
  #app .logo {
    height: 32px;
    background: rgba(255, 255, 255, 0.2);
    margin: 16px 5px 16px 5px;
  }

  .site-layout-sub-header-background {
    background: #fff;
  }

  .site-layout-background {
    background: #fff;
  }

  [data-theme='dark'] .site-layout-sub-header-background {
    background: #141414;
  }
</style>
