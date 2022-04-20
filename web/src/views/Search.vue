<template>
  <div :style="{ display: 'flex', minHeight: '100vh' }">
    <transition name="search-drawer">
      <div
        class="drawer"
        v-show="showDrawer"
      >
        <a-input-search
          v-model:value="searchStr"
          placeholder="请输入关键字搜索"
          style="width: 480px; margin: 10px 10px"
          @search="onSearch"
        />
        <a-list
          item-layout="horizontal"
          :data-source="searchBackList"
          class="searchList"
        >
          <template #renderItem="{ item }">
            <a-list-item @click="getNoteContent(item.id)">
              <a-list-item-meta>
                <template #title>
                  <span class="searchBackTitle">{{ item.title }}</span>
                  <a-breadcrumb separator=">">
                    <a-breadcrumb-item
                      v-for="(name, index) in item.route"
                      :key="index"
                    >{{name}}</a-breadcrumb-item>
                  </a-breadcrumb>
                </template>
                <template #avatar>
                  <img src="@/assets/img/note.svg" />
                </template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </div>
    </transition>
    <div
      id="ckeditor"
      :style="{backgroundColor: 'rgb(255,255,255)', width: '100%', minHeight: '100vh' }"
    >
      <CKEditor
        :note-id="noteid"
        :title="noteTitle"
        :content="editorContent"
        :create-time="noteCreateTime"
        @saveClick="saveHandler"
      />
    </div>
  </div>

</template>

<script lang="ts">
import {
  ref,
  defineComponent,
  computed,
  onUnmounted,
  watch,
  nextTick,
} from "vue";
import { useStore } from "vuex";
import CKEditor from "@/components/CKEditor.vue";
import editor from "@/composables/note/Editor";
import axios from "axios";
import noteList from "@/composables/note/NoteList";

export default defineComponent({
  components: {
    CKEditor,
  },

  setup() {
    const store = useStore();
    const searchStr = ref<string>("");
    const searchBackList = ref();
    const { saveHandler } = editor();
    const { noteid, editorContent, noteTitle, noteCreateTime, getNoteContent } =
      noteList();

    //根据关键字查询
    const onSearch = async (search: string) => {
      try {
        const resp = await axios.get(`/search/${search}`);
        const data = resp.data;
        if (!data.success) {
          console.error("查询失败");
        } else {
          //取笔记的路径
          for (let item of data.content) {
            try {
              const resp = await axios.get(`/note/category/${item.id}`);
              const data = resp.data;
              if (!data.success) {
                console.error("获取笔记路径失败");
              } else {
                item.route = data.content;
              }
            } catch (e) {
              console.error(e);
            }
          }
          searchBackList.value = data.content;
        }
      } catch (e) {
        console.error(e);
      }
    };

    //检索词设置高亮
    const highLight = () => {
      const titleList = document.getElementsByClassName("searchBackTitle");
      [].forEach.call(titleList, (element: Element) => {
        element.innerHTML = element.innerHTML.replace(
          searchStr.value,
          `<mark>${searchStr.value}</mark>`
        );
      });
    };

    watch(searchBackList, async () => {
      await nextTick();
      //1、若标题命中检索词，高亮检索词
      const titleList = document.getElementsByClassName("searchBackTitle");
      [].forEach.call(titleList, (element: Element) => {
        element.innerHTML = element.innerHTML.replace(
          searchStr.value,
          `<mark>${searchStr.value}</mark>`
        );
      });
    });

    //组件卸载后关闭drawer
    onUnmounted(() => {
      store.commit("showSearchDrawer", false);
    });

    return {
      //检索
      onSearch,
      searchStr,
      searchBackList,
      //笔记
      noteid,
      editorContent,
      noteTitle,
      noteCreateTime,
      getNoteContent,
      saveHandler,
      showDrawer: computed(() => {
        return store.state.searchDrawerVisible;
      }),
    };
  },
});
</script>

<style>
.drawer {
  width: 500px;
  min-width: 500px;
  border: 1px solid lightgrey;
  background-color: white;
}

.search-drawer-enter-active,
.search-drawer-leave-active {
  transition: all 0.5s ease-in;
}

.search-drawer-enter-from,
.search-drawer-leave-to {
  opacity: 0;
  width: 0;
  min-width: 0;
}
.searchBackTitle {
  margin-top: 10px;
  font-weight: bold;
}
.searchList {
  cursor: pointer;
}
</style>
