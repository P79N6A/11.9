YEEPAY = YEEPAY || {};

YEEPAY.safe = {
	controls : [],
	
	// 控件初始化，放在script标签顶端，不要放在document.ready方法中
	init : function(ctx,controlId,domId,token){
		if(navigator.userAgent.indexOf("MSIE")<0){
		   navigator.plugins.refresh();
		}
		var obj = this.getObject();
		obj.ctx = ctx;
		obj.domId = domId;
		obj.controlId = controlId;
		obj.token = token;
		obj.pgeditor = new $.pge({
			pgePath: ctx+"/ocx/",//控件文件目录
			pgeId: controlId,//控件ID
			pgeEdittype: 0,//控件类型,0星号,1明文
			pgeEreg1: "[\\d]*",//输入过程中字符类型限制
			pgeEreg2: "[\\d]{6}",//输入完毕后字符类型判断条件
			pgeMaxlength: 6,//允许最大输入长度
			pgeTabindex: 4,//tab键顺序
			pgeClass: "ocx_style",//控件css样式
			pgeInstallClass: "ocx_install",//针对安装或升级
			//pgeOnkeydown:"FormSubmit()",//回车键响应函数
			pgeBackColor: "16777215",//背景色
			pgeForeColor: "3618615",//前景色
		    tabCallback:"input2"//非IE tab键焦点切换的ID
		});
		if (obj.pgeditor.osBrowser==1 || obj.pgeditor.osBrowser==3) {
			obj.href = obj.pgeditor.settings.pgePath + obj.pgeditor.pgeditIEExe;
		} else if (obj.pgeditor.osBrowser==2 || obj.pgeditor.osBrowser==4 || obj.pgeditor.osBrowser==5 || obj.pgeditor.osBrowser==6 || obj.pgeditor.osBrowser==8) {
			obj.href = obj.pgeditor.settings.pgePath + obj.pgeditor.pgeditFFExe;
		}
		this.controls[this.controls.length]=obj;
		return obj;
	},
	
	getObject : function(){
		return {
			pgeditor : {},
			
			href : false,
			
			timer : {},
			
			ctx : "",
			
			domId : "",
			
			controlId : "",
			
			token : "",
			
			// 显示控件安装弹出层
			showInstallDialog : function (supportCridit){
				if(!this.pgeditor.isInstalled && supportCridit=="true"){
				   	popup(1,null,null,this.token,"YEEPAY.safe.controls[0].click2Install()");
				   	this.startTimer();
				}
				else if(!this.pgeditor.isInstalled && supportCridit=="false"){
					popup(7,null,null,this.token,"YEEPAY.safe.controls[0].click2Install()");
				   	this.startTimer();
				}
				else{ //控件升级
					if(this.pgeditor.osBrowser==2){
						this.pgeditor.pgeVersion = this.pgeditor.getVersion();
						if(this.pgeditor.pgeVersion!=PGEdit_FF_VERSION){
							this.pgeditor.setDownText();
							popup(10,null,null,this.token,"YEEPAY.safe.controls[0].click2Install()");
				   			this.startTimer();
						}
			        }else if(this.pgeditor.osBrowser==1 || this.pgeditor.osBrowser==3){
						var control = document.getElementById(this.controlId);
						var a = control.output5;
			        	if(a==undefined){
			        		this.pgeditor.setDownText();
							popup(10,null,null,this.token,"YEEPAY.safe.controls[0].click2Install()");
				   			this.startTimer();
			        	}
			        }
					this.pgeditor.pgInitialize();
				}
			},
			
			// 控件安装弹出层按钮点击回调函数
			click2Install : function(){
				window.open(this.href);
			},
			
			// 检测控件是否安装完毕
			checkInstall : function (){
				this.pgeditor.isInstalled = this.pgeditor.checkInstall();
				try{
				   navigator.plugins.refresh(true);
				}catch(e){}
				if(this.pgeditor.isInstalled){
					if(this.pgeditor.osBrowser==2){//控件升级
						this.pgeditor.pgeVersion = this.pgeditor.getVersion();
						if(this.pgeditor.pgeVersion!=PGEdit_FF_VERSION){
							return;
						}
			        }
					clearInterval(this.timer);
					var isChrome =navigator.userAgent.toLowerCase().indexOf("chrome") != -1;
					var isSafari =navigator.userAgent.toLowerCase().indexOf("safari") != -1;
					if(isChrome || isSafari){
						//此处补充chrome和safari下post页面刷新提交的实现
					}
					else{
						location.reload();
					}
				}
			},
			
			// 启动控件是否安装完毕的检测轮询
			startTimer : function (){
				clearInterval(this.timer);
				this.timer =  window.setInterval("YEEPAY.safe.controls[0].checkInstall()",3000);
			},
			
			// 将控件html代码输出到页面
			generate : function(){
				this.pgeditor.isInstalled = this.pgeditor.checkInstall();
		      	if(this.pgeditor.isInstalled){
		      		this.pgeditor.generate();
		      	}
		      	else{
		          	if (this.pgeditor.osBrowser==1 || this.pgeditor.osBrowser==3) {
		          		document.write(this.pgeditor.getpgeHtml());
		          	}
		      	}
		      	this.pgeditor.pgInitialize();
		      	var isIE11 =navigator.userAgent.toLowerCase().search(/(trident.*rv*)([\w.]+)/)!=-1;
				if(isIE11){
					var link = document.createElement('link');
					link.type="text/css";
					link.rel="stylesheet";
					link.href=this.ctx+"/css/safe.ie.css";
					document.body.appendChild(link);
				}
				
			},
			
			beforeSubmit : function(token){
				var safe = this;
				var rtn = true;
				//alert(this.ctx+"/safe/password?token="+token+"?"+new Date().getTime());
				$.ajax({
					url: this.ctx+"/safe/password?token="+token,
					type: "GET",
					async: false,
					cache: false,
					success: function(srand_num){
					    safe.pgeditor.pwdSetSk(srand_num);
					    $("#"+safe.domId).val(safe.pgeditor.pwdResult());
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
					    rtn = false;
					}
				 });
				 return rtn;
			}
		};
	}
};