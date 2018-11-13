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
    console.log("%c\u524D\u7AEF\u5F00\u53D1\u4E3B\u8981\u8D1F\u8D23\u4EBA：" + infoMap.developers, "color:#eed503; font-size:16px;");
    console.log("%c\u5E2E\u52A9\u4E0E\u53CD\u9988：" + infoMap.contact, "color:#eed503; font-size:16px;");
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
                instance = new _createDiv('<img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/loading.gif"><span>' + message + '</span>');
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


/**
 * Created by ying-xia on 17/6/22.
 */
(function (exports) {
    var Controller = {
        setForm: function(obj,flag) {   //创建表单提交数据到银联
            var url = obj.url,
                charset = obj.charset,
                method = obj.method,
                params = obj.params;
            var html = '<form name="toBankForm" id="toBankForm" target='+(flag?'\"_blank\"':'\"_self\"')+' action="'+url+'" method="'+method+'"  accept-charset="'+charset+'">'
            for(var item in params){
                html += '<input type="hidden" name="'+item+'" value="'+params[item]+'" />'
            }
            html += '</form>';
            dom.querySelector("body").insertAdjacentHTML('beforeEnd',html)
            dom.querySelector("#toBankForm").submit();
            dom.querySelector("body").removeChild(dom.querySelector("#toBankForm"));
        },
        quickPayValidate: function () {
            User.form.validate("#quickPayForm");
        },
        accountPayFormPayValidate: function () {
            User.form.validate("#accountPayForm");
        },
        cardNoInput: function () {
            /*
             * 输入银行卡号
             * */
            var value = this.value;
            this.value = Util.format(value);
            dom.querySelector("#addItemWrapper").innerHTML = "";
            dom.querySelector(".fn-btn").classList.remove("none");
            if (User.form.strategy.bankCardNo(this.value)) {
                Action.getAddItem.call(dom.querySelector(".fn-btn"));
            }
        },
        forgetTradingPassword: function () {
            /*账户支付忘记交易密码*/
            function autoSetPas() {
                /*
                 * 自动跳转设置密码
                 * */
                User.timing(function (second) {
                    dom.querySelector("#secondRemain").textContent = second;
                }, function () {
                    dom.querySelector(".auto-set").classList.add("none");
                    dom.querySelector(".set-password").click();
                }, 5);
                dom.querySelector(".set-password").addEventListener("click", function () {
                    clearTimeout(session.getItem("timeId"));
                    dom.querySelector(".auto-set").classList.add("none");
                }, false);
            }

            Util.popup.call(Util, '', User.template.forgetTradingPassword, autoSetPas);
        },
        getAddItem: function () {
            /*获取卡信息*/
            Action.getAddItem.call(dom.querySelector(".fn-btn"), "manual");
        },
        qrcodeManager: function () {
            /*
             * 二维码管理
             * */
            var btn = Array.from(dom.querySelectorAll(".qrcode .support-app .icon"));
            var qrcode = Array.from(dom.querySelectorAll(".qrcode .qrcode-item"));
            var qrcodeStream = Array.from(dom.querySelectorAll(".qrcode .qrcode-stream"));
            var tip = dom.querySelector(".qrcode .tip");

            function showQrcode(index) {
                /*展示二维码*/
                var active = dom.querySelector(".qrcode .qrcode-item.active");
                if (Util.type(active) == "element") {
                    active.classList.remove("active");
                }
                //zym start
                if(dom.querySelector(".qrcode-load-error")){
                    dom.querySelector(".qrcode-load-error").classList.add("none");
                }
                //zym end
                qrcode[index].classList.add("active");
            }

            function iconActive() {
                /*激活切换按钮*/
                var active = dom.querySelector(".qrcode .support-app .icon.active");
                if (Util.type(active) == "element") {
                    active.classList.remove("active");
                }
                this.classList.add("active");
                dom.querySelector(".qrcode .status").classList.add("none"); //分期支付标识隐藏
                tip.textContent = this.getAttribute("data-tip");
                dom.querySelector("#hook-qrcode-app").style.left = this.offsetLeft + this.offsetWidth / 4 + "px";
            }

            function loadQrcode(index) {
                /*加载二维码*/
                var img = qrcode[index].querySelector(".qrcode-stream");
                if (img.getAttribute("complete") != "true") {
                    img.src = img.getAttribute("data-original");
                }
            }

            function complete() {
                /*二维码加载成功*/
                this.setAttribute("complete", "true");
                dom.querySelector(".qrcode-load-error").classList.add("none");
                dom.querySelector(".qrcode-item.active .qrcode-area").classList.remove("hidden");
            }

            function qrcodeloadError() {
                /*
                 * 二维码加载失败
                 * */
                dom.querySelector(".qrcode-load-error").classList.remove("none");
                dom.querySelector(".qrcode-item.active .qrcode-area").classList.add("hidden");
            }

            function refreshQrcode() {
                /*刷新二维码*/
                var img = dom.querySelector(".qrcode-item.active .qrcode-stream");
                img.src = img.getAttribute("data-original");
            }

            btn.forEach(function (icon, index) {
                icon.addEventListener("click", iconActive.bind(icon), false);
                icon.addEventListener("click", showQrcode.bind(icon, index), false);
                icon.addEventListener("click", loadQrcode.bind(icon, index), false);
            });
            qrcodeStream.forEach(function (stream) {
                /*二维码绑定失败和加载成功事件*/
                stream.addEventListener("error", qrcodeloadError, false);
                stream.addEventListener("load", complete.bind(stream), false);
            });
            dom.querySelector(".refresh-qrcode").addEventListener("click", refreshQrcode, false);//刷新二维码
            btn[0].click();//模拟点击第一个按钮
        },
        showAgreement: function () {
            /*
             * 服务协议
             * */
            Util.popup('', User.template.agreement);
        },
        showBindCardAgreement: function () {
            /*
             * 绑卡支付服务协议
             * */
            Util.popup('', User.template.bindCardAgreement);
        },
        showBOCAgreement: function () {
            /*
             * 中国银行服务协议
             * */
            Util.popup('', User.template.chinaBankAgreement);
        },
        disagree: function () {
            dom.querySelector("#agreement").checked ? User.form.enabledSubmit("#firstPayBtn") : User.form.disabledSubmit("#firstPayBtn");
        },
        select: function () {
            User.form.select(Action.bindPayPayment.bind(Action));
        },
        queryQrCode: function () {
            /*
             * 查询扫码标示  轮询扫描二维码
             * */
            Action.getQueryQrCode("isInstallment");
        },
        queryStatus: function () {
            /*
             * 查询扫码支付结果
             * */
            Action.getQueryStatus(parseInt(session.getItem("jsTrys")) * 3, "qrcode");
        },
        verifyCode: function () {
            //账户支付  验证码
            var href = this.src.substring(0, this.src.indexOf("?"));
            this.src = href + "?token=" + session.getItem("token") + '&' + Date.now();
        },
        addBankCardRouter: function (event) {
            /*
             * 添加新卡需要授权弹出验证码层，否则直接显示添加新卡页面
             * */
            if (session.getItem("isAuthorized") == "false") {
                /*需要授权弹层*/
                event.preventDefault();
                event.returnValue = false;
                Action.merchantAuthority();
            } else {
                /*添加新卡*/
                User.form.reset("#quickPayForm");
            }
        },
        expireTime: function () {
            /*是否显示支付剩余时间*/
            var expireTime = dom.querySelector("#expireTime .emp").textContent.replace(/-/g, "/");
            var delta = new Date(expireTime).getTime() - new Date().getTime() <= 30 * 60 * 1000;
            if (delta) {
                dom.querySelector("#expireTime").classList.remove("none");
            }
        },
        showClientId: function () {
            if (session.getItem("needClientId")) {
                dom.querySelector("#bankPay .tab-wrapper #b2bTab").click();
                dom.querySelector('#bankPay .tab-content-wrapper .tab-content.active li[bankcode=' + session.getItem("needClientId") + ']').click();
                Action.eBankPayRoute();
            }
        },
        bindCardValidate: function () {
            User.form.validate("#bindCardForm");
        },
        bindCardNoInput: function () {
            /*
             * 绑卡输入银行卡号
             * */
            var value = this.value;
            this.value = Util.format(value);
            dom.querySelector("#addItemWrapper").innerHTML = "";
            dom.querySelector(".fn-btn").classList.remove("none");
            Progress.step(1);
            if (User.form.strategy.bankCardNo(this.value)) {
                Action.getBindCardInfo.call(dom.querySelector(".fn-btn"));
            }
        },
        unbind: function () {
            /*
             * 解除绑卡
             * */
            Util.popup(null, Cashier.Components.UIBIND_CONFRIM.content);
            dom.querySelector(".unbind-confrim .confrim").addEventListener("click", Action.unBindConfrim.bind(null, this.getAttribute("data-bindid")));
            dom.querySelector(".unbind-confrim .cancel").addEventListener("click", Util.closePopup);
        },
        bindPayCardNoInput: function () {
            /*
             * 输入银行卡号
             * */
            var value = this.value;
            this.value = Util.format(value);
            var cache = this.value.replace(/[\s\n\r]/g, "");
            dom.querySelector("#addItemWrapper").innerHTML = "";
            dom.querySelector(".fn-btn").classList.remove("none");
            var check = User.form.strategy.onlyNumber(cache) && (User.form.strategy.length(cache, 14) || User.form.strategy.length(cache, 15) || User.form.strategy.length(cache, 16) || User.form.strategy.length(cache, 19)) ? true : false;
            /*16位或19位且仅为数字时自动获bin信息*/
            if (check) {
                Action.getBindPayAddItem.call(dom.querySelector(".fn-btn"));
            }
        },
        getBindPayAddItem: function () {
            /*获取绑卡支付信息*/
            Action.getBindPayAddItem.call(dom.querySelector(".fn-btn"), "manual");
        },
        getBindCardInfo: function () {
            /*获取单独绑卡卡信息*/
            Action.getBindCardInfo.call(dom.querySelector(".fn-btn"), "manual");
        },
        againBindCard: function () {
            /*重新绑卡*/
            document.querySelector("#bindCardInfo").innerHTML = "";
            document.querySelector("#bindCardInfo").insertAdjacentHTML("beforeend", Cashier.applyComponent(Cashier.Components.BIND_CARD));
            Cashier.events();
        }
    };
    exports.Controller = Controller;
})(window);
/**
 * Created by ying-xia on 17/6/13.
 */
(function (exports) {
    function _getEvent(fn) {
        /*
         * 返回dom节点绑定的方法
         * */
        var list = fn.split(".");
        var method = null;
        for (var i = 0, l = list.length; i < l; i++) {
            method = i == 0 ? exports[list[i]] : method[list[i]];
        }
        return method;
    }

    var Cashier = {
        Template: {},
        events: function () {
            /*
             * 获取有events属性的元素，自动绑定事件
             * */
            var els = Array.from(dom.querySelectorAll("[events]"));
            var factory = [];//保存事件列表
            els.forEach(function (el) {
                var str = el.getAttribute("events").replace(/(^\{*)|(\}*$)/g, "");
                var cache = str.split(",");
                factory = cache.map(function (current) {
                    return current.split(":");
                });
                factory.forEach(function (current) {
                    var fn = _getEvent(current[1]);
                    if (current[0] == "auto") {
                        fn();
                    } else {
                        el.addEventListener(current[0], fn);
                    }

                });
                el.removeAttribute("events");
            });
            return this;
        },
        request: function () {
            /*
             *处理具有ajax属性的元素，主动请求服务
             * */
            var els = Array.from(dom.querySelectorAll("[ajax]"));
            els.forEach(function (el) {
                if (typeof _getEvent(el.getAttribute("ajax")) == "function") {
                    _getEvent(el.getAttribute("ajax"))(el);
                    el.removeAttribute("ajax");
                }
            });
            return this;
        },
        applyLayout: function (tplNo, position, element) {
            /*
             * tplNo:模板编号
             * position:是相对于元素的位置,可能的值如下,默认值beforeend
             * 'beforebegin'→元素自身的前面
             * 'afterbegin'→插入元素内部的第一个子节点之前
             * 'beforeend'→插入元素内部的最后一个子节点之后
             * 'afterend'→元素自身的后面
             * element:插入html元素选择器，默认值container
             * */
            var source = this.Template[tplNo];
            var position = position || 'beforeend';
            var element = element || dom.querySelector(".container") || dom.body;
            element.insertAdjacentHTML(position, source);
            this.events();
            this.request();
        },
        applyComponent: function (component, context) {
            /*
             * 获取组件
             * component组件名称
             * context组件执行上下文
             * */
            if (Util.type(context) == "object" || Util.type(context) == "array") {
                component.context = context;
            }
            return component.content;
        }
    };
    exports.Cashier = Cashier;
})(window);
/**
 * Created by ying-xia on 16/11/12.
 * 包含了所有和后端交互的方法
 */

