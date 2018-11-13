/**
 * 弹窗扩展 通用消息处理：等待，错误信息提示
 */
var MessageBoxExt = {

	SUCCESS : "操作成功",
	/**
	 * ajax效果说明： <br>
	 * 1.NONE: 无效果，主要为使用通用错误处理<br>
	 * 2.BASIC: 只提示请稍候，成功后自动关闭<br>
	 * 3.NORMAL:ajax调用成功后点击“确定”只关闭成功提示框<br>
	 * 4.CLOSEWINDOW:ajax调用成功后点击“确定”关闭窗口<br>
	 * 5.UNPOPALL:ajax调用成功后点击“确定”关闭所有弹出层<br>
	 * 6.REDIRECT:ajax调用成功后点击“确定”跳转到指定页面<br>
	 * 7.CALLBACK:ajax调用成功后点击“确定”回调指定处理
	 */
	styles : {
		NONE : "NONE",
		BASIC : "BASIC",
		NORMAL : "NORMAL",
		CLOSEWINDOW : "CLOSEWINDOW",
		UNPOPALL : "UNPOPALL",
		REDIRECT : "REDIRECT",
		CALLBACK : "CALLBACK"
	},
	popups : {},
	index : 0,
	/**
	 * 弹出提示框，可控制右上角的“X”
	 */
	_alert : function(msg, options) {
		options = jQuery.extend({
			title : MessageBox.title,
			modal : true,
			draggable : false,
			resizable : false,
			width : 392
		}, options || {});
		MessageBox._addOptionsEvent(options);
		var p = $(MessageBox.div).addClass('alert').text(msg);
		p.dialog(options);
		// 不显示右上角“X”
		if (options.closeable == false) {
			$(p[0].previousSibling.lastElementChild).hide();
		}
		this.popups[this.index++] = p;
		return p;
	},
	/**
	 * 关闭弹出提示框
	 */
	_removeAlert : function() {
		var i = this.index - 1;
		if (this.popups[i]) {
			this.popups[i].dialog("close");
			this.popups[i] = null;
			this.index = i;
		}
	},
	/**
	 * 检查ajax效果，默认NORMAL
	 */
	_checkStyle : function(style) {
		if (!style) {
			return this.styles.NORMAL;
		}
		style = style.toUpperCase();
		for ( var i in this.styles) {
			if (style == this.styles[i]) {
				return this.styles[i];
			}
		}
		return this.styles.NORMAL;
	},

	/**
	 * 提示处理中
	 */
	wait : function(msg) {
		msg = msg || "正在处理，请稍候...";
		var options = {
			closeable : false
		};
		this._alert(msg, options);
	},

	/**
	 * 关闭“处理中”提示
	 */
	unwait : function() {
		this._removeAlert();
	},

	/**
	 * 提示操作成功
	 */
	success : function(msg) {
		msg = msg || this.SUCCESS;
		MessageBox.alert(msg);
	},

	/**
	 * 根据返回数据检查是否真正成功
	 */
	_checkSuccess : function(result, request) {
		if (result && result.success == true || result.success == "true") {
			return true;
		} else {
			return false;
		}
	},

	/**
	 * 取得错误信息
	 */
	_getErrMsg : function(result, request) {
		var type = "json";
		if (request) {
			var contentType = request.getResponseHeader('Content-Type') || "";
			contentType = contentType.toLowerCase();
			if (contentType.indexOf("html") != -1) {
				type = "html";
			} else if (contentType.indexOf("stream") != -1) {
				type = "stream";
			}
		}
		var msg = null;
		// json返回的数据
		if (type == "json") {
			msg = result.errMsg || this.SUCCESS;
		} else if (type == "stream") {
			return "download";
		} else if (type == "html") {
			if (result.indexOf("错误") == -1) {
				msg = this.SUCCESS;
			} else {
				msg = result.responseText;
			}
		} else if (typeof (result) == "undefined") {
			msg = this.SUCCESS;
		} else {
			msg = result || this.SUCCESS;
		}
		return msg;
	},

	/**
	 * 提示操作成功，点击确定按钮只关闭提示框
	 */
	_styleNormal : function(msg) {
		MessageBox.alert(msg || this.SUCCESS);
	},

	/**
	 * 提示操作成功，点击确定按钮回调相应处理
	 */
	_styleCallback : function(msg, callback, result, txtStatus) {
		var btn = [ {
			"text" : MessageBox.buttons['ok'],
			"click" : function() {
				$(this).dialog("close");
				if (callback) {
					callback(result, txtStatus);
				} else {
					MessageBox.alert("没有提供回调函数");
				}
			}
		} ];
		MessageBox.alert(msg || this.SUCCESS, {
			buttons : btn
		});
	},

	/**
	 * 提示操作成功，点击确定按钮关闭窗口
	 */
	_styleCloseWindow : function(msg) {
		var btn = [ {
			"text" : MessageBox.buttons['ok'],
			"click" : function() {
				window.open('', '_self', '');
				window.close();
			}
		} ];
		MessageBox.alert(msg || this.SUCCESS, {
			buttons : btn
		});
	},

	/**
	 * 提示操作成功，点击确定按钮关闭所以弹出层
	 */
	_styleUnpopAll : function(msg) {
		var btn = [ {
			"text" : MessageBox.buttons['ok'],
			"click" : function() {
				$(this).dialog("close");
				MessageBoxExt.closeAll();
			}
		} ];
		MessageBox.alert(msg || this.SUCCESS, {
			buttons : btn
		});
	},

	/**
	 * 提示操作成功，点击确定按钮跳转到指定url
	 */
	_styleRedirect : function(msg, toUrl) {
		var btn = [ {
			"text" : MessageBox.buttons['ok'],
			"click" : function() {
				$(this).dialog("close");
				MessageBoxExt.closeAll();
				if (toUrl) {
					window.location.href = toUrl;
				} else {
					window.location.reload();
				}
			}
		} ];
		MessageBox.alert(msg || this.SUCCESS, {
			buttons : btn
		});
	},

	/**
	 * 自动矫正url
	 */
	_correctUrl : function(url) {
		if (typeof (url) != "string") {
			MessageBox.alert("URL不正确");
			return "";
		}
		if (url.indexOf("/") != 0) {
			url = "/" + url;
		}
		if (url.indexOf(GV.ctxPath) < 0) {
			url = GV.ctxPath + url;
		}
		return url;
	},

	/**
	 * 关闭所有弹出层
	 */
	closeAll : function() {
		while (this.index > 0) {
			this._removeAlert();
		}
		jQuery.each(MessageBox.popups, function(obj) {
			MessageBox.unpopup(obj);
		});
	},

	/**
	 * 弹出提示信息
	 */
	info : function(msg) {
		MessageBox.alert(msg);
	},

	/**
	 * 弹出错误信息
	 */
	error : function(msg, afterBizError) {
		msg = msg || "网络异常，请稍后重试！";
		var options = {
			title : "出错啦！",
			modal : true,
			buttons : [ {
				"text" : MessageBox.buttons['ok'],
				"click" : function() {
					$(this).dialog("close");
					if (afterBizError) {
						afterBizError();
					}
				}
			} ],
			draggable : false,
			resizable : false,
			width : 392
		};
		MessageBox._addOptionsEvent(options);
		$(MessageBox.div).addClass('alert error').html(msg).dialog(options);
	},

	/**
	 * ajax调用
	 */
	ajax : function(H) {
		var style = MessageBoxExt._checkStyle(H.style);
		if (style != MessageBoxExt.styles.NONE) {
			MessageBoxExt.wait();
		}
		// 自动矫正url
		$.ajax({
			url : this._correctUrl(H.url),
			type : H.type || "POST",
			data : H.data || {},
			cache : H.cache,
			dataType : H.dataType || "json",
			success : function(result, textStatus, request) {
				if (style != MessageBoxExt.styles.NONE) {
					MessageBoxExt.unwait();
				}
				var msg = MessageBoxExt._getErrMsg(result, request);
				if (H.getMessage) {
					msg = H.getMessage(result, textStatus);
				}
				var realSuccess = MessageBoxExt._checkSuccess(result, request);
				if (H.checkSuccess) {
					realSuccess = H.checkSuccess(result, textStatus, request);
				} else if (style == MessageBoxExt.styles.NONE) {
					realSuccess = true;
				} else if (msg == "download") {
					realSuccess = true;
				} else if (!realSuccess) {
					realSuccess = msg == MessageBoxExt.SUCCESS;
				}
				if (realSuccess && H.success) {
					H.success(result, textStatus, request);
				}
				if (realSuccess) {
					if (msg == "download") {
						// 文件下载
						result.execCommand('SaveAs');
					} else if (style == MessageBoxExt.styles.NORMAL) {
						MessageBoxExt._styleNormal(msg);
					} else if (style == MessageBoxExt.styles.CLOSEWINDOW) {
						MessageBoxExt._styleCloseWindow(msg);
					} else if (style == MessageBoxExt.styles.UNPOPALL) {
						MessageBoxExt._styleUnpopAll(msg);
					} else if (style == MessageBoxExt.styles.REDIRECT) {
						MessageBoxExt._styleRedirect(msg, H.toUrl);
					} else if (style == MessageBoxExt.styles.CALLBACK) {
						if (H.callback) {
							MessageBoxExt._styleCallback(msg, H.callback,
									result, textStatus);
						} else {
							MessageBoxExt._styleNormal(msg);
						}
					}
				} else {
					MessageBoxExt.error(msg, H.afterBizError);
				}
				return true;
			},
			error : function(result, textStatus) {
				if (style != MessageBoxExt.styles.NONE) {
					MessageBoxExt.unwait();
				}
				if (H.error) {
					H.error(result, textStatus);
				}
			}
		});
	},

	/**
	 * MessageBox基本函数
	 */
	alert : function(msg, options) {
		MessageBox.alert(msg, options);
	},

	/**
	 * 确认提示框，默认消息为“是否确认此操作？”
	 */
	confirm : function(msg, callback, options) {
		msg = msg || "是否确认此操作？";
		MessageBox.confirm(msg, callback, options);
	},

	/**
	 * 关闭弹出层
	 * 
	 * @param id
	 *            弹出层ID或弹出层对象
	 */
	close : function(id) {
		if (typeof (id) == "string") {
			MessageBox.unpopup(id);
		} else {
			MessageBox.close(id);
		}
	},

	/**
	 * 弹出一个层
	 */
	popup : function(id, options) {
		MessageBox.popup(id, options);
	},

	/**
	 * 关闭一个层
	 */
	unpopup : function(id) {
		MessageBox.unpopup(id);
	},

	/**
	 * ajax加载一个页面（区块）然后弹出
	 * 
	 * @param id
	 *            弹出层ID
	 * @param url
	 *            请求url
	 * @param options
	 *            其他选项，按钮、宽高等
	 * @param callback
	 *            弹出后回调处理
	 */
	load : function(id, url, options, callback) {
		options = options || {};
		MessageBoxExt.wait();
		$.ajax({
			url : this._correctUrl(url),
			type : "GET",
			cache : false,
			success : function(result, textStatus) {
				MessageBoxExt.unwait();
				var target = $("#" + id);
				if (target.length < 1) {
					target = $(document.createElement("div"));
					target.attr("id", id);
					target.hide();
					target.html(result);
					$("body").append(target);
				} else {
					target.html(result);
				}
				MessageBox.popup(id, options);
				if (callback) {
					callback(result, textStatus);
				}
			},
			error : function() {
				MessageBoxExt.unwait();
			}
		});
	},

	/**
	 * 弹出内容体
	 */
	popupContent : function(id, data, options) {
		MessageBox.popupContent(id, data, options);
	}
};

