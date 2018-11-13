/// <reference path="jquery-1.3.2-vsdoc2.js" />
mulitinputbox.prototype.showTipFlag=true;
mulitinputbox.prototype.noneTipMessage="";
mulitinputbox.prototype.tipMessage="";
mulitinputbox.prototype.separator=",";
function mulitinputbox(id) {
    //换行符号
	this.newLine="\n";
	this.showLine="\n";
	var showing=false;
    //初始化信息
	var textBox = $(id);
	var textareaId=textBox.attr("id")+"multiBoxId";
	var tipdivId=textBox.attr("id")+"tipdivId";
	textBox.attr("readonly","readonly");
	textBox.each(function(i, dom) {
		textBox = $(dom);
		textBox.wrap('<div />');
		var additionalHeight = $.browser.msie ? 3 : 6;
	});
	//选项容器。
	if (!this.optionsContainer) {
		this.optionsContainer = $('<div id="multiInputBoxDivId"  />').appendTo($('body'));
		//if there is jquery.bgiframe plugin, use it to fix ie6 select/z-index bug.
		//search "z-index ie6 select bug" for more infomation
		if ($.fn.bgiframe)
			this.optionsContainer.bgiframe();
	}  
	//不同浏览器支持的换行符不同
	if($.browser.msie&&($.browser.version == "6.0"||$.browser.version == "7.0"||$.browser.version == "8.0")){ 
				 this.showLine="\r\n";
	} 
	var mulitObj = this;
	//textBox.bind("click", domClick);
	//焦点进入输入框
	textBox.click(function() {
		show();
	});
	//鼠标离开有效区域
//	function domClick(e) {
//		var dom = $.browser.msie ? event.srcElement : e.target;
//		if (showing == true && $(dom).attr("class") != "MultiInputBox") { //层显示的话隐藏
//			hide();
//		}
//		if(showing == false && $(dom).attr("class") == textBox.attr("class")){
//			show();
//		}
//	}
	//显示输入框
	function show() {
		mulitObj.optionsContainer.show();
		var html = "<textarea id='"+textareaId+"' class='MultiInputBox'></textarea><div id='"+tipdivId+"' class='tipTextDiv'></div>";
			var loc = { left: textBox.offset().left, top: textBox.offset().top, width: textBox.width()  + 40 ,height: textBox.height()*7 }
		mulitObj.optionsContainer.html(html.toString()).css(loc);
		showing = true;
		var existVal = textBox.val(); //获得当前框值
		var valAry=existVal.split(mulitObj.separator);
		var str="";
		if(existVal!=""&&valAry.length>0){
			for(var inx in valAry){
				//$("#"+textareaId).append(valAry[inx]+mulitObj.showLine);
				str+=valAry[inx]+mulitObj.showLine;
			}
			document.getElementById(textareaId).value=str;
			if(mulitObj.tipMessage==""){
				showTipMessage("当前有效查询条件",valAry.length);					
			}else{
				showTipMessage(mulitObj.tipMessage,valAry.length);					
			}
		}else{
			if(mulitObj.noneTipMessage==""){					
				showTipMessage("多条件时请用回车键换行");	
			}else{
				showTipMessage(mulitObj.noneTipMessage);	
			}		
		}
		//$(document).bind("click", domClick);
		$("#"+textareaId).focus();
		$("#"+textareaId).blur( function() {
			hide();
		});
		
		$("#"+textareaId).bind("input propertychange", function() {
			var inputAry=$(this).val().split(mulitObj.newLine);
			var num=0;
			for(var inx in inputAry){
				if(inputAry[inx] != null && $.trim(inputAry[inx]) != ""){
					num++;
				}
			}
			if(inputAry!=""&&num>0){
				if(mulitObj.tipMessage==""){
					showTipMessage("当前有效查询条件",num);					
				}else{
					showTipMessage(mulitObj.tipMessage,num);					
				}
			}else{
				if(mulitObj.noneTipMessage==""){					
					showTipMessage("多条件时请用回车键换行");	
				}else{
					showTipMessage(mulitObj.noneTipMessage);	
				}
			}
		});
	}         //show end
	function hide() {
		if (showing) {
			//var inputVal=$("#"+textareaId).val();
			var inputVal=document.getElementById(textareaId).value;
			var inputAry=inputVal.split(mulitObj.newLine);
			var textVal="";
			if(inputAry.length>0){
				for(var inx in inputAry){
					if(inputAry[inx]!=null && $.trim(inputAry[inx])!=""){
						textVal+=$.trim(inputAry[inx])+mulitObj.separator;
					}
				}
				if(textVal!="")
					textVal=textVal.substring(0,textVal.length-1);
			}
			textBox.val(textVal);
			var flag=true;
			mulitObj.optionsContainer.hide();
			showing = false;
			//$(document).unbind("click");
		}
	} //hide_end
	//显示提示信息
	function showTipMessage(meg,num){
		var showHtml="<ul>";
		for(var i=0;i<meg.length;i++){
			showHtml+="<li>"+meg.charAt(i)+"</li>";
		}	
		if(num!=null&&num!=""&&num>=0){
			showHtml+="<li>"+num+"</li>";
		}
		showHtml+="</ul>";
		$("#"+tipdivId).html(showHtml);
	}
} //mulitinputbox_end