(function (global) {
    'use strict';
    //生产环境action
    var action = session.getItem("contextUrl");
    //开发环境action
    //var action = "dev/mock";
    var Action = {
        ajax: function (options) {
            /*
             * ajax请求
             * */
            var _options = {
                async: true,
                method: "GET",
                url: location.href,
                //timeout: 10000,
                encrypt: false,
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8'
            };
            if (Util.type(options) == "object") {
                for (var key in options) {
                    _options[key] = options[key];
                }
            }
            _options.data = Util.type(_options.data) == "object" ? params(_options.data) : "";
            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4) {
                    if (xhr.status >= 200 && xhr.status < 300 || xhr.status == 304) {
                        typeof _options.success == "function" ? _options.success(xhr.responseText, xhr.status, xhr) : false;
                    } else {
                        typeof _options.error == "function" ? _options.error(xhr.status, xhr) : false;
                    }
                }

            };
            function params(data) {
                /*
                 * 数据键值对转换成字符串
                 * */
                var arr = [];
                for (var key in data) {
                    //特殊字符传参产生的问题可以使用encodeURIComponent()进行编码处理
                    _options.encrypt ? arr.push(encodeURIComponent(key) + '=' + BASE64.encoder(data[key])) : arr.push(encodeURIComponent(key) + '=' + encodeURIComponent(data[key]));
                }
                return arr.join('&');
            }

            if (_options.method == "GET") {
                xhr.open(_options.method, _options.url + "?" + _options.data, _options.async);
                xhr.setRequestHeader("Content-Type", _options.contentType);
                if (_options.async) {
                    xhr.timeout = _options.timeout;
                    xhr.ontimeout = _options.timeoutCallback || null;
                }
                xhr.send();
            } else {
                xhr.open(_options.method, _options.url, _options.async);
                xhr.setRequestHeader("Content-Type", _options.contentType);
                if (_options.async) {
                    xhr.timeout = _options.timeout;
                    xhr.ontimeout = _options.timeoutCallback || null;
                }
                xhr.send(_options.data);
            }
            return xhr;
        },
        ncpayRouter: function () {
            Action.ajax({
                url: action + "/newpc/routpaywayCustomized",
                data: {
                    token: session.getItem("token")
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.errormsg) {
                        User.Error("#ncpayToolWrapper", data.errormsg + "(" + data.errorcode + ")", "component-error");
                        return;
                    }
                    session.setItem("ncpayType", data.ncpayType);
                    switch (data.ncpayType) {
                        case "firstPay":
                            dom.querySelector("#ncpayToolWrapper").insertAdjacentHTML("beforeEnd", Cashier.Components.NCPAYFIRST.content);
                            Cashier.events();
                            break;
                        case "bindPay":
                            Action.ajax({
                                /*
                                 * 获取绑卡列表
                                 * */
                                url: action + "/newpc/requestCustomized/bind",
                                data: {
                                    token: session.getItem("token")
                                },
                                success: function (responseText) {
                                    var data = JSON.parse(responseText);
                                    session.setItem("isAuthorized", data.authorized);
                                    if (data.errormsg) {
                                        User.Error("#ncpayToolWrapper", data.errormsg + "(" + data.errorcode + ")", "component-error");
                                    } else {
                                        dom.querySelector("#ncpayToolWrapper").insertAdjacentHTML("beforeEnd", Cashier.applyComponent(Cashier.Components.NCPAYBIND, data));
                                        Cashier.events();
                                    }
                                }
                            });
                            break;
                        case "firstPass":
                            Action.ajax({
                                /*
                                 * 获取卡信息
                                 * */
                                url: action + "/newpc/requestCustomized/passcardno",
                                data: {
                                    token: session.getItem("token")
                                },
                                success: function (responseText) {
                                    var data = JSON.parse(responseText);
                                    if (data.errormsg) {
                                        User.Error("#ncpayToolWrapper", data.errormsg + "(" + data.errorcode + ")", "component-error");
                                    } else {
                                        dom.querySelector("#ncpayToolWrapper").insertAdjacentHTML("beforeEnd", Cashier.applyComponent(Cashier.Components.NCPAYPASS, data));
                                        Cashier.events();
                                    }
                                }
                            });
                            break;
                        default :
                            return "";
                    }
                }
            });
        },
        getValidateCode: function (event) {
            /*
             * 首次支付获取验证码
             * */
            var self = event.target;
            User.autoValidateCode.call(self);
            var sendDate = JSON.parse(session.getItem("cardInfo"));
            Action.ajax({
                url: action + "/newpc/firstpay/smsSend",
                data: sendDate,
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.bizStatus == "success") {
                        /*
                         * 验证码发送成功
                         * */
                        dom.querySelector(".send-tip").classList.remove("none");
                        if (data.smsType == "VOICE") {
                            /*语音播报短信验证码*/
                            dom.querySelector(".send-tip").innerHTML = typeof data.sendSMSNo == "undefined" ? '语音验证码播报中，请注意接听' : data.sendSMSNo + '语音验证码播报中，请注意接听';
                        } else {
                            dom.querySelector(".send-tip").innerHTML = '已向您的手机号' + Util.ciphertext(data.phoneNo, 3, 8) + '发送了一条验证码，查收后填写';
                        }
                    } else {
                        /*
                         * 验证码发送失败
                         * */
                        User.form.showErrorMessage.call(self, data.errormsg + '(' + data.errorcode + ')');
                        dom.querySelector(".send-tip").classList.add("none");
                    }
                }
            });
        },
        getBindSms: function (event, sendData) {
            /*
             * 绑卡获取验证码
             * */
            var self = this;

            User.form.disabledSubmit(self);
            Action.ajax({
                url: action + "/newpc/bindpay/bindSmsSend",
                data: sendData,
                success: function (responseText) {
                    var data = JSON.parse(responseText);

                    function inform(phone) {
                        if (session.getItem("needSms") == "VOICE") {
                            /*语音播报短信验证码*/
                            dom.querySelector(".send-tip").innerHTML = typeof data.sendSMSNo == "undefined" ? '语音验证码播报中，请注意接听' : data.sendSMSNo + '语音验证码播报中，请注意接听';
                        } else {
                            dom.querySelector(".send-tip").innerHTML = '已向您的手机号' + Util.ciphertext(phone, 3, 8) + '发送了一条验证码，查收后填写';
                        }
                    }

                    if (data.bizStatus == "success") {
                        /*
                         * 验证码发送成功
                         * */
                        User.autoValidateCode.call(self);
                        if ("#smsForm .confirm") {
                            User.form.enabledSubmit(dom.querySelector("#smsForm .confirm"));
                        }
                        dom.querySelector(".send-tip").classList.remove("none");
                        var currentSelected = dom.querySelector(".current-selected");
                        if (sendData.phoneNo) {
                            var unicode = BASE64.decoder(sendData.phoneNo);
                            var str = '';
                            for (var i = 0, len = unicode.length; i < len; ++i) {
                                str += String.fromCharCode(unicode[i]);
                            }
                            inform(str);
                        } else if (currentSelected.getAttribute("data-phoneno")) {
                            /*
                             * 银行手机号
                             * */
                            inform(currentSelected.getAttribute("data-phoneno"));
                        } else if (dom.querySelector("#bindItemForm").phoneNo) {
                            /*
                             * 补充项手机号
                             * */
                            inform(dom.querySelector("#bindItemForm").phoneNo.value);

                        } else {
                            inform(currentSelected.getAttribute("data-ypmobile"));
                        }
                    } else {
                        /*
                         * 验证码发送失败
                         * */
                        User.form.enabledSubmit(self);
                        User.form.showErrorMessage.call(self, data.errormsg + '(' + data.errorcode + ')');
                        dom.querySelector(".send-tip").classList.add("none");
                    }
                }
            });
        },
        firstpayConfirm: function () {
            /*
             * 首次确认支付
             * */
            var sendDate = JSON.parse(session.getItem("cardInfo"));
            var verifycode = dom.querySelector("#smsForm").verifycode;
            sendDate.verifycode = verifycode.value;
            if (User.form.autoValidate("#smsForm")) {
                /*
                 * 确认支付发送请求前对验证码进行校验，如果校验成功发送请求，否则显示错误信息
                 * */
                User.form.disabledSubmit("#smsForm .confirm");
                Util.closePopup();
                Util.loading("支付等待中...");
                Action.ajax({
                    url: action + "/newpc/firstpay/confirm",
                    data: sendDate,
                    success: function (responseText) {
                        /*
                         * viewTarget(如果是pay_fail，表示短验次数超限，跳转到支付失败页面,)
                         *bizStatus(没有异常情况返回 success,否则返回failed),errorcode(成功没有，失败有值)，errormsg(成功没有，失败有值)
                         * */
                        var data = JSON.parse(responseText);
                        if (data.viewTarget == "") {
                            /*viewTarget等于空串，直接跳转失败页*/
                            location.assign("pay_fail.vm");
                            return;
                        }
                        if (data.bizStatus == "success") {
                            /*
                             * 支付成功关闭loading，跳到成功页面
                             * */
                            //Action.getQueryStatus(parseInt(session.getItem("jsTrys")), "YJZF");
                        } else {
                            /*
                             * 验证码验证错误，关闭loading，显示验证码模板并显示错误信息
                             * */
                            Util.hideLoading();
                            Util.mask();
                            dom.querySelector("#Popup").classList.remove("none");
                            User.form.enabledSubmit("#smsForm .confirm");
                            User.form.showErrorMessage.call(dom.querySelector("#smsForm").verifycode, data.errormsg + '(' + data.errorcode + ')');
                        }
                    }
                });
            }
        },
        getAddItem: function (triggerMode) {
            /*
             * 获取补充项
             * triggerMode:触发模式，自动触发或点击下一步触发
             * */
            var self = this;
            var validate = User.form.autoValidate(".bind-card-form");
            if (validate) {
                User.form.disabledSubmit(self);
                dom.querySelector(".bind-card-form .loading").classList.remove("none");
                Action.ajax({
                    url: action + "/newpc/requestCustomized/notpasscardno",
                    data: User.form.serialize(".bind-card-form", {
                        token: session.getItem("token"),
                        isBindCardChangeCard: session.getItem("isBindCardChangeCard")
                    }),
                    success: function (responseText) {
                        dom.querySelector(".bind-card-form .loading").classList.add("none");
                        User.form.enabledSubmit(self);
                        session.setItem("bankAddItem", responseText);
                        var data = JSON.parse(responseText);
                        if (data.validateStatus == "success") {
                            var addItem = Cashier.applyComponent(Cashier.Components.bankAddItem, data);
                            self.classList.add("none");
                            dom.querySelector("#addItemWrapper").insertAdjacentHTML("beforeend", addItem);
                            User.form.validate(".bind-card-form");
                            Cashier.events();
                        } else {
                            if (triggerMode == "manual") {
                                User.Error(".bind-card-form", data.errormsg + '(' + data.errorcode + ')');
                            }
                        }
                    }
                });
            }
        },
        firstPay: function (isPassCardNo) {
            /*
             * 首次支付下单
             * isPassCardNo是否是透传卡号 true透传
             * */
            var sendData = User.form.serialize(".bind-card-form", {token: session.getItem("token")});
            session.setItem("cardInfo", JSON.stringify(sendData));//保存卡全部信息，方便获取验证码时再次使用
            if (isPassCardNo) {
                if (!User.form.autoValidate(".bind-card-form", "cardno"))return;
            }
            if (!User.form.autoValidate(".bind-card-form"))return;
            User.form.disabledSubmit("#firstPayBtn");
            Action.ajax({
                url: action + "/newpc/firstpay/smsSend",
                data: sendData,
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    User.form.enabledSubmit("#firstPayBtn");
                    if (data.bizStatus == "success") {
                        /*
                         * 短验发送成功
                         * 禁用补充项
                         * 开始自动倒计时
                         * */
                        Util.popup("", User.template.smsCode, function () {
                            dom.querySelector("#smsForm .confirm").onclick = Action.firstpayConfirm.bind(Action);
                            dom.querySelector("#smsForm .validate-code").onclick = Action.getValidateCode;
                        });
                        dom.querySelector(".send-tip").classList.remove("none");
                        if (data.smsType == "VOICE") {
                            /*语音播报短信验证码*/
                            dom.querySelector(".send-tip").innerHTML = typeof data.sendSMSNo == "undefined" ? '语音验证码播报中，请注意接听' : data.sendSMSNo + '语音验证码播报中，请注意接听';
                        } else {
                            dom.querySelector(".send-tip").innerHTML = '已向您的手机号' + Util.ciphertext(data.phoneNo, 3, 8) + '发送了一条验证码，查收后填写';
                        }
                        User.autoValidateCode.call(dom.querySelector("#smsForm .validate-code"));
                        User.form.hideErrorMessage(dom.querySelector("#smsForm"));
                    } else {
                        /*
                         * 短验发送失败
                         * */
                        User.Error(".bind-card-form", data.errormsg + '(' + data.errorcode + ')');
                    }
                }
            });
        },
        eBankPayRoute: function () {
            /*
             * 网银支付点击下一步
             * 如果需要网银企业客户号，弹出弹层输入网银企业客户号
             * 否则直接支付
             * */
            var currentBank = dom.querySelector(".bank-list.active .active");
            if (currentBank.getAttribute("isneedclient") == "true") {
                Util.popup("", User.template.clientId, function () {
                    User.form.validate("#clientIdForm");
                    dom.querySelector("#clientIdForm .bank-logo").className = "bank-logo " + currentBank.getAttribute("bankcode");
                    dom.querySelector("#clientIdForm .bank-name").innerHTML = currentBank.getAttribute("bankname");
                });
                return;
            }
            Action.eBankPay();
        },
        eBankPay: function (clientId) {
            /*
             * 网银支付
             * */
            if (clientId) {
                /*
                 * 如果需要网银企业客户号点击确实需校验表单
                 * */
                if (!User.form.autoValidate("#clientIdForm")) {
                    return;
                }
            }
            var currentBank = dom.querySelector(".bank-list.active .active");
            var sendData = {
                token: session.getItem("token"),
                bankCode: currentBank.getAttribute("bankcode"),
                ebankAccountType: currentBank.getAttribute("bankaccounttype")
            };
            if (clientId) {
                sendData.clientId = dom.querySelector("#clientIdForm").clientId.value;
                User.form.autoValidate("#clientIdForm");
            }
            Action.ajax({
                url: action + "/newpc/ebank/pay",
                async: true,
                data: sendData,
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.errorCode) {
                        if (clientId) {
                            Util.closePopup();
                        }
                        User.Error(".bank-pay-list", data.errorMsg);
                        return;
                    }
                    Util.popup("", User.template.waitingPay, function () {
                        dom.querySelector(".waiting-pay .bank-name").innerHTML = currentBank.getAttribute("bankname");
                        dom.querySelector(".waiting-pay .issue").href = data.questionUrl;
                        //dom.querySelector(".waiting-pay .merchant-order-id").innerHTML = data.merchantOrderId;
                        if (session.getItem("token")) {
                        Action.ajax({
                            url: session.getItem("contextUrl") + "/newwap/yeepay/qrcode",
                            data: {
                                    token: session.getItem("token"),
                                orderId: session.getItem("orderId"),
                                merchantNo: session.getItem("merchantNo")
                            },
                            success: function (path) {
                                if (path) {
                                dom.querySelector(".waiting-pay .yp-qrcode img").src = path;
                                    dom.querySelector("#Popup .waiting-pay .yeepay").classList.remove("none");
                                }
                            }
                        });
                        }
                        /*跳转到银行支付页*/
                        if(data.redirectType == 'TO_PCC'){
                            var a=document.createElement("a");
                            a.href=data.payUrl;
                            a.target="_blank";
                            dom.body.appendChild(a);
                            a.click();
                            // window.open(data.payUrl, "_blank");
                        }else if(data.redirectType == 'TO_BANK'){
                            var ebankUrlInfo = typeof data.ebankUrlInfo == 'string' ? JSON.parse(data.ebankUrlInfo) : data.ebankUrlInfo;
                            Controller.setForm(ebankUrlInfo,true);
                        }
                    });
                }
            });
        },
        ebankResult: function () {
            /*
             * 网银支付点击已完成支付查询结果页
             * */
            location.assign(action + "/newpc/ebank/result/" + session.getItem("token"));
        },
        bindPayPayment: function (currentOption) {
            /*
             * 绑卡支付下单
             * 下单前先清空补充项
             * 如果当前银行卡没有超出限额，请求补充项
             * */
            /*if (dom.querySelector(".global-error") != null) {
                dom.querySelector(".global-error").classList.add("none");
             }*/
            User.form.disabledSubmit("#bindPayBtn");
            dom.querySelector("#bindItemFormWrapper").innerHTML = "";
            if (currentOption.getAttribute("data-disabled") == "true")return;
            /*----------------*/

            dom.querySelector("#bindItemFormWrapper").innerHTML = '<p class="waiting-add-item"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/loading.gif" width="23" />正在加载补充信息...</p>';
            Action.ajax({
                url: action + "/newpc/bindpayCustomized/requestPayment",
                data: {
                    token: session.getItem("token"),
                    bindId: dom.querySelector(".current-selected").getAttribute("data-bindid")
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    session.setItem("needSms", data.smsType);
                    dom.querySelector(".waiting-add-item").classList.add("none");
                    if (data.errormsg) {
                        User.Error("#bindItemFormWrapper", data.errormsg + '(' + data.errorcode + ')');
                    } else {
                        var addItem = Cashier.applyComponent(Cashier.Components.bindCardAddItem, data);
                        dom.querySelector("#bindItemFormWrapper").insertAdjacentHTML("beforeend", addItem);
                        User.form.validate("#bindItemForm");
                        User.form.enabledSubmit("#bindPayBtn");
                    }

                }
            });
        },
        bindPayConfirm: function (needSms, sendData) {
            /*
             * 绑卡确认支付
             * needSms 类型:boolean 是否需要验证码
             * */

            function request() {
                Util.loading("支付等待中...");
                Action.ajax({
                    data: sendData,
                    url: action + "/newpc/bindpay/confirm",
                    success: function (responseText) {
                        /*
                         * viewTarget(如果是pay_fail，表示短验次数超限，跳转到支付失败页面,)
                         *bizStatus(没有异常情况返回 success,否则返回failed),errorcode(成功没有，失败有值)，errormsg(成功没有，失败有值)
                         * */
                        var data = JSON.parse(responseText);
                        if (data.viewTarget == "") {
                            /*viewTarget等于空串，直接跳转失败页*/
                            location.assign("pay_fail.vm");
                            return;
                        }
                        if (data.bizStatus == "success") {
                            /*
                             * 支付成功关闭loading，跳到成功页面
                             * */
                            if (dom.querySelector("#Popup") != null) {
                                dom.querySelector("#Popup").classList.add("none");
                            }
                        } else if (data.bizStatus == "failed") {
                            Util.hideLoading();
                            Util.mask();
                            if (needSms) {
                                dom.querySelector("#Popup").classList.remove("none");
                                User.form.enabledSubmit("#smsForm .confirm");
                                User.form.showErrorMessage.call(dom.querySelector("#smsForm").verifycode, data.errormsg + '(' + data.errorcode + ')');
                        } else {
                                if (dom.querySelector("#Popup") != null) {
                                    dom.querySelector("#Popup").classList.add("none");
                                }
                                Util.hideMask();
                                User.Error("#bindItemFormWrapper", data.errormsg + '(' + data.errorcode + ')', null, "beforeEnd");
                            }
                        } else {
                            /*
                             * 验证码验证错误，关闭loading，显示验证码模板并显示错误信息
                             * */
                            Util.hideLoading();
                            Util.mask();
                            dom.querySelector("#Popup").classList.remove("none");
                            User.form.enabledSubmit("#smsForm .confirm");
                            User.form.showErrorMessage.call(dom.querySelector("#smsForm").verifycode, data.errormsg + '(' + data.errorcode + ')');
                        }
                    }
                });
            }

            if (needSms) {
                /*
                 * 需要短信验证码
                 * */
                if (User.form.autoValidate("#smsForm")) {
                    /*提交前校验表单*/
                    var verifycode = dom.querySelector("#smsForm").verifycode;
                    sendData.verifycode = verifycode.value;
                    Util.closePopup();
                    User.form.disabledSubmit("#smsForm .confirm");
                    request();
                }
                return;
            }
            request();
        },
        bindPay: function (event) {
            /*
             * 绑卡点击立即支付
             * 点击后先检验补充项信息格式是否合法
             * 校验通过后判定是否需要短信验证码
             * 如果需要短信验证码弹出验证码框
             * 不需要验证码直接支付,调用bindPayConfirm方法
             * */
            if (User.form.autoValidate("#bindItemForm") || User.form.autoValidate("#bindItemForm") == undefined) {
                var needSms = session.getItem("needSms") == "undefined" ? false : true;
                var sendData = User.form.serialize("#bindItemForm", {
                    token: session.getItem("token"),
                    bindId: dom.querySelector(".current-selected").getAttribute("data-bindid")
                });
                if (needSms) {
                    /*
                     * 需要短验
                     * */
                    Action.ajax({
                        data: User.form.serialize("#bindItemForm", {
                            token: session.getItem("token"),
                            bindId: dom.querySelector(".current-selected").getAttribute("data-bindid"),
                            smsType: session.getItem("needSms")
                        }),
                        url: action + "/newpc/bindpay/bindSmsSend",
                        success: function (responseText) {
                            var data = JSON.parse(responseText);
                            if (data.bizStatus == "success") {
                                Util.popup("", User.template.smsCode, function () {
                                    dom.querySelector("#smsForm .confirm").onclick = Action.bindPayConfirm.bind(Action, true, sendData);
                                    dom.querySelector("#smsForm .validate-code").onclick = Action.getBindSms.bind(dom.querySelector("#smsForm .validate-code"), event, User.form.serialize("#bindItemForm", {
                                        token: session.getItem("token"),
                                        bindId: dom.querySelector(".current-selected").getAttribute("data-bindid"),
                                        smsType: session.getItem("needSms")
                                    }));
                                    dom.querySelector(".send-tip").classList.remove("none");
                                    var currentSelected = dom.querySelector(".current-selected");

                                    function inform(phone) {
                                        if (session.getItem("needSms") == "VOICE") {
                                            /*语音播报短信验证码*/
                                            dom.querySelector(".send-tip").innerHTML = typeof data.sendSMSNo == "undefined" ? '语音验证码播报中，请注意接听' : data.sendSMSNo + '语音验证码播报中，请注意接听';
                                        } else {
                                            dom.querySelector(".send-tip").innerHTML = '已向您的手机号' + Util.ciphertext(phone, 3, 8) + '发送了一条验证码，查收后填写';
                                        }
                                    }

                                    if (currentSelected.getAttribute("data-phoneno")) {
                                        /*
                                         * 银行手机号
                                         * */
                                        inform(currentSelected.getAttribute("data-phoneno"));
                                    } else if (dom.querySelector("#bindItemForm").phoneNo) {
                                        /*
                                         * 补充项手机号
                                         * */
                                        inform(dom.querySelector("#bindItemForm").phoneNo.value);

                                    } else {
                                        inform(currentSelected.getAttribute("data-ypmobile"));
                                    }

                                });
                                User.autoValidateCode.call(dom.querySelector("#smsForm .validate-code"));//自动倒计时
                                User.form.hideErrorMessage(dom.querySelector("#smsForm"));//绑定得到焦点隐藏错误信息
                            } else {
                                /*
                                 * 获取验证码失败
                                 * */
                                User.Error("#bindItemFormWrapper", data.errormsg + '(' + data.errorcode + ')', null, "afterBegin");
                            }
                        }
                    });

                } else {
                    /*
                     * 不需要短验
                     * */
                    Action.bindPayConfirm(false, sendData);
                }
            }
        },
        getQueryQrCode: function (identify) {
            /*
             * 轮询扫描二维码结果
             * */
            var time = parseInt(session.getItem("jsTrys")) * 3;
            +function polling() {
                if (time <= 0) {
                    return;
                } else {
                    time--;
                    global.pollingController = Action.ajax({
                        url: action + "/newpc/scan/listen",
                        data: identify == "isInstallment" ? {
                            token: session.getItem("token"),
                            isInstallment: "isInstallment"
                        } : {token: session.getItem("token")},
                        success: function (responseText) {
                            var data = JSON.parse(responseText);
                            if (data.scan) {
                                /*
                                 * 扫描成功显示成功提示
                                 * */
                                identify == "isInstallment" ? dom.querySelector(".qrcode .status").classList.remove("none") : dom.querySelector(".qrcode .status").classList.add("none");
                            } else {
                                polling();
                            }
                        }
                    });
                }
            }();
        },
        getQueryStatus: function (whileTimes) {

            +function forTimes() {
                whileTimes--;
                Action.ajax({
                    url: action + "/newpc/result/querystate",
                    data: {
                        token: session.getItem("token"),
                        rad: Date.now()
                    },
                    success: function (responseText) {
                        var data = JSON.parse(responseText);
                        if (data.queryState) {
                            Action.getQueryResult();
                        } else {
                            if (whileTimes > 0) {
                                forTimes()
                            } else {
                                Action.getQueryResult();
                            }
                        }
                    }
                });
            }();
        },

        getQueryResult: function () {
            Action.ajax({
                url: action + "/newpc/query/result",
                data: {
                    token: session.getItem("token")
                },
                success: function (responseText) {
                    Util.hideLoading();
                    var data = JSON.parse(responseText);
                    if (data.resultState == "SUCCESS" || data.resultState == "CANCEL") {
                        location.assign(data.successUrl);
                    } else if (data.resultState == "FAILED") {
                        User.Error(".order-info", data.errormsg + '(' + data.errorcode + ')', null, "afterend");
                        dom.querySelector(".global-error").scrollIntoView(false);
                    } else if (data.resultState == "PAYING") {
                        Util.popup("", User.template.queryResult, function () {
                            dom.querySelector(".query-result .issue").href = data.questionUrl;
                            dom.querySelector(".query-result .merchant-order-id").innerHTML = data.merchantOrderId;
                        });
                    }
                }
            });
        },
        limit: function () {
            /*
             * 查看银行列表与限额
             * 先从缓存读取银行列表，如果没有再请求服务器
             * */
            var ul = dom.querySelector("#supportBankList");
            ul.innerHTML = "";
            if (session.getItem("supportBankList")) {
                ul.insertAdjacentHTML("beforeEnd", session.getItem("supportBankList"));
                return;
            }
            Action.ajax({
                url: action + "/newpc/supportbanklist",
                data: {
                    token: session.getItem("token")
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    var lis = "";
                    for (var key in data.supportBanklist) {
                        data.supportBanklist[key].forEach(function (bank, index) {
                            lis += '<li class="' + (data.supportBanklist[key].length == 2 ? 'vertical' : '') + '"><span class="bank' + (index == 1 ? ' hidden' : '') + '"><i class="bank-logo ' + bank.bankCode + '"></i>' + bank.bankName + '</span><span class="card-type">' + (bank.debit == 'DEBIT' ? '储蓄卡' : '信用卡') + '</span><span class="single">' + (bank.limitOfBill == -1 ? '无限制' : bank.limitOfBill / 100) + '</span><span class="day">' + (bank.limitOfDay == -1 ? '无限制' : bank.limitOfDay / 100) + '</span><span class="month">' + (bank.limitOfMonth == -1 ? '无限制' : bank.limitOfMonth / 100) + '</span></li>';
                        });
                    }
                    session.setItem("supportBankList", lis);
                    ul.classList.add("complete");
                    ul.insertAdjacentHTML("beforeEnd", lis);
                }
            });
        },
        merchantAuthority: function () {
            /*
             * 请求授权
             * */
            Action.ajax({
                url: action + "/merchantAuthority/request",
                data: {
                    token: session.getItem("token")
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    session.setItem("phoneLater", data.phoneLater);
                    session.setItem("cardNoLater", data.idCardNoLater);
                    if (data.processStatusEnum == "SUCCESS") {
                        if (dom.querySelector("#Popup .authorized") != null) {
                            Util.blur();
                            dom.querySelector("#Popup").classList.remove("none");
                            dom.querySelector("#mask").classList.remove("none");
                        } else {
                            Util.popup("", User.template.authorized, User.Authorized.bindInteraction);//调起授权验证码弹层
                        }
                        if (data.smsSendStatus == "SEND_SUCCESS") {
                            User.Authorized.timing();
                        }
                    } else {
                        dom.querySelector("#addNewCard").checked = true;
                    }
                }
            });
        },
        authoritySendSms: function () {
            /*
             * 发送短信接口（点击重新获取短信）
             * */
            Action.ajax({
                url: action + "/merchantAuthority/shareCardSendSms",
                data: {
                    token: session.getItem("token")
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.smsSendStatus == "SEND_SUCCESS") {
                        User.Authorized.timing();
                    } else {
                        dom.querySelector(".authorized .obtain-btn").disabled = false;
                        dom.querySelector(".authorized .prompt").classList.add("none");
                        dom.querySelector(".authorized .errormsg").classList.remove("none");
                        dom.querySelector(".authorized .errormsg").innerHTML = data.errormsg + "(" + data.errorcode + ")";
                    }
                }
            });
        },
        authoritySendSmsConfirm: function () {
            /*
             * 授权短信确认接口
             * */
            Action.ajax({
                url: action + "/merchantAuthority/shareCardSmsConfirm",
                data: {
                    token: session.getItem("token"),
                    validateCode: session.getItem("verifycode")
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.smsConfirmStatus == "SMS_CONFIRM_SUCCESS") {
                        Util.closePopup();
                        var bankList = "";
                        /*遍历已授权银行卡*/
                        data.bankCardsToShow.forEach(function (bank, index) {
                            bankList += '<div class="option"' + (typeof bank.unusableCause != "undefined" ? 'data-disabled="true"' : '') + 'data-bindid="' + bank.bindid + '"' +
                                '                         data-phoneNo="' + bank.phoneNo + '" data-ypMobile="' + bank.ypMobile + '">' +
                                '                        <div class="bank"><span class="bank-logo ' + bank.bankCode + '"></span>' + bank.bankName + '</div>' +
                                '                        <div class="card-info">************' + bank.cardlater + '<span class="card-type">' +
                                (bank.cardtype == "CREDIT" ? '信用卡' : '储蓄卡') +
                                '                            </span>' +
                                (typeof bank.unusableCause != "undefined" ? '<p class="excess">' + bank.unusableCause + '</p>' : '') +
                                '                        </div>' +
                                '                    </div>';
                        });
                        dom.querySelector(".select .option-wrapper").innerHTML = bankList;
                        session.setItem("isAuthorized", "false");//已经成功拿到授权的银行卡，不需要重新授权
                    } else {
                        dom.querySelector(".authorized .submit").disabled = false;
                        dom.querySelector(".authorized .prompt").classList.add("none");
                        dom.querySelector(".authorized .errormsg").classList.remove("none");
                        dom.querySelector(".authorized .errormsg").innerHTML = data.errormsg + "(" + data.errorcode + ")";
                    }
                }
            });
        },
        //网银支付接口zym
        bankPay: function () {
            var self = this;
            Action.ajax({
                // url: action + "/ebankCustomized/index?token=677b6941-593e-47f9-a0af-ea2ec6610698",
                url: action + "/newpc/ebankCustomized/index",
                data: {
                    token: session.getItem("token")
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.errormsg) {
                        User.Error("#bankPay", data.errormsg + '(' + data.errorcode + ')');

                    } else {
                        var EANKList = Cashier.applyComponent(Cashier.Components.EANKList, data);
                        dom.querySelector("#bankPay").insertAdjacentHTML("beforeend", EANKList);
                        Cashier.events();
                    }
                }
            });
        },
        //二维码支付  接口zym
        QRcode: function () {
            Action.ajax({
                url: action + "/newpc/scanPayToolsSupportCustomized",
                data: {
                    token: session.getItem("token")
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data) {
                        var SCCANPAYList = Cashier.applyComponent(Cashier.Components.SCCANPAYList, data);
                        dom.querySelector("#qrcodePay").insertAdjacentHTML("beforeend", SCCANPAYList);
                        Cashier.events();
                    }
                }
            })
        },
        //账户支付
        accountPay: function () {
            /*
             * 下一步提交表单
             * 提交前对表单校验，校验通过提交到后端
             * */
            var validate = User.form.autoValidate("#accountPayForm");
            var button = this;
            if (validate) {
                button.disabled = true;
                Util.loading("支付等待中...");
                Action.ajax({
                    url: action + "/newpc/accountpay/pay",
                    // url: "mock/accountPay.vm",
                    data: User.form.serialize("#accountPayForm", {
                        token: session.getItem("token")
                    }),
                    success: function (responseText) {
                        var data = JSON.parse(responseText);
                        if (data.bizStatus == "FAIL") {
                            button.disabled = false;
                            Util.hideLoading();

                            dom.querySelector("#verifyCodeImg").click();
                            if (data.retryTimes && data.retryTimes > 0) {
                                User.Error("#accountPayError", "密码输入错误，您还可以重试" + data.retryTimes + "次");
                                return;
                            }
                            if (data.frozenTime && data.frozenTime != null) {
                                User.Error("#accountPayError", "由于密码输入错误次数过多，您的帐号已经被冻结，请" + data.frozenTime + "后重试");
                                return;
                            }
                            User.Error("#accountPayError", data.errormsg + "(" + data.errorcode + ")");
                        } else if (data.bizStatus == "SUCCESS") {
                            /*
                             * 查询支付结果
                             * */
                            button.disabled = false;
                        }
                    }
                });
            }
        },
        getBindCardInfo: function (triggerMode) {
            /*
             * 获取绑卡需要的卡信息
             * triggerMode:触发模式，自动触发或点击下一步触发
             * */
            var self = this;
            var validate = User.form.autoValidate("#bindCardForm");
            if (validate) {
                User.form.disabledSubmit(self);
                dom.querySelector("#bindCardForm .loading").classList.remove("none");
                Action.ajax({
                    url: action + "/bindCard/binInfo",
                    data: User.form.serialize("#bindCardForm", {
                        token: session.getItem("token"),
                        merchantNo: orderInfo.merchantNo,
                        merchantFlowId: orderInfo.merchantFlowId,
                        userNo: orderInfo.userNo,
                        userType: orderInfo.userType,
                        cardType: orderInfo.cardType,
                        bizType: orderInfo.bizType,
                        bizFlowId: orderInfo.bizFlowId,
                        bindCallBackUrl: orderInfo.bindCallBackUrl,
                        bindFrontCallBackUrl: orderInfo.bindFrontCallBackUrl,
                        ext: orderInfo.ext
                    }),
                    success: function (responseText) {
                        dom.querySelector("#bindCardForm .loading").classList.add("none");
                        User.form.enabledSubmit(self);
                        session.setItem("bankAddItem", responseText);
                        var data = JSON.parse(responseText);
                        if (data.bizStatus == "success") {
                            Progress.step(2);
                            var context = {
                                bankCode: data.bindCardInfo.bankCode,
                                bankName: data.bindCardInfo.bankName,
                                cardlater: data.bindCardInfo.cardlater,
                                cardType: data.bindCardInfo.cardType,
                                needname: true,
                                needidno: true,
                                needmobile: true
                            };
                            if (data.bindCardInfo.cardType == "2") {
                                context.needvalid = true;
                                context.needcvv = true;
                            }
                            var addItem = Cashier.applyComponent(Cashier.Components.onlybindCardAddItem, context);
                            self.classList.add("none");
                            dom.querySelector("#addItemWrapper").innerHTML = "";
                            dom.querySelector("#addItemWrapper").insertAdjacentHTML("beforeend", addItem);
                            User.form.validate("#bindCardForm");
                            Cashier.events();
                        } else {
                            if (triggerMode == "manual") {
                                User.Error("#bindCardForm", data.errormsg + '(' + data.errorcode + ')');
                            }
                        }
                    }
                });
            }
        },
        unBindConfrim: function (bindId) {
            /*
             * 解绑确认
             * */
            Util.closePopup();
            Util.loading("正在解绑...");
            Action.ajax({
                url: action + "/newpc/unbindCard",
                data: {
                    token: session.getItem("token"),
                    bindId: bindId
                },
                success: function (responseText) {
                    Util.hideLoading();
                    var data = JSON.parse(responseText);
                    if (data.bizStatus == "success") {
                        dom.querySelector(".select .option-wrapper").removeChild(dom.querySelector('#card' + bindId));
                        if (dom.querySelectorAll(".select .option-wrapper .option").length) {
                            /*
                             * 绑卡列表不为空，更新下拉列表
                             * */
                            dom.querySelector(".select .option-wrapper .option").click();
                            dom.querySelector(".select").classList.remove("open");
                        } else {
                            /*
                             * 绑卡列表为空，跳转首次支付
                             * */
                            dom.querySelector("#ncpayToolWrapper").innerHTML = "";
                            dom.querySelector("#ncpayToolWrapper").insertAdjacentHTML("beforeEnd", Cashier.Components.NCPAYFIRST.content);
                            Cashier.events();
                        }
                    } else {
                        Util.popup(null, Cashier.applyComponent(Cashier.Components.UIBIND_ERROR, data));
                        Cashier.events();
                    }
                }
            });
        },
        bindPayRouter: function () {
            /*
             * 绑卡支付路由
             * */
            Action.ajax({
                url: action + "/newpc/customeized/bindcardlist",
                data: {
                    token: session.getItem("token")
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.errormsg) {
                        User.Error("#bindPayToolWrapper", data.errormsg + "(" + data.errorcode + ")", "component-error");
                        return;
                    }
                    if (data.hasOwnProperty("bankCardsToShow") && data.bankCardsToShow.length) {
                        /*
                         * 绑卡支付已经绑卡
                         * */
                        var context = {bindCards: data.bankCardsToShow, showChangeCard: data.showChangeCard};
                        dom.querySelector("#bindPayToolWrapper").insertAdjacentHTML("beforeEnd", Cashier.applyComponent(Cashier.Components.BINDCARDPAY, context));
                    } else {
                        /*
                         * 绑卡支付首次支付
                         * */
                        dom.querySelector("#bindPayToolWrapper").insertAdjacentHTML("beforeEnd", Cashier.Components.BINDPAYFIRST.content);
                    }
                    Cashier.events();
                }
            });
        },
        bindCardPayOrderInfo: function () {
            /*
             * 绑卡支付获取订单信息
             * */
            Action.ajax({
                url: action + "/bindCard/prepareBindCard",
                data: {
                    token: session.getItem("token")
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.bizStatus == "success") {
                        orderInfo = data.bindCardMerchantRequest;
                        Action.bindPayRouter();
                    } else {
                        User.Error("#bindPayToolWrapper", data.errormsg + "(" + data.errorcode + ")", "component-error");
                    }

                }
            });
        },
        getBindPayAddItem: function (triggerMode) {
            /*
             * 获取绑卡支付补充项
             * triggerMode:触发模式，自动触发或点击下一步触发
             * */
            var self = this;
            var validate = User.form.autoValidate(".bind-card-form");
            if (validate) {
                User.form.disabledSubmit(self);
                dom.querySelector(".bind-card-form .loading").classList.remove("none");
                Action.ajax({
                    url: action + "/bindCard/binInfo",
                    data: User.form.serialize(".bind-card-form", {
                        token: session.getItem("token"),
                        merchantNo: orderInfo.merchantNo,
                        merchantFlowId: orderInfo.merchantFlowId,
                        userNo: orderInfo.userNo,
                        userType: orderInfo.userType,
                        cardType: orderInfo.cardType,
                        bizType: orderInfo.bizType,
                        bizFlowId: orderInfo.bizFlowId,
                        bindCallBackUrl: orderInfo.bindCallBackUrl,
                        bindFrontCallBackUrl: orderInfo.bindFrontCallBackUrl,
                        ext: orderInfo.ext
                    }),
                    success: function (responseText) {
                        dom.querySelector(".bind-card-form .loading").classList.add("none");
                        User.form.enabledSubmit(self);
                        session.setItem("bankAddItem", responseText);
                        var data = JSON.parse(responseText);
                        if (data.bizStatus == "success") {
                            var context = {
                                bankCode: data.bindCardInfo.bankCode,
                                bankName: data.bindCardInfo.bankName,
                                cardlater: data.bindCardInfo.cardlater,
                                cardType: data.bindCardInfo.cardType,
                                needname: true,
                                needidno: true,
                                needmobile: true
                            };
                            if (data.bindCardInfo.cardType == 2) {
                                context.needvalid = true;
                                context.needcvv = true;
                            }
                            var addItem = Cashier.applyComponent(Cashier.Components.bindCardPayAddItem, context);
                            self.classList.add("none");
                            dom.querySelector("#addItemWrapper").innerHTML = "";
                            dom.querySelector("#addItemWrapper").insertAdjacentHTML("beforeend", addItem);
                            User.form.validate(".bind-card-form");
                            Cashier.events();
                        } else {
                            if (triggerMode == "manual") {
                                User.Error(".bind-card-form", data.errormsg + '(' + data.errorcode + ')');
                            }
                        }
                    }
                });
            }
        },
        bindCardConfrim: function () {
            /*
             * 确认绑卡
             * */
            var self = this;
            var validate = User.form.autoValidate(".bind-card-form");
            if (validate) {
                User.form.disabledSubmit(self);
                Action.ajax({
                    url: action + "/bindCard/authBindCardConfirm",
                    data: User.form.serialize(".bind-card-form", {
                        token: session.getItem("token"),
                        merchantNo: orderInfo.merchantNo,
                        merchantFlowId: orderInfo.merchantFlowId,
                        userNo: orderInfo.userNo,
                        userType: orderInfo.userType,
                        cardType: orderInfo.cardType,
                        bizType: orderInfo.bizType,
                        bizFlowId: orderInfo.bizFlowId,
                        bindCallBackUrl: orderInfo.bindCallBackUrl,
                        bindFrontCallBackUrl: orderInfo.bindFrontCallBackUrl,
                        ext: orderInfo.ext,
                        nopOrderId: orderInfo.nopOrderId || "",
                        requestFlowId: orderInfo.requestFlowId || ""
                    }),
                    success: function (responseText) {
                        var data = JSON.parse(responseText);
                        if (data.smsStatus) {
                            User.form.enabledSubmit(self);
                            User.Error(".bind-card-form", data.errormsg + '(' + data.errorcode + ')');
                            return;
                        }
                        if (data.bizStatus == "bindCardSuccess") {
                            Progress.step(3);
                            dom.querySelector("#bindCardInfo").innerHTML = "";
                            dom.querySelector("#bindCardInfo").insertAdjacentHTML("beforeend", Cashier.applyComponent(Cashier.Components.BIND_CARD_SUCCESS, data));
                        } else if (data.bizStatus == "bindCardFail") {
                            /*
                             * 失败
                             * */
                            dom.querySelector("#bindCardInfo").innerHTML = "";
                            dom.querySelector("#bindCardInfo").insertAdjacentHTML("beforeend", Cashier.applyComponent(Cashier.Components.BIND_CARD_FAIL));
                            Cashier.events();
                        } else if (data.bizStatus == "bindCardUnKnown") {
                            /*
                             * 查询
                             * */
                            dom.querySelector("#bindCardInfo").innerHTML = "";
                            dom.querySelector("#bindCardInfo").insertAdjacentHTML("beforeend", Cashier.applyComponent(Cashier.Components.BIND_CARD_QUERY));
                            Cashier.events();
                        } else {
                            User.form.enabledSubmit(self);
                            User.Error(".bind-card-form", data.errormsg + '(' + data.errorcode + ')');
                        }
                    },
                    error: function () {
                        User.form.enabledSubmit(self);
                    }
                });
            }
        },
        bindCardPayConfrim: function (event) {
            /*
             * 绑卡支付确认
             * */
            var self = this;
            var validate = User.form.autoValidate(".bind-card-form");
            if (validate) {
                User.form.disabledSubmit(self);
                Action.ajax({
                    url: action + "/bindCard/authBindCardConfirm",
                    data: User.form.serialize(".bind-card-form", {
                        token: session.getItem("token"),
                        merchantNo: orderInfo.merchantNo,
                        merchantFlowId: orderInfo.merchantFlowId,
                        userNo: orderInfo.userNo,
                        userType: orderInfo.userType,
                        cardType: orderInfo.cardType,
                        bizType: orderInfo.bizType,
                        bizFlowId: orderInfo.bizFlowId,
                        bindCallBackUrl: orderInfo.bindCallBackUrl,
                        bindFrontCallBackUrl: orderInfo.bindFrontCallBackUrl,
                        ext: orderInfo.ext,
                        nopOrderId: orderInfo.nopOrderId,
                        requestFlowId: orderInfo.requestFlowId
                    }),
                    success: function (responseText) {
                        var data = JSON.parse(responseText);
                        var sendData = {};
                        if (data.bizStatus == "bindPayNeedItem") {
                            sendData = data.authBindConfirm;
                            sendData.token = session.getItem("token");
                            sendData.cardno = data.needBankCardItem.cardno || "";
                            sendData.owner = data.needBankCardItem.owner || "";
                            sendData.idno = data.needBankCardItem.idno || "";
                            sendData.phoneNo = BASE64.encoder(data.needBankCardItem.phoneNo) || "";
                            sendData.ypMobile = BASE64.encoder(data.needBankCardItem.ypMobile) || "";
                            sendData.avlidDate = data.needBankCardItem.avlidDate || "";
                            sendData.cvv = data.needBankCardItem.cvv || "";
                            sendData.smsType = data.smsType || "";
                            if (data.smsType && (data.needBankCardItem.hasOwnProperty("needSurportDTO") && data.needBankCardItem.needSurportDTO.bankPWDIsNeed)) {
                                Util.popup("", User.template.psdCode, function () {
                                    User.form.enabledSubmit(self);
                                    User.form.validate("#smsForm");
                                    dom.querySelector("#smsForm").bankPWD.addEventListener("input", function () {
                                        this.value.length == 6 ? dom.querySelector("#smsForm .validate-code").disabled = false : dom.querySelector("#smsForm .validate-code").disabled = true;
                                        sendData.bankPWD = BASE64.encoder(dom.querySelector("#smsForm").bankPWD.value);
                                    }, false);
                                    dom.querySelector("#smsForm .validate-code").onclick = Action.getBindSms.bind(dom.querySelector("#smsForm .validate-code"), event, sendData);
                                    dom.querySelector("#smsForm .confirm").onclick = function () {
                                        if (User.form.autoValidate("#smsForm")) {
                                            sendData.bankPWD = BASE64.encoder(dom.querySelector("#smsForm").bankPWD.value);
                                            sendData.verifycode = dom.querySelector("#smsForm").verifycode.value;
                                            Action.bindPayConfirm.call(Action, true, sendData);
                                        }
                                    }
                                });
                            } else if (data.smsType) {
                                Util.popup("", User.template.smsCode, function () {
                                    User.form.enabledSubmit(self);
                                    dom.querySelector("#smsForm .validate-code").onclick = Action.getBindSms.bind(dom.querySelector("#smsForm .validate-code"), event, sendData);
                                    dom.querySelector("#smsForm .confirm").onclick = function () {
                                        if (User.form.autoValidate("#smsForm")) {
                                            sendData.verifycode = dom.querySelector("#smsForm").verifycode.value;
                                            Action.bindPayConfirm.call(Action, true, sendData);
                                        }
                                    }
                                    User.form.hideErrorMessage(dom.querySelector("#smsForm"));
                                    dom.querySelector("#smsForm .validate-code").click();
                                });
                            } else if (data.needBankCardItem.hasOwnProperty("needSurportDTO") && data.needBankCardItem.needSurportDTO.bankPWDIsNeed) {
                                Util.popup("", User.template.bindCardPayPas, function () {
                                    User.form.enabledSubmit(self);
                                    dom.querySelector("#smsForm .confirm").onclick = function () {
                                        if (User.form.autoValidate("#smsForm")) {
                                            sendData.bankPWD = BASE64.encoder(dom.querySelector("#smsForm").bankPWD.value);
                                            Action.bindPayConfirm.call(Action, false, sendData);
                                        }
                                    }
                                });
                            }
                        } else if (data.bizStatus == "bindPaySuccess") {
                            /*
                             * 成功
                             * */
                            User.form.enabledSubmit(self);
                        } else if (data.bizStatus == "bindPayUnknown" || data.bizStatus == "bindPayFail") {
                            User.form.enabledSubmit(self);
                            User.Error(".bind-card-form", data.errormsg + '(' + data.errorcode + ')');
                        }
                    },
                    error: function () {
                        User.form.enabledSubmit(self);
                    }
                });
            }
        },
        getBindCardsmsCode: function () {
            /*
             * 绑卡首次获取短验
             * */
            var self = this;
            var validate = User.form.autoValidate(".bind-card-form", "smsCode");
            dom.querySelector(".verify-code").focus();
            if (validate) {
                User.form.disabledSubmit(self);
                Action.ajax({
                    url: action + "/bindCard/authBindCard",
                    data: User.form.serialize(".bind-card-form", {
                        token: session.getItem("token"),
                        merchantNo: orderInfo.merchantNo,
                        merchantFlowId: orderInfo.merchantFlowId,
                        userNo: orderInfo.userNo,
                        userType: orderInfo.userType,
                        cardType: orderInfo.cardType,
                        bizType: orderInfo.bizType,
                        bizFlowId: orderInfo.bizFlowId,
                        bindCallBackUrl: orderInfo.bindCallBackUrl,
                        bindFrontCallBackUrl: orderInfo.bindFrontCallBackUrl,
                        ext: orderInfo.ext
                    }),
                    success: function (responseText) {
                        var data = JSON.parse(responseText);
                        if (data.bizStatus == "success") {
                            orderInfo.nopOrderId = data.authBindRequest.nopOrderId;
                            orderInfo.requestFlowId = data.authBindRequest.requestFlowId;
                            User.timing(function (second) {
                                self.textContent = second + "s";
                            }, function () {
                                self.removeEventListener("click", Action.getBindCardsmsCode, false);
                                self.addEventListener("click", Action.getBindCardAgainsmsCode, false);
                                User.form.enabledSubmit(self);
                                self.textContent = "重新获取";
                            });
                        } else {
                            User.Error(".bind-card-form", data.errormsg + '(' + data.errorcode + ')');
                            User.form.enabledSubmit(self);
                        }
                    },
                    error: function () {
                        User.form.enabledSubmit(self);
                    }
                });
            }
        },
        getBindCardAgainsmsCode: function () {
            /*
             * 绑卡重新获取短验
             * */
            var self = this;
            var validate = User.form.autoValidate(".bind-card-form", "smsCode");
            dom.querySelector(".verify-code").focus();
            if (validate) {
                User.form.disabledSubmit(self);
                Action.ajax({
                    url: action + "/bindCard/authBindCardSMS",
                    data: User.form.serialize(".bind-card-form", {
                        token: session.getItem("token"),
                        merchantNo: orderInfo.merchantNo,
                        merchantFlowId: orderInfo.merchantFlowId,
                        nopOrderId: orderInfo.nopOrderId,
                        requestFlowId: orderInfo.requestFlowId,
                        userNo: orderInfo.userNo,
                        userType: orderInfo.userType,
                        cardType: orderInfo.cardType,
                        bizType: orderInfo.bizType,
                        bizFlowId: orderInfo.bizFlowId,
                        bindCallBackUrl: orderInfo.bindCallBackUrl,
                        bindFrontCallBackUrl: orderInfo.bindFrontCallBackUrl,
                        ext: orderInfo.ext
                    }),
                    success: function (responseText) {
                        var data = JSON.parse(responseText);
                        if (data.bizStatus == "success") {
                            orderInfo.nopOrderId = data.authBindSMS.nopOrderId;
                            orderInfo.requestFlowId = data.authBindSMS.requestFlowId;
                            User.timing(function (second) {
                                self.textContent = second + "s";
                            }, function () {
                                User.form.enabledSubmit(self);
                                self.textContent = "重新获取";
                            });
                        } else {
                            User.Error(".bind-card-form", data.errormsg + '(' + data.errorcode + ')');
                            User.form.enabledSubmit(self);
                        }
                    },
                    error: function () {
                        User.form.enabledSubmit(self);
                    }
                });
            }
        },
        queryBindCard: function () {
            /*
             * 查询绑卡结果
             * */
            var self = this;
            User.form.disabledSubmit(self);
            Action.ajax({
                url: action + "/bindCard/queryBindCardOrder",
                data: {
                    token: session.getItem("token"),
                    nopOrderId: orderInfo.nopOrderId,
                    requestFlowId: orderInfo.requestFlowId,
                    merchantNo: orderInfo.merchantNo,
                    merchantFlowId: orderInfo.merchantFlowId,
                    userNo: orderInfo.userNo,
                    userType: orderInfo.userType,
                    cardType: orderInfo.cardType,
                    bizType: orderInfo.bizType,
                    bizFlowId: orderInfo.bizFlowId,
                    bindCallBackUrl: orderInfo.bindCallBackUrl,
                    bindFrontCallBackUrl: orderInfo.bindFrontCallBackUrl,
                    ext: orderInfo.ext
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.orderStatus == "bindCardSuccess") {
                        Progress.step(3);
                        document.querySelector("#bindCardInfo").innerHTML = "";
                        document.querySelector("#bindCardInfo").insertAdjacentHTML("beforeend", Cashier.applyComponent(Cashier.Components.BIND_CARD_SUCCESS, data));
                    } else if (data.orderStatus == "bindCardFail") {
                        document.querySelector("#bindCardInfo").innerHTML = "";
                        document.querySelector("#bindCardInfo").insertAdjacentHTML("beforeend", Cashier.applyComponent(Cashier.Components.BIND_CARD_FAIL, data));
                        Cashier.events();
                    } else {
                        //dom.querySelector("#errormsg").innerHTML = '[' + data.errorcode + ']' + data.errormsg;
                        User.form.enabledSubmit(self);
                    }
                },
                error: function () {
                    User.form.enabledSubmit(self);
                }
            });
        }
    };
    global.Action = Action;
})
(window);

