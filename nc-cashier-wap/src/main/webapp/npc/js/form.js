var Form = {
    init: function (str) { //str提示文字样式缺省为placeholder
        this.file().placeholder().radio().selects();
        return this;	
    },
    placeholder: function (str) { //输入框文字提示
        if (!("placeholder" in document.createElement("input"))) { //如果浏览器不支持placeholder属性
            var inputs = document.getElementsByTagName("input"),
                textareas = document.getElementsByTagName("textarea");
            var str = str || "placeholder";
            var labels = [];
            var ind = 0;
            function createLabel(elem) { //创建label标签
                var clientLeft = elem.getBoundingClientRect().left,
                    clientTop = elem.getBoundingClientRect().top,
                    inputHeight = elem.offsetHeight;
                var label = document.createElement("label");
                label.className = str;
                label.innerHTML = elem.getAttribute("placeholder");
                labels.push(label);
                document.body.appendChild(label);
                var mean = (inputHeight - label.offsetHeight) / 2;
                var paddingLeft = parseInt(elem.currentStyle.paddingLeft),
                    borderLeftWidth = parseInt(elem.currentStyle.borderLeftWidth),
                    paddingTop = parseInt(elem.currentStyle.paddingTop),
                    borderTopWidth = parseInt(elem.currentStyle.borderTopWidth);
                label.style.cssText = "position:absolute;" + "left:" + (clientLeft + paddingLeft + borderLeftWidth) + "px;";
                if (elem.nodeName.toLowerCase() == "input") {
                    label.style.top = (clientTop + mean) + "px";
                } else {
                    label.style.top = (clientTop + paddingTop + borderTopWidth) + "px";
                }
            }
            for (var i = 0, l = inputs.length; i < l; i++) {
                if (inputs[i].type == "text") { //单行文本框
                    inputs[i].value = inputs[i].getAttribute("placeholder");
                    YEEPAY.addClass(inputs[i], str);
                    YEEPAY.addHandler(inputs[i], "focus", function (event) {
                        var target = YEEPAY.getTarget(event);
                        if (target.value == target.getAttribute("placeholder")) {
                            target.value = "";
                            YEEPAY.removeClass(target, str);
                        }
                    });
                    YEEPAY.addHandler(inputs[i], "blur", function (event) {
                        var target = YEEPAY.getTarget(event);
                        if (target.value == "") {
                            target.value = target.getAttribute("placeholder");
                            YEEPAY.addClass(target, str);
                        }
                    });
                } else if (inputs[i].type == "password") { //密码输入框
                    inputs[i]._index = ind;
                    ind++;
                    createLabel(inputs[i]);
                    YEEPAY.addHandler(inputs[i], "focus", function (event) {
                        var target = YEEPAY.getTarget(event);
                        if (target.value == "") {
                            labels[target._index].style.display = "none";
                        }
                    });
                    YEEPAY.addHandler(inputs[i], "blur", function (event) {
                        var target = YEEPAY.getTarget(event);
                        if (target.value == "") {
                            labels[target._index].style.display = "block";
                        }
                    });
                }
            }
            for (var j = 0, l = textareas.length; j < l; j++) { //多行文本框
                textareas[j]._index = ind;
                ind++;
                createLabel(textareas[j]);
                YEEPAY.addHandler(textareas[j], "focus", function (event) {
                    var target = YEEPAY.getTarget(event);
                    if (target.value == "") {
                        labels[target._index].style.display = "none";
                    }
                });
                YEEPAY.addHandler(textareas[j], "blur", function (event) {
                    var target = YEEPAY.getTarget(event);
                    if (target.value == "") {
                        labels[target._index].style.display = "block";
                    }
                });
            }

        }
        return this;
    },
    radio: function () { //单选按钮
        var radios = [];
        var labels = [];
        var inputs = document.getElementsByTagName("input");
        var ind = 0;
        for (var i = 0; i < inputs.length; i++) {
            if (inputs[i].type == "radio") {
								YEEPAY.addClass(inputs[i].parentNode,"hidden");
                radios.push(inputs[i]);
                var label = document.createElement("label");
                label.innerHTML = inputs[i].value;
                label.setAttribute("name", inputs[i].name);
                label._index = ind;
								label.className="radioLabel";
                ind++;
                label.style.cssText = "position:absolute; display:inline-block; background:url(../images/icon2.png) no-repeat -2px -367px; height:14px; padding-left:18px; overflow:hidden; visibility: visible;";
                label.style.left = inputs[i].offsetLeft + "px";
                label.style.top = inputs[i].offsetTop + "px";
                labels.push(label);
                inputs[i].parentNode.appendChild(label);
                label.onclick = function () {
                    for (var j = 0; j < labels.length; j++) {
                        if (labels[j].getAttribute("name") == this.getAttribute("name")) {
                            labels[j].style.backgroundPosition = "-2px -367px";
														labels[j].setAttribute("checked", "false");
                        }
                    }
                    this.style.backgroundPosition = "-2px -529px";
										this.setAttribute("checked", "true");
                    radios[this._index].click();
                }
								label.onmouseover=function(){
										if (this.getAttribute("checked")=="true") {
												this.style.backgroundPosition = "-2px -529px";
											} else {
													this.style.backgroundPosition = "-2px -475px";
												}
								}
								label.onmouseout=function(){
										if (this.getAttribute("checked")=="true") {
												this.style.backgroundPosition = "-2px -421px";
											} else {
													this.style.backgroundPosition = "-2px -367px";
												}
								}
								if(inputs[i].checked){//默认选中
										label.onclick();
										label.setAttribute("checked", "true");
								}
            }
        }
				return this;
    },
		checkbox:function(){//复选框
				var checkboxs = [];
				var labels = [];
				var inputs = document.getElementsByTagName("input");
				var ind = 0;
				for (var i = 0; i < inputs.length; i++) {
						if (inputs[i].type == "checkbox") {
								YEEPAY.addClass(inputs[i].parentNode,"hidden");
								checkboxs.push(inputs[i]);
								var label = document.createElement("label");
								label.id="checkboxBtn";
								label.innerHTML = inputs[i].value;
								label.setAttribute("checked", "false");
								label._index = ind;
								ind++;
								label.style.cssText = "position:absolute; display:inline-block; background:url(../images/icon2.png) no-repeat -2px -583px; height:14px; padding-left:18px; overflow:hidden; visibility: visible;";
								label.style.left = inputs[i].offsetLeft + "px";
								label.style.top = inputs[i].offsetTop + "px";
								labels.push(label);
								inputs[i].parentNode.appendChild(label);
								label.onclick = function () {
										if (this.getAttribute("checked")=="true") {
												this.setAttribute("checked", "false");
												this.style.backgroundPosition = "-2px -583px";
										} else {
												this.setAttribute("checked", "true");
												this.style.backgroundPosition = "-2px -745px";	
										}
										checkboxs[this._index].click();
								}
								label.onmouseover=function(){
										if (this.getAttribute("checked")=="true") {
												this.style.backgroundPosition = "-2px -745px";
											} else {
													this.style.backgroundPosition = "-2px -691px";
												}
								}
								label.onmouseout=function(){
										if (this.getAttribute("checked")=="true") {
												this.style.backgroundPosition = "-2px -637px";
											} else {
													this.style.backgroundPosition = "-2px -583px";
												}
								}
								if(inputs[i].checked){//默认选中
										label.onclick();
								}
						}
				}
				return this;
		},
		file:function(txt){//上传文件
				var files = [];
				var newinputs = [];
				var txt = txt || "点击上传";
				var inputs = document.getElementsByTagName("input");
				for (var i = 0; i < inputs.length; i++) {
						if (inputs[i].type == "file") {
								YEEPAY.addClass(inputs[i],"hidden");
								//YEEPAY.addClass(inputs[i].parentNode,"pr");
								files.push(inputs[i]);
								var input = document.createElement("input");
								input.type = "text";
								input.className="uploadFile";
								input.placeholder = txt;
								input.style.cssText = "position:absolute; padding-right:25px; background:url(../images/icon2.png) no-repeat 97% -790px; visibility: visible;" + "left:" + inputs[i].offsetLeft + "px;" + "top:" + inputs[i].offsetTop + "px;";
								newinputs.push(input);
								inputs[i].parentNode.appendChild(input);
						}
				}
				for (var j = 0; j < files.length; j++) {
						(function (j) {
								newinputs[j].onclick = function () {
										files[j].click();
								}
								files[j].onchange = function () {
										newinputs[j].value = this.value;
								}
						})(j);
				}
				return this;
		},
		selects:function(){ //下拉菜单
				var selects = document.getElementsByTagName("select");
				var that=this;
				function createSelect(elem) { //模拟select
						var sel = document.createElement("div"),
								ul = document.createElement("ul"),
								span = document.createElement("span"),
								arrow = document.createElement("span");
						sel.className = "simuSel";
						ul.className = "optionWrap";
						arrow.className = "arrow";
						var pl=0;
						if(elem.currentStyle){
								pl=parseInt(elem.currentStyle.paddingLeft);
							}else{
								pl=parseInt(document.defaultView.getComputedStyle(elem,null).paddingLeft);
									}
						sel.style.cssText = "position:absolute;" + "left:" + elem.offsetLeft + "px;" + "top:" + elem.offsetTop + "px;" + "width:" + elem.offsetWidth + "px;" + "height:" + elem.offsetHeight + "px;";
						ul.style.cssText = "position:absolute; z-index:300; display:none; left:-1px;" + "top:" + elem.offsetHeight + "px;" + "width:" + elem.offsetWidth + "px;";
						span.style.cssText="display:block;"+"line-height:"+elem.offsetHeight+"px;"+"margin-left:"+pl+"px;";
						arrow.style.cssText="position:absolute; right:4px; display:block; width:10px; height:6px; background:url(../images/icon2.png) no-repeat -6px -275px; overflow:hidden;"+"top:"+((elem.offsetHeight-3)/2)+"px;";
						var option = elem.getElementsByTagName("option");
						for (var i = 0, len = option.length; i < len; i++) {
								var li = document.createElement("li");
								li._index = i;
								li.innerHTML = option[i].innerHTML;
								li.style.cssText = "height:" + elem.offsetHeight + "px;" + "line-height:" + elem.offsetHeight + "px;";
								ul.appendChild(li);
								if (option[i].selected) { //默认选中
										span.innerHTML = option[i].innerHTML;
								}
						}
						sel.appendChild(span);
						sel.appendChild(ul);
						sel.appendChild(arrow);
						YEEPAY.addHandler(sel, "click", function (event) {
								YEEPAY.stopPropagation(event);
								document.documentElement.click();
								ul.style.display = "block";
						});
						YEEPAY.addHandler(document.documentElement, "click", function (event) {
								ul.style.display = "none";
						});
						YEEPAY.addHandler(ul, "click", function (event) {
								YEEPAY.stopPropagation(event);
								target = YEEPAY.getTarget(event);
								span.innerHTML = target.innerHTML;
								option[target._index].selected = "true";
								ul.style.display = "none";
						});
						elem.parentNode.appendChild(sel);
				}
				for (var i = 0, len = selects.length; i < len; i++) {
						YEEPAY.addClass(selects[i], "hidden");
						//YEEPAY.addClass(selects[i].parentNode,"pr");
						createSelect(selects[i]);
				}
				return this;
		},
    formatBankCard: function (id) { //银行卡格式美化
        var elem = document.getElementById(id);
        YEEPAY.addHandler(document.getElementById(id), 'keyup', function (event) {
					if(event.keyCode!==8 && event.keyCode!==37 && event.keyCode!==39){
            var str = (elem.value).replace(/[^\d]/g, "");
            var maxlen = 19;
            if (str.length < maxlen) {
                maxlen = str.length;
            }
            var temp = "";
            for (var i = 0; i < maxlen; i++) {
                temp = temp + str.substring(i, i+1);
                if (i != 0 && (i + 1) % 4 == 0) {
                    temp = temp + " ";
                }
            }
            elem.value = temp;
         }
				});
				return this;
    }
}
Form.init();