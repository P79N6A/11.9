/*最后修改日期 2018.10.23 16:38*/
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
        compatible: "IE8+",
        developers: "\u5C0F\u9ED1\u624B",
        contact: "ying.xia@yeepay.com"
    }
    /*console.log("%c\u524D\u7AEF\u5F00\u53D1\u4E3B\u8981\u8D1F\u8D23\u4EBA：" + infoMap.developers, "color:#eed503; font-size:16px;");
     console.log("%c\u5E2E\u52A9\u4E0E\u53CD\u9988：" + infoMap.contact, "color:#eed503; font-size:16px;");*/
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
        /*if (obj instanceof Element) {
         return 'element';
         }*/
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
        if (this.type(cache) != "array") return false;//没有匹配的script
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
            if (!Modernizr.csstransforms) {
                dom.querySelector("#Popup").style.marginLeft = dom.querySelector("#Popup").offsetWidth / -2 + "px";
                dom.querySelector("#Popup").style.marginTop = dom.querySelector("#Popup").offsetHeight / -2 + "px";
            }
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
            dom.querySelector(".header").classList.add("filter-blur");
            dom.querySelector(".main").classList.add("filter-blur");
            dom.querySelector(".footer").classList.add("filter-blur");
        } catch (error) {
            //
        }
    };
    Util.unblur = function () {
        //使内容清晰
        try {
            dom.querySelector(".header").classList.remove("filter-blur");
            dom.querySelector(".main").classList.remove("filter-blur");
            dom.querySelector(".footer").classList.remove("filter-blur");
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
                instance = new _createDiv('<img src="../assets/images/loading.gif"><span>' + message + '</span>');
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
        if (input.nodeType == 1 && input.nodeName.toLowerCase() == "input" && reg.test(input.type)) input.value = "";
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
 * Created by ying-xia on 16/11/12.
 * 包含了所有和后端交互的方法
 */

(function (global) {
    'use strict';
    //生产环境action
    //var action = "/nc-cashier-wap/newpc/";
    //测试环境action
    var action = location.protocol + "//" + "location.host";
    var Action = {
        ajax: function (options) {
            /*
             * ajax请求
             * */
            var _options = {
                async: true,
                method: "GET",
                url: location.href,
                //    timeout: 10000,
                encrypt: false,
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8'
                // contentType: 'application/json; charset=UTF-8'
            }
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
        setForm: function (obj, flag) {   //创建表单提交数据到银联
            var url = obj.url,
                charset = obj.charset,
                method = obj.method,
                params = obj.params;
            var html = '<form name="toBankForm" id="toBankForm" target=' + (flag ? '\"_blank\"' : '\"_self\"') + ' action="' + url + '" method="' + method + '"  accept-charset="' + charset + '">'
            for (var item in params) {
                html += '<input type="hidden" name="' + item + '" value="' + params[item] + '" />'
            }
            html += '</form>';
            dom.querySelector("body").insertAdjacentHTML('beforeEnd', html)
            dom.querySelector("#toBankForm").submit();
            dom.querySelector("body").removeChild(dom.querySelector("#toBankForm"));
        },
        getValidateCode: function (event) {
            /*
             * 首次支付获取验证码
             * */
            var self = event.target;
            User.autoValidateCode.call(self);
            var sendDate = JSON.parse(session.getItem("cardInfo"));
            Action.ajax({
                url: session.getItem("contextUrl") + "/newpc/firstpay/smsSend",
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
                url: session.getItem("contextUrl") + "/newpc/bindpay/bindSmsSend",
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
                    url: session.getItem("contextUrl") + "/newpc/firstpay/confirm",
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
                            // Util.hideLoading();
                            //location.assign("/nc-cashier-wap/newpc/pay/success_cancel/"+session.getItem("token"));
                            Action.getQueryStatus(parseInt(session.getItem("jsTrys")), "YJZF");
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
            var validate = User.form.autoValidate("#quickPayForm");
            if (validate) {
                dom.querySelector("#quickPayForm .loading").classList.remove("none");
                Action.getComponent({
                    data: User.form.serialize("#quickPayForm", {
                        token: session.getItem("token"),
                        isBindCardChangeCard: session.getItem("isBindCardChangeCard")
                    }),
                    //url: "cardbin_item.vm"
                    url: session.getItem("contextUrl") + "/newpc/request/notpasscardno"
                }, "#addItemWrapper", function () {
                    self.classList.add("none");
                    User.form.validate("#quickPayForm");
                    dom.querySelector("#quickPayForm .loading").classList.add("none");
                    var error = dom.querySelector("#quickPayWrapper .global-error");
                    if (error) {
                        error.classList.add("none");
                        session.removeItem("errMsg");
                    }
                }, function (data) {
                    dom.querySelector("#quickPayForm .loading").classList.add("none");
                    if (triggerMode == "manual") {
                        User.Error("#quickPayWrapper .quick-pay-main", data.errormsg + '(' + data.errorcode + ')', null, "afterBegin");
                    }
                });
            }
        },
        firstPay: function (isPassCardNo) {
            /*
             * 首次支付下单
             * isPassCardNo是否是透传卡号 true透传
             * */
            var sendData = User.form.serialize("#quickPayForm", {token: session.getItem("token")});
            session.setItem("cardInfo", JSON.stringify(sendData));//保存卡全部信息，方便获取验证码时再次使用
            if (isPassCardNo) {
                if (!User.form.autoValidate("#quickPayForm", "cardno")) return;
            }
            if (!User.form.autoValidate("#quickPayForm")) return;
            this.ajax({
                url: session.getItem("contextUrl") + "/newpc/firstpay/smsSend",
                data: sendData,
                success: function (responseText) {
                    var data = JSON.parse(responseText);
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
                        User.Error("#quickPayWrapper .quick-pay-main", data.errormsg + '(' + data.errorcode + ')', null, "afterBegin");
                    }
                }
            });
        },
        eBankPayRoute: function (scene, rechargeType) {
            /*
             * 网银支付点击下一步
             * 如果需要网银企业客户号，弹出弹层输入网银企业客户号
             * 否则直接支付
             * */
            var currentBank = (rechargeType == "NET" || typeof rechargeType == 'undefined') ? dom.querySelector("#bankPayWrapper .bank-list.active .active") : dom.querySelector("#remitBankPayWrapper .bank-list.active .active");
            if (currentBank.getAttribute("isneedclient") == "true") {
                Util.popup("", User.template.clientId, function () {
                    User.form.validate("#clientIdForm");
                    dom.querySelector("#clientIdForm .bank-logo").className = "bank-logo " + currentBank.getAttribute("bankcode");
                    dom.querySelector("#clientIdForm .bank-name").innerHTML = currentBank.getAttribute("bankname");
                    if (typeof scene != "undefined") {
                        dom.querySelector("#clientIdForm button").setAttribute("onclick", "Action.eBankPay(true" + ",\"" + scene + "\"," + "\"" + rechargeType + "\"" + ")");
                    }
                });
                return;
            }
            this.eBankPay(null, scene, rechargeType);
        },
        eBankPay: function (clientId, scene, rechargeType) {
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
            var currentBank = (rechargeType == "NET" || typeof rechargeType == 'undefined') ? dom.querySelector("#bankPayWrapper .bank-list.active .active") : dom.querySelector("#remitBankPayWrapper .bank-list.active .active");
            var sendData = {
                token: session.getItem("token"),
                payScene: currentBank.getAttribute("payScene"),
                bankCode: currentBank.getAttribute("bankcode"),
                ebankAccountType: currentBank.getAttribute("bankaccounttype")
            }
            if (clientId) {
                sendData.clientId = dom.querySelector("#clientIdForm").clientId.value;
                User.form.autoValidate("#clientIdForm");
            }
            this.ajax({
                url: session.getItem("contextUrl") + "/newpc/ebank/pay",
                data: sendData,
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.errorCode) {
                        if (clientId) {
                            Util.closePopup();
                        }
                        if (rechargeType == "NET" || typeof rechargeType == 'undefined') {
                            dom.querySelector("#bankPayWrapper .bank-container .error-wrapper").innerHTML = "";
                            User.Error("#bankPayWrapper .bank-container .error-wrapper", data.errorMsg);
                        } else {
                            dom.querySelector("#remitBankPayWrapper .bank-container .error-wrapper").innerHTML = "";
                            User.Error("#remitBankPayWrapper .bank-container .error-wrapper", data.errorMsg);
                        }
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
                        if (data.redirectType == 'TO_PCC') {
                            var a = document.createElement("a");
                            a.href = data.payUrl;
                            scene ? a.target = "_self" : a.target = "_blank";
                            dom.body.appendChild(a);
                            a.click();
                            //window.open(data.payUrl, "_blank");
                        } else if (data.redirectType == 'TO_BANK') {
                            var ebankUrlInfo = typeof data.ebankUrlInfo == 'string' ? JSON.parse(data.ebankUrlInfo) : data.ebankUrlInfo;
                            var target = scene ? false : true;
                            Action.setForm(ebankUrlInfo, target);
                        }

                    });
                }
            });
        },
        ebankResult: function () {
            /*
             * 网银支付点击已完成支付查询结果页
             * */
            location.assign(session.getItem("contextUrl") + "/newpc/ebank/result/" + session.getItem("token"));
        },
        bindPayPayment: function (currentOption) {
            /*
             * 绑卡支付下单
             * 下单前先清空补充项
             * 如果当前银行卡没有超出限额，请求补充项
             * */
            if (dom.querySelector(".global-error") != null) {
                dom.querySelector(".global-error").classList.add("none");
            }
            User.form.disabledSubmit("#bindPayBtn");
            dom.querySelector("#bindItemFormWrapper").innerHTML = "";
            if (currentOption.getAttribute("data-disabled") == "true") return;
            /*----------------*/
            dom.querySelector("#bindItemFormWrapper").innerHTML = '<p class="waiting-add-item"><img src="../assets/images/loading.gif" width="23" />正在加载补充信息...</p>';
            this.getComponent({
                data: {
                    token: session.getItem("token"),
                    bindId: dom.querySelector(".quick-pay .current-selected").getAttribute("data-bindid")
                },
                url: session.getItem("contextUrl") + "/newpc/bindpay/requestPayment"
            }, "#bindItemFormWrapper", function () {
                User.form.enabledSubmit("#bindPayBtn");
            }, function (data) {
                /*
                 * 下单错误回调
                 * */
                dom.querySelector("#bindItemFormWrapper").innerHTML = "";
                User.Error("#quickPayWrapper .quick-pay-main", data.errormsg + '(' + data.errorcode + ')', null, "afterBegin");
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
                    url: session.getItem("contextUrl") + "/newpc/bindpay/confirm",
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
                } else {
                    User.form.enabledSubmit("#smsForm .confirm");
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
                var needSms = session.getItem("needSms") == "" ? false : true;
                if (needSms) {
                    /*
                     * 需要短验
                     * */
                    this.ajax({
                        data: User.form.serialize("#bindItemForm", {
                            token: session.getItem("token"),
                            bindId: dom.querySelector(".quick-pay .current-selected").getAttribute("data-bindid"),
                            smsType: session.needSms
                        }),
                        url: session.getItem("contextUrl") + "/newpc/bindpay/bindSmsSend",
                        success: function (responseText) {
                            var data = JSON.parse(responseText);
                            if (data.bizStatus == "success") {
                                var sendData = User.form.serialize("#bindItemForm", {
                                    token: session.getItem("token"),
                                    bindId: dom.querySelector(".quick-pay .current-selected").getAttribute("data-bindid")
                                });
                                Util.popup("", User.template.smsCode, function () {
                                    dom.querySelector("#smsForm .confirm").onclick = function () {
                                        User.form.disabledSubmit(this);
                                        sendData.verifycode = dom.querySelector("#smsForm").verifycode.value;
                                        Action.bindPayConfirm.call(Action, true, sendData);
                                    }
                                    dom.querySelector("#smsForm .validate-code").onclick = Action.getBindSms.bind(dom.querySelector("#smsForm .validate-code"), event, User.form.serialize("#bindItemForm", {
                                        token: session.getItem("token"),
                                        bindId: dom.querySelector(".current-selected").getAttribute("data-bindid"),
                                        smsType: session.getItem("needSms")
                                    }));
                                    dom.querySelector(".send-tip").classList.remove("none");
                                    var currentSelected = dom.querySelector(".quick-pay .current-selected");

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
                                User.Error("#quickPayWrapper .quick-pay-main", data.errormsg + '(' + data.errorcode + ')', null, "afterBegin");
                            }
                        }
                    });

                } else {
                    /*
                     * 不需要短验
                     * */
                    this.bindPayConfirm(false, User.form.serialize("#bindItemForm", {
                        token: session.getItem("token"),
                        bindId: dom.querySelector(".quick-pay .current-selected").getAttribute("data-bindid")
                    }));
                }
            }
        },
        getComponent: function (options, selector, callback, error) {
            /*
             * 获取组件
             * options:请求组件时需要的参数
             * selector:请求成功后插入到页面位置选择器
             * callback:插入到页面后的回调函数
             * error:服务器返回错误信息回调
             * */
            function successCallBack(responseText) {
                /*
                 * 请求成功回调
                 * */
                try {
                    /*返回值是JSON*/
                    var data = JSON.parse(responseText);
                    if (typeof error == "function") {
                        error(data);
                    }
                } catch (error) {
                    /*返回值是vm*/
                    dom.querySelector(selector).innerHTML = "";
                    dom.querySelector(selector).insertAdjacentHTML("beforeEnd", responseText);
                    Util.runScript(responseText);
                    if (typeof callback == "function") {
                        callback(responseText);
                    }
                }

            }

            function errorCallBack() {
                //alert("请检查网络！");
            }

            options.success = successCallBack;
            options.error = errorCallBack;
            this.ajax(options);
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
                        url: session.getItem("contextUrl") + "/newpc/scan/listen",
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
                                identify == "isInstallment" ? dom.querySelector(".instalment .status").classList.remove("none") : dom.querySelector(".qrcode .status").classList.remove("none");
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
                    url: session.getItem("contextUrl") + "/newpc/result/querystate",
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
            this.ajax({
                url: session.getItem("contextUrl") + "/newpc/query/result",
                data: {
                    token: session.getItem("token")
                },
                success: function (responseText) {
                    Util.hideLoading();
                    var data = JSON.parse(responseText);
                    if (data.resultState == "SUCCESS" || data.resultState == "CANCEL") {
                        location.assign(data.successUrl);
                    } else if (data.resultState == "FAILED") {
                        User.Error("#globalErrorWrapper", data.errormsg + '(' + data.errorcode + ')');
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
            this.ajax({
                url: session.getItem("contextUrl") + "/newpc/supportbanklist",
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
            this.ajax({
                url: session.getItem("contextUrl") + "/merchantAuthority/request",
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
            this.ajax({
                url: session.getItem("contextUrl") + "/merchantAuthority/shareCardSendSms",
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
            this.ajax({
                url: session.getItem("contextUrl") + "/merchantAuthority/shareCardSmsConfirm",
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
                        isAuthorized = true;//已经成功拿到授权的银行卡，不需要重新授权
                    } else {
                        dom.querySelector(".authorized .submit").disabled = false;
                        dom.querySelector(".authorized .prompt").classList.add("none");
                        dom.querySelector(".authorized .errormsg").classList.remove("none");
                        dom.querySelector(".authorized .errormsg").innerHTML = data.errormsg + "(" + data.errorcode + ")";
                    }
                }
            });
        },
        unBindConfrim: function (bindId) {
            /*
             * 解绑确认
             * */
            Util.closePopup();
            Util.loading("正在解绑...");
            Action.ajax({
                url: session.getItem("contextUrl") + "/newpc/unbindCard",
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
                            dom.querySelector("#quickPayWrapper .select-wrapper").classList.add("none");
                            dom.querySelector("#quickPayForm").style.display = "block";
                            dom.querySelector("#quickPayForm .back-my-card").classList.add("none");
                        }
                    } else {
                        Util.popup(null, User.template.unBindError);
                    }
                }
            });
        },
        unbind: function (event) {
            /*
             * 解除绑卡
             * */
            event.stopPropagation();
            Util.popup(null, User.template.unBindConfrim);
            dom.querySelector(".unbind-confrim .confrim").addEventListener("click", Action.unBindConfrim.bind(null, this.getAttribute("data-bindid")));
            dom.querySelector(".unbind-confrim .cancel").addEventListener("click", Util.closePopup);
        },
        bindCardPayOrderInfo: function () {
            /*
             * 绑卡支付获取订单信息
             * */
            Action.ajax({
                url: session.getItem("contextUrl") + "/bindCard/prepareBindCard",
                data: {
                    token: session.getItem("token")
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.bizStatus == "success") {
                        orderInfo = data.bindCardMerchantRequest;
                        Action.getComponent({
                                "data": {"token": sessionStorage.getItem("token")},
                                "url": session.getItem("contextUrl") + "/newpc/bindcardlist/"
                            },
                            "#quickPayWrapper", null, function (data) {
                                if (dom.querySelector(".module-error") != null) return;
                                var html = '<input type="radio" name="pay-mode-handler" id="payModeQuick" class="pay-mode-handler"><div class="quick-pay"><label for="payModeQuick"><h2>绑卡支付 <span>无需开通网银</span></h2></label><div class="quick-pay-main"></div>';
                                dom.querySelector("#quickPayWrapper").insertAdjacentHTML("beforeEnd", html);
                                User.Error("#quickPayWrapper .quick-pay-main", data.errormsg + '(' + data.errorcode + ')', "module-error");
                            });
                    } else {
                        var html = '<input type="radio" name="pay-mode-handler" id="payModeQuick" class="pay-mode-handler"><div class="quick-pay"><label for="payModeQuick"><h2>绑卡支付 <span>无需开通网银</span></h2></label><div class="quick-pay-main"></div>';
                        dom.querySelector("#quickPayWrapper").insertAdjacentHTML("beforeEnd", html);
                        User.Error("#quickPayWrapper .quick-pay-main", data.errormsg + '(' + data.errorcode + ')', "module-error");
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
                    url: session.getItem("contextUrl") + "/bindCard/binInfo",
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
                            var addItem = User.bindCardPayAddItem(context);
                            self.classList.add("none");
                            dom.querySelector("#addItemWrapper").innerHTML = "";
                            dom.querySelector("#addItemWrapper").insertAdjacentHTML("beforeend", addItem);
                            dom.querySelector("#addItemWrapper .bind-card-sms").addEventListener("click", Action.getBindCardsmsCode, false);
                            dom.querySelector("#addItemWrapper #firstPayBtn").addEventListener("click", Action.bindCardPayConfrim, false);
                            dom.querySelector("#agreement").addEventListener("click", function () {
                                this.checked ? User.form.enabledSubmit("#firstPayBtn") : User.form.disabledSubmit("#firstPayBtn");
                            }, false);
                            User.form.validate(".bind-card-form");
                        } else {
                            if (triggerMode == "manual") {
                                User.Error(".bind-card-form", data.errormsg + '(' + data.errorcode + ')');
                            }
                        }
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
                    url: session.getItem("contextUrl") + "/bindCard/authBindCard",
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
        bindCardPayConfrim: function (event) {
            /*
             * 绑卡支付确认
             * */
            var self = this;
            var validate = User.form.autoValidate(".bind-card-form");
            if (validate) {
                User.form.disabledSubmit(self);
                Action.ajax({
                    url: session.getItem("contextUrl") + "/bindCard/authBindCardConfirm",
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
                                    User.form.hideErrorMessage(dom.querySelector("#smsForm"));//绑定得到焦点隐藏错误信息
                                    User.form.enabledSubmit(self);
                                    dom.querySelector("#smsForm .validate-code").onclick = Action.getBindSms.bind(dom.querySelector("#smsForm .validate-code"), event, sendData);
                                    dom.querySelector("#smsForm .confirm").onclick = function () {
                                        if (User.form.autoValidate("#smsForm")) {
                                            sendData.verifycode = dom.querySelector("#smsForm").verifycode.value;
                                            Action.bindPayConfirm.call(Action, true, sendData);
                                        }
                                    }
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
                    url: session.getItem("contextUrl")+"/bindCard/authBindCardSMS",
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
        marketinginfo: function () {
            /*获取营销立减活动信息*/
            Action.ajax({
                url: session.getItem("contextUrl") + "/market/info",
                data: {
                    token: session.getItem("token")
                },
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                        if (data.doMarketActivity == "Y") {
                            /*
                            * doMarketActivity是否做营销活动
                            * Y:做活动
                            * N:不做活动
                            * */
                            if (data.activityCopyWrites.hasOwnProperty("ALL")) {
                        /*所有支付方式都支持做营销活动*/
                        var guide = dom.querySelector(".all-random-guide");
                        guide.classList.remove("none");
                                guide.textContent = data.activityCopyWrites.ALL.copyWrite;
                            }
                            if (data.activityCopyWrites.hasOwnProperty("NCPAY")) {
                            /*一键支付支持营销活动*/
                            var guide = dom.querySelector(".quick-pay .product-random-guide");
                            guide.classList.remove("none");
                                guide.textContent = data.activityCopyWrites.NCPAY.copyWrite;
                        }
                            if (data.activityCopyWrites.hasOwnProperty("EANK")) {
                            /*网银支付支持营销活动*/
                                if (data.activityCopyWrites.EANK.hasOwnProperty("copyWrite")) {
                                    /*网银不区分银行， 全部支持营销活动*/
                                var guide = dom.querySelector(".bank-pay .product-random-guide");
                                guide.classList.remove("none");
                                    guide.textContent = data.activityCopyWrites.EANK.copyWrite;
                                }
                                if (data.activityCopyWrites.EANK.hasOwnProperty("activityCopyWrites")) {
                                    /*银行单独或多个支持活动*/
                                    if (data.activityCopyWrites.EANK.activityCopyWrites.hasOwnProperty("INDIVIDUAL")) {
                                        /*个人网银*/
                                        for (var key in data.activityCopyWrites.EANK.activityCopyWrites.INDIVIDUAL.activityCopyWrites) {
                                    dom.querySelector('.bank-pay li[bankaccounttype=B2C][bankcode=' + key + ']').classList.add("marketing");
                                }
                            }
                                    if (data.activityCopyWrites.EANK.activityCopyWrites.hasOwnProperty("BUSINESS")) {
                                        /*企业网银*/
                                        for (var key in data.activityCopyWrites.EANK.activityCopyWrites.BUSINESS.activityCopyWrites) {
                                            dom.querySelector('.bank-pay li[bankaccounttype=B2B][bankcode=' + key + ']').classList.add("marketing");
                                        }
                                    }
                                }
                        }
                    }
                    }
                }
            );
        },
        clfEasyIndex: function () {
            /*
            * 分期易支持的银行和期数等信息
            * */
            Action.ajax({
                data: {"token": session.getItem("token")},
                url: session.getItem("contextUrl") + "/clfEasy/index",
                method: "POST",
                success: function (response) {
                    var body = JSON.parse(response);
                    Template.clfEasy.context = body;

                        /*8个以上银行隐藏*/
                        function hideMore() {
                            var banks = dom.querySelectorAll("#installmentPayForm .bank-item label");
                            banks[7].insertAdjacentHTML("afterEnd", '<label class="more-handle">显示更多银行</label>')
                            if (banks.length > 8) {
                                for (var i = 7, l = banks.length; i < l; i++) {
                                    banks[i].classList.add("none");
                                }

                            }
                            dom.querySelector("#installmentPayForm .more-handle").addEventListener("click", showMore);
                        }

                        function showMore() {
                            var banks = dom.querySelectorAll("#installmentPayForm .bank-item label");
                            for (var i = 7, l = banks.length; i < l; i++) {
                                banks[i].classList.remove("none");
                            }
                            this.classList.add("none");
                        }

                        /*更换银行更新期数*/
                        function updataPeriod() {
                            var arr = JSON.parse(this.getAttribute("data-periodlist"));
                            var periodlist = "";
                            for (var i = 0, l = arr.length; i < l; i++) {
                                periodlist += '<input type="radio" data-firstPayment="' + arr[i].firstPayment + '" name="period" value="' + arr[i].period + '" id="period' + i + '"><label for="period' + i + '">' + arr[i].period + '期 | ' + arr[i].terminalPayment + '元/期</label>';
                            }
                            dom.querySelector("#installmentPayForm .period-wrapper").innerHTML = periodlist;
                            periodAddEvent();
                            /*默认选中第一个分期*/
                            dom.querySelector("#installmentPayForm .period-wrapper input").click();
                            /*更换银行清空卡信息*/
                            dom.querySelector("#installmentPayForm").cardno.value = "";
                            dom.querySelector("#installmentPayForm").cardno.focus();
                            dom.querySelector("#installmentAddItemWrapper").innerHTML = "";
                            dom.querySelector("#installmentPayForm .fn-btn").classList.remove("none");
                        }

                        /*更换期数更新首期还款额，清空卡信息，从新下单*/
                        function updatafirstPayment() {
                            dom.querySelector("#firstPayment").textContent = this.getAttribute("data-firstpayment");
                        }

                        /*更换银行 从新给期数绑定事件*/
                        function periodAddEvent() {
                            var inputs = dom.querySelectorAll("#installmentPayForm .period-wrapper input");
                            for (var i = 0, l = inputs.length; i < l; i++) {
                                inputs[i].addEventListener("change", updatafirstPayment);
                            }
                        }

                    if (body.bizStatus == "success") {
                        /*渲染分期易内容*/
                        dom.querySelector("#installmentPayWrapper").innerHTML = Template.clfEasy.content;
                        hideMore();
                        var inputs = dom.querySelectorAll("#installmentPayForm .bank-item input");
                        for (var i = 0, l = inputs.length; i < l; i++) {
                            inputs[i].addEventListener("change", updataPeriod);
                        }
                        /*默认选中第一个银行*/
                        dom.querySelector("#installmentPayForm .bank-item input").click();
                        /*绑定表单校验*/
                        User.form.validate("#installmentPayForm");
                        /*模块切换清除错误信息*/
                        dom.querySelector("label[for=installmentModeQuick]").addEventListener("mousedown", function () {
                            dom.querySelector("#installmentModeQuick").click();
                            User.form.reset("#installmentPayForm");
                        }, false);
                        /*下一步按钮绑定事件*/
                        dom.querySelector("#installmentPayForm .fn-btn").addEventListener("click", Action.getInstallmentAddItem.bind(dom.querySelector("#installmentPayForm .fn-btn"), "manual"), false);
                        /*输入卡号格式化 满足条件自动获取卡bin信息*/
                        dom.querySelector("#installmentPayForm").cardno.addEventListener("input", function () {
                            var value = this.value;
                            this.value = Util.format(value);
                            dom.querySelector("#installmentAddItemWrapper").innerHTML = "";
                            dom.querySelector("#installmentPayForm .fn-btn").classList.remove("none");
                            if (User.form.strategy.bankCardNo(this.value)) {
                                Action.getInstallmentAddItem.call(dom.querySelector("#installmentPayForm .fn-btn"));
                            }
                        }, false);
                    } else {
                        dom.querySelector("#installmentPayWrapper").innerHTML = Template.clfEasy.moduleError;
                        User.Error("#installmentPayWrapper .quick-pay-main", body.errorMsg + '(' + body.errorCode + ')', "module-error");
                        hideError(dom.querySelector("#installmentPayWrapper .pay-mode-handler"));
                    }

                }
            });
        },
        getInstallmentAddItem: function (triggerMode) {
            /*
             * 分期易卡Bin和补充项信息
             * triggerMode:触发模式，自动触发或点击下一步触发
             * */
            var self = this;
            var validate = User.form.autoValidate("#installmentPayForm");
            if (validate) {
                dom.querySelector("#installmentPayForm .loading").classList.remove("none");
                Action.ajax({
                    data: User.form.serialize("#installmentPayForm", {
                        token: session.getItem("token")
                    }),
                    url: session.getItem("contextUrl") + "/clfEasy/prerouter",
                    method: "POST",
                    success: function (response) {
                        var body = JSON.parse(response);
                        Template.cardInfo.context = body;
                        if (body.bizStatus == "success") {
                    self.classList.add("none");
                            dom.querySelector("#installmentPayForm .loading").classList.add("none");
                            dom.querySelector("#installmentAddItemWrapper").innerHTML = Template.cardInfo.content;
                    User.form.validate("#installmentPayForm");
                            dom.querySelector("#agreeClf").addEventListener("click", function () {
                                this.checked ? User.form.enabledSubmit("#clfPayBtn") : User.form.disabledSubmit("#clfPayBtn");
                            }, false);
                        } else {
                    dom.querySelector("#installmentPayForm .loading").classList.add("none");
                    if (triggerMode == "manual") {
                                User.Error("#installmentPayForm", body.errorMsg + '(' + body.errorCode + ')', null, "beforeEnd");
                            }
                        }

                    }
                });
            }
        },
        cflEasyPay: function (isPassCardNo) {
            /*
             * 分期支付下一步下单 发短验
             * isPassCardNo是否是透传卡号 true透传
             * */
            var sendData = User.form.serialize("#installmentPayForm", {token: session.getItem("token")});
            session.setItem("clfEasyCardInfo", JSON.stringify(sendData));//保存卡全部信息，方便获取验证码时再次使用
            if (isPassCardNo) {
                if (!User.form.autoValidate("#installmentPayForm", "cardno")) return;
                }
            if (!User.form.autoValidate("#installmentPayForm")) return;
            User.form.disabledSubmit("#clfPayBtn");
            this.ajax({
                url: session.getItem("contextUrl") + "/clfEasy/request",
                data: sendData,
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.bizStatus == "success") {
                        /*
                         * 下单成功，自动获取短验发送成功
                         * 开始自动倒计时
                         * */
                        dom.querySelector("#installmentPayForm").smsType.value = data.smsType;
                        Util.popup("", User.template.smsCode, function () {
                            dom.querySelector("#smsForm .confirm").onclick = Action.clfEasyPayConfirm.bind(Action);
                            dom.querySelector("#smsForm .validate-code").onclick = Action.getClfValidateCode;
                        });
                        User.form.hideErrorMessage(dom.querySelector("#smsForm"));
                        dom.querySelector("#smsForm .validate-code").click();
                    } else {
                        /*
                         * 下单失败
                         * */
                        User.Error("#installmentPayForm", data.errorMsg + '(' + data.errorCode + ')', null, "beforeEnd");
                    }
                    User.form.enabledSubmit("#clfPayBtn");
                }
            });
        },
        clfEasyPayConfirm: function () {
            /*
             * 分期支付确认支付
             * */
            var sendDate = JSON.parse(session.getItem("clfEasyCardInfo"));
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
                    url: session.getItem("contextUrl") + "/clfEasy/ajax/confirmPay",
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
                            Action.getQueryStatus(parseInt(session.getItem("jsTrys")), "CLF");
                        } else {
                            /*
                             * 验证码验证错误，关闭loading，显示验证码模板并显示错误信息
                             * */
                            Util.hideLoading();
                            Util.mask();
                            dom.querySelector("#Popup").classList.remove("none");
                            User.form.enabledSubmit("#smsForm .confirm");
                            User.form.showErrorMessage.call(dom.querySelector("#smsForm").verifycode, data.errorMsg + '(' + data.errorCode + ')');
                        }
                    }
                });
            }
        },
        getClfValidateCode: function (event) {
            /*
             * 分期易获取验证码
             * */
            var self = event.target;
            var sendData = User.form.serialize("#installmentPayForm", {
                token: session.getItem("token")
            });
            Action.ajax({
                url: session.getItem("contextUrl") + "/clfEasy/ajax/smsSend",
                data: sendData,
                success: function (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.bizStatus == "success") {
                        /*
                         * 验证码发送成功
                         * */
                        User.autoValidateCode.call(self);
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
                        User.form.showErrorMessage.call(self, data.errorMsg + '(' + data.errorCode + ')');
                        dom.querySelector(".send-tip").classList.add("none");
                    }
                }
            });
        }
    };
    global.Action = Action;
})
(window);

(function (global) {
    var Template = {
        clfEasy: {//分期易支付
            title: '分期易支付',
            set context(context) {
                this.compile = context;
            },
            get content() {
                var usableBankList = "";
                for (var key in this.compile.usableBankList) {
                    usableBankList += '<input type="radio" data-periodList=\'' + JSON.stringify(this.compile.periodList[key]) + '\' name="bankCode" value="' + key + '" id="' + key + '"><label for="' + key + '"><span' + ' class="bank-logo ' + key + '"></span>' + this.compile.usableBankList[key] + '</label>';
                }
                return '<input type="radio" name="pay-mode-handler" checked id="installmentModeQuick" class="pay-mode-handler">' +
                    '<div class="quick-pay">' +
                    '    <label for="installmentModeQuick">' +
                    '        <h2>分期易 <span>请选择银行</span>' +
                    '            <p class="product-random-guide none">随机立减</p></h2>' +
                    '    </label>' +
                    '    <div class="quick-pay-main">' +
                    '        <form id="installmentPayForm">' +
                    '            <div class="sington">' +
                    '                <span class="title">选择银行</span>' +
                    '                <div class="bank-item">' + usableBankList + '</div>' +
                    '            </div>' +
                    '            <div class="sington">' +
                    '                <span class="title">期数</span>' +
                    '                <div class="period-wrapper">' +
                    '                </div>' +
                    '                <div class="period-amount">' +
                    '                    <p>付款总额：¥<span>' + this.compile.totalAmount + '</span>元 = 订单总额：¥<span>' + this.compile.orderAmount + '</span>元 + 手续费：¥<span>' + this.compile.totalFee + '</span>元</p>' +
                    '                    <p>首期还款：¥<span id="firstPayment">--</span>元</p>' +
                    '                    <p>（实际付款金额以银行账单为准）</p>' +
                    '                </div>' +
                    '            </div>' +
                    '            <div class="sington"><label><span class="title">银行卡</span><input type="text" autocomplete="off"' +
                    '                                                                             placeholder="请输入个人银行卡号"' +
                    '                                                                             name="cardno"' +
                    '                                                                             maxlength="23" encrypt="true"' +
                    '                                                                             autofocus><img' +
                    '                    src="' + sessionStorage.getItem("contextUrl") + '/newpc/assets/images/loading.gif" width="24" class="loading none"> </label>' +
                    '            </div>' +
                    '            <div id="installmentAddItemWrapper"></div>' +
                    '            <button type="button" class="fn-btn">下一步</button>' +
                    '        </form>' +
                    '    </div>';
            },
            get moduleError() {
                return '<input type="radio" name="pay-mode-handler" id="installmentModeQuick" class="pay-mode-handler"><div class="quick-pay"><label for="installmentModeQuick"><h2>分期易 <span>请选择银行</span></h2></label><div class="quick-pay-main"></div>';
            }
        },
        cardInfo: {
            "title": "分期易卡信息",
            set context(context) {
                this.compile = context;
            },
            get content() {
                return '<div class="card-bin-wrapper">' +
                    '    <div class="card-bin">' +
                    '        <div class="bank"><span class="bank-logo ' + this.compile.cardBin.bankCode + '"></span>' + this.compile.cardBin.bankName + '</div>' +
                    '        <div class="bank-info">' +
                    '            **********' + this.compile.cardBin.cardLater4 +
                    '            <span class="card-type">' +
                    (this.compile.cardBin.cardType == "CREDIT" ? "信用卡" : "储蓄卡")
                    + '</span>' +
                    '        </div>' +
                    '    </div>' +
                    '</div>' +
                    (
                        this.compile.needItem.needOwner ?
                            '    <div class="sington"><label><span class="title">姓名</span><input type="text" autocomplete="off"' +
                            '                                                                    placeholder="请输入您的真实姓名"' +
                        '                                                                    name="owner"' +
                            '                                                                    maxlength="16" encrypt="true"></label>' +
                            '    </div>' : ""

                    )
                    +
                    (
                        this.compile.needItem.needIdNo ?
                            '    <div class="sington"><label><span class="title">身份证号</span><input type="text" autocomplete="off"' +
                            '                                                                      placeholder="请输入您的身份证号"' +
                            '                                                                      name="idno"' +
                            '                                                                      maxlength="18" encrypt="true"></label>' +
                            '    </div>' : "") +
                    (
                        this.compile.needItem.needAvlidDate ?
                            '    <div class="sington"><label><span class="title">有效期</span><input type="text" autocomplete="off"' +
                            '                                                                     placeholder="如：09/15 请输入0915"' +
                        '                                                                     name="avlidDate"' +
                            '                                                                     maxlength="4" encrypt="true">' +
                            '        <div class="sample valid"><img src="' + sessionStorage.getItem("contextUrl") + '/newpc/assets/images/5nor_03.png"></div>' +
                            '    </label>' +
                            '    </div>' : "") +
                    (
                        this.compile.needItem.needCvv ?
                            '    <div class="sington"><label><span class="title">C V V</span><input type="password" autocomplete="off"' +
                            '                                                                       placeholder="信用卡背面后三位数字"' +
                        '                                                                       name="cvv"' +
                            '                                                                       maxlength="3" encrypt="true">' +
                            '        <div class="sample cvv"><img src="' + sessionStorage.getItem("contextUrl") + '/newpc/assets/images/5nor_06.png"></div>' +
                            '    </label>' +
                            '    </div>' : "") +
                    (
                        this.compile.needItem.needBankPWD ?
                            ' <div class="sington"><label><span class="title">取款密码</span><input type="password" autocomplete="off"' +
                            '                                                                      placeholder="请输入取款密码"' +
                            '                                                                      name="pass"' +
                            '                                                                      maxlength="6" encrypt="true"></label>' +
                            '    </div>' : "") +
                    (
                        this.compile.needItem.needPhoneNo ?
                            '    <div class="sington"><label><span class="title">手机号</span><input type="text" autocomplete="off"' +
                            '                                                                     placeholder="请输入银行预留手机号"' +
                        '                                                                     name="phoneNo"' +
                            '                                                                     maxlength="11" encrypt="true" ></label>' +
                            '    </div>' : "") +
                    '<input type="hidden" id="bankCode" name="bankCode" readonly value="' + this.compile.cardBin.bankCode + '">' +
                    '<input type="hidden" name="bankName" readonly value="' + this.compile.cardBin.bankName + '">' +
                    '<input type="hidden" name="cardType" readonly value="' + this.compile.cardBin.cardType + '">' +
                    '<input type="hidden" name="smsType" readonly value="' + this.compile.smsType + '">' +
                    '<div><label><input type="checkbox" name="agreement" id="agreeClf" checked><span' +
                    '        class="checkbox icon"></span>同意</label><a' +
                    '        href="javascript:void(0)" onclick="Util.popup(\'\',User.template.clfAgreement)">《服务协议》</a>' +
                    '</div>' +
                    '<button type="button" id="clfPayBtn" onclick="Action.cflEasyPay()">下一步</button>';
            }
        }
    }
    global.Template = Template;
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
                    if (reg.test(elem.type) || ((elem.type == "radio" || elem.type == "checkbox") && elem.checked)) {
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
                    dom.querySelector(selector).classList.remove("slvzr-disabled");
                }
                else {
                    selector.disabled = false;
                    selector.classList.remove("slvzr-disabled");
                }

            }

            function disabledSubmit(selector) {
                /*禁用提交按钮
                 * */
                if (Util.type(selector) == "string") {
                    dom.querySelector(selector).disabled = true;
                    dom.querySelector(selector).classList.add("slvzr-disabled");
                }
                else {
                    selector.disabled = true;
                    selector.classList.add("slvzr-disabled");
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
                if (this.name == "verifycode" || this.name == "smsCode") {
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
                if (this.name == "bankPWD") {
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
                function selectController() {
                    /*
                     * 下拉菜单显示、隐藏控制
                     * */
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
                })
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
                this.updata();
                this.factory.tabs.forEach(function (tabs, ind) {
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
            var max = 12;//默认显示银行个数
            var text = "显示更多银行";
            var cache = [];//存储隐藏的银行
            function update() {
                uls = array(dom.querySelectorAll(".bank-container .bank-list"));
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
                    }, false);
                });
                return this;
            }

            function more() {
                /*
                 * 银行12个以上时显示更多银行按钮
                 * */
                update();
                var handler = '<li class="more-handler">' + text + '</li>';//显示更多银行按钮
                function hide() {
                    /*
                     * 隐藏第12个以后的银行，把隐藏的银行存入数组
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
                     * 显示第12个以后的银行
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
                    '                <div class="yp-qrcode"><img width="95" alt="易宝公众号"></div>' +
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
                    '                <div class="yp-qrcode"><img src="../assets/images/yeepay.png" alt="易宝公众号"></div>' +
                    '                <div class="yp-desc"><p>可扫码关注易宝支付公共账号输入订单号快速查单：）</p>' +
                    '                    <p class="number">商户订单号: <span class="merchant-order-id"></span></p></div>' +
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
                clfAgreement = '<div class="agreement">' +
                    '<p class="title">分期易用户服务协议</p>' +
                    '<div class="elaborate"><p>一、概述</p>' +
                    '<p>1、易宝支付《分期易用户服务协议》（以下简称“本协议”）是您自愿与易宝支付有限公司（以下简称“易宝支付”）就易宝分期易支付服务（以下简称“本服务”）的使用所签订的有效合约。您通过网络页面点击确认本协议或以其他方式选择接受本协议，即表示您与易宝支付已达成协议并同意接受本协议的全部约定内容。</p>' +
                    '<p>2、在您接受本协议之前，请您仔细阅读本协议的全部内容，如您不同意接受本协议的任意内容，或者无法准确理解相关条款含义的，请不要进行后续操作。如果您对本协议的条款有疑问的，请通过易宝支付客服渠道进行询问（易宝支付客服电话为4001-500-800），易宝支付将向您解释条款内容。</p>' +
                    '<p>3、您同意，易宝支付有权随时对本协议内容进行单方面的变更，无需另行单独通知您；若您在本协议内容公告变更生效后继续使用本服务的，表示您已充分阅读、理解并接受变更修改后的协议内容，也将遵循变更修改后的协议内容使用本服务；若您不同意变更修改后的协议内容，您应在变更生效前停止使用本服务。</p>' +
                    '<p>二、双方权利义务</p>' +
                    '<p>1、您应确保您在使用本服务时的银行卡为您本人的银行卡，确保您使用银行卡的行为合法、有效，未侵犯任何第三方合法权益；否则因此造成易宝支付、商户、持卡人损失的，您应负责赔偿并承担全部法律责任。</p>' +
                    '<p>2、您应妥善保管银行卡、卡号、密码、发卡行预留的手机号码等与银行卡有关的一切信息。如您遗失或泄漏前述信息，您应及时通知发卡行及/或易宝支付，以减少可能发生的损失。无论是否已通知发卡行及/或易宝支付，因您的原因所致损失需由您自行承担。</p>' +
                    '<p>3、您在使用本服务时，应当认真确认交易信息，包括且不限于商品名称、数量、金额等，并按易宝支付业务流程发出约定的指令。您认可和同意：您发出的指令不可撤回或撤销，易宝支付一旦根据您的指令委托银行或第三方从银行卡中划扣资金给收款人，您不应以非本人意愿交易或其他任何原因要求易宝支付退款或承担其他责任。</p>' +
                    '<p>4、您不应将本服务用于任何违反国家相关法律法规或本协议的目的。</p>' +
                    '<p>5、您在对使用本服务过程中发出指令的真实性及有效性承担全部责任；您承诺，易宝支付依照您的指令进行操作的一切风险由您承担。\n</p>' +
                    '<p>6、您认可并以易宝支付系统平台记录的数据为准。</p>' +
                    '<p>7、同时您授权易宝支付有权留存您在易宝支付网站填写的相应信息，以供后续向您持续性地提供相应服务（包括但不限于将本信息用于向您推广、提供其他更加优质的产品或服务）。</p>' +
                    '<p>8、出现下列情况之一的，易宝支付有权立即终止您使用易宝支付相关服务而无需承担任何责任：</p>' +
                    '<p>（1）违反本协议的约定；</p>' +
                    '<p>（2）将本服务用于非法目的；</p>' +
                    '<p>（3）违反易宝支付/或其他关联公司网站的条款、协议、规则、通告等相关规定，而被上述任一网站终止提供服务的；</p>' +
                    '<p>（4）易宝支付认为向您提供本服务存在风险的；</p>' +
                    '<p>（5）您的银行卡无效、有效期届满或注销（如有）。</p>' +
                    '<p>9、若您未违反本协议约定且因不能归责于您的原因，造成银行卡内资金通过本服务出现损失，且您未从中获益的，您可向易宝支付申请补偿。您应在知悉资金发生损失后及时通知易宝支付并按要求提交相关的申请材料和证明文件，您同意，您能否得到补偿及具体金额取决于易宝支付自身独立的判断，易宝支付承诺不会因此损害您的合法权益。</p>' +
                    '<p>10、您同意并认可易宝支付最终的补偿行为并不代表前述资金损失应归责于易宝支付，亦不代表易宝支付须为此承担其他任何责任。您同意，易宝支付在向您支付补偿的同时，即刻取得您可能或确实存在的就前述资金损失而产生的对第三方的所有债权及其他权利，包括但不限于就上述债权向第三方追偿的权利，且您不再就上述已经让渡给易宝支付的债权向该第三方主张任何权利，亦不再就资金损失向易宝支付主张任何权利。</p>' +
                    '<p>11、此外，在接受补偿的同时或之后，您从其它渠道挽回了前述资金损失的，或有新证据证明您涉嫌欺诈的，或者发生您应当自行承担责任的其他情形，您应在第一时间返还易宝支付向您支付的补偿款项，否则易宝支付有权向您进行追偿。同时您承诺：资金损失事实真实存在，赔偿申请材料真实、有效。您已充分知晓并认识到，基于虚假信息申请保险赔偿将涉及刑事犯罪，易宝支付有权向国家有关机构申请刑事立案侦查。\n</p>' +
                    '<p>三、承诺与保证</p>' +
                    '<p>1、您保证使用本服务过程中提供的如姓名、身份证号、银行卡卡号及银行预留手机号等信息真实、准确、完整、有效。易宝支付按照您设置的相关信息为您提供相应服务，对于因您提供信息不真实或不完整所造成的损失由您自行承担。</p>' +
                    '<p>2、您授权易宝支付在您使用本服务期间或本服务终止后，有权保留您在使用本服务期间所形成的相关信息数据，同时该授权不可撤销。</p>' +
                    '<p>四、其他条款</p>' +
                    '<p>1、您同意，本协议适用中华人民共和国大陆地区法律。因易宝支付与您就本协议的签订、履行或解释发生争议，双方应努力友好协商解决。如协商不成，易宝支付和用户同意由易宝支付住所地法院管辖审理双方的纠纷或争议。</p>' +
                    '<p>2、本协议内容包括协议正文及所有易宝支付已经发布的或将来可能发布的易宝支付服务的使用规则。所有规则为本协议不可分割的一部分，与协议正文具有相同法律效力。若您在本协议内容发生修订后，继续使用本服务的，则视为您同意最新修订的协议内容；否则您须立即停止使用本服务。</p>' +
                    '<p>3、您同意，本协议之效力、解释、变更、执行与争议解决均适用中华人民共和国法律，没有相关法律规定的，参照通用国际商业惯例和（或）行业惯例。</p>' +
                    '<p>4、本协议部分内容被有管辖权的法院认定为违法或无效的，不因此影响其他内容的效力。</p>' +
                    '<p>5、本协议未尽事宜，您需遵守易宝支付网站上公布的相关服务协议及相关规则。</p></div>' +
                    '<div class="agree-btn"><button onclick=' + 'Util.closePopup(function(){dom.querySelector("#agreeClf").checked=true;User.form.enabledSubmit("#clfPayBtn")});' + '>已阅读并同意该服务协议</button></div>' +
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
                    '        <li>找到<em>“服务中心”&gt;我的操作员账号</em>或<em>“系统管理”</em>菜单</li>' +
                    '        <li>选择修改交易密码</li>' +
                    '    </ol>' +
                    '    <a class="button set-password" href="http://www.yeepay.com/selfservice/login.action" target="_blank">修改交易密码</a>' +
                    '    <span class="auto-set"><em id="secondRemain">5</em>s后自动跳转</span>' +
                    '</div>',
                unBindConfrim = '<div class="unbind-confrim"><p>是否要解除此银行卡的绑定关系？</p><button class="confrim">确定</button><button class="cancel">取消</button></div>',
                unBindError = '<div class="unbind-error"><h2>解除绑卡失败</h2><p>[10005]系统异常</p><button class="confrim" onclick="Util.closePopup()">确定</button></div>',
                bindCardPayPas = '<div class="sms-code bind-card-pas">' +
                    '        <h2>支付确认</h2>' +
                    '        <form id="smsForm" style="margin-left: 80px">' +
                    '            <div class="sington icon"><label><span class="title">取款密码</span><input type="password"' +
                    '                                                                                  placeholder="请输入取款密码"' +
                    '                                                                                  maxlength="6" encrypt="true" name="bankPWD"></label>' +
                    '            </div>' +
                    '            <button class="confirm" type="button">确认</button>' +
                    '        </form>' +
                    '</div>',
                psdCode = '<div class="sms-code bind-card-pas">' +
                    '        <h2>支付确认</h2>' +
                    '        <form id="smsForm" style="margin-left: 34px">' +
                    '            <div class="sington icon"><label><span class="title">取款密码</span><input type="password"' +
                    '                                                                                  placeholder="请输入取款密码"' +
                    '                                                                                  maxlength="6" encrypt="true" name="bankPWD" style="width:332px"></label>' +
                    '            </div>' +
                    '            <div class="sington icon"><label><span class="title">验证码</span><input type="text"' +
                    '                                                                                  placeholder="请输入验证码"' +
                    '                                                                                  maxlength="6" name="verifycode"></label>' +
                    '                <button type="button" class="validate-code" disabled>获取验证码</button>' +
                    '            </div>' +
                    '            <button class="confirm" type="button" disabled>确认</button>' +
                    '        </form>' +
                    '</div>';
            return {
                smsCode: smsCode,
                waitingPay: waitingPay,
                clientId: clientId,
                queryResult: queryResult,
                agreement: agreement,
                clfAgreement: clfAgreement,
                bindCardAgreement: bindCardAgreement,
                chinaBankAgreement: chinaBankAgreement,
                idChoice: idChoice,
                authorized: authorized,
                forgetTradingPassword: forgetTradingPassword,
                unBindConfrim: unBindConfrim,
                unBindError: unBindError,
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
        var bindCardPayAddItem = function (data) {
            var item = '<div class="card-bin bind-card-bin">' +
                '        <div class="bank"><span class="bank-logo ' + data.bankCode + '"></span>' + data.bankName + '</div>' +
                '        <div class="bank-info">' +
                '            **********' + data.cardlater +
                '            <span class="card-type">' +
                (data.cardType == 2 ? "信用卡" : "储蓄卡") +
                '                             </span>' +
                '        </div>' +
                '    </div>';
            item += data.merchantSamePersonConf ? '<p class="prompt">请使用<span class="emp">本人信息</span>进行支付，支付成功后不可更改</p>' : '';
            if (data.needname) {
                item += '<div class="sington"><label><span class="title">姓名</span><input type="text" encrypt="true" autocomplete="off"' +
                    '                                                                placeholder="请输入您的真实姓名"' +
                    '                                                                     name="owner"' + (data.owner ? "readonly value=" + data.owner : "") + '></label>' +
                    '</div>';
            }
            if (data.needidno) {
                item += '<div class="sington"><label><span class="title">身份证号</span><input type="text" encrypt="true" maxlength="18" autocomplete="off"' +
                    '                                                                placeholder="请输入您的身份证号"' +
                    '                                                                     name="idno"' + (data.idno ? "readonly value=" + data.idno : "") + '></label>' +
                    '</div>';
            }
            if (data.needvalid) {
                item += '<div class="sington"><label><span class="title">有效期</span><input type="text" encrypt="true" autocomplete="off"' +
                    '                                                                      placeholder="如：09/15 请输入0915"' +
                    '                                                                      name="avlidDate"' +
                    '                                                                      maxlength="4"><div class="sample valid"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/5nor_03.png"></div></label>' +
                    '</div>';
            }
            if (data.needcvv) {
                item += '<div class="sington"><label><span class="title">C V V</span><input type="password" encrypt="true" autocomplete="off"' +
                    '                                                                        placeholder="信用卡背面后三位数字"' +
                    '                                                                        name="cvv"' +
                    '                                                                        maxlength="3"><div class="sample cvv"><img src="' + session.getItem("contextUrl") + '/customizedPc/assets/images/5nor_06.png"></div></label>' +
                    '</div>';
            }
            if (data.needpass) {
                item += '<div class="sington"><label><span class="title">取款密码</span><input type="password" encrypt="true" autocomplete="off"' +
                    '                                                                       placeholder="请输入取款密码"' +
                    '                                                                       name="bankPWD"' +
                    '                                                                       maxlength="6"></label>' +
                    '</div>';
            }
            if (data.needmobile) {
                item += '<div class="sington"><label><span class="title">手机号</span><input type="text" encrypt="true" maxlength="11" autocomplete="off"' +
                    '                                                                placeholder="请输入银行预留手机号"' +
                    '                                                                     name="phoneNo"' + (data.phoneNo ? "readonly value=" + data.phoneNo : "") + '></label>' +
                    '</div>';
            }
            item += '<div class="sington"><label><span class="title">验证码</span><input type="text" placeholder="输入您的验证码"' +
                '                                                                             class="verify-code" maxlength="6" name="smsCode"></label>' +
                '<button class="get-smsCode bind-card-sms" type="button">获取验证码</button>' +
                '            </div>';
            item += '<input type="hidden" name="bankCode" readonly value="' + data.bankCode + '">' +
                '<input type="hidden" name="bankName" readonly value="' + data.bankName + '">' +
                '<div class="sington"><label><input type="checkbox" name="agreement" id="agreement" checked><span' +
                '        class="checkbox icon"></span>同意</label><a' +
                '        href="javascript:void(0)" onclick="Util.popup(\'\',User.template.bindCardAgreement)">《服务协议》</a>' + (data.bankCode == "BOC" ? '<span class="china-bank-agree">和<a href="javascript:void(0)" onclick="Util.popup(\'\',User.template.chinaBankAgreement)">《中国银行快捷支付服务协议》</a></span>' : '') +
                '            <div id="bindCardPayError"></div>' +
                '</div><button type="button" id="firstPayBtn">确定</button>';
            return item;
        };

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
            Authorized: Authorized,
            bindCardPayAddItem: bindCardPayAddItem
        }
    }();
    global.User = User;

}(window));