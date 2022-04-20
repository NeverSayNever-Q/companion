import { onMounted, ref, watch, nextTick } from "vue";
import { DropEvent, TreeDataItem } from "ant-design-vue/es/tree/Tree";
import axios from "axios";
import { Tool } from "@/utils/tools";
import { message } from "ant-design-vue";

export default function () {
  //新建目录
  const newCategory = async (type: number) => {
    try {
      const resp = await axios.post("/category/", { type: type });
      const data = resp.data;
      if (!data.success) {
        console.error("新建目录失败");
      } else {
        const respTree = Tool.array2Tree([data.content], "0");
        respTree[0].isEdit = true;
        //更新数据源
        gData.value.push(respTree[0]);
        //获取焦点
        await nextTick();
        const newElem = document.getElementById(data.content.id);
        if (newElem) {
          newElem.click();
          newElem.focus();
        }
      }
    } catch (e) {
      console.error(e);
    }
  };

  //删除目录
  const delCategoryTree = async (categoryid: string) => {
    try {
      const resp = await axios.delete(`/category/${categoryid}`);
      const data = resp.data;
      if (!data.success) {
        console.error("删除目录失败");
      } else getCategoryTree();
    } catch (e) {
      console.error(e);
    }
  };

  //更新目录名称
  const updateCategoryTitle = async (categoryid: string, title: string) => {
    try {
      const resp = await axios.patch(`/category/${categoryid}/${title}`);
      const data = resp.data;
      if (!data.success) {
        console.error("更新目录失败");
      }
    } catch (e) {
      console.error(e);
    }
  };

  //目录更新
  const changeEditState = (arr: any, categoryid: string, title = "") => {
    arr.forEach((item: any) => {
      if (item.key === categoryid) {
        item.isEdit = !item.isEdit;
        if (Tool.isNotEmpty(title)) {
          item.title = title;
        }
      }
      if (item.children.length > 0) {
        changeEditState(item.children, categoryid);
      }
    });
  };

  //更新拖拽后的目录结构
  const updateCategoryTree = async (gData: TreeDataItem[]) => {
    try {
      const categoryTreeArray = Tool.tree2Array(gData, "0");
      const resp = await axios.patch("/category/", categoryTreeArray);
      const data = resp.data;
      if (!data.success) {
        console.error("更新目录失败");
      }
    } catch (e) {
      console.error(e);
    }
  };

  //取目录树
  const getCategoryTree = async () => {
    try {
      const resp = await axios.get("/category/");
      const data = resp.data;
      if (data.success) {
        gData.value = Tool.array2Tree(data.content, "0");
      }
    } catch (e) {
      console.error(e);
    }
  };

  //取笔记列表
  const getNoteList = async (notebookid: string) => {
    try {
      if (notebookid) {
        const resp = await axios.get(`/note/?noteBookId=${notebookid}`);
        const data = resp.data;
        if (data.success) {
          lData.value = data.content;
        }
      }
    } catch (e) {
      console.error(e);
    }
  };

  //目录拖拽事件
  const gData = ref<TreeDataItem[]>([]);
  const onDrop = (info: DropEvent) => {
    console.log("DDDDDD", info);
    const dropKey = info.node.eventKey;
    const dragKey = info.dragNode.eventKey;
    const dropPos = info.node.pos.split("-");
    const dropPosition =
      info.dropPosition - Number(dropPos[dropPos.length - 1]);
    const loop = (data: TreeDataItem[], key: string, callback: any) => {
      data.forEach((item, index, arr) => {
        if (item.key === key) {
          return callback(item, index, arr);
        }
        if (item.children) {
          return loop(item.children, key, callback);
        }
      });
    };
    const data = [...gData.value];

    // Find dragObject
    let dragObj: TreeDataItem = {};
    loop(
      data,
      dragKey,
      (item: TreeDataItem, index: number, arr: TreeDataItem[]) => {
        arr.splice(index, 1);
        dragObj = item;
      }
    );
    if (!info.dropToGap) {
      // Drop on the content
      loop(data, dropKey, (item: TreeDataItem) => {
        item.children = item.children || [];
        // where to insert 示例添加到尾部，可以是随意位置
        item.children.push(dragObj);
      });
    } else if (
      (info.node.children || []).length > 0 && // Has children
      info.node.expanded && // Is expanded
      dropPosition === 1 // On the bottom gap
    ) {
      loop(data, dropKey, (item: TreeDataItem) => {
        item.children = item.children || [];
        // where to insert 示例添加到尾部，可以是随意位置
        item.children.unshift(dragObj);
      });
    } else {
      let ar: TreeDataItem[] = [];
      let i = 0;
      loop(
        data,
        dropKey,
        (item: TreeDataItem, index: number, arr: TreeDataItem[]) => {
          ar = arr;
          i = index;
        }
      );
      if (dropPosition === -1) {
        ar.splice(i, 0, dragObj);
      } else {
        ar.splice(i + 1, 0, dragObj);
      }
    }

    //拖拽后更新排序
    updateCategoryTree(data);
    //目录重新渲染
    gData.value = data;
  };

  //树节点展开事件
  const lData = ref();
  const selectedNoteBookID = ref();
  const onSelect = (
    selectedKeys: any,
    e: { selected: boolean; selectedNodes: any; node: any; event: Event }
  ) => {
    //
    const selectedElem = e.event.currentTarget as HTMLElement;
    if (
      e.event.currentTarget &&
      !selectedElem.classList.contains("ant-tree-node-selected")
    ) {
      selectedElem.classList.add("ant-tree-node-selected");
    }
    //展开树节点
    e.node.onExpand();
    //更新笔记列表
    if (selectedKeys && e.node.isLeaf) {
      getNoteList(selectedKeys ?? []);
      selectedNoteBookID.value = selectedKeys ?? [];
    }
  };

  //目录右键菜单
  const listAreaCategoryContextMenuItems = [
    {
      name: "重命名",
      icon: "EditOutlined",
      categoryid: "",
      action: "rename",
    },
    {
      name: "删除",
      icon: "DeleteOutlined",
      categoryid: "",
      action: "del",
    },
  ];
  const blankAreaCategoryContextMenuItems = [
    {
      name: "新建分区",
      icon: "PlusOutlined",
      categoryid: "",
      action: "newPartition",
    },
    {
      name: "新建笔记本",
      icon: "PlusOutlined",
      categoryid: "",
      action: "newNoteBook",
    },
  ];

  //笔记右键点击事件
  const categoryContextMenuItems = ref(blankAreaCategoryContextMenuItems);
  const categoryContextMenuHandler = (handler: any) => {
    if (
      handler &&
      handler.target.classList &&
      handler.target.classList.contains("node-title")
    ) {
      listAreaCategoryContextMenuItems.forEach((item) => {
        item.categoryid = handler.target.getAttribute("id");
      });
      categoryContextMenuItems.value = listAreaCategoryContextMenuItems;
    } else {
      categoryContextMenuItems.value = blankAreaCategoryContextMenuItems;
    }
  };

  //笔记右键菜单点击事件
  const categoryContextItemClickHandler = (event: any) => {
    const action = event.currentTarget.getAttribute("action") ?? "";
    const categoryid = event.currentTarget.getAttribute("categoryid") ?? "";
    switch (action) {
      //重命名，设置为可编辑
      case "rename": {
        changeEditState(gData.value, categoryid);
        break;
      }
      //删除
      case "del": {
        delCategoryTree(categoryid);
        break;
      }
      //新建分区
      case "newPartition": {
        newCategory(0);
        break;
      }
      //新建笔记本
      case "newNoteBook": {
        newCategory(1);
        break;
      }
    }
  };

  //修改目录名称事件
  const editCategoryNameHandler = (
    categoryid: string,
    title: string,
    isConfirm: boolean
  ) => {
    if (isConfirm) {
      const reg = /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/g;
      if (reg.test(title)) {
        updateCategoryTitle(categoryid, title);
        changeEditState(gData.value, categoryid, title);
      } else {
        message.error("请输入字母、数字和汉字！");
      }
    } else {
      changeEditState(gData.value, categoryid);
    }
  };

  watch(lData, async () => {
    await nextTick();
    //1、将重新挂载的笔记列表的第一个笔记选中
    const container = document.getElementsByClassName("list-group-item");
    if (container != null && container.length > 0) {
      (container[0] as HTMLElement).click();
    }
  });

  onMounted(() => {
    //挂载时，取目录树
    getCategoryTree();
  });

  return {
    gData,
    onDrop,
    lData,
    selectedNoteBookID,
    onSelect,
    categoryContextMenuItems,
    categoryContextMenuHandler,
    categoryContextItemClickHandler,
    editCategoryNameHandler,
  };
}
