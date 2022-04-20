<template>
  <a-layout>
    <a-layout-sider
      breakpoint="lg"
      collapsed-width="0"
      width="60px"
      :style="{ overflow: 'auto', height: '100vh', position: 'fixed', left: 0 }"
    >
      <div class="logo"></div>
      <a-menu
        theme="dark"
        mode="inline"
      >
        <a-menu-item
          key="note"
          @click="showNoteDrawer"
        >
          <router-link to="/">
            <read-outlined />
          </router-link>
        </a-menu-item>
        <a-menu-item
          key="search"
          @click="showSearchDrawer"
        >
          <router-link to="/search">
            <search-outlined />
          </router-link>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout :style="{ marginLeft: '60px'}">
      <a-layout-content>
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>
<script lang="ts">
import { defineComponent } from "vue";
import { useStore } from "vuex";

export default defineComponent({
  setup() {
    const store = useStore();

    const onCollapse = (collapsed: boolean, type: string) => {
      console.log(collapsed, type);
    };

    const onBreakpoint = (broken: boolean) => {
      console.log(broken);
    };

    // note抽屉
    const showNoteDrawer = () => {
      store.commit("showNoteDrawer", !store.state.noteDrawerVisible);
    };

    // search抽屉
    const showSearchDrawer = () => {
      store.commit("showSearchDrawer", !store.state.searchDrawerVisible);
    };

    return {
      onCollapse,
      onBreakpoint,
      showNoteDrawer,
      showSearchDrawer,
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

[data-theme="dark"] .site-layout-sub-header-background {
  background: #141414;
}
</style>