/**
 * Created by ying-xia on 17/6/13.
 */
(function (exports) {
    var Components = {
        NCPAY: {//首次支付
            title: '银行卡快捷支付',
            set context(context) {
                this.compile = context;
            },
            get content() {
                Action.ncpayRouter();
                return '<div id="ncpayToolWrapper"></div>';
            }
        },
        BK_ZF: {//绑卡支付
            title: '绑卡支付',
            set context(context) {
                this.compile = context;
            },
            get content() {
                Action.bindCardPayOrderInfo();
                return '<div id="bindPayToolWrapper"></div>';
            }
        },
        BINDPAYFIRST: {//绑卡首次支付
            title: '绑卡首次支付',
            set context(context) {
                this.compile = context;
            },
            get content() {
                return '<div id="quickPay" class="cashier-pay-tool">' + ' <div class="limit">' +
                    '        <input type="checkbox" id="showLimit">' +
                    '        <label for="showLimit" class="handler" events="{click:Action.limit}">可用银行列表与限额</label>' +
                    '        <div class="cont">' +
                    '            <label for="showLimit" class="close icon">&#xe60e;</label>' +
                    '            <p class="title">支持银行列表与限额</p>' +
                    '            <div class="caption"><span class="bank">支持银行</span><span class="card-type">卡种</span><span' +
                    '                    class="single">单笔限额（元）</span><span' +
                    '                    class="day">单日限额（元）</span><span class="month">单月限额（元）</span>' +
                    '            </div>' +
                    '            <ul class="tb" id="supportBankList">' +
                    '            </ul>' +
                    '        </div>' +
                    '    </div>' + '<form id="quickPayForm" class="bind-card-form" events="{auto:Controller.quickPayValidate}">' +
                    '       <div class="sington"><label><span class="title">银行卡</span><input type="text" autocomplete="off"' +
                    '                                                                        placeholder="请输入个人银行卡号"' +
                    '                                                                        name="cardno"' +
                    '                                                                        maxlength="23" encrypt="true" autofocus events="{input:Controller.bindPayCardNoInput}"><img' +
                    '               src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/loading.gif" width="24" class="loading none"> </label>' +
                    '       </div>' +
                    '       <div id="addItemWrapper"></div>' +
                    '       <button type="button" class="fn-btn" events="{click:Controller.getBindPayAddItem}">下一步</button>' +
                    '   </form></div>';
            }
        },
        NCPAYFIRST: {//首次支付
            title: '银行卡快捷支付',
            set context(context) {
                this.compile = context;
            },
            get content() {
                return '<div id="quickPay" class="cashier-pay-tool">' + ' <div class="limit">' +
                    '        <input type="checkbox" id="showLimit">' +
                    '        <label for="showLimit" class="handler" events="{click:Action.limit}">可用银行列表与限额</label>' +
                    '        <div class="cont">' +
                    '            <label for="showLimit" class="close icon">&#xe60e;</label>' +
                    '            <p class="title">支持银行列表与限额</p>' +
                    '            <div class="caption"><span class="bank">支持银行</span><span class="card-type">卡种</span><span' +
                    '                    class="single">单笔限额（元）</span><span' +
                    '                    class="day">单日限额（元）</span><span class="month">单月限额（元）</span>' +
                    '            </div>' +
                    '            <ul class="tb" id="supportBankList">' +
                    '            </ul>' +
                    '        </div>' +
                    '    </div>' + '<form id="quickPayForm" class="bind-card-form" events="{auto:Controller.quickPayValidate}">' +
                    '       <div class="sington"><label><span class="title">银行卡</span><input type="text" autocomplete="off"' +
                    '                                                                        placeholder="请输入个人银行卡号"' +
                    '                                                                        name="cardno"' +
                    '                                                                        maxlength="23" encrypt="true" autofocus events="{input:Controller.cardNoInput}"><img' +
                    '               src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/loading.gif" width="24" class="loading none"> </label>' +
                    '       </div>' +
                    '       <div id="addItemWrapper"></div>' +
                    '       <button type="button" class="fn-btn" events="{click:Controller.getAddItem}">下一步</button>' +
                    '   </form></div>';
            }
        },
        NCPAYBIND: {//绑卡支付
            title: '银行卡快捷支付',
            set context(context) {
                this.compile = context;
            },
            get content() {
                var item = "";
                var self = this;
                this.compile.bindCards.forEach(function (card) {
                    item += '<div id="card' + card.bindid + '" class="option" data-disabled="' + (card.unusableCause ? "true" : "false") + '" data-bindid="' + card.bindid + '" data-phoneno="' + card.phoneNo + '" data-ypmobile="' + card.ypMobile + '">' +
                        '                        <div class="bank"><span class="bank-logo ' + card.bankCode + '"></span>' + card.bankName + '</div>' +
                        '                        <div class="card-info">************' + card.cardlater + '<span class="card-type">' +
                        (card.cardtype == "CREDIT" ? "信用卡" : "储蓄卡") +
                        '                                                            </span>' +
                        (card.unusableCause ? ' <p class="excess">' + card.unusableCause + '</p>' : "") +
                        '                                                    </div>' +
                        (self.compile.unBindCard ? '<div class="unbind" data-bindid="' + card.bindid + '">解除绑定</div>' : '') +
                        '                    </div>';
                });
                return '<div id="quickPay" class="cashier-pay-tool"><input type="checkbox" id="addNewCard">' + ' <div class="limit">' +
                    '        <input type="checkbox" id="showLimit">' +
                    '        <label for="showLimit" class="handler" events="{click:Action.limit}">可用银行列表与限额</label>' +
                    '        <div class="cont">' +
                    '            <label for="showLimit" class="close icon">&#xe60e;</label>' +
                    '            <p class="title">支持银行列表与限额</p>' +
                    '            <div class="caption"><span class="bank">支持银行</span><span class="card-type">卡种</span><span' +
                    '                    class="single">单笔限额（元）</span><span' +
                    '                    class="day">单日限额（元）</span><span class="month">单月限额（元）</span>' +
                    '            </div>' +
                    '            <ul class="tb" id="supportBankList">' +
                    '            </ul>' +
                    '        </div>' +
                    '    </div>' + '<div class="select-wrapper" events="{auto:Controller.select}">' +
                    '            <div class="select">' +
                    '                <div class="current-selected icon"></div>' +
                    '                <div class="option-wrapper">' + item +
                    '                    </div>' +
                    '            </div>' +
                    (this.compile.showChangeCard ? '<label for="addNewCard"><span class="add-bankCard-btn" events="{click:Controller.addBankCardRouter}"><i class="icon">&#xe62a;</i>添加银行卡</span></label>' : '') +
                    '                        <div id="bindItemFormWrapper"></div>' +
                    '            <button type="button" id="bindPayBtn" events="{click:Action.bindPay}">立即支付</button>' +
                    '        </div>' + '<form id="quickPayForm" class="bind-card-form" events="{auto:Controller.quickPayValidate}">' +
                    '                        <label for="addNewCard" class="back-my-card"><i class="icon">&#xe631;</i>返回我的银行卡</label>' +
                    '                        <div class="sington"><label><span class="title">银行卡</span><input type="text" encrypt="true" autocomplete="off" placeholder="请输入个人银行卡号" name="cardno" maxlength="23" autofocus events="{input:Controller.cardNoInput}"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/loading.gif" width="24" class="loading none"> </label>' +
                    '            </div>' +
                    '            <div id="addItemWrapper"></div>' +
                    '            <button type="button" class="fn-btn" events="{click:Controller.getAddItem}">下一步</button>' +
                    '        </form></div>';
            }
        },
        BINDCARDPAY: {//绑卡支付
            title: '银行卡快捷支付',
            set context(context) {
                this.compile = context;
            },
            get content() {
                var item = "";
                var self = this;
                this.compile.bindCards.forEach(function (card) {
                    item += '<div id="card' + card.bindid + '" class="option" data-disabled="' + (card.unusableCause ? "true" : "false") + '" data-bindid="' + card.bindid + '" data-phoneno="' + card.phoneNo + '" data-ypmobile="' + card.ypMobile + '">' +
                        '                        <div class="bank"><span class="bank-logo ' + card.bankCode + '"></span>' + card.bankName + '</div>' +
                        '                        <div class="card-info">************' + card.cardlater + '<span class="card-type">' +
                        (card.cardtype == "CREDIT" ? "信用卡" : "储蓄卡") +
                        '                                                            </span>' +
                        (card.unusableCause ? ' <p class="excess">' + card.unusableCause + '</p>' : "") +
                        '                                                    </div>' +
                        (self.compile.unBindCard ? '<div class="unbind" data-bindid="' + card.bindid + '">解除绑定</div>' : '') +
                        '                    </div>';
                });
                return '<div id="quickPay" class="cashier-pay-tool"><input type="checkbox" id="addNewCard">' + ' <div class="limit">' +
                    '        <input type="checkbox" id="showLimit">' +
                    '        <label for="showLimit" class="handler" events="{click:Action.limit}">可用银行列表与限额</label>' +
                    '        <div class="cont">' +
                    '            <label for="showLimit" class="close icon">&#xe60e;</label>' +
                    '            <p class="title">支持银行列表与限额</p>' +
                    '            <div class="caption"><span class="bank">支持银行</span><span class="card-type">卡种</span><span' +
                    '                    class="single">单笔限额（元）</span><span' +
                    '                    class="day">单日限额（元）</span><span class="month">单月限额（元）</span>' +
                    '            </div>' +
                    '            <ul class="tb" id="supportBankList">' +
                    '            </ul>' +
                    '        </div>' +
                    '    </div>' + '<div class="select-wrapper" events="{auto:Controller.select}">' +
                    '            <div class="select">' +
                    '                <div class="current-selected icon"></div>' +
                    '                <div class="option-wrapper">' + item +
                    '                    </div>' +
                    '            </div>' +
                    (this.compile.showChangeCard ? '<label for="addNewCard"><span class="add-bankCard-btn" events="{click:Controller.addBankCardRouter}"><i class="icon">&#xe62a;</i>添加银行卡</span></label>' : '') +
                    '                        <div id="bindItemFormWrapper"></div>' +
                    '            <button type="button" id="bindPayBtn" events="{click:Action.bindPay}">立即支付</button>' +
                    '        </div>' + '<form id="quickPayForm" class="bind-card-form" events="{auto:Controller.quickPayValidate}">' +
                    '                        <label for="addNewCard" class="back-my-card"><i class="icon">&#xe631;</i>返回我的银行卡</label>' +
                    '                        <div class="sington"><label><span class="title">银行卡</span><input type="text" encrypt="true" autocomplete="off" placeholder="请输入个人银行卡号" name="cardno" maxlength="23" autofocus events="{input:Controller.bindPayCardNoInput}"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/loading.gif" width="24" class="loading none"> </label>' +
                    '            </div>' +
                    '            <div id="addItemWrapper"></div>' +
                    '            <button type="button" class="fn-btn" events="{click:Controller.getBindPayAddItem}">下一步</button>' +
                    '        </form></div>';
            }
        },
        NCPAYPASS: {//透传支付
            title: '银行卡快捷支付',
            set context(context) {
                this.compile = context;
            },
            get content() {
                var item = this.compile.merchantSamePersonConf ? '<p class="prompt">请使用<span class="emp">本人信息</span>进行支付，支付成功后不可更改</p>' : '';
                if (this.compile.needname) {
                    item += '<div class="sington"><label><span class="title">姓名</span><input type="text" encrypt="true" autocomplete="off"' +
                        '                                                                placeholder="请输入您的真实姓名"' +
                        '                                                                     name="name"' + (this.compile.owner ? "readonly value=" + this.compile.owner : "") + '></label>' +
                        '</div>';
                }
                if (this.compile.needidno) {
                    item += '<div class="sington"><label><span class="title">身份证号</span><input type="text" encrypt="true" maxlength="18" autocomplete="off"' +
                        '                                                                placeholder="请输入您的身份证号"' +
                        '                                                                     name="idno"' + (this.compile.idno ? "readonly value=" + this.compile.idno : "") + '></label>' +
                        '</div>';
                }
                if (this.compile.needvalid) {
                    item += '<div class="sington"><label><span class="title">有效期</span><input type="text" encrypt="true" autocomplete="off"' +
                        '                                                                      placeholder="如：09/15 请输入0915"' +
                        '                                                                      name="valid"' +
                        '                                                                      maxlength="4"><div class="sample valid"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/5nor_03.png"></div></label>' +
                        '</div>';
                }
                if (this.compile.needcvv) {
                    item += '<div class="sington"><label><span class="title">C V V</span><input type="password" encrypt="true" autocomplete="off"' +
                        '                                                                        placeholder="信用卡背面后三位数字"' +
                        '                                                                        name="cvv2"' +
                        '                                                                        maxlength="3"><div class="sample cvv"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/5nor_06.png"></div></label>' +
                        '</div>';
                }
                if (this.compile.needpass) {
                    item += '<div class="sington"><label><span class="title">取款密码</span><input type="password" encrypt="true" autocomplete="off"' +
                        '                                                                       placeholder="请输入取款密码"' +
                        '                                                                       name="pass"' +
                        '                                                                       maxlength="6"></label>' +
                        '</div>';
                }
                if (this.compile.needmobile) {
                    item += '<div class="sington"><label><span class="title">手机号</span><input type="text" encrypt="true" maxlength="11" autocomplete="off"' +
                        '                                                                placeholder="请输入银行预留手机号"' +
                        '                                                                     name="phone"' + (this.compile.phoneNo ? "readonly value=" + this.compile.phoneNo : "") + '></label>' +
                        '</div>';
                }
                return '<div id="quickPay" class="cashier-pay-tool"><form id="quickPayForm" class="bind-card-form" events="{auto:Controller.quickPayValidate}">' +
                    '    <div class="card-bin-wrapper pass-card-bin clearfix">' +
                    '        <span class="title">银行卡</span>' +
                    '        <div class="card-bin">' +
                    '            <div class="bank"><span class="bank-logo ' + this.compile.bankCode + '"></span>' + this.compile.bankName + '</div>' +
                    '            <div class="bank-info">' +
                    '                **********' + this.compile.cardlater + '<span class="card-type">' +
                    (this.compile.cardtype == "CREDIT" ? "信用卡" : "储蓄卡") +
                    '                                    </span>' +
                    '            </div>' +
                    '        </div>' +
                    '    </div>' + item +
                    '    <input type="hidden" value="' + this.compile.bankCode + '" readonly="" name="bankCode">' +
                    '    <input type="hidden" value="' + this.compile.cardtype + '" readonly="" name="cardType">' +
                    '    <input type="hidden" value="' + this.compile.bankName + '" readonly="" name="bankName">' +
                    '    <input type="hidden" encrypt="true" value="' + this.compile.cardNo + '" readonly="" name="cardno">' + '<div class="sington"><label><input type="checkbox" name="agreement" id="agreement" checked events="{click:Controller.disagree}"><span' +
                    '        class="checkbox icon"></span>同意</label><a' +
                    '        href="javascript:void(0)" events="{click:Controller.showAgreement}">《服务协议》</a>' + (this.compile.bankCode == "BOC" ? '<span class="china-bank-agree">和<a href="javascript:void(0)" events="{click:Controller.showBOCAgreement}">《中国银行快捷支付服务协议》</a></span>' : '') +
                    '</div>' +
                    '    <button type="button" id="firstPayBtn" class="pass-fn-btn" events="{click:Action.firstPay}">下一步</button>' +
                    '</form></div>';
            }
        },
        EANK: {
            title: '网银支付',
            set context(context) {
                this.compile = context;
            },
            get content() {
                return '<div class="cashier-pay-tool bank-pay-list" id="bankPay" ajax="Action.bankPay"></div>';
            }
        },
        EANKList: {
            title: '网银支付',  //个人支付  企业支付  对应的银行列表
            set context(context) {
                this.compile = context;
            },
            get content() {
                var item = '';
                var item0 = '<div class="tab-wrapper" events="{auto:User.Bank.more,auto:User.Tab.bind,auto:User.Bank.select}">';

                if (this.compile.b2cBanks) {
                    item0 += '<span class="tab">个人支付</span>';
                    item += '<ul class="tab-content bank-list clearfix">';
                    array(this.compile.b2cBanks).forEach(function (it, index) {
                        item += '    <li class="' + (index == 0 ? 'active' : '') + '"  bankaccounttype="B2C" isneedclient="' + it.needClient + '" bankcode="' + it.bankCode + '" bankname="' + it.bankName + '">' +
                            '      <span class="bank-logo ' + it.bankCode + '"></span>' + it.bankName +
                            '      <p class="card-type">' +
                            '        <span>' + (it.cardType == "ONLYBY" ? "信用卡" : "储蓄卡") + '</span></p>' +
                            '    </li>'
                    })
                    item += '</ul>'
                }
                if (this.compile.b2bBanks) {
                    item0 += '<span class="tab" id="b2bTab" events="{auto:Controller.showClientId}">企业支付</span>';
                    item += '<ul class="tab-content bank-list clearfix">';
                    array(this.compile.b2bBanks).forEach(function (it, index) {
                        item += '    <li class="' + (index == 0 ? 'active' : '') + '" bankaccounttype="B2B" isneedclient="' + it.needClient + '" bankcode="' + it.bankCode + '" bankname="' + it.bankName + '">' +
                            '      <span class="bank-logo ' + it.bankCode + '"></span>' + it.bankName +
                            '      <p class="card-type">' +
                            '        <span>' + (it.cardType == "ONLYBY" ? "信用卡" : "储蓄卡") + '</span></p>' +
                            '    </li>'
                    })
                    item += '</ul>'
                }
                item0 += '</div>';
                item0 += '<div class="tab-content-wrapper">';
                return item0 + item + '</div><button type="button" events="{click:Action.eBankPayRoute}">下一步</button>';
            }
        },
        ZF_ZHZF: {
            title: '账户支付',
            set context(context) {
                this.compile = context;
            },
            get content() {
                return '<div id="accountPay" class="cashier-pay-tool">' +
                    '        <form id="accountPayForm" events="{auto:Controller.accountPayFormPayValidate}">' +
                    '            <div class="sington"><label><span class="title">企业账户名</span><input encrypt="true" type="text" placeholder="请输入企业账户名"' +
                    '                                                                               maxlength="50" name="userAccount"' +
                    '                                                                               autofocus=""></label>' +
                    '            </div>' +
                    '            <div class="sington"><label><span class="title">交易密码</span><input encrypt="true" type="password" placeholder="请输入交易密码"' +
                    '                                                                              maxlength="32" name="tradePassword"' +
                    '                                                                              autocomplete="off"></label>' +
                    '            </div>' +
                    '            <div class="sington"><label><span class="title">验证码</span><input type="text" maxlength="6" placeholder="输入您的验证码"' +
                    '                                                                             class="verify-code" name="captcha"></label><img' +
                    '                    class="verifycode-img" id="verifyCodeImg"' +
                    '                    src="' + session.getItem("contextUrl") + '/captcha/refresh?token=' + session.getItem("token") + '" events="{click:Controller.verifyCode}" width="122" height="52" title="换一张">' +
                    '            </div>' +
                    '            <div id="accountPayError" class="back-error"></div>' +
                    '            <div class="sington">' +
                    '                <button id="passPayButton" type="button" events="click:Action.accountPay">下一步</button>' +
                    '            </div>' +
                    '            <a id="forgetTradingPassword" href="javascript:void(0)" events="{click:Controller.forgetTradingPassword}">忘记交易密码</a>' +
                    '        </form>' +
                    '    </div>';
            }
        },
        SCCANPAY: {
            title: '二维码支付',
            set context(context) {
                this.compile = context;
            },
            get content() {
                return '<div id="qrcodePay" class="cashier-pay-tool" ajax="Action.QRcode"></div>';
            }
        },
        SCCANPAYList: {
            title: '二维码支付',
            set context(context) {
                this.compile = context;
            },
            get content() {
                var itemIcon = '';  //存放icon
                var item = '<div class="qrcode">' +
                    '                <div class="tip"></div>' +
                    '        <div class="qrcode-item-wrapper">' +
                    '<p class="qrcode-load-error none">二维码加载失败！<button class="refresh-qrcode">刷新</button></p>';

                if (this.compile.wechatLogo) {//weixin

                    item += '            <div class="qrcode-item weixin">' +
                        '                <div class="qrcode-area"><img' +
                        '                          data-original="' + session.getItem("contextUrl") + '/newpc/wx/qrcode/request?token=' + session.getItem("token") + '" class="qrcode-stream" id="weixinQrcode"' +
                        '                        >' +
                        '                    <i class="icon logo weixin">&#xe611;</i>' +
                        '                </div>' +
                        '            </div>';
                    itemIcon += '           <i class="icon weixin" data-tip="打开微信扫一扫直接付款" id="weixinQrIcon">&#xe611;</i>';
                }
                if (this.compile.alipayLogo) {//支付宝
                    item += '            <div class="qrcode-item alipay">' +
                        '                <div class="qrcode-area"><img' +
                        '                         data-original="' + session.getItem("contextUrl") + '/newpc/zfb/qrcode/request?token=' + session.getItem("token") + '" class="qrcode-stream" id="alipayQrcode"' +
                        '                        >' +
                        '                    <i class="icon logo alipay">&#xe6ab;</i>' +
                        '                </div>' +
                        '            </div>';
                    itemIcon += '           <i class="icon alipay" data-tip="打开支付宝扫一扫直接付款" id="alipayQrIcon">&#xe6ab;</i>';
                }
                if (this.compile.unionLogo) {//银联钱包
                    item += '            <div class="qrcode-item union">' +
                        '                <div class="qrcode-area"><img' +
                        '                         data-original="' + session.getItem("contextUrl") + '/newpc/union/qrcode/request?token=' + session.getItem("token") + '" class="qrcode-stream" id="installmentQrcode"' +
                        '                        >' +
                        '                    <i class="icon logo union icon-sprites"></i>' +
                        '                </div>' +
                        '            </div>';
                    itemIcon += '           <i class="icon union icon-sprites" data-tip="打开银联钱包扫一扫直接付款" id="unionQrIcon"></i>';
                }
                if (this.compile.installment) {//分期支付
                    item += '            <div class="qrcode-item installment">' +
                        '                <div class="qrcode-area"><img' +
                        '                         data-original="' + session.getItem("contextUrl") + '/newpc/getHirePurchaseDirectQrCode?token=' + session.getItem("token") + '" class="qrcode-stream" id="installmentQrcode"' +
                        '                        >' +
                        '                    <i class="icon logo installment icon-sprites"></i>' +
                        '                </div>' +
                        '            </div>';
                    itemIcon += '           <i class="icon installment icon-sprites" data-tip="扫一扫分期支付" id="installmentQrIcon"></i>';
                }
                if (this.compile.jdLogo) {//京东支付
                    item += '            <div class="qrcode-item jd">' +
                        '                <div class="qrcode-area"><img' +
                        '                         data-original="' + session.getItem("contextUrl") + '/newpc/jd/qrcode/request?token=' + session.getItem("token") + '" class="qrcode-stream" id="jdQrcode"' +
                        '                        >' +
                        '                    <i class="icon logo jd">&#xe6a6;</i>' +
                        '                </div>' +
                        '            </div>';
                    itemIcon += '           <i class="icon jd" data-tip="打开京东APP扫一扫直接付款" id="jdQrIcon">&#xe6a6;</i>';
                }
                if (this.compile.qqLogo) {//qq支付
                    item += ' <div class="qrcode-item qq">' +
                        '                <div class="qrcode-area"><img' +
                        '                         data-original="' + session.getItem("contextUrl") + '/newpc/qq/qrcode/request?token=' + session.getItem("token") + '" class="qrcode-stream" id="qqQrcode"' +
                        '                        >' +
                        '                    <i class="icon logo qq icon-sprites"></i>' +
                        '                </div>' +
                        '            </div>';
                    itemIcon += '           <i class="icon qq icon-sprites" data-tip="打开QQ扫一扫直接付款" id="qqQrIcon"></i>';
                }
                if (this.compile.yjzfLogo) {//快捷支付
                    item += ' <div class="qrcode-item yjzf">' +
                        '                <div class="qrcode-area"><img' +
                        '                         data-original="' + session.getItem("contextUrl") + '/newpc/getYJZFQrCode?token=' + session.getItem("token") + '" class="qrcode-stream" id="yjzfQrcode"' +
                        '                        >' +
                        '                    <i class="icon logo yjzf"><img width="20" src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/logo_01.png"></i>' +
                        '                </div>' +
                        '            </div>';
                    itemIcon += '<i class="icon" data-tip="扫一扫直接付款"><img width="28" src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/logo_01.png"></i>';
                }
                item += '            <span class="arrow" id="hook-qrcode-app"></span>' +
                    '            </div>' +
                    '        <div class="support-app" events="{auto:Controller.qrcodeManager">';
                item += itemIcon;  //串接icon
                item += '        </div>' +
                    '        <div class="status clearfix none"><i class="icon">&#xe66b;</i>' +
                    '            <p>扫描成功，请在手机上完成付款。</p></div>' +
                    '    </div>';
                return item;
            }
        },
        bankAddItem: {
            title: "卡信息补充项",
            set context(context) {
                this.compile = context;
            },
            get content() {
                var item = '<div class="card-bin">' +
                    '        <div class="bank"><span class="bank-logo ' + this.compile.bankCode + '"></span>' + this.compile.bankName + '</div>' +
                    '        <div class="bank-info">' +
                    '            **********' + this.compile.cardlater +
                    '            <span class="card-type">' +
                    (this.compile.cardType == "CREDIT" ? "信用卡" : "储蓄卡") +
                    '                             </span>' +
                    '        </div>' +
                    '    </div>';
                item += this.compile.merchantSamePersonConf ? '<p class="prompt">请使用<span class="emp">本人信息</span>进行支付，支付成功后不可更改</p>' : '';
                if (this.compile.needname) {
                    item += '<div class="sington"><label><span class="title">姓名</span><input type="text" encrypt="true" autocomplete="off"' +
                        '                                                                placeholder="请输入您的真实姓名"' +
                        '                                                                     name="name"' + (this.compile.owner ? "readonly value=" + this.compile.owner : "") + '></label>' +
                        '</div>';
                }
                if (this.compile.needidno) {
                    item += '<div class="sington"><label><span class="title">身份证号</span><input type="text" encrypt="true" maxlength="18" autocomplete="off"' +
                        '                                                                placeholder="请输入您的身份证号"' +
                        '                                                                     name="idno"' + (this.compile.idno ? "readonly value=" + this.compile.idno : "") + '></label>' +
                        '</div>';
                }
                if (this.compile.needvalid) {
                    item += '<div class="sington"><label><span class="title">有效期</span><input type="text" encrypt="true" autocomplete="off"' +
                        '                                                                      placeholder="如：09/15 请输入0915"' +
                        '                                                                      name="valid"' +
                        '                                                                      maxlength="4"><div class="sample valid"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/5nor_03.png"></div></label>' +
                        '</div>';
                }
                if (this.compile.needcvv) {
                    item += '<div class="sington"><label><span class="title">C V V</span><input type="password" encrypt="true" autocomplete="off"' +
                        '                                                                        placeholder="信用卡背面后三位数字"' +
                        '                                                                        name="cvv2"' +
                        '                                                                        maxlength="3"><div class="sample cvv"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/5nor_06.png"></div></label>' +
                        '</div>';
                }
                if (this.compile.needpass) {
                    item += '<div class="sington"><label><span class="title">取款密码</span><input type="password" encrypt="true" autocomplete="off"' +
                        '                                                                       placeholder="请输入取款密码"' +
                        '                                                                       name="pass"' +
                        '                                                                       maxlength="6"></label>' +
                        '</div>';
                }
                if (this.compile.needmobile) {
                    item += '<div class="sington"><label><span class="title">手机号</span><input type="text" encrypt="true" maxlength="11" autocomplete="off"' +
                        '                                                                placeholder="请输入银行预留手机号"' +
                        '                                                                     name="phone"' + (this.compile.phoneNo ? "readonly value=" + this.compile.phoneNo : "") + '></label>' +
                        '</div>';
                }
                item += '<input type="hidden" name="bankCode" readonly value="' + this.compile.bankCode + '">' +
                    '<input type="hidden" name="bankName" readonly value="' + this.compile.bankName + '">' +
                    '<input type="hidden" name="cardType" readonly value="' + this.compile.cardType + '"><div class="sington"><label><input type="checkbox" name="agreement" id="agreement" checked events="{click:Controller.disagree}"><span' +
                    '        class="checkbox icon"></span>同意</label><a' +
                    '        href="javascript:void(0)" events="{click:Controller.showAgreement}">《服务协议》</a>' + (this.compile.bankCode == "BOC" ? '<span class="china-bank-agree">和<a href="javascript:void(0)" events="{click:Controller.showBOCAgreement}">《中国银行快捷支付服务协议》</a></span>' : '') +
                    '</div><button type="button" id="firstPayBtn" events="{click:Action.firstPay}">下一步</button>';
                return item;
            }
        },
        bindCardAddItem: {
            title: "绑卡信息补充项",
            set context(context) {
                this.compile = context;
            },
            get content() {
                var item = '<form id="bindItemForm">';
                item += item += this.compile.merchantSamePersonConf ? '<p class="prompt">请使用<span class="emp">本人信息</span>进行支付，支付成功后不可更改</p>' : '';
                if (this.compile.needBankCardDTO) {
                    if (this.compile.needBankCardDTO.needSurportDTO) {
                        if (this.compile.needBankCardDTO.needSurportDTO.ownerIsNeed) {
                            item += '<div class="sington"><label><span class="title">姓名</span><input type="text" encrypt="true" autocomplete="off"' +
                                '                                                                placeholder="请输入您的真实姓名"' +
                                '                                                                     name="owner"' + (this.compile.needBankCardDTO.owner ? "readonly value=" + this.compile.needBankCardDTO.owner : "") + '></label>' +
                                '</div>';
                        }
                        if (this.compile.needBankCardDTO.needSurportDTO.idnoIsNeed) {
                            item += '<div class="sington"><label><span class="title">身份证号</span><input type="text" encrypt="true" maxlength="18" autocomplete="off"' +
                                '                                                                placeholder="请输入您的身份证号"' +
                                '                                                                     name="idno"' + (this.compile.needBankCardDTO.idno ? "readonly value=" + this.compile.needBankCardDTO.idno : "") + '></label>' +
                                '</div>';
                        }
                        if (this.compile.needBankCardDTO.needSurportDTO.avlidDateIsNeed) {
                            item += '<div class="sington"><label><span class="title">有效期</span><input type="text" encrypt="true" autocomplete="off"' +
                                '                                                                      placeholder="如：09/15 请输入0915"' +
                                '                                                                      name="avlidDate"' +
                                '                                                                      maxlength="4"><div class="sample valid"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/5nor_03.png"></div></label>' +
                                '</div>';
                        }
                        if (this.compile.needBankCardDTO.needSurportDTO.cvvIsNeed) {
                            item += '<div class="sington"><label><span class="title">C V V</span><input type="password" encrypt="true" autocomplete="off"' +
                                '                                                                        placeholder="信用卡背面后三位数字"' +
                                '                                                                        name="cvv"' +
                                '                                                                        maxlength="3"><div class="sample cvv"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/5nor_06.png"></div></label>' +
                                '</div>';
                        }
                        if (this.compile.needBankCardDTO.needSurportDTO.bankPWDIsNeed) {
                            item += '<div class="sington"><label><span class="title">取款密码</span><input type="password" encrypt="true" autocomplete="off"' +
                                '                                                                       placeholder="请输入取款密码"' +
                                '                                                                       name="bankPWD"' +
                                '                                                                       maxlength="6"></label>' +
                                '</div>';
                        }
                        if (this.compile.needBankCardDTO.needSurportDTO.phoneNoIsNeed) {
                            item += '<div class="sington"><label><span class="title">手机号</span><input type="text" encrypt="true" maxlength="11" autocomplete="off"' +
                                '                                                                placeholder="请输入银行预留手机号"' +
                                '                                                                     name="phoneNo"' + (this.compile.phoneNo ? "readonly value=" + this.compile.needBankCardDTO.phoneNo : "") + '></label>' +
                                '</div>';
                        }
                        if (this.compile.needBankCardDTO.needSurportDTO.ypMobileIsNeed) {
                            item += '<div class="sington"><label><span class="title">易宝手机号</span><input type="text" encrypt="true" maxlength="11" autocomplete="off"' +
                                '                                                                placeholder="请输入易宝预留手机号"' +
                                '                                                                     name="ypMobile"' + (this.compile.needBankCardDTO.ypMobile ? "readonly value=" + this.compile.needBankCardDTO.ypMobile : "") + '></label>' +
                                '</div>';
                        }
                    }
                }
                item += '<div id="bindCardPayError"></div>';
                item += "</form>";
                return item;
            }
        },
        onlybindCardAddItem: {
            title: "单独绑卡补充项",
            set context(context) {
                this.compile = context;
            },
            get content() {
                var item = '<div class="card-bin">' +
                    '        <div class="bank"><span class="bank-logo ' + this.compile.bankCode + '"></span>' + this.compile.bankName + '</div>' +
                    '        <div class="bank-info">' +
                    '            **********' + this.compile.cardlater +
                    '            <span class="card-type">' +
                    (this.compile.cardType == 2 ? "信用卡" : "储蓄卡") +
                    '                             </span>' +
                    '        </div>' +
                    '    </div>';
                item += this.compile.merchantSamePersonConf ? '<p class="prompt">请使用<span class="emp">本人信息</span>进行支付，支付成功后不可更改</p>' : '';
                if (this.compile.needname) {
                    item += '<div class="sington"><label><span class="title">姓名</span><input type="text" encrypt="true" autocomplete="off"' +
                        '                                                                placeholder="请输入您的真实姓名"' +
                        '                                                                     name="owner"' + (this.compile.owner ? "readonly value=" + this.compile.owner : "") + '></label>' +
                        '</div>';
                }
                if (this.compile.needidno) {
                    item += '<div class="sington"><label><span class="title">身份证号</span><input type="text" encrypt="true" maxlength="18" autocomplete="off"' +
                        '                                                                placeholder="请输入您的身份证号"' +
                        '                                                                     name="idno"' + (this.compile.idno ? "readonly value=" + this.compile.idno : "") + '></label>' +
                        '</div>';
                }
                if (this.compile.needvalid) {
                    item += '<div class="sington"><label><span class="title">有效期</span><input type="text" encrypt="true" autocomplete="off"' +
                        '                                                                      placeholder="如：09/15 请输入0915"' +
                        '                                                                      name="avlidDate"' +
                        '                                                                      maxlength="4"><div class="sample valid"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/5nor_03.png"></div></label>' +
                        '</div>';
                }
                if (this.compile.needcvv) {
                    item += '<div class="sington"><label><span class="title">C V V</span><input type="password" encrypt="true" autocomplete="off"' +
                        '                                                                        placeholder="信用卡背面后三位数字"' +
                        '                                                                        name="cvv"' +
                        '                                                                        maxlength="3"><div class="sample cvv"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/5nor_06.png"></div></label>' +
                        '</div>';
                }
                if (this.compile.needpass) {
                    item += '<div class="sington"><label><span class="title">取款密码</span><input type="password" encrypt="true" autocomplete="off"' +
                        '                                                                       placeholder="请输入取款密码"' +
                        '                                                                       name="pass"' +
                        '                                                                       maxlength="6"></label>' +
                        '</div>';
                }
                if (this.compile.needmobile) {
                    item += '<div class="sington"><label><span class="title">手机号</span><input type="text" encrypt="true" maxlength="11" autocomplete="off"' +
                        '                                                                placeholder="请输入银行预留手机号"' +
                        '                                                                     name="phoneNo"' + (this.compile.phoneNo ? "readonly value=" + this.compile.phoneNo : "") + '></label>' +
                        '</div>';
                }
                item += '<div class="sington"><label><span class="title">验证码</span><input type="text" maxlength="6" placeholder="输入您的验证码"' +
                    '                                                                             class="verify-code" name="smsCode"></label>' +
                    '<button class="get-smsCode" type="button" events="{click:Action.getBindCardsmsCode}">获取验证码</button>' +
                    '</div>';
                item += '<input type="hidden" name="bankCode" readonly value="' + this.compile.bankCode + '">' +
                    '<input type="hidden" name="bankName" readonly value="' + this.compile.bankName + '">' +
                    '<div class="sington"><label><input type="checkbox" name="agreement" id="agreement" checked events="{click:Controller.disagree}"><span' +
                    '        class="checkbox icon"></span>同意</label><a' +
                    '        href="javascript:void(0)" events="{click:Controller.showBindCardAgreement}">《服务协议》</a>' + (this.compile.bankCode == "BOC" ? '<span class="china-bank-agree">和<a href="javascript:void(0)" events="{click:Controller.showBOCAgreement}">《中国银行快捷支付服务协议》</a></span>' : '') +
                    '</div><button type="button" id="firstPayBtn" events="{click:Action.bindCardConfrim}">确定</button>';
                return item;
            }
        },
        bindCardPayAddItem: {
            title: "绑卡支付补充项",
            set context(context) {
                this.compile = context;
            },
            get content() {
                var item = '<div class="card-bin">' +
                    '        <div class="bank"><span class="bank-logo ' + this.compile.bankCode + '"></span>' + this.compile.bankName + '</div>' +
                    '        <div class="bank-info">' +
                    '            **********' + this.compile.cardlater +
                    '            <span class="card-type">' +
                    (this.compile.cardType == 2 ? "信用卡" : "储蓄卡") +
                    '                             </span>' +
                    '        </div>' +
                    '    </div>';
                item += this.compile.merchantSamePersonConf ? '<p class="prompt">请使用<span class="emp">本人信息</span>进行支付，支付成功后不可更改</p>' : '';
                if (this.compile.needname) {
                    item += '<div class="sington"><label><span class="title">姓名</span><input type="text" encrypt="true" autocomplete="off"' +
                        '                                                                placeholder="请输入您的真实姓名"' +
                        '                                                                     name="owner"' + (this.compile.owner ? "readonly value=" + this.compile.owner : "") + '></label>' +
                        '</div>';
                }
                if (this.compile.needidno) {
                    item += '<div class="sington"><label><span class="title">身份证号</span><input type="text" encrypt="true" maxlength="18" autocomplete="off"' +
                        '                                                                placeholder="请输入您的身份证号"' +
                        '                                                                     name="idno"' + (this.compile.idno ? "readonly value=" + this.compile.idno : "") + '></label>' +
                        '</div>';
                }
                if (this.compile.needvalid) {
                    item += '<div class="sington"><label><span class="title">有效期</span><input type="text" encrypt="true" autocomplete="off"' +
                        '                                                                      placeholder="如：09/15 请输入0915"' +
                        '                                                                      name="avlidDate"' +
                        '                                                                      maxlength="4"><div class="sample valid"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/5nor_03.png"></div></label>' +
                        '</div>';
                }
                if (this.compile.needcvv) {
                    item += '<div class="sington"><label><span class="title">C V V</span><input type="password" encrypt="true" autocomplete="off"' +
                        '                                                                        placeholder="信用卡背面后三位数字"' +
                        '                                                                        name="cvv"' +
                        '                                                                        maxlength="3"><div class="sample cvv"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/5nor_06.png"></div></label>' +
                        '</div>';
                }
                if (this.compile.needpass) {
                    item += '<div class="sington"><label><span class="title">取款密码</span><input type="password" encrypt="true" autocomplete="off"' +
                        '                                                                       placeholder="请输入取款密码"' +
                        '                                                                       name="pass"' +
                        '                                                                       maxlength="6"></label>' +
                        '</div>';
                }
                if (this.compile.needmobile) {
                    item += '<div class="sington"><label><span class="title">手机号</span><input type="text" encrypt="true" maxlength="11" autocomplete="off"' +
                        '                                                                placeholder="请输入银行预留手机号"' +
                        '                                                                     name="phoneNo"' + (this.compile.phoneNo ? "readonly value=" + this.compile.phoneNo : "") + '></label>' +
                        '</div>';
                }
                item += '<div class="sington"><label><span class="title">验证码</span><input type="text" placeholder="输入您的验证码"' +
                    '                                                                             class="verify-code" maxlength="6" name="smsCode"></label>' +
                    '<button class="get-smsCode" type="button" events="{click:Action.getBindCardsmsCode}">获取验证码</button>' +
                    '            </div>';
                item += '<input type="hidden" name="bankCode" readonly value="' + this.compile.bankCode + '">' +
                    '<input type="hidden" name="bankName" readonly value="' + this.compile.bankName + '">' +
                    '<div class="sington"><label><input type="checkbox" name="agreement" id="agreement" checked events="{click:Controller.disagree}"><span' +
                    '        class="checkbox icon"></span>同意</label><a' +
                    '        href="javascript:void(0)" events="{click:Controller.showBindCardAgreement}">《服务协议》</a>' + (this.compile.bankCode == "BOC" ? '<span class="china-bank-agree">和<a href="javascript:void(0)" events="{click:Controller.showBOCAgreement}">《中国银行快捷支付服务协议》</a></span>' : '') +
                    '            <div id="bindCardPayError"></div>' +
                    '</div><button type="button" id="firstPayBtn" events="{click:Action.bindCardPayConfrim}">确定</button>';
                return item;
            }
        },
        FYHK_FYHKZF: {
            title: "非银行卡支付",
            set context(context) {
                this.compile = context;
            },
            get content() {
                return '<div class="not-bank-pay cashier-pay-tool">跳转到非银行卡支付升级版<a href="' + notBankCardPayUrl + '" target="_blank">前往&gt;</a></div>';
            }

        },
        BIND_CARD: {
            title: '绑卡',
            set context(context) {
                this.compile = context;
            },
            get content() {
                dom.querySelector(".progress-bar").classList.remove("none");
                return '<div id="bindCard" class="cashier-pay-tool">' + '<form id="bindCardForm" class="bind-card-form" events="{auto:Controller.bindCardValidate}">' +
                    '       <div class="sington"><label><span class="title">银行卡卡号</span><input type="text" autocomplete="off"' +
                    '                                                                        placeholder="请输入个人银行卡号"' +
                    '                                                                        name="cardno"' +
                    '                                                                        maxlength="23" encrypt="true" autofocus events="{input:Controller.bindCardNoInput}"><img' +
                    '               src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/loading.gif" width="24" class="loading none"> </label>' +
                    '       </div>' +
                    '       <div id="addItemWrapper"></div>' +
                    '       <button type="button" class="fn-btn" events="{click:Controller.getBindCardInfo}">下一步</button>' +
                    '   </form></div>';
            }
        },
        BIND_CARD_SUCCESS: {
            title: "绑卡成功",
            set context(context) {
                this.compile = context;
            },
            get content() {
                dom.querySelector(".progress-bar").classList.remove("none");
                return '<div class="bind-card-success">' +
                    '            <p><i class="icon">&#xe647;</i>添加银行卡成功</p>' +
                    (this.compile.frontCallBackUrl ? '<a href="' + this.compile.frontCallBackUrl + '" class="button">点击返回</a>' : '') +
                    '   </div>';
            }

        },
        BIND_CARD_FAIL: {
            title: "绑卡失败",
            set context(context) {
                this.compile = context;
            },
            get content() {
                dom.querySelector(".progress-bar").classList.add("none");
                return '<div class="bind-card-success">' +
                    '            <p><i class="icon fail">&#xe68c;</i>' + (this.compile.errormsg ? this.compile.errorcode + this.compile.errormsg : '绑卡失败，请重新绑定银行卡') + '</p>' +
                    (this.compile.errormsg ? '' : '<a href="javascript:void(0)" class="button" events="{click:Controller.againBindCard}">重新绑卡</a>') +
                    '   </div>';
            }

        },
        BIND_CARD_QUERY: {
            title: "绑卡结果查询",
            set context(context) {
                this.compile = context;
            },
            get content() {
                dom.querySelector(".progress-bar").classList.add("none");
                return '<div class="bind-card-success">' +
                    '            <p><i class="icon fail">&#xe612;</i><span id="errormsg">绑卡请求超时，请查询绑卡结果</span></p>' +
                    '          <button class="button" events="{click:Action.queryBindCard}">查询结果</button>' +
                    '   </div>';
            }

        },
        UIBIND_CONFRIM: {
            title: "解除绑卡确认",
            set context(context) {
                this.compile = context;
            },
            get content() {
                return '<div class="unbind-confrim"><p>是否要解除此银行卡的绑定关系？</p><button class="confrim">确定</button><button class="cancel">取消</button></div>';
            }

        },
        UIBIND_ERROR: {
            title: "解除绑卡失败",
            set context(context) {
                this.compile = context;
            },
            get content() {
                return '<div class="unbind-error"><h2>' + this.title + '</h2><p>' + '[' + this.compile.errorCode + ']' + this.compile.errorMsg + '</p><button class="confrim" events="{click:Util.closePopup}">确定</button></div>';
            }

        }
    };
    exports.Cashier.Components = Components;
})(window);
/**
 */
