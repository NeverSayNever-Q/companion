<template>
    <ul id="noteContainer" @click.prevent="selectItem">
        <li v-for="item in lData" :key="item.id" :id= "'note_' + item.id" :title="item.title">
            <span />&nbsp; &nbsp; &nbsp;{{ item.title }}
        </li>
    </ul>
</template>

<script lang="ts">
    import {toRef, PropType, onUpdated} from "vue";

    export interface qItem{
        id: string,
        title: string,
    }

    export default {
        name: "QList",
        props: {
            dataSource: Array as PropType<qItem[]>,
        },
        emits: ['qListClick'],
        setup(props: any, context: any) {
            const lData =  toRef(props, 'dataSource');

            //被选中元素
            let selectedItem: HTMLElement;

            //绑定列表点击事件：1、更改样式；2、返回NoteID和title
            const selectItem = (event: any) => {
                //取消当前选中对象的样式
                if(!(typeof(selectedItem) === 'undefined')) {
                    selectedItem.classList.remove("itemSelected");
                    selectedItem.getElementsByTagName("span")[0].style.backgroundColor = "";
                }
                if(event.target.nodeName === "LI"){
                    // 更改样式：更改背景色&添加选中图标
                    event.target.classList.add("itemSelected");
                    event.target.getElementsByTagName("span")[0].style.backgroundColor = 'rgb(119, 25, 170)';

                    //添加当前选中对象至selectedItem
                    selectedItem = event.target;

                    context.emit('qListClick', {
                        'id': event.target.getAttribute("id"),
                        'title': event.target.getAttribute("title"),
                    })
                }
            }

            onUpdated(() => {
                //1、将重新挂载的笔记列表的第一个笔记选中
                const container = document.getElementById("noteContainer");
                if(container != null && container.children.length > 0){
                    const firstItem = container.firstElementChild;
                    if(firstItem != null && firstItem.nodeName === "LI"){
                        (firstItem as HTMLElement ).click();
                    }
                }
            })

            return {
                lData,
                selectItem,
            }
        }
    }
</script>

<style scoped>
    ul{
        padding-inline-start: 0px;
    }
    li{
        list-style: none;
        margin: 10px auto;
        height: 30px;
        line-height: 30px;
        overflow: hidden;
    }
    li:hover{
        cursor: default;
    }
    span{
        display: inline-block;
        width: 3px;
        height: 30px;
        vertical-align: top;
        /*background-color: rgb(119, 25, 170);*/
    }
    .itemSelected{
        background-color: rgb(225, 223, 221);
    }
</style>