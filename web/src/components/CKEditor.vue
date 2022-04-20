<template>
  <div
    class="header"
    @keydown.ctrl="saveContent"
  >
    <div class="title">
      <input
        id="titleInput"
        maxlength="10"
        @input="editTitle"
        v-model.trim="noteTitle"
      />
      <a-divider style="margin: 0" />
      <div class="noteCTime">{{noteCreateTimeCompute}}</div>
    </div>
    <div>
      <a-button @click="saveContent">
        <template #icon>
          <WalletOutlined />
        </template>
        保存
      </a-button>
      &nbsp;&nbsp;
      <a-dropdown>
        <template #overlay>
          <a-menu>
            <a-menu-item key="study">基础</a-menu-item>
            <a-menu-item key="accumulation">经验</a-menu-item>
          </a-menu>
        </template>
        <a-button style="background-color: bisque">
          <TagOutlined />
          标签
          <DownOutlined />
        </a-button>
      </a-dropdown>
    </div>

  </div>
  <div
    id="editorContainer"
    @keydown.ctrl="saveContent"
  >
    <ckeditor
      :editor="editor"
      :config="editorConfig"
      v-model="ContentRealTime"
      :model-value="editorData"
    ></ckeditor>
  </div>
</template>

<script>
import BalloonEditor from "ckeditor5-custom-build";
import CKEditor from "@ckeditor/ckeditor5-vue";
import { Tool } from "@/utils/tools";
import { ref, toRef, computed, watch } from "vue";
import { message } from "ant-design-vue";

export default {
  name: "CKEditor",
  props: {
    content: String,
    noteId: String,
    title: String,
    createTime: String,
  },
  components: {
    // 编辑器组件的局部注册方式
    ckeditor: CKEditor.component,
  },
  emits: ["saveClick", "editTitle"],
  setup(props, context) {
    const contentInit = toRef(props, "content");
    const noteid = toRef(props, "noteId");
    const noteTitleInit = toRef(props, "title");
    const noteCreateTime = toRef(props, "createTime");

    const noteTitle = ref("");

    watch(noteTitleInit, () => {
      noteTitle.value = noteTitleInit.value;
    });

    let ContentRealTime = ref("");

    const saveContent = (event) => {
      if (event.keyCode === 83 || event.currentTarget.innerText === "保存") {
        event.preventDefault();
        if (Tool.isEmpty(noteTitle.value)) {
          message.warn("标题不能为空，请输入标题");
        }
        if (!Tool.isEmpty(noteTitle.value))
          context.emit("saveClick", {
            noteid: noteid.value,
            title: noteTitle.value,
            content: ContentRealTime.value,
          });
      }
    };
    const noteCreateTimeCompute = computed(() => {
      if (noteCreateTime.value) return noteCreateTime.value.replace("T", " ");
      else return "";
    });
    const editTitle = () => {
      context.emit("editTitle", {
        noteId: noteid.value,
        title: noteTitle.value,
      });
    };
    return {
      editor: BalloonEditor,
      editorData: contentInit,
      editorConfig: {
        // The configuration of the editor.
      },
      noteCreateTimeCompute,
      saveContent,
      editTitle,
      noteTitle,
      ContentRealTime,
    };
  },
};
</script>
<style>
.ck-editor__editable {
  min-height: 600px;
  height: 600px;
  margin-left: 20px;
}

.header {
  display: flex;
  align-items: center;
  height: 80px;
}

.title {
  padding-left: 20px;
  margin: 30px 300px 0 0;
}

.title input {
  border: none;
  outline: none;
  font-size: 30px;
  width: inherit;
}
.noteCTime {
  margin-left: 30px;
  color: #a1a3a6;
  min-height: 50px;
}
</style>