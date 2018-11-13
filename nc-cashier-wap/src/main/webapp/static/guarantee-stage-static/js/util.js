/**
 * Created by ying-xia on 16/9/13.
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
        developers: "\u5C0F\u9ED1\u624B",
        contact: "ying.xia@yeepay.com"
    };
    //console.log("%c\u524D\u7AEF\u5F00\u53D1\u4E3B\u8981\u8D1F\u8D23\u4EBA：" + infoMap.developers, "color:#eed503; font-size:16px;");
    //console.log("%c\u5E2E\u52A9\u4E0E\u53CD\u9988：" + infoMap.contact, "color:#eed503; font-size:16px;");
    var Util = Util || {};
    Util.type = function (obj) {
        /*
         * 返回数据类型
         * */
        var toString = Object.prototype.toString;
        var map = {
            '[object Boolean]': 'boolean',
            '[object Number]': 'number',
            '[object String]': 'string',
            '[object Function]': 'function',
            '[object Array]': 'array',
            '[object Date]': 'date',
            '[object RegExp]': 'regExp',
            '[object Undefined]': 'undefined',
            '[object Null]': 'null',
            '[object Object]': 'object'
        };
        if (obj instanceof Element) {
            return 'element';
        }
        return map[toString.call(obj)];
    };
    Util.back = function () {
        /*
         * 返回上一页
         * */
        global.history.back();
        return this;
    };
    Util.getParam = function (arg) {
        /*
         * 返回get请求参数值
         * */
        var src = location.href;
        return src.indexOf("&", src.indexOf(arg)) == -1 ? src.slice(src.indexOf("=", src.indexOf(arg)) + 1) : src.slice(src.indexOf("=", src.indexOf(arg)) + 1, src.indexOf("&", src.indexOf(arg)));
    };
    Util.runScript = function (str) {
        var search = /<script[^>]*>[\s\S]*?<\/[^>]*script>/gi;
        var cache = str.match(search);
        if (this.type(cache) != "array")return false;//没有匹配的script
        cache.forEach(function (currert) {
            var nsrc = currert.replace(currert.substr(currert.length - 9, 9), "").replace(currert.substr(0, 8), "");
            var scr = dom.createElement("script");
            try {
                scr.text = nsrc;
            } catch (error) {
                scr.appendChild(dom.createTextNode(nsrc));
            }
            dom.body.appendChild(scr);
            dom.body.removeChild(scr);
        });
    };
    Util.tip = function (msg, callback, iconfont, duration) {
        /*
         * 提示弹框
         * msg：提示信息  类型字符串
         * callback：回调函数
         * iconfont：图标控制  默认显示绿勾，值为fail显示红色叹号
         * duration：弹框显示时间,默认3s  类型数字
         * */
        var duration = duration || 3000;
        var icon = iconfont == "fail" ? "&#xe605;" : "&#xe604;";
        var className = status == "fail" ? "tip fail" : "tip";
        var html = '<div id="Tip" class="' + className + '">' +
            '<i class="iconfont">' + icon + '</i>' + msg +
            '</div>';

        function _close() {
            /*
             * 隐藏tip
             * */
            dom.body.removeChild(dom.querySelector("#Tip"));
            //dom.querySelector("#Tip").classList.add("none");
            if (typeof callback == "function") {
                callback();
            }
        }

        function _autoHide() {
            /*
             * 自动隐藏tip
             * */
            setTimeout(_close, duration);
        }

        dom.body.insertAdjacentHTML("beforeEnd", html);
        _autoHide();
        return this;
    };
    Util.popup = function (title, content, callback) {
        /*
         * popup
         * title：标题文字
         * content：popup内容
         * callback：调起弹层后回调函数
         * */
        var title = title || "";
        var html = '<div id="Popup">' +
            '<h2>' + title + '<i class="icon close">&#xe60e;</i></h2>' +
            '<div class="content">' +
            content +
            '</div>' +
            '</div>';
        this.mask();
        if (dom.querySelector("#Popup") == null) {
            dom.body.insertAdjacentHTML("beforeEnd", html);
            dom.querySelector("#Popup .close").addEventListener("mousedown", this.closePopup, false);
        } else {
            dom.querySelector("#Popup").classList.remove("none");
            dom.querySelector("#Popup .content").innerHTML = content;
        }
        if (typeof callback == "function") {
            callback();
        }
        return this;
    };
    Util.closePopup = function (callback) {
        /*
         * 关闭popup
         * */
        Util.hideMask();
        dom.querySelector("#Popup").classList.add("none");
        if (typeof callback == "function") {
            callback();
        }
    };
    Util.ciphertext = function (str, start, end) {
        /*
         * 掩码处理
         * str 需要处理的字符串
         * [start] 开始位置  类型：数字
         * [end] 结束位置   类型：数字
         * */
        var cache = str.slice(start, end).split("");
        var encryption = cache.map(function () {
            var arr = [];
            arr.push("*");
            return arr;
        }).join("");
        return str.replace(str.slice(start, end), encryption);
    };
    Util.format = function (str, digit) {
        /**
         * 格式化字符串，默认4位一空格
         */
        str = str.replace(/[\s\n\r]/g, "");
        var len = str.length,
            digit = digit || 4;
        if (len <= digit) {
            return str;
        }
        var array = [];
        var start = 0;
        for (var i = 0; i < Math.ceil(len / digit); i++) {
            array.push(str.substr(start, digit));
            start += digit;
        }
        return array.join(" ");
    };
    Util.blur = function () {
        //使内容模糊
        try {
            dom.querySelector("#header").classList.add("filter-blur");
            dom.querySelector(".container").classList.add("filter-blur");
            dom.querySelector("#footer").classList.add("filter-blur");
        } catch (error) {
            //
        }
    };
    Util.unblur = function () {
        //使内容清晰
        try {
            dom.querySelector("#header").classList.remove("filter-blur");
            dom.querySelector(".container").classList.remove("filter-blur");
            dom.querySelector("#footer").classList.remove("filter-blur");
        } catch (error) {
            //
        }
    };
    Util.mask = (function () {
        /*
         * 创建蒙层
         * */
        var instance;
        return function () {
            if (!instance) {
                instance = new _createDiv();
                instance.id = "mask";
            } else {
                instance.classList.remove("none");
            }
            Util.blur();
            return this;
        }
    }());
    Util.loading = (function () {
        /*
         * 创建loading
         * */
        var instance;
        return function (message) {
            Util.mask();
            if (!instance) {
                instance = new _createDiv('<img src="' +location.hostname+':'+location.port + '/assets/images/loading.gif"><span>' + message + '</span>');
                instance.id = "loading";
            } else {
                instance.classList.remove("none");
            }
            return this;
        }
    }());
    Util.hideMask = function () {
        /*
         * 隐藏蒙层
         * */
        if (dom.querySelector("#mask") instanceof Element) {
            dom.querySelector("#mask").classList.add("none");
        }
        this.unblur();
        return this;
    };
    Util.hideLoading = function () {
        /*
         * 隐藏loading
         * */
        this.hideMask();
        if (dom.querySelector("#loading") instanceof Element) {
            dom.querySelector("#loading").classList.add("none");
        }
        return this;
    };
    Util.clearInput = function (input) {
        /*
         * 清空输入框内容
         * */
        var reg = /text|tel|password|number|search|url|email|file/;
        if (input.nodeType == 1 && input.nodeName.toLowerCase() == "input" && reg.test(input.type))input.value = "";
    };
    Util.alert = (function () {
        var instance;
        return function (content, callback, mask) {
            /*
             * 模拟alert
             * content  要提示的内容
             * callback  关闭后回调函数
             * mask 布尔值 是否显示蒙层
             * */
            var needMask = typeof mask == "undefined" || mask == true ? true : false;
            if (!instance) {
                instance = true;
                if (needMask) {
                    this.mask();
                }
                var html = '<div id="alert">' +
                    '<h2>提示</h2>' +
                    '<p class="content">' + content + '</p>' +
                    '<span class="close">知道了</span>' +
                    '</div>';


                var close = function () {
                    dom.querySelector("#alert").classList.add("none");
                    if (needMask) {
                        this.hideMask();
                    }
                    if (typeof callback == "function") {
                        callback();
                    }
                };
                dom.body.insertAdjacentHTML("beforeEnd", html);
                dom.querySelector("#alert .close").addEventListener("click", close.bind(Util), false);
            } else {
                dom.querySelector("#alert .content").textContent = content;
                dom.querySelector("#alert").classList.remove("none");
            }
        }
    }());
    function _createDiv(html) {
        /*
         * 创建div
         * */
        var html = html || "";
        this.html = html;
        return this.init();
    }

    _createDiv.prototype.init = function () {
        var div = dom.createElement("div");
        div.innerHTML = this.html;
        dom.body.appendChild(div);
        return div;
    };
    global.Util = Util;
}(window));