(function (global) {
    var User = function () {
        var form = function () {
            var reg = /text|tel|password|number|search|url|email|file|hidden/;
            var strategy = {
                /*
                 * 表单验证策略
                 * */
                required: function (value) {
                    /*
                     * 必填
                     * */
                    return value != "";

                },
                onlyNumber: function (value) {
                    /*
                     * 仅数字
                     * */
                    return !!/^\d+$/.test(value);

                },
                onlyChinese: function (value) {
                    /*
                     * 仅中文
                     * */
                    return !!/^[\u4E00-\u9FA5·.]+$/.test(value);

                },
                maxLength: function (value, max) {
                    /*
                     * 最大长度
                     * */
                    return value.length <= max;

                },
                minLength: function (value, min) {
                    /*
                     * 最小长度
                     * */
                    return value.length >= min;

                },
                length: function (value, len) {
                    /*
                     * 固定长度
                     * */
                    return value.length == len;

                },
                idCardNo: function (value) {
                    /*
                     * 身份证号
                     * */
                    return !!/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/.test(value);

                },
                bankCardNo: function (value) {
                    /*
                     * 银行卡号， 必须为数字， 长度为14~19
                     * */
                    var cache = value.replace(/[\s\n\r]/g, "");
                    return !!/^\d+$/.test(cache) && cache.length >= 14 && cache.length <= 19;
                },
                phone: function (value) {
                    /*
                     *手机号
                     * */
                    return /^1\d{10}$/.test(value);
                }
            };

            function serialize(selector, supplement) {
                /*
                 * 表单序列化
                 * supplement：补充数据
                 * */
                var obj = {};
                if (Util.type(supplement) == "object") {
                    for (key in supplement) {
                        obj[key] = supplement[key];
                    }
                }
                var elements = array(dom.querySelector(selector).elements);
                elements.forEach(function (elem) {
                    if (reg.test(elem.type)) {
                        obj[elem.name] = elem.getAttribute("encrypt") == "true" ? BASE64.encoder(elem.value.replace(/[\s\n\r]/g, "")) : elem.value.replace(/[\s\n\r]/g, "");
                    }
                });
                return obj;
            }

            function showErrorMessage(errorMessage) {
                var target = this.parent(".sington");
                target.classList.add("error");
                if (target.contains(target.querySelector(".errormsg"))) {
                    target.querySelector(".errormsg").innerHTML = errorMessage;
                } else {
                    target.insertAdjacentHTML("beforeEnd", '<p class="errormsg">' + errorMessage + '</p>');
                }
            }

            function hideErrorMessage(elem) {
                /*
                 *input得到焦点 隐藏错误信息
                 * */
                elem = Util.type(elem) == "string" ? dom.querySelector(elem) : elem;
                var inputs = elem.querySelectorAll(".sington input");
                for (var i = 0, l = inputs.length; i < l; i++) {
                    inputs[i].addEventListener("focus", function () {
                        this.parent(".sington").classList.remove("error");
                        Array.from(dom.querySelectorAll(".global-error")).forEach(function (error) {
                            error.classList.add("none");
                        });
                    }, false);
                }
                return this;
            }

            function allInputReadOnly(selector, ignore) {
                /*
                 * 设置input为只读
                 * selector form选择器
                 * ignore 忽略的input name值
                 * */
                var inputs = dom.querySelector(selector).querySelectorAll("input");
                for (var i = 0, l = inputs.length; i < l; i++) {
                    if (reg.test(inputs[i].type) && inputs[i].name != ignore) {
                        inputs[i].readOnly = true;
                    }
                }
            }

            function allInputWrite(selector, ignore) {
                /*
                 * 设置input可写入
                 * selector form选择器
                 * ignore 忽略的input name值
                 * */
                var inputs = dom.querySelector(selector).querySelectorAll("input");
                for (var i = 0, l = inputs.length; i < l; i++) {
                    if (reg.test(inputs[i].type) && inputs[i].name != ignore) {
                        inputs[i].readOnly = false;
                    }
                }
            }


            function enabledSubmit(selector) {
                /*启用提交按钮
                 * */
                if (Util.type(selector) == "string") {
                    dom.querySelector(selector).disabled = false;
                }
                else {
                    selector.disabled = false;
                }

            }

            function disabledSubmit(selector) {
                /*禁用提交按钮
                 * */
                if (Util.type(selector) == "string") {
                    dom.querySelector(selector).disabled = true;
                }
                else {
                    selector.disabled = true;
                }
            }

            function proxy(event) {
                try {
                    if (event.relatedTarget == null) {
                        return;
                    }
                } catch (e) {

                }
                this.value = this.value.trim();//去处前后空格
                session.setItem("blur", this.name);
                if (this.name == "cardno") {
                    /*
                     * 银行卡号
                     * */
                    if (!strategy.bankCardNo(this.value)) {
                        showErrorMessage.call(this, "银行卡号格式不正确");
                        return false;
                    }
                }
                if (this.name == "name" || this.name == "owner") {
                    /*
                     * 真实姓名
                     * */
                    if (!strategy.required(this.value)) {
                        showErrorMessage.call(this, "姓名不能为空");
                        return false;
                    } else if (!strategy.onlyChinese(this.value)) {
                        showErrorMessage.call(this, "姓名只能为中文");
                        return false;
                    }
                }
                if (this.name == "idno") {
                    /*
                     * 身份证号
                     * */
                    if (!strategy.required(this.value)) {
                        showErrorMessage.call(this, "身份证号码不能为空");
                        return false;
                    } else if (!strategy.idCardNo(this.value)) {
                        showErrorMessage.call(this, "身份证号格式不正确");
                        return false;
                    }
                }
                if (this.name == "phone" || this.name == "phoneNo" || this.name == "ypMobile") {
                    /*
                     * 手机号
                     * */
                    if (!strategy.required(this.value)) {
                        showErrorMessage.call(this, "手机号不能为空");
                        return false;
                    } else if (!strategy.onlyNumber(this.value)) {
                        showErrorMessage.call(this, "手机号只能为数字");
                        return false;
                    } else if (!strategy.length(this.value, 11)) {
                        showErrorMessage.call(this, "手机号只能为11位数字");
                        return false;
                    } else if (!strategy.phone(this.value)) {
                        showErrorMessage.call(this, "手机号格式不正确");
                        return false;
                    }
                }
                if (this.name == "verifycode") {
                    /*
                     * 验证码
                     * */
                    if (!strategy.required(this.value)) {
                        showErrorMessage.call(this, "验证码不能为空");
                        return false;
                    } else if (!strategy.onlyNumber(this.value)) {
                        showErrorMessage.call(this, "验证码只能为数字");
                        return false;
                    }
                }
                if (this.name == "valid" || this.name == "avlidDate") {
                    /*
                     * 有效期
                     * */
                    if (!strategy.required(this.value)) {
                        showErrorMessage.call(this, "有效期不能为空");
                        return false;
                    } else if (!strategy.onlyNumber(this.value)) {
                        showErrorMessage.call(this, "有效期必须是数字");
                        return false;
                    }
                }
                if (this.name == "pass" || this.name == "bankPWD") {
                    /*
                     * 取款密码
                     * */
                    if (!strategy.required(this.value)) {
                        showErrorMessage.call(this, "取款密码不能为空");
                        return false;
                    } else if (!strategy.onlyNumber(this.value)) {
                        showErrorMessage.call(this, "取款密码必须是数字");
                        return false;
                    }
                }
                if (this.name == "cvv2" || this.name == "cvv") {
                    /*
                     * cvv
                     * */
                    if (!strategy.required(this.value)) {
                        showErrorMessage.call(this, "CVV不能为空");
                        return false;
                    } else if (!strategy.onlyNumber(this.value)) {
                        showErrorMessage.call(this, "CVV必须是数字");
                        return false;
                    }
                }
                if (this.name == "clientId") {
                    /*
                     * 企业客户号
                     * */
                    if (!strategy.required(this.value)) {
                        showErrorMessage.call(this, "企业客户号不能为空");
                        return false;
                    }
                }
                if (this.name == "userAccount") {
                    /*
                     * 企业账户名
                     * */
                    if (!strategy.required(this.value)) {
                        showErrorMessage.call(this, "企业账户名不能为空");
                        return false;
                    }
                }
                if (this.name == "tradePassword") {
                    /*
                     * 企业账户交易密码
                     * */
                    if (!strategy.required(this.value)) {
                        showErrorMessage.call(this, "交易密码不能为空");
                        return false;
                    } else if (!strategy.minLength(this.value, 6)) {
                        showErrorMessage.call(this, "交易密码最小长度为6位");
                        return false;
                    } else if (!strategy.maxLength(this.value, 32)) {
                        showErrorMessage.call(this, "交易密码最大长度为32位");
                        return false;
                    }
                }
                if (this.name == "captcha" || this.name == "smsCode") {
                    /*
                     * 验证码
                     * */
                    if (!strategy.required(this.value)) {
                        showErrorMessage.call(this, "验证码不能为空");
                        return false;
                    }
                }
                return true;
            }

            function validate(selector, ignore) {
                var form = dom.querySelector(selector);
                hideErrorMessage(form);
                var inputs = form.querySelectorAll("input");
                for (var i = 0, l = inputs.length; i < l; i++) {
                    if (reg.test(inputs[i].type) && inputs[i].name != ignore && !inputs[i].readOnly) {
                        inputs[i].addEventListener("blur", proxy, false);
                    }
                }
            }

            function reset(selector) {
                var form = dom.querySelector(selector);
                //form.reset();
                array(form.querySelectorAll(".sington")).forEach(function (current) {
                    current.classList.remove("error");
                });
                setTimeout(function () {
                    try {
                        form[session.getItem("blur")].focus();
                    }
                    catch (error) {
                        //自动获取焦点失败
                    }

                }, 100);
            }

            function autoValidate(selector, ignore) {
                /*
                 * 自动校验
                 * ignore 忽略输入框name值
                 * */
                var inputs = dom.querySelector(selector).querySelectorAll("input");
                for (var i = 0, l = inputs.length; i < l; i++) {
                    if (reg.test(inputs[i].type) && inputs[i].name != ignore && !inputs[i].readOnly) {
                        var result = proxy.call(inputs[i]);
                        if (!result) {
                            return false;
                        }
                    }
                }
                return true;
            }

            function select(callback) {
                /*模拟下拉框*/
                var selects = array(dom.querySelectorAll(".select")),//保存所有select
                    currentSelecteds = array(dom.querySelectorAll(".select .current-selected")),//保存每个select选中项
                    optionWrappers = array(dom.querySelectorAll(".select .option-wrapper")),//保存option外层div
                    options = array(dom.querySelectorAll(".select .option"));//保存每个select下拉项
                function selectController(event) {
                    /*
                     * 下拉菜单显示、隐藏控制
                     * */
                    if (event && event.target.classList.contains("unbind")) {
                        Controller.unbind.call(event.target);
                        return;
                    }
                    var target = this;
                    currentSelecteds.forEach(function (currentValue) {
                        if (target != currentValue) {
                            currentValue.parent(".select").classList.remove("open");
                        }
                    });
                    this.parent(".select").classList.toggle("open");
                }

                function selected() {
                    /*
                     * 点击下拉选项，为当前项重新赋值
                     * */
                    var currentSelected = this.parent(".select").querySelector(".current-selected");
                    currentSelected.innerHTML = this.innerHTML;
                    /*
                     * 附加数据
                     * */
                    var attrs = this.attributes;
                    var len = attrs.length;
                    for (var i = len - 1; i >= 0; i--) {
                        if (/^data-/.test(attrs[i].name)) {
                            currentSelected.setAttribute(attrs[i].name, attrs[i].value);
                        }
                    }
                    selectController.call(currentSelected);
                    if (typeof callback == "function") {
                        callback(this);
                    }
                }

                currentSelecteds.forEach(function (currentValue) {
                    /*
                     * 每一个select绑定点击事件
                     * */
                    currentValue.addEventListener("click", selectController, false);
                });
                optionWrappers.forEach(function (currentValue) {
                    /*
                     * option点击事件委托
                     * */
                    currentValue.addEventListener("click", function (event) {
                        if (event.target.classList.contains("unbind")) {
                            Controller.unbind.call(event.target);
                            return;
                        }
                        if (event.target.parent(".option")) {
                            selected.call(event.target.parent(".option"));
                        } else if (event.target.classList.contains("option")) {
                            selected.call(event.target);
                        }
                    }, false);
                });
                /*options.forEach(function (currentValue) {
                 /!*
                 * 每一个没有禁用的option绑定点击事件
                 * *!/
                 currentValue.addEventListener("click", selected, false);
                 });*/
                currentSelecteds.forEach(function (currentValue, index) {
                    /*
                     * 默认选中第一个
                     * */
                    var firstOption = selects[index].querySelector(".option");
                    firstOption.click();
                    selects[index].classList.remove("open");
                });
                dom.body.addEventListener("click", function (event) {
                    /*
                     * 点击下拉框以外区域隐藏下拉菜单
                     * */
                    if (event.target.parent(".select") == null && dom.querySelector(".select.open") != null) {
                        dom.querySelector(".select.open").classList.remove("open");
                    }
                }, false);
            }

            return {
                autoValidate: autoValidate,
                validate: validate,
                strategy: strategy,
                showErrorMessage: showErrorMessage,
                hideErrorMessage: hideErrorMessage,
                enabledSubmit: enabledSubmit,
                disabledSubmit: disabledSubmit,
                allInputReadOnly: allInputReadOnly,
                allInputWrite: allInputWrite,
                select: select,
                reset: reset,
                serialize: serialize
            };
        }();
        /*
         * tab切换
         * */
        var Tab = {
            factory: {
                /*
                 * 保存tab和每个tab对应的tabContent
                 * 为了可以复用，保存tab和tabContent时要基于不同的根元素分别存放到数组
                 * */
                tabs: null,
                tabContents: null
            },
            updata: function () {
                this.factory.tabs = array(dom.querySelectorAll(".tab-wrapper")).map(function (root) {
                    return array(root.querySelectorAll(".tab"));
                });
                this.factory.tabContents = array(dom.querySelectorAll(".tab-content-wrapper")).map(function (root) {
                    return array(root.querySelectorAll(".tab-content"));
                });
            },
            removeActive: function (elem) {
                /*
                 * 重置当前活动tab或tabContent为默认状态
                 * */
                if (elem.classList.contains("active")) {
                    elem.classList.remove("active");
                }
            },
            toggle: function (ind, index) {
                Tab.factory.tabs[ind].forEach(Tab.removeActive);
                Tab.factory.tabContents[ind].forEach(Tab.removeActive);
                this.classList.add("active");
                Tab.factory.tabContents[ind][index].classList.add("active");
            },
            bind: function () {
                Tab.updata();
                Tab.factory.tabs.forEach(function (tabs, ind) {
                    tabs.forEach(function (tab, index) {
                        tab.addEventListener("click", function () {
                            Tab.toggle.call(this, ind, index)
                        }, false);
                        if (index == 0) {
                            tab.click();
                        }
                    });
                });
            }
        };
        /*
         * 网银支付选择银行
         * */
        var Bank = function () {
            var uls = null,
                liBox = null;//把所有银行存到数组
            var max = 15;//默认显示银行个数
            var text = "显示更多银行";
            var cache = [];//存储隐藏的银行
            function update() {
                uls = array(dom.querySelectorAll(".bank-pay-list .bank-list"));
                liBox = uls.map(function (ul) {
                    return array(ul.querySelectorAll("li"));
                });//把所有银行存到数组
            }

            function select() {
                /*
                 * 选择银行卡
                 * */
                update();
                uls.forEach(function (ul) {
                    ul.addEventListener("click", function (event) {
                        var target = event.target || event.srcElement;
                        if (target.classList.contains("bank-logo")) {
                            target = target.parentNode;
                        }
                        if (target.nodeName.toLowerCase() == "li" && !target.classList.contains("more-handler")) {
                            this.querySelector(".active").classList.remove("active");
                            target.classList.add("active");
                        }
                        if (dom.querySelector('.global-error') instanceof Element) {
                            dom.querySelector('.global-error').classList.add("none")
                        }
                    }, false);
                });
                return this;
            }

            function more() {
                /*
                 * 银行15个以上时显示更多银行按钮
                 * */
                update();
                var handler = '<li class="more-handler">' + text + '</li>';//显示更多银行按钮
                function hide() {
                    /*
                     * 隐藏第15个以后的银行，把隐藏的银行存入数组
                     * */

                    cache.push(this.slice(max - 1));
                    cache.forEach(function (lis) {
                        lis.forEach(function (li) {
                            li.classList.add("none");
                        });
                    });
                }

                function show(index) {
                    /*
                     * 显示第15个以后的银行
                     * */
                    cache[index].forEach(function (li) {
                        li.classList.remove("none");
                    });
                }

                liBox.forEach(function (lis, index) {
                    /*
                     * 判断每一组的银行个数
                     * 如果大于max值隐藏max值以后的银行
                     * 插入更多银行按钮并绑定点击隐藏事件
                     * */
                    if (lis.length > max) {
                        uls[index].insertAdjacentHTML("beforeEnd", handler);
                        hide.call(lis);
                        uls[index].querySelector(".more-handler").addEventListener("click", function () {
                            this.classList.add("none");
                            show(index);
                        }, false);
                    } else {
                        cache.push([]);
                    }
                });
                return this;
            }

            return {
                select: select,
                more: more
            }
        }();

        function checkIsArgee() {
            /*
             * 检查是否同意绑卡协议
             * */
            return !!dom.querySelector("#isArgee").checked;

        }

        function bankCard() {
            /*返回我的银行卡*/
            dom.querySelector('#addNewCard').checked = false;
        }


        function timing(fn, callback, second) {
            /*倒计时
             * fn开始计时回调函数
             * callback计时完成回调函数
             * second倒计时秒数，默认60
             * */
            var timeId = null;
            second = second || 60;
            function start() {
                if (second <= 0) {
                    if (typeof callback == "function") {
                        callback();
                    }
                    session.setItem("timeId", 0);
                    clearTimeout(timeId);
                    return;
                }
                if (typeof fn == "function") {
                    fn(second);
                    --second;
                }
                timeId = setTimeout(start, 1000);
                session.setItem("timeId", timeId);
            }

            start();
        }

        var template = function () {
            /*
             * 页面模板包括：
             * 短验弹框
             * 跳到银行支付页面后，当前页等待支付完成的弹框，网银企业客户号，快捷支付未查询到结果，服务协议，身份选择,跨商户授权
             * */
            var smsCode = '<div class="sms-code">' +
                    '        <h2>手机校验</h2>' +
                    '        <p class="send-tip none"></p>' +
                    '        <form id="smsForm">' +
                    '            <div class="sington icon"><label><span class="title">验证码</span><input type="text"' +
                    '                                                                                  placeholder="请输入验证码"' +
                    '                                                                                  maxlength="6" name="verifycode"></label>' +
                    '                <button type="button" class="validate-code">获取验证码</button>' +
                    '            </div>' +
                    '            <button class="confirm" type="button">确认支付</button>' +
                    '        </form>' +
                    '    </div>',
                waitingPay = '<div class="waiting-pay">' +
                    '        <p class="open-new">请在新打开的<span class="bank-name"></span>页面完成支付</p>' +
                    '        <p class="tip">支付完成前请不要关闭此窗口</p>' +
                    '        <div class="btn-group"><a href="javascript:Action.ebankResult()" class="button complete">已完成支付</a><a href="javascript:void(0)"' +
                    '                                                                                       class="button issue" target="_blank">支付遇到问题</a>' +
                    '        </div>' +
                    '        <div class="misc clearfix">' +
                    '            <div class="fail"><p>跳转不到银行页面？</p>' +
                    '                <a href="http://www.yeepay.com/customerService/questionDetail/57ad493b02e381434704c8d3" target="_blank">查看解决办法</a><p>若支付不成功，您可以</p>' +
                    '                <a href="javascript:Util.closePopup()">选择其他支付方式</a></div>' +
                    '            <div class="yeepay clearfix none">' +
                    '                <div class="yp-qrcode"><img src="' + session.getItem("contextUrl") + '/assets/images/yeepay.png" width="95" alt="易宝公众号"></div>' +
                    '                <div class="yp-desc"><p>扫码随时随地查订单</p>' +
                    '                    <p class="number"><a href="https://www.yeepay.com/customerService/unifiedQuerySearch?orderId=' + session.getItem("orderId") + '" target="_blank"> 懒得扫？<br>点我直接查！</a></p></div>' +
                    '            </div>' +
                    '        </div>' +
                    '    </div>',
                queryResult = '<div class="query-result">' +
                    '        <p class="open-new">支付仍在后台处理中 请耐心等待</p>' +
                    '        <p class="tip">若一直没有收到支付结果，您可以：</p>' +
                    '        <div class="btn-group"><a href="javascript:Util.closePopup()" class="button complete">再次提交支付</a><a href="javascript:void(0)"' +
                    '                                                                                       class="button issue" target="_blank">支付遇到问题</a>' +
                    '        </div>' +
                    '        <div class="misc clearfix">' +
                    '            <div class="yeepay clearfix">' +
                    '                <div class="yp-qrcode"><img src="assets/images/yeepay.png" alt="易宝公众号"></div>' +
                    '                <div class="yp-desc"><p>可扫码关注易宝支付公共账号输入订单号快速查单：）</p>' +
                    '                    <p class="number">商户订单号 <span class="merchant-order-id"></span></p></div>' +
                    '            </div>' +
                    '        </div>' +
                    '        <p class="repeat-tip">- 若产生重复扣款，金额会立即返回您的银行账户，请放心使用 -</p>' +
                    '    </div>',
                clientId = '<div id="clientId">' +
                    '    <p class="title">' +
                    '        <span class="account-type">企业</span>需提供您在所选银行的网银企业客户号' +
                    '    </p>' +
                    '    <form id="clientIdForm">' +
                    '        <div class="sington"><span class="bank-logo"></span><span class="bank-name">广发银行</span><input type="text" autofocus name="clientId" placeholder="请输入企业客户号"></div>' +
                    '        <button type="button" onclick="Action.eBankPay(true)">确定</button>' +
                    '    </form>' +
                    '</div>',
                agreement = '<div class="agreement">' +
                    '<p class="title">易宝一键支付用户服务协议</p>' +
                    '<div class="elaborate"><p>一、概述</p>' +
                    '<p>1、易宝支付《一键支付用户服务协议》（以下简称“本协议”）是您自愿与易宝支付有限公司（以下简称“易宝支付”）就易宝一键支付服务（以下简称“本服务”）的使用所签订的有效合约。您通过网络页面点击确认本协议或以其他方式选择接受本协议，即表示您与易宝支付已达成协议并同意接受本协议的全部约定内容。</p>' +
                    '<p>2、在您接受本协议之前，请您仔细阅读本协议的全部内容，如您不同意接受本协议的任意内容，或者无法准确理解相关条款含义的，请不要进行后续操作。如果您对本协议的条款有疑问的，请通过易宝支付客服渠道进行询问（易宝支付客服电话为4001-500-800），易宝支付将向您解释条款内容。</p>' +
                    '<p>3、您同意，易宝支付有权随时对本协议内容进行单方面的变更，无需另行单独通知您；若您在本协议内容公告变更生效后继续使用本服务的，表示您已充分阅读、理解并接受变更修改后的协议内容，也将遵循变更修改后的协议内容使用本服务；若您不同意变更修改后的协议内容，您应在变更生效前停止使用本服务。</p>' +
                    '<p>二、双方权利义务</p>' +
                    '<p>1、您应确保您在使用本服务时的银行卡为您本人的银行卡，确保您使用银行卡的行为合法、有效，未侵犯任何第三方合法权益；否则因此造成易宝支付、商户、持卡人损失的，您应负责赔偿并承担全部法律责任。</p>' +
                    '<p>2、您应确保开通本服务所提供的手机号码为本人所有，并授权易宝支付可以通过第三方渠道对您所提供手机号码的真实性、有效性进行核实。</p>' +
                    '<p>3、您应妥善保管银行卡、卡号、密码、发卡行预留的手机号码等与银行卡有关的一切信息。如您遗失或泄漏前述信息，您应及时通知发卡行及/或易宝支付，以减少可能发生的损失。无论是否已通知发卡行及/或易宝支付，因您的原因所致损失需由您自行承担。</p>' +
                    '<p>4、您在使用本服务时，应当认真确认交易信息，包括且不限于商品名称、数量、金额等，并按易宝支付业务流程发出约定的指令。您认可和同意：您发出的指令不可撤回或撤销，易宝支付一旦根据您的指令委托银行或第三方从银行卡中划扣资金给收款人，您不应以非本人意愿交易或其他任何原因要求易宝支付退款或承担其他责任。</p>' +
                    '<p>5、您不应将本服务用于任何违反国家相关法律法规或本协议的目的。</p>' +
                    '<p>6、您在对使用本服务过程中发出指令的真实性及有效性承担全部责任；您承诺，易宝支付依照您的指令进行操作的一切风险由您承担。</p>' +
                    '<p>7、您认可并以易宝支付系统平台记录的数据为准。</p>' +
                    '<p>8、同时您授权易宝支付有权留存您在易宝支付网站填写的相应信息，以供后续向您持续性地提供相应服务（包括但不限于将本信息用于向您推广、提供其他更加优质的产品或服务）。</p>' +
                    '<p>9、出现下列情况之一的，易宝支付有权立即终止您使用易宝支付相关服务而无需承担任何责任：</p>' +
                    '<p>（1）违反本协议的约定；</p>' +
                    '<p>（2）将本服务用于非法目的；</p>' +
                    '<p>（3）违反易宝支付/或其他关联公司网站的条款、协议、规则、通告等相关规定，而被上述任一网站终止提供服务的；</p>' +
                    '<p>（4）易宝支付认为向您提供本服务存在风险的；</p>' +
                    '<p>（5）您的银行卡无效、有效期届满或注销（如有）。</p>' +
                    '<p>10、若您未违反本协议约定且因不能归责于您的原因，造成银行卡内资金通过本服务出现损失，且您未从中获益的，您可向易宝支付申请补偿。您应在知悉资金发生损失后及时通知易宝支付并按要求提交相关的申请材料和证明文件，您同意，您能否得到补偿及具体金额取决于易宝支付自身独立的判断，易宝支付承诺不会因此损害您的合法权益。</p>' +
                    '<p>11、您同意并认可易宝支付最终的补偿行为并不代表前述资金损失应归责于易宝支付，亦不代表易宝支付须为此承担其他任何责任。您同意，易宝支付在向您支付补偿的同时，即刻取得您可能或确实存在的就前述资金损失而产生的对第三方的所有债权及其他权利，包括但不限于就上述债权向第三方追偿的权利，且您不再就上述已经让渡给易宝支付的债权向该第三方主张任何权利，亦不再就资金损失向易宝支付主张任何权利。</p>' +
                    '<p>12、此外，在接受补偿的同时或之后，您从其它渠道挽回了前述资金损失的，或有新证据证明您涉嫌欺诈的，或者发生您应当自行承担责任的其他情形，您应在第一时间返还易宝支付向您支付的补偿款项，否则易宝支付有权向您进行追偿。同时您承诺：资金损失事实真实存在，赔偿申请材料真实、有效。您已充分知晓并认识到，基于虚假信息申请保险赔偿将涉及刑事犯罪，易宝支付有权向国家有关机构申请刑事立案侦查。</p>' +
                    '<p>三、承诺与保证</p>' +
                    '<p>1、您保证使用本服务过程中提供的如姓名、身份证号、银行卡卡号及银行预留手机号等信息真实、准确、完整、有效。易宝支付按照您设置的相关信息为您提供相应服务，对于因您提供信息不真实或不完整所造成的损失由您自行承担。</p>' +
                    '<p>2、您授权易宝支付在您使用本服务期间或本服务终止后，有权保留您在使用本服务期间所形成的相关信息数据，同时该授权不可撤销。</p>' +
                    '<p>四、其他条款</p>' +
                    '<p>1、您同意，本协议适用中华人民共和国大陆地区法律。因易宝支付与您就本协议的签订、履行或解释发生争议，双方应努力友好协商解决。如协商不成，易宝支付和用户同意由易宝支付住所地法院管辖审理双方的纠纷或争议。</p>' +
                    '<p>2、本协议内容包括协议正文及所有易宝支付已经发布的或将来可能发布的易宝支付服务的使用规则。所有规则为本协议不可分割的一部分，与协议正文具有相同法律效力。若您在本协议内容发生修订后，继续使用本服务的，则视为您同意最新修订的协议内容；否则您须立即停止使用本服务。</p>' +
                    '<p>3、您同意，本协议之效力、解释、变更、执行与争议解决均适用中华人民共和国法律，没有相关法律规定的，参照通用国际商业惯例和（或）行业惯例。</p>' +
                    '<p>4、本协议部分内容被有管辖权的法院认定为违法或无效的，不因此影响其他内容的效力。</p>' +
                    '<p>5、本协议未尽事宜，您需遵守易宝支付网站上公布的相关服务协议及相关规则。</p></div>' +
                    '<div class="agree-btn"><button onclick=' + 'Util.closePopup(function(){dom.querySelector("#agreement").checked=true;User.form.enabledSubmit("#firstPayBtn")});' + '>已阅读并同意该服务协议</button></div>' +
                    '</div>',
                bindCardAgreement = '<div class="agreement">' +
                    '<p class="title">易宝绑卡支付用户服务协议</p>' +
                    '<div class="elaborate">' +
                    '<p>一、概述</p>' +
                    '<p>1、易宝支付《绑卡支付用户服务协议》（以下简称“本协议”）是您自愿与易宝支付有限公司（以下简称“易宝支付”）就易宝绑卡支付服务（以下简称“本服务”）的使用所签订的有效合约。您通过网络页面点击确认本协议或以其他方式选择接受本协议，即表示您与易宝支付已达成协议并同意接受本协议的全部约定内容。</p>' +
                    '<p>2、在您接受本协议之前，请您仔细阅读本协议的全部内容，如您不同意接受本协议的任意内容，或者无法准确理解相关条款含义的，请不要进行后续操作。如果您对本协议的条款有疑问的，请通过易宝支付客服渠道进行询问（易宝支付客服电话为4001-500-800），易宝支付将向您解释条款内容。</p>' +
                    '<p>3、您同意，易宝支付有权随时对本协议内容进行单方面的变更，无需另行单独通知您；若您在本协议内容公告变更生效后继续使用本服务的，表示您已充分阅读、理解并接受变更修改后的协议内容，也将遵循变更修改后的协议内容使用本服务；若您不同意变更修改后的协议内容，您应在变更生效前停止使用本服务。</p>' +
                    '<p>二、双方权利义务</p>' +
                    '<p>1、您应确保您在使用本服务时的银行卡为您本人的银行卡，确保您使用银行卡的行为合法、有效，未侵犯任何第三方合法权益；否则因此造成易宝支付、商户、持卡人损失的，您应负责赔偿并承担全部法律责任。</p>' +
                    '<p>2、您应确保开通本服务所提供的手机号码为本人所有，并授权易宝支付可以通过第三方渠道对您所提供手机号码的真实性、有效性进行核实。</p>' +
                    '<p>3、您应妥善保管银行卡、卡号、密码、发卡行预留的手机号码等与银行卡有关的一切信息。如您遗失或泄漏前述信息，您应及时通知发卡行及/或易宝支付，以减少可能发生的损失。无论是否已通知发卡行及/或易宝支付，因您的原因所致损失需由您自行承担。</p>' +
                    '<p>4、您在使用本服务时，应当认真确认交易信息，包括且不限于商品名称、数量、金额等，并按易宝支付业务流程发出约定的指令。您认可和同意：您发出的指令不可撤回或撤销，易宝支付一旦根据您的指令委托银行或第三方从银行卡中划扣资金给收款人，您不应以非本人意愿交易或其他任何原因要求易宝支付退款或承担其他责任。</p>' +
                    '<p>5、您不应将本服务用于任何违反国家相关法律法规或本协议的目的。</p>' +
                    '<p>6、您在对使用本服务过程中发出指令的真实性及有效性承担全部责任；您承诺，易宝支付依照您的指令进行操作的一切风险由您承担。' +
                    '<p>7、您认可并以易宝支付系统平台记录的数据为准。</p>' +
                    '<p>8、同时您授权易宝支付有权留存您在易宝支付网站填写的相应信息，以供后续向您持续性地提供相应服务（包括但不限于将本信息用于向您推广、提供其他更加优质的产品或服务）。</p>' +
                    '<p>9、出现下列情况之一的，易宝支付有权立即终止您使用易宝支付相关服务而无需承担任何责任：</p>' +
                    '<p>（1）违反本协议的约定；</p>' +
                    '<p>（2）将本服务用于非法目的；</p>' +
                    '<p>（3）违反易宝支付/或其他关联公司网站的条款、协议、规则、通告等相关规定，而被上述任一网站终止提供服务的；</p>' +
                    '<p>（4）易宝支付认为向您提供本服务存在风险的；</p>' +
                    '<p>（5）您的银行卡无效、有效期届满或注销（如有）。</p>' +
                    '<p>10、若您未违反本协议约定且因不能归责于您的原因，造成银行卡内资金通过本服务出现损失，且您未从中获益的，您可向易宝支付申请补偿。您应在知悉资金发生损失后及时通知易宝支付并按要求提交相关的申请材料和证明文件，您同意，您能否得到补偿及具体金额取决于易宝支付自身独立的判断，易宝支付承诺不会因此损害您的合法权益。</p>' +
                    '<p>11、您同意并认可易宝支付最终的补偿行为并不代表前述资金损失应归责于易宝支付，亦不代表易宝支付须为此承担其他任何责任。您同意，易宝支付在向您支付补偿的同时，即刻取得您可能或确实存在的就前述资金损失而产生的对第三方的所有债权及其他权利，包括但不限于就上述债权向第三方追偿的权利，且您不再就上述已经让渡给易宝支付的债权向该第三方主张任何权利，亦不再就资金损失向易宝支付主张任何权利。</p>' +
                    '<p>12、此外，在接受补偿的同时或之后，您从其它渠道挽回了前述资金损失的，或有新证据证明您涉嫌欺诈的，或者发生您应当自行承担责任的其他情形，您应在第一时间返还易宝支付向您支付的补偿款项，否则易宝支付有权向您进行追偿。同时您承诺：资金损失事实真实存在，赔偿申请材料真实、有效。您已充分知晓并认识到，基于虚假信息申请保险赔偿将涉及刑事犯罪，易宝支付有权向国家有关机构申请刑事立案侦查。</p>' +
                    '<p>三、承诺与保证</p>' +
                    '<p>1、您保证使用本服务过程中提供的如姓名、身份证号、银行卡卡号及银行预留手机号等信息真实、准确、完整、有效。易宝支付按照您设置的相关信息为您提供相应服务，对于因您提供信息不真实或不完整所造成的损失由您自行承担。</p>' +
                    '<p>2、您授权易宝支付在您使用本服务期间或本服务终止后，有权保留您在使用本服务期间所形成的相关信息数据，同时该授权不可撤销。</p>' +
                    '<p>四、其他条款</p>' +
                    '<p>1、您同意，本协议适用中华人民共和国大陆地区法律。因易宝支付与您就本协议的签订、履行或解释发生争议，双方应努力友好协商解决。如协商不成，易宝支付和用户同意由易宝支付住所地法院管辖审理双方的纠纷或争议。</p>' +
                    '<p>2、本协议内容包括协议正文及所有易宝支付已经发布的或将来可能发布的易宝支付服务的使用规则。所有规则为本协议不可分割的一部分，与协议正文具有相同法律效力。若您在本协议内容发生修订后，继续使用本服务的，则视为您同意最新修订的协议内容；否则您须立即停止使用本服务。</p>' +
                    '<p>3、您同意，本协议之效力、解释、变更、执行与争议解决均适用中华人民共和国法律，没有相关法律规定的，参照通用国际商业惯例和（或）行业惯例。</p>' +
                    '<p>4、本协议部分内容被有管辖权的法院认定为违法或无效的，不因此影响其他内容的效力。</p>' +
                    '<p>5、本协议未尽事宜，您需遵守易宝支付网站上公布的相关服务协议及相关规则。</p>' +
                    '</div>' +
                    '<div class="agree-btn"><button onclick=' + 'Util.closePopup(function(){dom.querySelector("#agreement").checked=true;User.form.enabledSubmit("#firstPayBtn")});' + '>已阅读并同意该服务协议</button></div>' +
                    '</div>',
                chinaBankAgreement = '<div class="agreement">' +
                    '<p class="title">中国银行快捷支付服务协议</p>' +
                    '<div class="elaborate">' +
                    '<p>请客户认真阅读本协议文本。</p>' +
                    '<p>1.中国银行股份有限公司借记卡快捷支付业务（以下简称“快捷支付业务”）是中国银行股份有限公司（以下简称“中国银行”）和易宝支付有限公司  公司（下称“合作支付机构”）联合为中国银行借记卡持卡客户（以下简称“客户”）提供的一项电子支付服务。凡是持有中国银行股份有限公司在中国大陆地区发行的借记卡的客户，均可申请该业务。</p>' +
                    '<p>2.客户在签约快捷支付业务时，将收到中国银行发送的带有手机交易码的签约确认短信。客户应认真阅读本协议，在同意本协议约定内容的前提下输入手机交易码。客户输入手机交易码即视同为同意本协议内容。</p>' +
                    '<p>3.客户通过互联网（Internet）在合作支付机构网站或客户端，选择在线与中国银行签订本服务协议，将指定的借记卡与此时登录的账户进行关联，开通快捷支付功能，并承担由此产生的一切责任。</p>' +
                    '<p>4.客户同意，中国银行以合作支付机构传送至中国银行的户名（如与合作支付机构约定不校验姓名则删除）、卡号、手机号码、证件号码（如与合作支付机构约定不校验证件号码则删除）等身份要素作为确认客户身份、为客户开通快捷支付业务的唯一证据。中国银行比对户名（如与合作支付机构约定不校验姓名则删除）、卡号、手机号码、证件号码（如与合作支付机构约定不校验证件号码则删除）与客户在银行预留信息一致后，通过95566发送手机交易码至客户预留手机号上，并对合作支付机构传送的客户填写的手机交易码进行验证，验证一致通过后，完成客户指定的借记卡与此时所登录账户的关联。客户须保证在中国银行预留手机号码为本人手机号码并确保其准确性。基于提高交易安全的目的，中国银行有权变更上述身份要素。客户须妥善保管本人中国银行借记卡信息，本人身份信息、手机交易码、安全认证工具等敏感信息，不得将敏感信息向他人透露。因客户银行预留信息错误，或个人信息、手机等保管不善产生的风险及损失由客户本人承担。</p>' +
                    '<p>5.开通快捷支付业务后，客户同意并授权中国银行在本协议约定的服务范围内，将其所关联借记卡的原有支付验证方式变更为按照合作支付机构发送的交易指令从客户指定的借记卡账户中支付相应款项。客户需在其绑定的借记卡的余额内进行支付，否则中国银行有权拒绝执行交易指令。</p>' +
                    '<p>6.中国银行可根据业务发展需要，设置或修改支付限额，客户对此无异议，客户办理快捷支付业务时需同时受中国银行和合作支付机构设置的支付限额的约束。在任何情况下，支付金额不应超过中国银行或合作支付机构设置的支付限额（以较低者为准）。如实际支付金额大于规定的支付限额，中国银行将拒绝执行交易指令。</p>' +
                    '<p>7.本协议项下，中国银行仅提供通过合作支付机构发生的支付结算服务。客户因通过合作支付机构购买商品而产生的一切关于商品或服务质量、商品交付，交易款项扣收与划付的争议均由客户与商户或商品/服务的实际销售商自行协商解决。</p>' +
                    '<p>8.客户使用借记卡快捷支付业务应防范的风险包括但不限于：</p>' +
                    '<p>（1）客户借记卡快捷支付使用的用户名、账户、密码等重要信息可能被其他人猜出、偷窥或通过木马病毒、假冒网站、欺诈电话或短信等手段窃取，从而导致客户信息泄露、资金损失、账户被恶意操作等后果。</p>' +
                    '<p>（2）手机可能被盗或未经许可被他人使用，若手机银行密码同时被窃取，可能造成账户资金损失等情况；若客户更换手机号时未取消以原手机号开通的短信提醒服务，当原手机号被他人使用时，可能造成客户的账户信息泄露等情况。</p>' +
                    '（3）客户使用设备的操作系统、杀毒软件、网络防火墙等客户端软件未及时更新或安装补丁程序，可能导致客户信息泄露、资金损失、账户被恶意操作等后果。</p>' +
                    '<p>9.客户应采取风险防范措施，安全使用借记卡快捷支付业务，相关措施包括不限于：</p>' +
                    '<p>（1）妥善保护借记卡快捷支付业务使用的用户名、账户、密码等重要身份识别标识、身份认证信息及工具，确保上述重要信息和认证工具不经口头或其它方式发生泄露或被盗用。</p>' +
                    '<p>（2）对所使用设备的操作系统、杀毒软件、网络防火墙等客户端软件采取有效的措施（如及时更新系统或安装补丁程序等）防范操作系统软件漏洞风险、电子设备中的病毒等。</p>' +
                    '<p>（3）妥善保管借记卡、手机及与借记卡快捷支付业务相关的重要身份证件等资料，不交给其他人保管，不放置在不安全场所。</p>' +
                    '<p>（4）避免使用容易被其他人猜出或破解的密码（如：采用生日、电话号码等与个人信息相关的信息或具有规律性的数字或字母组合作为快捷支付业务密码），避免设置与其他用途相同的密码（如：采用电子邮件密码作为借记卡快捷支付密码）。客户应定期或不定期更新其密码。</p>' +
                    '<p>（5） 应经常关注账户内资金变化，如发现任何通过中国银行借记卡快捷支付从事的未经客户许可的操作及交易，客户应马上与支付机构或中国银行取得联系。</p>' +
                    '<p>10.借记卡快捷支付业务资金清算可能出现异常及差错交易，银行及合作支付机构将对异常及差错交易及时进行纠正处理。银行及合作支付机构将根据客户要求提供交易记录的时间和方式等信息。</p>' +
                    '<p>11.客户不得利用中国银行快捷支付业务进行虚假交易、洗钱、诈骗、恐怖融资等行为，且有义务配合中国银行进行相关调查，一旦客户拒绝配合进行相关调查或中国银行认为客户存在或涉嫌虚假交易、洗钱或任何其他非法活动、欺诈或违反诚信原则的行为、或违反本协议约定的，中国银行有权采取以下一种、多种或全部措施：</p><p>（1）暂停或终止提供本协议项下快捷支付业务服务；</p><p>（2）终止本协议；</p><p>（3）取消客户的用卡资格。</p>' +
                    '<p>12.客户同意中国银行就快捷支付业务的开展，向支付机构提供客户留存在中国银行的户名（如与支付机构约定不校验姓名则去掉）、银行卡号、证件号码（如与支付机构约定不校验证件号码则去掉）、手机号码等信息。</p>' +
                    '<p>13.客户可以登录中国银行官方网站撤销本人名下指定借记卡与支付机构账户关联的快捷支付功能。</p>' +
                    '<p>14.中国银行系统升级、业务变化、收费变更，或根据业务发展需要修改本协议，中国银行将提前进行公告。若客户有异议，有权选择撤销相关服务，若客户选择继续接受该服务的，视为客户同意并接受相关业务或协议按变更或修改后的内容执行。双方同意，本协议所称公告均指在中国银行网点、中国银行网站【http://www.boc.cn】、电子银行渠道等进行公告。</p>' +
                    '<p>15.选择打勾的客户，视同已通读上述条款，对条款的内容及其后果已充分理解，对所有内容均无异议。</p>' +
                    '<p>16.本协议在客户于支付机构提供的页面服务协议栏位打勾并提交后生效。本协议的生效、履行及解释均使用中华人民共和国法律（为本协议之目的，不含中国香港、澳门、台湾地区法律）。本协议项下的争议向中国银行所在地人民法院起诉。</p>' +
                    '<p>合同使用说明：本合同为个人支付客户使用中国银行借记卡快捷支付服务时与中国银行签订的服务协议。中国银行借记卡快捷支付合作支付机构应在支付客户签约中国银行借记卡快捷支付服务时向客户展示该协议，客户阅签该协议后方可签约借记卡快捷支付服务。</p>' + '</div>' +
                    '<div class="agree-btn"><button onclick=' + 'Util.closePopup(function(){dom.querySelector("#agreement").checked=true;User.form.enabledSubmit("#firstPayBtn")});' + '>已阅读并同意该服务协议</button></div>' +
                    '</div>',
                idChoice = '<div class="id-choice">' +
                    '        <p class="pay-result"><i class="icon">&#xe66b;</i>支付已完成</p>' +
                    '        <p class="tip">为保障支付安全，请选择实名身份。选择后只能使用该身份的卡支付；其他身份的卡将不可用。详情咨询<span class="tel">4001-500-800</span></p>' +
                    '        <ul class="id-list">' +
                    '            <li class="new-id-trigger" onclick="User.Identity.add()">使用其他身份</li>' +
                    '        </ul>' +
                    '        <div class="confirm-layout">' +
                    '            <button type="button" class="confirm">确定并返回商户</button>' +
                    '        </div>' +
                    '    </div>' +
                    '    <div class="new-id">' +
                    '        <p class="caption"><span class="back" onclick="User.Identity.back()">返回</span>使用其他身份</p>' +
                    '        <form id="addIdForm">' +
                    '            <div class="sington"><label><span class="title">姓名</span><input type="text"' +
                    '                                                                                 placeholder="输入您的真实姓名"' +
                    '                                                                                 name="name" encrypt="true" maxlength="16"></label>' +
                    '            </div>' +
                    '            <div class="sington"><label><span class="title">身份证</span><input type="text"' +
                    '                                                                                  placeholder="请输入你的身份证号"' +
                    '                                                                                  name="idno" encrypt="true" maxlength="18"></label>' +
                    '            </div>' +
                    '            <div class="confirm-layout">' +
                    '                <button type="button" class="confirm">确定并返回商户</button>' +
                    '            </div>' +
                    '        </form>' +
                    '    </div>',
                authorized = '<div class="authorized">' +
                    '    <h2>快捷添加银行卡</h2>' +
                    '    <p class="prompt none">您(身份证号：<span id="cardNoLater"></span>)有其他可用银行卡。通过短信验证后可快速使用。短信已发送至：<span id="phoneLater"></span>。如有疑问，致电4001-500-800</p>' +
                    '    <form class="verifycode">' +
                    '        <input type="tel" class="unit" autofocus>' +
                    '        <input type="tel" class="unit">' +
                    '        <input type="tel" class="unit">' +
                    '        <input type="tel" class="unit">' +
                    '        <input type="tel" class="unit">' +
                    '        <input type="tel" class="unit">' +
                    '        <button type="button" class="obtain-btn">点击重新获取</button>' +
                    '    </form>' +
                    '    <p class="errormsg none"></p>' +
                    '    <div class="submit-wrapper"><button class="submit" disabled>验证</button><a href="javascript:void(0)" class="add-new-card">直接添加新卡</a> </div>' +
                    '</div>',
                forgetTradingPassword = '<div class="retrieve-password">' +
                    '<h2>修改交易密码操作指南</h2>' +
                    '    <ol>' +
                    '        <li>登录易宝商户后台</li>' +
                    '        <li>找到<em class="emp">“服务中心”&gt;我的操作员账号</em>或<em class="emp">“系统管理”</em>菜单</li>' +
                    '        <li>选择修改交易密码</li>' +
                    '    </ol>' +
                    '    <a class="button set-password" href="http://www.yeepay.com/selfservice/login.action" target="_blank">修改交易密码</a>' +
                    '    <span class="auto-set"><em id="secondRemain">5</em>s后自动跳转</span>' +
                    '</div>',
                bindCardPayPas = '<div class="sms-code bind-card-pas">' +
                    '        <h2>支付确认</h2>' +
                    '        <form id="smsForm">' +
                    '            <div class="sington icon"><label><span class="title">取款密码</span><input type="password"' +
                    '                                                                                  placeholder="请输入取款密码"' +
                    '                                                                                  maxlength="6" name="bankPWD"></label>' +
                    '            </div>' +
                    '            <button class="confirm" type="button">确认</button>' +
                    '        </form>' +
                    '    </div>',
                psdCode = '<div class="sms-code bind-card-pas">' +
                    '        <h2>支付确认</h2>' +
                    '        <p class="send-tip none"></p>' +
                    '        <form id="smsForm">' +
                    '            <div class="sington icon" style="margin-left: 0"><label><span class="title">取款密码</span><input type="password"' +
                    '                                                                                  placeholder="请输入取款密码"' +
                    '                                                                                  maxlength="6" encrypt="true" name="bankPWD" style="width:427px"></label>' +
                    '            </div>' +
                    '            <div class="sington icon" style="margin-left: 0"><label><span class="title">验证码</span><input type="text"' +
                    '                                                                                  placeholder="请输入验证码"' +
                    '                                                                                  maxlength="6" name="verifycode"></label>' +
                    '                <button type="button" class="validate-code" disabled>获取验证码</button>' +
                    '            </div>' +
                    '            <button class="confirm" disabled type="button" style="margin-left: 115px">确认</button>' +
                    '        </form>' +
                    '</div>';
            return {
                smsCode: smsCode,
                waitingPay: waitingPay,
                clientId: clientId,
                queryResult: queryResult,
                agreement: agreement,
                bindCardAgreement: bindCardAgreement,
                chinaBankAgreement: chinaBankAgreement,
                idChoice: idChoice,
                authorized: authorized,
                forgetTradingPassword: forgetTradingPassword,
                bindCardPayPas: bindCardPayPas,
                psdCode: psdCode
            }
        }();

        function Error(selector, errormsg, cls, position, duration) {
            /*错误信息
             * selector错误信息显示位置选择器
             * errormsg错误信息
             * cls错误信息样式
             * position插入selector位置
             * duration显示持续时间
             * */
            var root = dom.querySelector(selector);
            cls = cls || "global-error";
            position = position || "beforeEnd";
            var error = root.querySelector("." + cls);
            session.setItem("errMsg", errormsg);//错误信息存储到缓存，点击在线客服时传给客服
            if (!error) {
                /*
                 * 错误信息不存在
                 * */
                root.insertAdjacentHTML(position, '<div class=' + cls + '>' + errormsg + '</div>');
            } else {
                /*
                 * 错误信息已经存在，更新错误信息
                 * */
                error.innerHTML = errormsg;
                error.classList.remove("none");
            }
            if (Util.type(duration) == "number") {
                setTimeout(function () {
                    error.classList.add("none");
                }, duration);
            }
        }

        function ErrorHandler(selector, obj, duration) {
            /*错误信息包含点击按钮（换卡支付等）
             * selector错误信息显示位置选择器
             * obj map类型 包含errormsg，bankName,bankCode
             * duration显示持续时间
             * */
            var root = dom.querySelector(selector);
            var temp = '<div class="global-error">' +
                '                ' + obj.errormsg + '<span onclick="User.payModeBank(event)" class="other-bank" bankCode="' + obj.bankCode + '">' + obj.bankName + '网银支付</span><span' +
                '        class="change-card" onclick="User.cardPayments(event)">换卡支付></span>' +
                '                </div>';
            if (!root.contains(dom.querySelector(".global-error"))) {
                root.insertAdjacentHTML("beforeEnd", temp);
                var globalError = dom.querySelector(".global-error");
            } else {
                var globalError = dom.querySelector(".global-error");
                globalError.classList.remove("none");
                root.appendChild(globalError);
            }
            if (Util.type(duration) == "number") {
                setTimeout(function () {
                    globalError.classList.add("none");
                }, duration);
            }
        }

        function payModeBank(event) {
            /*
             * 网银支付
             * */
            var target = event.target || event.srcElement;
            dom.querySelector("#payModeBank").checked = true;
            var bank = dom.querySelector(".bank-list li[bankcode=" + target.getAttribute("bankcode") + "]");
            if (bank != null) {
                bank.click();
            }
        }

        function cardPayments() {
            /*
             * 换卡支付
             * */
            dom.querySelector("#addItemWrapper").innerHTML = "";
            dom.querySelector("#quickPayForm").cardno.value = "";
            dom.querySelector("#quickPayForm").cardno.focus();
            dom.querySelector("#quickPayForm .fn-btn").classList.remove("none");

        }

        function autoValidateCode() {
            /*
             * 自动获取验证码，开始倒计时
             * */
            var self = this;
            self.form.verifycode.focus();
            User.form.disabledSubmit(self);
            User.timing(function (second) {
                /*
                 * 开始60秒倒计时
                 * */
                self.innerHTML = second + "s后重新获取";
            }, function () {
                /*
                 * 倒计时结束回调
                 * */
                User.form.enabledSubmit(self);
                self.innerHTML = "重新获取";
                self.form.querySelector(".sington").classList.remove("error");
            });
        }

        var Identity = function () {
            function add() {
                /*
                 *添加新身份
                 * */
                dom.querySelector(".new-id").classList.add("show");
                dom.querySelector(".id-choice").classList.add("filter-blur");
            }

            function back() {
                /*
                 *返回身份选择
                 * */
                dom.querySelector(".new-id").classList.remove("show");
                dom.querySelector(".id-choice").classList.remove("filter-blur");
                User.form.reset("#addIdForm");
            }

            return {add: add, back: back}
        }();
        var Authorized = function () {
            /*
             * 跨商户授权
             * */
            function bindInteraction() {
                dom.querySelector(".add-new-card").addEventListener("click", function () {
                    /*
                     * 授权点击直接添加新卡，关闭弹层显示添加新卡页面
                     * */
                    Util.closePopup();
                    dom.querySelector("#addNewCard").checked = true;
                }, false);
                dom.querySelector(".authorized .obtain-btn").addEventListener("click", function () {
                    /*
                     * 授权点击重新获取，请求验证码
                     * */
                    this.disabled = true;
                    Action.authoritySendSms();
                }, false);
                dom.querySelector(".authorized .submit").addEventListener("click", function () {
                    /*
                     * 授权短信确认
                     * */
                    this.disabled = true;
                    Action.authoritySendSmsConfirm();
                }, false);
                /*
                 * 验证码输入框交互
                 * */
                var units = array(dom.querySelectorAll(".verifycode input"));//保存验证码每一个单元格
                var rule = /^\d+$/;

                function saveSession() {
                    /*
                     * 保存验证码到session
                     * */
                    var str = "";
                    units.forEach(function (unit) {
                        str += unit.value;
                    });
                    session.setItem("verifycode", str);
                    session.getItem("verifycode").length == 6 ? dom.querySelector(".authorized .submit").disabled = false : dom.querySelector(".authorized .submit").disabled = true;
                }

                function prev(elem) {
                    /*
                     * 上一个同级节点
                     * */
                    if ("previousElementSibling" in elem) {
                        return elem.previousElementSibling;
                    }
                    var temp = elem;
                    while (temp.previousSibling != null && temp.previousSibling.nodeType != 1) {
                        temp = temp.previousSibling;
                    }
                    return temp.previousSibling;
                }

                function next(elem) {
                    /*
                     * 下一个同级节点
                     * */
                    if ("nextElementSibling" in elem) {
                        return elem.nextElementSibling;
                    }
                    var temp = elem;
                    while (temp.nextSibling != null && temp.nextSibling.nodeType != 1) {
                        temp = temp.nextSibling;
                    }
                    return temp.nextSibling;
                }

                function del() {
                    /*
                     * 删除
                     * */
                    if (this.value != "") {
                        this.value = ""
                    } else {
                        if (prev(this) != null) {
                            prev(this).value = "";
                            prev(this).focus();
                        }
                    }
                }

                function focus() {
                    var val = this.value;
                    this.value = val + "";
                    this.focus();
                }

                function processor(event) {
                    switch (event.keyCode) {
                        case 37:
                            /*按下左箭头键*/
                            if (prev(this) != null) {
                                focus.call(prev(this));
                            }
                            return;
                        case 39:
                            /*按下右箭头键*/
                            if (next(this) != null) {
                                focus.call(next(this));
                            }
                            return
                    }
                    var lastText = this.value.slice(-1);
                    if (rule.test(lastText)) {
                        this.value = lastText;
                        next(this) != null ? focus.call(next(this)) : false;
                    } else {
                        this.value = this.value.replace(/[^0-9]/, "");
                    }
                    saveSession();
                }

                units.forEach(function (unit) {
                    unit.addEventListener("keyup", processor, false);
                    unit.addEventListener("keydown", function (event) {
                        if (event.keyCode == 8) {
                            /*
                             * 按下delete键
                             * */
                            del.call(this);
                        }
                    }, false);
                });
            }

            function timing() {
                dom.querySelector("#phoneLater").innerHTML = session.getItem("phoneLater");
                dom.querySelector("#cardNoLater").innerHTML = session.getItem("cardNoLater");
                dom.querySelector(".authorized .prompt").classList.remove("none");
                dom.querySelector(".authorized .errormsg").classList.add("none");
                User.timing(function (second) {
                    /*
                     * 开始倒计时
                     * */
                    dom.querySelector(".authorized .obtain-btn").innerHTML = second + "s后重新获取";
                }, function () {
                    /*
                     * 倒计时结束
                     * */
                    dom.querySelector(".authorized .obtain-btn").disabled = false;
                    dom.querySelector(".authorized .obtain-btn").innerHTML = "点击重新获取";
                });
            }

            return {bindInteraction: bindInteraction, timing: timing}
        }();

        /********导出对外接口**********/
        return {
            form: form,
            Tab: Tab,
            Bank: Bank,
            timing: timing,
            bankCard: bankCard,
            template: template,
            Error: Error,
            ErrorHandler: ErrorHandler,
            payModeBank: payModeBank,
            cardPayments: cardPayments,
            autoValidateCode: autoValidateCode,
            Identity: Identity,
            Authorized: Authorized
        }
    }();
    global.User = User;

}(window));