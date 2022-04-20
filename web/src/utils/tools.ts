export class Tool {
  /**
   * 空校验 null或""都返回true
   */
  public static isEmpty(obj: any) {
    if (typeof obj === "number") {
      return false;
    } else if (typeof obj === "string") {
      return !obj || obj.replace(/\s+/g, "") === "";
    } else {
      return !obj || JSON.stringify(obj) === "{}" || obj.length === 0;
    }
  }

  /**
   * 非空校验
   */
  public static isNotEmpty(obj: any) {
    return !this.isEmpty(obj);
  }

  /**
   * 对象复制
   * @param obj
   */
  public static copy(obj: object) {
    if (Tool.isNotEmpty(obj)) {
      return JSON.parse(JSON.stringify(obj));
    }
  }

  /**
   * 使用递归将数组转为树形结构
   * 父ID属性为parent
   */
  public static array2Tree(array: any, parentId: string) {
    if (Tool.isEmpty(array)) {
      return [];
    }

    const result = [];
    for (let i = 0; i < array.length; i++) {
      const c = array[i];
      // console.log(Number(c.parent), Number(parentId));
      if (String(c.parentid) === String(parentId)) {
        const tempObj = {
          title: c.name,
          key: c.id,
          children: [] as any[],
          isLeaf: !!c.type,
          sort: c.sort,
          slots: { title: "custom" },
          isEdit: false,
        };
        // 递归查看当前节点对应的子节点
        const children = Tool.array2Tree(array, c.id);
        if (Tool.isNotEmpty(children)) {
          tempObj.children = children;
        }
        result.push(tempObj);
      }
    }
    //每个层级的节点按sort排序
    result.sort((r1, r2) => {
      return Number(r1.sort) - Number(r2.sort);
    });
    return result;
  }

  /**
   * 使用递归将树形结构转为数组
   * 父ID属性为parent
   */
  public static tree2Array(tree: any, parentid: string): any {
    if (Tool.isEmpty(tree)) {
      return [];
    }

    const result = [];
    for (let i = 0; i < tree.length; i++) {
      const c = tree[i];
      const tempObj = {
        id: c.key,
        parentid: parentid,
        sort: i + 1,
      };
      result.push(tempObj);
      if (c.children.length > 0) {
        result.push(...Tool.tree2Array(c.children, c.key));
      }
    }
    console.log("dqdqd", result);
    return result;
  }
}
