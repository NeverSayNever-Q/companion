<template>
  <div :style="{ display: 'flex', minHeight: '100vh', height: '100vh' }">
    <a-dropdown :trigger="['contextmenu']">
      <transition name="notebook-drawer">
        <!-- 笔记目录树-->
        <div
          class="drawer category"
          v-show="showDrawer"
          @contextmenu="categoryContextMenuHandler"
        >
          <a-tree
            class="draggable-tree"
            draggable
            :tree-data="gData"
            @drop="onDrop"
            @select="onSelect"
          >
            <template v-slot:custom="item">
              <span v-if="item.isEdit">
                <input
                  type="text"
                  v-model="item.title"
                  style="height: 20px;"
                  @blur="editCategoryNameHandler(item.key, item.title, true)"
                />
                &nbsp;<span @click="editCategoryNameHandler(item.key, item.title, true)">
                  <CheckCircleTwoTone />
                </span>
                &nbsp;<span @click="editCategoryNameHandler(item.key, item.title, false)">
                  <CloseCircleTwoTone />
                </span>
              </span>
              <span v-else>
                <img
                  v-if="item.isLeaf"
                  style="width:18px;margin-right:5px"
                  src="@/assets/img/note.svg"
                />
                <span
                  :id="item.key"
                  class="node-title"
                >{{ item.title }} </span>
              </span>
            </template>
          </a-tree>
          <div class="bottomButtonWrapper">
            <a-button
              action="newNoteBook"
              @click="categoryContextItemClickHandler"
              style="width: 100%; border-width: 1px 0 0 0"
            >
              <template #icon>
                <DiffTwoTone />
              </template>
              <span class="bottomButtonText">新建笔记本</span>
            </a-button>
          </div>

        </div>
      </transition>
      <template #overlay>
        <a-menu>
          <a-menu-item
            v-for="(item, index) in categoryContextMenuItems"
            :key="index"
            @click="categoryContextItemClickHandler"
            :categoryid="item.categoryid"
            :action="item.action"
          >
            <component :is="$antIcons[item.icon]" />
            {{item.name}}
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
    <a-dropdown :trigger="['contextmenu']">
      <transition name="note-drawer">
        <div
          class="drawer"
          v-show="showDrawer"
          @contextmenu="noteContextMenuHandler"
        >
          <draggable
            :list="lData"
            item-key="id"
            class="list-group"
            ghost-class="ghost"
            @start="dragging = true"
            @end="dragging = false"
            @move="noteMoveHandler"
          >
            <template #item="{ element }">
              <div
                :id="element.id"
                @click="selectItemHandler"
                @blur="noteTitleBlurAndEnterHandler"
                @keydown.enter.prevent="noteTitleBlurAndEnterHandler"
                class="list-group-item"
                :class="{ 'not-draggable': false }"
              >
                {{ element.title }}
              </div>
            </template>
          </draggable>
          <div class="bottomButtonWrapper">
            <a-button
              action="new"
              @click="noteContextItemClickHandler"
              style="width: 100%; border-width: 1px 0 0 0"
            >
              <template #icon>
                <DiffTwoTone />
              </template>
              <span class="bottomButtonText">新建笔记</span>
            </a-button>
          </div>
        </div>
      </transition>
      <template #overlay>
        <a-menu>
          <a-menu-item
            v-for="(item, index) in noteContextMenuItems"
            :key="index"
            @click="noteContextItemClickHandler"
            :noteid="item.noteid"
            :action="item.action"
          >
            <component :is="$antIcons[item.icon]" />
            {{item.name}}
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
    <div
      id="ckeditor"
      :style="{backgroundColor: 'rgb(255,255,255)', width: '100%', minHeight: '500px'}"
    >
      <CKEditor
        :note-id="noteid"
        :title="noteTitle"
        :content="editorContent"
        :create-time="noteCreateTime"
        @saveClick="saveHandler"
        @editTitle="editTitleHandler"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, onUnmounted } from "vue";
import { useStore } from "vuex";
import CKEditor from "@/components/CKEditor.vue";
import categoryTree from "@/composables/note/CategoryTree";
import noteList from "@/composables/note/NoteList";
import editor from "@/composables/note/Editor";
import draggable from "vuedraggable";

export default defineComponent({
  components: {
    CKEditor,
    draggable,
  },

  setup() {
    const store = useStore();
    const {
      gData,
      onDrop,
      lData,
      selectedNoteBookID,
      onSelect,
      categoryContextMenuItems,
      categoryContextMenuHandler,
      categoryContextItemClickHandler,
      editCategoryNameHandler,
    } = categoryTree();
    const {
      noteid,
      editorContent,
      noteTitle,
      noteCreateTime,
      selectItemHandler,
      noteContextMenuItems,
      noteContextMenuHandler,
      noteContextItemClickHandler,
      noteTitleBlurAndEnterHandler,
      noteMoveHandler,
      editTitleHandler,
    } = noteList(lData, selectedNoteBookID);
    const { saveHandler } = editor();

    //组件卸载后关闭drawer
    onUnmounted(() => {
      store.commit("showNoteDrawer", false);
    });

    return {
      gData, //目录源数据
      lData, //笔记本源数据
      onDrop,
      onSelect,
      //笔记
      noteid,
      noteTitle,
      noteCreateTime,
      editorContent,
      saveHandler,
      selectItemHandler,
      noteContextMenuItems,
      noteContextMenuHandler,
      noteContextItemClickHandler,
      noteTitleBlurAndEnterHandler,
      noteMoveHandler,
      editTitleHandler,
      //目录
      categoryContextMenuItems,
      categoryContextMenuHandler,
      categoryContextItemClickHandler,
      editCategoryNameHandler,
      showDrawer: computed(() => {
        return store.state.noteDrawerVisible;
      }),
    };
  },
});
</script>

<style scoped>
.drawer {
  width: 250px;
  min-width: 250px;
  border: 1px solid lightgrey;
  background-color: white;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.note-drawer-enter-active,
.note-drawer-leave-active,
.notebook-drawer-enter-active,
.notebook-drawer-leave-active {
  transition: all 0.5s ease-in;
}

.note-drawer-enter-from,
.note-drawer-leave-to,
.notebook-drawer-enter-from,
.notebook-drawer-leave-to {
  opacity: 0;
  width: 0;
  min-width: 0;
}

.ghost {
  opacity: 0.5;
  background: #c8ebfb;
}

.not-draggable {
  cursor: no-drop;
}

.list-group-item {
  margin: 10px auto;
  padding: 5px 0px 5px 20px;
  height: 30px;
  min-height: 30px;
}

.list-group-item:hover {
  cursor: pointer;
}

.itemSelected {
  background-color: rgb(186, 231, 255);
}
.bottomButton {
  width: 100%;
}
.bottomButtonText {
  color: rgb(80, 152, 255);
  letter-spacing: 5px;
}
</style>
