/**
 * Created by 二月红 on 17/8/22.
 */
;
(function (global, undefined) {
    'use strict';
    /*
     * 该文件包含了一些前端实用的函数
     * */
    var dom = global.dom = global.document;
    var array = global.array = Array.from;
    var console = global.console;
    var infoMap = {
        version: 1.0,
        compatible: "IE9+",
        developers: "zym",
        contact: "yingmei.zhang@yeepay.com"
    };
    var Tip = Tip || {};
    Tip.titleContentBtn = function (title, content, callback){
        // * title：标题文字
        //  * content：popup内容
        //  * callback：调起弹层后回调函数
        var title = title || '';
        var html = '<div id="tip1">' +
            '<h2 class="bold">' + title + '</h2>' +
            '<div class="content">' +
            content +
            '</div>' +
            '<button class="close-btn">确定</button>' +
            '</div>';
        if (dom.querySelector("#tip1") == null) {
            dom.body.insertAdjacentHTML("beforeEnd", html);
        }else{
            dom.querySelector("#tip1").classList.remove("none");
            dom.querySelector("#tip1 .content").innerHTML = content;
        }
        if (typeof callback == "function") {
            callback();
        }
        return this;
    }
    global.Tip = Tip;
}(window));

