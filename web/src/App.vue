<template>
  <a-layout>
    <a-layout-sider
            breakpoint="lg"
            collapsed-width="0"
            width="60px"
    >
      <div class="logo" />
      <a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="inline">
        <a-menu-item key="1">
          <home-outlined />
        </a-menu-item>
        <a-menu-item key="2" @click="showDrawer">
          <read-outlined />
        </a-menu-item>
        <a-menu-item key="3">
          <search-outlined />
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header :style="{ backgroundColor: 'Ivory', padding: 0 }" />
      <a-layout-content >
          <div :style="{ display: 'flex', minHeight: '500px' }">
            <transition name="notebook-drawer">
              <div class="drawer" v-show="visible" :style="{ backgroundColor: 'grey', borderRight: '-1px'}"></div>
            </transition>
            <transition name="note-drawer">
              <div class="drawer" v-show="visible" :style="{ backgroundColor: 'rgb(239,228,176)'}"></div>
            </transition>
            <div id="editor" :style="{backgroundColor: 'rgb(112,146,190)', width: '100%', minHeight: '500px' }"></div>
          </div>
      </a-layout-content>
      <a-layout-footer style="text-align: center">
        Ant Design ©2018 Created by Ant UED
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';

  export default defineComponent({
    setup() {
      const onCollapse = (collapsed: boolean, type: string) => {
        console.log(collapsed, type);
      };

      const onBreakpoint = (broken: boolean) => {
        console.log(broken);
      };

      // 抽屉
      let visible = ref<boolean>(false);
      const showDrawer = () => {
        visible.value = !visible.value;
      }

      return {
        selectedKeys: ref<string[]>(['4']),
        onCollapse,
        onBreakpoint,
        showDrawer,
        visible
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

  .drawer{
    width: 180px;
    border:1px solid #000
  }

  .note-drawer-enter-active,
  .note-drawer-leave-active,
  .notebook-drawer-enter-active,
  .notebook-drawer-leave-active {
    transition: all 0.5s ease-in-out;
  }

  .note-drawer-enter-from,
  .note-drawer-leave-to,
  .notebook-drawer-enter-from,
  .notebook-drawer-leave-to{
    width: 0;
  }
</style>