/**
 * 日期时间选择控件
 **/
var DatePickerExt = {
	_defaultDateOption : function(options) {
		if (typeof (options) == "string") {
			options = {
				showButtonPanel : true
			}
		}
		options = options || {};
		options.defaultDate = options.defaultDate || "+1d";
		options.numberOfMonths = options.numberOfMonths || 1;
		options.showButtonPanel = true;
		return options;
	},
	_defaultTimeOption : function(options) {
		options = options || {};
		if (typeof (options.showSecond) == "undefined") {
			options.showSecond = true;
		}
		options.defaultDate = options.defaultDate || "+0d";
		options.timeFormat = options.timeFormat || "hh:mm:ss";
		return options;
	},
	/**
	 * 日期选择
	 */
	date : function(start, options) {
		options = this._defaultDateOption(options);
		$("#" + start).datepicker(options);
	},
	/**
	 * 时间选择
	 */
	time : function(start, options) {
		options = this._defaultTimeOption(options);
		$("#" + start).datetimepicker(options);
	},
	/**
	 * 日期区间选择
	 */
	between : function(start, end, options) {
		options = this._defaultDateOption(options);
		var dates = $("#" + start + ", #" + end).datepicker(
			jQuery.extend({
				defaultDate : options.defaultDate,
				numberOfMonths : options.numberOfMonths,
				onSelect : function(selectedDate) {
					var option = this.id == start ? "minDate" : "maxDate";
					var instance = $(this).data("datepicker");
					var date = $.datepicker.parseDate(
						instance.settings.dateFormat
						|| $.datepicker._defaults.dateFormat,
						selectedDate, instance.settings);
					dates.not(this).datepicker("option", option, date);
					$(this).blur();
				}
			}, options));
		return dates;
	},
	timeBetween : function(start, end, options) {
		options = this._defaultTimeOption(options);
		var startDateTextBox = $('#' + start);
		var endDateTextBox = $('#' + end);
		startDateTextBox.datetimepicker(jQuery.extend({
				onClose: function (dateText, inst) {
					if(dateText == null || isBlank(dateText))
						return;
					var testStartDate = startDateTextBox.datetimepicker('getDate');
					var endDateTmp = addDate(dateText, 7);//设置查询时间间隔为7天
					var testEndDate = endDateTextBox.datetimepicker('getDate');
					if(testEndDate == null || testStartDate > testEndDate){
						testEndDate = endDateTmp;
					}
					var option = {
						minDate:testStartDate,
						maxDate:endDateTmp
					};
					endDateTextBox.datetimepicker('option',option);
					endDateTextBox.datetimepicker('setDate',testEndDate);
				}
			}, options));
		endDateTextBox.datetimepicker(jQuery.extend({
				// onClose: function (dateText, inst) {
				// 	var testStartDate = startDateTextBox.datetimepicker('getDate');
				// 	if(testStartDate==null)
				// 		testStartDate = dateText;
				// 	if (testStartDate > dateText) {
				// 		startDateTextBox.datetimepicker('setDate', dateText);
				// 	} else {
				// 		startDateTextBox.datetimepicker('setDate', testStartDate);
				// 	}
				// }
			}, options));
	},
	/**
	 * 快捷查询
	 *
	 * @param options:{selector:"selector",formId:"formId"}
	 */
	latest : function(start, end, options) {
		options = options || {};
		options.selector = options.selector || ".search_tit > ul > li > a";
		var $start = $("#" + start);
		var $end = $("#" + end);
		var $form = $("#" + options.formId);
		var inputs = inputSelector
			|| "input[type='text'],input[type='password'],input[type='checkbox'],input[type='radio'],select,textarea"
		$(options.selector).click(function() {
			if ($form.length > 0) {
				$form.find(inputs).each(function() {
					$(this).val("");
				});
			}
			var text = $.trim($(this).text());
			if (text == "1年前") {
				$start.datepicker('setDate', "");
				$end.datepicker('setDate', "-1y");
			} else if (text == "3个月") {
				$start.datepicker('setDate', "-3m");
				$end.datepicker('setDate', "0d");
			} else if (text == "1个月") {
				$start.datepicker('setDate', "-1m");
				$end.datepicker('setDate', "0d");
			} else if (text == "最近7天") {
				$start.datepicker('setDate', "-7d");
				$end.datepicker('setDate', "0d");
			}
			if ($form.length > 0) {
				$form.submit();
			}
		});
	}
};