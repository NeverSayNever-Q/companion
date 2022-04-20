import { nextTick, ref } from "vue";
import axios from "axios";
import { Tool } from "@/utils/tools";
import { message } from "ant-design-vue";

export default function (lData?: any, selecedtNoteBookID?: any) {
  const editorContent = ref();
  const noteid = ref();
  const noteTitle = ref();
  const noteCreateTime = ref();
  const getNoteContent = async (id: string) => {
    try {
      if (id) {
        const resp = await axios.get(`/note/${id}`);
        const data = resp.data;
        if (!data.success) {
          console.error("获取笔记失败");
        } else {
          editorContent.value = data.content.content;
          noteid.value = id;
          noteTitle.value = data.content.title;
          noteCreateTime.value = data.content.createtime;
        }
      }
    } catch (e) {
      console.error(e);
    }
  };

  //删除笔记，并更新列表
  const delNote = async (id: string) => {
    try {
      if (id) {
        const resp = await axios.delete(`/note/${id}`);
        const data = resp.data;
        if (!data.success) {
          console.error("删除笔记失败");
        } else {
          for (let i = 0; i < lData.value.length; i++) {
            if (lData.value[i].id == id) {
              lData.value.splice(i, 1);
            }
          }
        }
      }
    } catch (e) {
      console.error(e);
    }
  };

  //更新笔记
  const updateNote = async (note: any) => {
    try {
      if (note) {
        const resp = await axios.patch("/note/", note);
        const data = resp.data;
        if (!data.success) {
          console.error("保存失败");
        }
      }
    } catch (e) {
      console.error(e);
    }
  };

  //新增笔记
  const addNote = async (noteBookID: string) => {
    try {
      if (noteBookID) {
        const resp = await axios.post("/note/", { notebookid: noteBookID });
        const data = resp.data;
        if (!data.success) {
          console.error("新建失败");
        } else {
          const newNote = data.content;
          //更新lData
          lData.value.push(newNote);
          //获取焦点
          await nextTick();
          const newElem = document.getElementById(newNote.id);
          if (newElem) {
            newElem.click();
            document.getElementById("titleInput")?.focus();
          }
        }
      }
    } catch (e) {
      console.error(e);
    }
  };

  //绑定笔记列表点击事件：1、更改样式；2、获取对应笔记的内容
  const selectItemHandler = (event: any) => {
    // 1、更改样式：取消所有列表项选中效果；为选中项添加选中效果
    const itemList = document.getElementsByClassName("list-group-item");
    [].forEach.call(itemList, (item: HTMLElement) => {
      item.classList.remove("itemSelected");
    });
    event.target.classList.add("itemSelected");

    //2、获取对应笔记的内容
    getNoteContent(event.target.getAttribute("id"));
  };

  //笔记右键菜单
  const listAreaNoteContextMenuItems = [
    {
      name: "重命名",
      icon: "EditOutlined",
      noteid: 0,
      action: "rename",
    },
    {
      name: "删除",
      icon: "DeleteOutlined",
      noteid: 0,
      action: "del",
    },
  ];
  const blankAreaNoteContextMenuItems = [
    {
      name: "新建",
      icon: "PlusOutlined",
      noteid: 0,
      action: "new",
    },
  ];

  //笔记右键点击事件
  const noteContextMenuItems = ref();
  const noteContextMenuHandler = (event: any) => {
    if (event.target.classList.contains("list-group-item")) {
      listAreaNoteContextMenuItems.forEach((item) => {
        item.noteid = event.target.getAttribute("id") ?? 0;
      });
      noteContextMenuItems.value = listAreaNoteContextMenuItems;
    } else {
      noteContextMenuItems.value = blankAreaNoteContextMenuItems;
    }
  };

  //笔记右键菜单点击事件
  const noteContextItemClickHandler = (event: any) => {
    const action = event.currentTarget.getAttribute("action") ?? "";
    const noteid = event.currentTarget.getAttribute("noteid") ?? "";
    switch (action) {
      //重命名，设置为可编辑
      case "rename": {
        document.getElementById("titleInput")?.focus();
        break;
      }
      //删除
      case "del": {
        delNote(noteid);
        break;
      }
      //新建
      case "new": {
        if (selecedtNoteBookID.value !== undefined) {
          addNote(selecedtNoteBookID.value[0]);
        } else {
          message.warn("请选择笔记本后，新建笔记");
        }
        break;
      }
    }
  };

  //列表失去焦点 || 键入enter时，取消可编辑属性，并保存标题
  const noteTitleBlurAndEnterHandler = (event: any) => {
    if (Tool.isEmpty(event.currentTarget.textContent)) {
      message.warn("请输入标题");
      return;
    }
    // 1、取消可编辑属性
    event.currentTarget.setAttribute("contenteditable", "false");
    // 2、保存标题
    const id = event.currentTarget.getAttribute("id") ?? "";
    const title = event.currentTarget.textContent.trim() ?? " ";
    const info = {
      id: id,
      title: title,
    };
    updateNote([info]);
  };

  //笔记列表拖拽事件，交换顺序
  const noteMoveHandler = (event: any) => {
    const fromElemID = event.dragged.getAttribute("id");
    const toElemID = event.related.getAttribute("id");

    let fromElemIndex = 0;
    let toElemIndex = 0;
    const noteList = Tool.copy(lData.value);
    //取拖拽元素的原先位置和目标位置
    noteList.forEach((note: any, index: number) => {
      if (note.id === fromElemID) fromElemIndex = index;
      if (note.id === toElemID) toElemIndex = index;
    });
    //更新数组中顺序，并根据数组顺序更新sort
    noteList.splice(toElemIndex, 0, ...noteList.splice(fromElemIndex, 1));
    noteList.forEach((note: any, index: number) => {
      note.sort = index + 1;
    });
    //保存排序
    updateNote(noteList);
  };

  //编辑器内修改标题时，同步修改笔记目录
  const editTitleHandler = (info: any) => {
    const targetNoteDiv = document.getElementById(info.noteId);
    if (targetNoteDiv != null) targetNoteDiv.innerText = info.title;
  };

  return {
    // 当前打开的笔记相关属性
    noteid,
    editorContent,
    noteTitle,
    noteCreateTime,
    // 事件
    getNoteContent,
    selectItemHandler,
    noteContextMenuItems,
    noteContextMenuHandler,
    noteContextItemClickHandler,
    noteTitleBlurAndEnterHandler,
    noteMoveHandler,
    editTitleHandler,
  };
}
