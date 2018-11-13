/**
 */
(function (global) {
    var User = function () {
        var dom = global.document;
        var parent = global.parentNode;
        function convertJson(){           //zym
            //将所有参数转化成json对象  
            //包括数组 字符串 json对象
            var json = {};
            array(arguments).map(function(obj){
                if (obj instanceof Object) {
                    for(var item in obj){
                        json[item] = obj[item];
                    }  
                }else if(typeof obj === 'string'){
                    json[obj] = obj; 
                }else if(obj instanceof Array){
                    obj.reduce(function(startItem,currentItem){   //数组转换成对象  [1,2] {"1":1,"2":2}
                       startItem[currentItem] = currentItem;
                    },{});
                    convertJson(obj);  //回调
                }
            });
           return json;
        }
       
        var form = function () {
            var reg = /text|tel|password|number|search|url|email|file|hidden/;
            var strategy = {
                // phone : /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/, //电话号
                phone : /^1\d{10}$/, //电话号
                email : /^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/, //邮箱
                idno : /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/,   //身份证
                chinese : /^[\u4E00-\u9FA5·.]+$/, //仅中文
                onlyNumber : /\d+$/,  //仅数字
                cardno : /^(\d{14,19})$/,  //14,15,16,17,18,19
                threeNo : /\d{3}$/, //三位数
                fourNo : /\d{4}$/,//4位数
                date: /^\d{4}-\d{2}-\d{2}$/   //yyyy-mm-dd
            }
            
            function serialize(selector, supplement) {
                /*
                 * 表单序列化
                 * supplement：补充数据
                 * */
                var obj = {};
                var elements = array(dom.querySelector(selector).elements);
                elements.forEach(function (elem) {
                    if (reg.test(elem.type)) {
                        obj[elem.name] = elem.getAttribute("encrypt") == "true" ? BASE64.encoder(elem.value.replace(/[\s\n\r]/g, "")) : elem.value.replace(/[\s\n\r]/g, "");
                    }
                });
                if (Util.type(supplement) == "object") {
                    for (key in supplement) {
                        obj[key] = supplement[key];
                    }
                }
                return obj;
            }
            function hideErrorMessage(elem) {
                /*
                 *input得到焦点 隐藏错误信息
                 * */
                elem = Util.type(elem) == "string" ? dom.querySelector(elem) : elem;
                var inputs = elem.querySelectorAll(".li input");
                Array.from(inputs).forEach(function(item,index){
                    item.addEventListener("focus",function(){
                        this.parentNode.classList.remove("error");
                        // this.parent(".li").classList.remove("error");
                        this.parentNode.classList.add("delInfo");
                    },false);
                });
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
                    //var timer ;
                    //clearTimeout(timer);
                    //timer = setTimeout(function(){
                    //    User.form.enabledSubmit(selector);
                    //},5000)
                }
                else {
                    selector.disabled = true;
                }
            }
            function reset(selector) {
                var form = dom.querySelector(selector);
                //form.reset();
                array(form.querySelectorAll(".li")).forEach(function (current) {
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
            function errorMsg(item,msg){  //错误提示  
                var errorInfo = {
                    "cardno":'银行卡',
                    "cvv":'CVV',
                    "owner":'姓名',
                    "idno":'身份证',
                    "phoneNo":'手机号',
                    "smsCode":'验证码',
                    "verifycode":'验证码',
                    "bankPWD":'密码',
                    "avlidDate":'有效期'
                };
                var itemName = item.getAttribute("name");
                var error = '<div class="errMsg icon pa">'+errorInfo[itemName]+msg+'</div>';
                var errMsgItem = item.parentNode.querySelector(".errMsg");
                if(!errMsgItem){
                    item.parentNode.insertAdjacentHTML("beforeEnd",error);
                }else{
                    errMsgItem.innerHTML = '';
                    errMsgItem.innerHTML = errorInfo[itemName]+msg;
                }
            }
            function setTimeHidden(item,duration){  //定时隐藏
                var timer = null;
                clearTimeout(timer);
                setTimeout(function(){
                    item.classList.remove("error");
                },duration);
            }
            function doValidate(item){
                var required = item.classList.contains("required");
                var _name = item.getAttribute("_name");
                var value = item.value;
                var target = item.parentNode;
                
                if(required){  //必填项
                    if(!item.value.trim()){
                        target.classList.add("error");
                        errorMsg(item,'不能为空');  //创建错误提示
                        setTimeHidden(target,900)  //错误提示1s消失
                        return false;
                    }else{
                        target.classList.remove("error");
                    }
                    if(_name){  //特殊校验处理
                        if(!strategy[_name].test(value.trim())){
                            target.classList.add("error");
                            errorMsg(item,'格式不正确');  //创建错误提示
                            setTimeHidden(target,900)  //错误提示1s消失
                            return false;
                        }else{
                            target.classList.remove("error");
                        }
                    }
                }else{  //非必填项
                    target.classList.remove("error");
                }

                return true;
            }
            function validate(selector) {
                var form = dom.querySelector(selector);
                hideErrorMessage(form);  //得到焦点隐藏错误信息
                var inputs = form.querySelectorAll("input");
                Array.from(inputs).forEach(function(item,index){
                   item.addEventListener("blur",function(){
                    // this.parent(".li").classList.remove("delInfo");
                    this.parentNode.classList.remove("delInfo");
                       var result = doValidate(item);
                       if (!result) {
                           return false;
                       }
                   },false);
                });

            }
            function autoValidate(selector) {
                /*
                 * 自动校验
                 * ignore 忽略输入框name值
                 * */
                //var inputs = dom.querySelector(selector).querySelectorAll("input");
                var requireds = dom.querySelector(selector).querySelectorAll(".required");
                for (var i = 0, l = requireds.length; i < l; i++) {
                    var result = doValidate(requireds[i]);
                    if (!result) {
                        return false;
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
                reg: reg,
                strategy: strategy,
                errorMsg:errorMsg,
                autoValidate: autoValidate,
                validate: validate,
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
                // window.session.setItem("timeId", timeId);
            }

            start();
        }

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

        function autoValidateCode(target) {
            /*
             * 自动获取验证码，开始倒计时
             * */
            var self = target;
            // self.parentNode.focus();
            User.form.disabledSubmit(self);
            User.timing(function (second) {
                /*
                 * 开始60秒倒计时
                 * */
                self.classList.remove("getCode");
                self.classList.add("resendMessage");
                self.innerHTML = second + "s后重新获取";
            }, function () {
                /*
                 * 倒计时结束回调
                 * */
                User.form.enabledSubmit(self);
                self.innerHTML = "重新获取";
                self.classList.remove("resendMessage");
                self.classList.add("getCode");
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
        /********导出对外接口**********/
        return {
            form: form,
            Tab: Tab,
            convertJson: convertJson,
            // delInfo:delInfo,
            timing: timing,
            // bankCard: bankCard,
            Error: Error,
            ErrorHandler: ErrorHandler,
            payModeBank: payModeBank,
            cardPayments: cardPayments,
            autoValidateCode: autoValidateCode,
            Identity: Identity,
        }
    }();
    global.User = User;

}(window));