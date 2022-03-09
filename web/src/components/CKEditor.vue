<template>
    <div @keydown.ctrl="saveContent">
        <ckeditor  :editor="editor" v-model="editorData" :config="editorConfig" ></ckeditor>
    </div>
</template>

<script>
    import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
    import CKEditor from '@ckeditor/ckeditor5-vue';
    import '@ckeditor/ckeditor5-build-classic/build/translations/zh-cn';
    import {toRef} from "vue";

    export default {
        name: 'CKEditor',
        components: {
            // 编辑器组件的局部注册方式
            ckeditor: CKEditor.component,
        },
        props: {
            content: String,
            noteId: String
        },
        emits: ['saveClick'],

        setup(props, context) {
            const content =  toRef(props, 'content');
            const noteId =  toRef(props, 'noteId');

            const saveContent = (event) => {
                if(event.keyCode === 83 ){
                    event.preventDefault();
                    context.emit("saveClick", {
                        "content": event.target.innerHTML,
                        "noteId": noteId.value,
                    });
                }
            }
            return {
                editor: ClassicEditor,
                editorData: content,
                editorConfig: {
                    language: "zh-cn",
                    highlight: "yellowMarker",
                },
                saveContent
            };
        }
    }
</script>
<style>
    .ck-editor__editable {
        min-height: 500px;
    }
</style>