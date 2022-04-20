import axios from "axios";

export default function () {
  //保存编辑器内容
  const saveHandler = async (info: any) => {
    try {
      if (info) {
        const note = {
          id: info.noteid,
          title: info.title,
          content: info.content,
        };
        const resp = await axios.patch("/note/", [note]);
        const data = resp.data;
        if (!data.success) {
          console.error("保存失败");
        }
      }
    } catch (e) {
      console.error(e);
    }
  };
  return { saveHandler };
}
