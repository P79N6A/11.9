var PGEdit_IE32_CLASSID="965DC6F3-6A13-40BD-B9C4-F123725B952B";
var PGEdit_IE32_CAB="yeepayEdit.cab#version=1,0,0,6";
var PGEdit_IE32_EXE="YeePaySecureGuardIE.msi";

var PGEdit_IE64_CLASSID="33344774-AAB5-4ED4-9C17-527F13863DA5";
var PGEdit_IE64_CAB="yeepayEditX64.cab#version=1,0,0,2";
var PGEdit_IE64_EXE="YeePaySecureGuardX64.msi";

var PGEdit_FF="YeePaySecureGuardFF.msi";
var PGEdit_Linux32="";
var PGEdit_Linux64="";
var PGEdit_FF_VERSION="1.0.3.7";
var PGEdit_Linux_VERSION="";

var PGEdit_MacOs="";
var PGEdit_MacOs_VERSION="";

var PGEdit_MacOs_Safari="";
var PGEdit_MacOs_Safari_VERSION="";

;(function($) {
	$.pge = function (options) {
		this.settings = $.extend(true, {}, $.pge.defaults, options);
		this.init();
	};

	$.extend($.pge, {
		defaults: {
			pgePath: "./ocx/",
			pgeId: "",
			pgeEdittype: 0,
			pgeEreg1: "",
			pgeEreg2: "",
			pgeMaxlength: 12,
			pgeTabindex: 2,
			pgeClass: "ocx_style",
			pgeInstallClass: "ocx_style",
			pgeOnkeydown:"",
			pgeFontName:"Arial Black",
			pgeFontSize:25,
			pgeBackColor: "8454016",//背景色
			pgeForeColor: "65535",//前景色		
			tabCallback:""
		},

		prototype: {
			init: function() {				
			    this.pgeDownText="请点此安装控件";
			    this.osBrowser = this.checkOsBrowser();
				this.pgeVersion = this.getVersion();			    			
				this.isInstalled = this.checkInstall();
			},

			checkOsBrowser: function() {
				var _IE = function(){ 
					var v = 3, div = document.createElement('div'), all = div.getElementsByTagName('i'); 
					while ( 
						div.innerHTML = '<!--[if gt IE ' + (++v) + ']><i></i><![endif]-->', 
						all[0] 
					); 
					if(v==4){
						var agent = navigator.userAgent.toLowerCase();
						var isIE11 =agent.search(/(trident.*rv*)([\w.]+)/)!=-1;
						if(isIE11){
							var rv = agent.indexOf("rv:");
							var rve = agent.indexOf(")");
							var version = agent.substring(rv+3,rve);
							version = Number(version);
							if(!version){
								version = 11;
							}
							return version;
						}
					}
					return v > 4 ? v : false ;
				};
				var userosbrowser;
				if((navigator.platform =="Win32") || (navigator.platform =="Windows")){
					if(_IE()){
						userosbrowser=1;//windows32ie32
						this.pgeditIEClassid=PGEdit_IE32_CLASSID;
						this.pgeditIECab=PGEdit_IE32_CAB;
						this.pgeditIEExe=PGEdit_IE32_EXE;
					}else{
						userosbrowser=2; //windowsff
						this.pgeditFFExe=PGEdit_FF;
					}
				}else if((navigator.platform=="Win64")){
					if(_IE()){
						userosbrowser=3;//windows64ie64
						this.pgeditIEClassid=PGEdit_IE64_CLASSID;
						this.pgeditIECab=PGEdit_IE64_CAB;
						this.pgeditIEExe=PGEdit_IE64_EXE;			
					}else{
						userosbrowser=2;//windowsff
						this.pgeditFFExe=PGEdit_FF;
					}
				}else if(navigator.userAgent.indexOf("Linux")>0){
					if(navigator.userAgent.indexOf("_64")>0){
						userosbrowser=4;//linux64
						this.pgeditFFExe=PGEdit_Linux64;
					}else{
						userosbrowser=5;//linux32
						this.pgeditFFExe=PGEdit_Linux32;
					}
					if(navigator.userAgent.indexOf("Android")>0){
                        userosbrowser=7;//Android
                     }					
				}else if(navigator.userAgent.indexOf("Macintosh")>0){
					if(navigator.userAgent.indexOf("Safari")>0 && (navigator.userAgent.indexOf("Version/5.1")>0 || navigator.userAgent.indexOf("Version/5.2")>0)){
						userosbrowser=8;//macos Safari 5.1 more
						this.pgeditFFExe=PGEdit_MacOs_Safari;
					}else if(navigator.userAgent.indexOf("Firefox")>0 || navigator.userAgent.indexOf("Chrome")>0){
						userosbrowser=6;//macos
						this.pgeditFFExe=PGEdit_MacOs;						
					}else if(navigator.userAgent.indexOf("Opera")>=0 && (navigator.userAgent.indexOf("Version/11.6")>0 || navigator.userAgent.indexOf("Version/11.7")>0)){
						userosbrowser=6;//macos
						this.pgeditFFExe=PGEdit_MacOs;						
					}else if(navigator.userAgent.indexOf("Safari")>=0 && (navigator.userAgent.indexOf("Version/4.0")>0 || navigator.userAgent.indexOf("Version/5.0")>0)){
						userosbrowser=6;//macos
						this.pgeditFFExe=PGEdit_MacOs;			
					}else{
						userosbrowser=0;//macos
						this.pgeditFFExe="";
					}
				}
				return userosbrowser;
			},
			
			getpgeHtml: function() {
				if (this.osBrowser==1 || this.osBrowser==3) {

					return '<span id="'+this.settings.pgeId+'_pge" style="display:none;"><OBJECT ID="' + this.settings.pgeId + '" CLASSID="CLSID:' + this.pgeditIEClassid + '" codebase="' 
					         
					        +this.settings.pgePath+ this.pgeditIECab + '" onkeydown="if(13==event.keyCode || 27==event.keyCode)'+this.settings.pgeOnkeydown+';" tabindex="'+this.settings.pgeTabindex+'" class="' + this.settings.pgeClass + '">' 
					        
					        + '<param name="edittype" value="'+ this.settings.pgeEdittype + '"><param name="maxlength" value="' + this.settings.pgeMaxlength +'">' 

							+ '<param name="input2" value="'+ this.settings.pgeEreg1 + '"><param name="input3" value="'+ this.settings.pgeEreg2 + '"></OBJECT></span>'
							
							+ '<div id="'+this.settings.pgeId+'_down" class="'+this.settings.pgeInstallClass+'" style="text-align:center;display:none;"><a href="'+this.settings.pgePath+this.pgeditIEExe+'">'+this.pgeDownText+'</a></div>';

				} else if (this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5) {
					
					return '<embed ID="' + this.settings.pgeId + '"  maxlength="'+this.settings.pgeMaxlength+'" edittype="'+this.settings.pgeEdittype+'" type="application/x-yeepay-edit" tabindex="'+this.settings.pgeTabindex+'" class="' + this.settings.pgeClass + '" >';

				} else if (this.osBrowser==6) {
					
					return '<embed ID="' + this.settings.pgeId + '" input2="'+ this.settings.pgeEreg1 + '" input3="'+ this.settings.pgeEreg2 + '" input4="'+Number(this.settings.pgeMaxlength)+'" input0="'+Number(this.settings.pgeEdittype)+'" type="application/microdone-passguard-plugin" version="'+PGEdit_MacOs_VERSION+'" tabindex="'+this.settings.pgeTabindex+'" class="' + this.settings.pgeClass + '">';
				
				} else if (this.osBrowser==8) {

					return '<embed ID="' + this.settings.pgeId + '" input2="'+ this.settings.pgeEreg1 + '" input3="'+ this.settings.pgeEreg2 + '" input4="'+Number(this.settings.pgeMaxlength)+'" input0="'+Number(this.settings.pgeEdittype)+'" type="application/microdone-passguard-safari-plugin" version="'+PGEdit_MacOs_Safari_VERSION+'" tabindex="'+this.settings.pgeTabindex+'" class="' + this.settings.pgeClass + '">';

				} else {

					return '<div id="'+this.settings.pgeId+'_down" class="'+this.settings.pgeInstallClass+'" style="text-align:center;">暂不支持此浏览器</div>';

				}				
			},
			
			getDownHtml: function() {
				if (this.osBrowser==1 || this.osBrowser==3) {
					return '<div id="'+this.settings.upeId+'_down" class="'+this.settings.pgeInstallClass+'" style="text-align:center;"><a href="'+this.settings.pgePath+this.pgeditIEExe+'">'+this.pgeDownText+'</a></div>';
				} else if (this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5 || this.osBrowser==6 || this.osBrowser==8) {

					return '<div id="'+this.settings.upeId+'_down" class="'+this.settings.pgeInstallClass+'" style="text-align:center;"><a href="'+this.settings.pgePath+this.pgeditFFExe+'">'+this.pgeDownText+'</a></div>';
				
				} else {

					return '<div id="'+this.settings.pgeId+'_down" class="'+this.settings.pgeInstallClass+'" style="text-align:center;">暂不支持此浏览器</div>';

				}				
			},
			
			load: function() {				
				if (!this.checkInstall()) {
					return this.getDownHtml();
				}else{		
				   if(this.osBrowser==2){  
						if(this.pgeVersion!=PGEdit_FF_VERSION){
							this.setDownText();
							return document.write(this.getDownHtml());	
						}				    
				   }else if(this.osBrowser==4 || this.osBrowser==5){
						if(this.pgeVersion!=PGEdit_Linux_VERSION){
							this.setDownText();
							return this.getDownHtml();	
						}
					} else if (this.osBrowser==6) {
						if(this.pgeVersion!=PGEdit_MacOs_VERSION){
							this.setDownText();
							return this.getDownHtml();	
						}
					}else if (this.osBrowser==8) {
						if(this.pgeVersion!=PGEdit_MacOs_Safari_VERSION){
							this.setDownText();
							return this.getDownHtml();	
						}
					}					
					return this.getpgeHtml();
				}
			},
			
			generate: function() {
				//alert(this.pgeVersion);
				   if(this.osBrowser==2){
					   //alert(this.pgeVersion);
						if(this.pgeVersion!=PGEdit_FF_VERSION){
							this.setDownText();
							return document.write(this.getDownHtml());	
						}
			       }else if(this.osBrowser==4 || this.osBrowser==5){   
						if(this.pgeVersion!=PGEdit_Linux_VERSION){
							this.setDownText();
							return document.write(this.getDownHtml());	
						}
					} else if (this.osBrowser==6) {
						if(this.pgeVersion!=PGEdit_MacOs_VERSION){
							this.setDownText();
							return document.write(this.getDownHtml());	
						}
					}else if (this.osBrowser==8) {
						if(this.pgeVersion!=PGEdit_MacOs_Safari_VERSION){
							this.setDownText();
							return document.write(this.getDownHtml());
						}
					}
					return document.write(this.getpgeHtml());				
			},
			
			pwdclear: function() {
				if (this.checkInstall()) {
					var control = document.getElementById(this.settings.pgeId);
					control.ClearSeCtrl();
				}				
			},
			pwdSetSk: function(s) {
				if (this.checkInstall()) {
					try {
						var control = document.getElementById(this.settings.pgeId);
						if (this.osBrowser==1 || this.osBrowser==3 || this.osBrowser==6 || this.osBrowser==8) {
							control.input1=s;
						} else if (this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5) {
							control.input(8,s);
						}					
					} catch (err) {
					}
				}				
			},
			pwdResult: function() {

				var code = '';

				if (!this.checkInstall()) {

					code = '01';
				}
				else{	
					try {
						var control = document.getElementById(this.settings.pgeId);
						if (this.osBrowser==1 || this.osBrowser==3) {
							code = control.output32;
						} else if (this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5) {
							code = control.output(7);
						}else if (this.osBrowser==6 || this.osBrowser==8) {
							code = control.get_output1();
						}					
					} catch (err) {
						code = '02';
					}
				}
				//alert(code);
				return code;
			},

			machineNetwork: function() {
				var code = '';

				if (!this.checkInstall()) {

					code = '01';
				}
				else{
					try {
						var control = document.getElementById(this.settings.pgeId);
						if (this.osBrowser==1 || this.osBrowser==3) {
							code = control.GetIPMacList();
						} else if (this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5) {
							code = control.output(11);
						}else if (this.osBrowser==6 || this.osBrowser==8) {
							code = control.get_output7(0);
						}
					} catch (err) {

						code = '02';

					}
				}
				return code;
			},
			machineDisk: function() {
				var code = '';

				if (!this.checkInstall()) {

					code = '01';
				}
				else{
					try {
						var control = document.getElementById(this.settings.pgeId);
						if (this.osBrowser==1 || this.osBrowser==3) {
							code = control.GetNicPhAddr(1);
						} else if (this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5) {
							code = control.output(13);
						}else if (this.osBrowser==6 || this.osBrowser==8) {
							code = control.get_output7(2);
						}
					} catch (err) {

						code = '02';

					}
				}
				return code;
			},
			machineCPU: function() {
				var code = '';

				if (!this.checkInstall()) {

					code = '01';
				}
				else{
					try {
						var control = document.getElementById(this.settings.pgeId);
						if (this.osBrowser==1 || this.osBrowser==3) {
							code = control.GetNicPhAddr(2);
						} else if (this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5) {
							code = control.output(15);
						}else if (this.osBrowser==6 || this.osBrowser==8) {
							code = control.get_output7(1);
						}
					} catch (err) {
						code = '02';
					}
				}
				return code;
			},
			pwdValid: function() {
				var code = '';

				if (!this.checkInstall()) {

					code = 1;
				}
				else{
					try {
						var control = document.getElementById(this.settings.pgeId);
						if (this.osBrowser==1 || this.osBrowser==3) {
							code = control.output5;
						} else if (this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5) {
							code = control.output(5);
						}else if (this.osBrowser==6 || this.osBrowser==8) {
							code = control.get_output5();
						}
					} catch (err) {

						code = 1;

					}
				}
				return code;
			},				
			pwdHash: function() {
				var code = '';

				if (!this.checkInstall()) {

					code = 0;
				}
				else{
					try {
						var control = document.getElementById(this.settings.pgeId);
						if (this.osBrowser==1 || this.osBrowser==3) {
							code = control.output2;
						} else if (this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5) {
							code = control.output(2);
						}else if (this.osBrowser==6 || this.osBrowser==8) {
							code = control.get_output2();
						}
					} catch (err) {

						code = 0;

					}
				}
				return code;
			},			
			pwdLength: function() {
				var code = '';

				if (!this.checkInstall()) {

					code = 0;
				}
				else{
					try {
						var control = document.getElementById(this.settings.pgeId);
						if (this.osBrowser==1 || this.osBrowser==3) {
							code = control.output3;
						} else if (this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5) {
							code = control.output(3);
						}else if (this.osBrowser==6 || this.osBrowser==8) {
							code = control.get_output3();
						}
					} catch (err) {

						code = 0;

					}
				}
				return code;
			},				
			pwdStrength: function() {
				var code = 0;

				if (!this.checkInstall()) {

					code = 0;

				}

				else{

					try {

						var control = document.getElementById(this.settings.pgeId);

						if (this.osBrowser==1 || this.osBrowser==3) {
							var l=control.output3;
							var n=control.output4;
						} else if (this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5) {
							var l=control.output(3);
							var n=control.output(4);
						}else if (this.osBrowser==6 || this.osBrowser==8) {
							var l=control.get_output3();
							var n=control.get_output4();
						}
						if(l==0){
							code = 0;
						}
						if(l<6){
							code = 1;
						}
						if(n==1 && l>=6){
							code = 2;
						}						
						if(n==2 && l>=6){
							code = 3;
						}	
						if(n==3 && l>=6){
							code = 4;
						}
					} catch (err) {

						code = 0;

					}

				}		
				return code;
								
			},

			checkInstall: function() {
				try {
					if (this.osBrowser==1) {

						var comActiveX = new ActiveXObject("yeepayEdit.yeepayEditCtrl.1"); 
					} else if (this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5 || this.osBrowser==6 || this.osBrowser==8) {

					    var arr=new Array();
					    if(this.osBrowser==6){
					    	var pge_info=navigator.plugins['PassGuard 1G'].description;
					    }else if(this.osBrowser==8){
					    	var pge_info=navigator.plugins['PassGuard Safari 1G'].description;
					    }else{
					    	var pge_info=navigator.plugins['YeePay SecureGuard'].description;
					    }
						if(pge_info.indexOf(":")>0){
							arr=pge_info.split(":");
							var pge_version = arr[1];
						}else{
							var pge_version = "";
						}
					} else if (this.osBrowser==3) {
						var comActiveX = new ActiveXObject("yeepayEditX64.yeepayEditCtrl.1");
					}
				}catch(e){
					return false;
				}
				return true;
			},
			getVersion: function() {
				try {
					if(navigator.userAgent.indexOf("MSIE")<0){
						var arr=new Array();
					    if(this.osBrowser==6){
					    	var pge_info=navigator.plugins['PassGuard 1G'].description;
					    }else if(this.osBrowser==8){
					    	var pge_info=navigator.plugins['PassGuard Safari 1G'].description;					    	
					    }else{
					    	var pge_info=navigator.plugins['YeePay SecureGuard'].description;
					    }
						if(pge_info.indexOf(":")>0){
							arr=pge_info.split(":");
							var pge_version = arr[1];
						}else{
							var pge_version = "";
						}
					}
					return pge_version;
				}catch(e){
					return "";
				}					
			},
			setDownText:function(){
				if(this.pgeVersion!=undefined && this.pgeVersion!=""){
						this.pgeDownText="请点此升级控件";
				}
			},			
			pgInitialize:function(){
				if(this.checkInstall()){
					if(this.osBrowser==1 || this.osBrowser==3){ 
			            $('#'+this.settings.pgeId+'_pge').show(); 
					}
					
					var control = document.getElementById(this.settings.pgeId);
					control.FontName=this.settings.pgeFontName;
					control.FontSize=this.settings.pgeFontSize;
					control.BackColor=this.settings.pgeBackColor;
					control.ForeColor=this.settings.pgeForeColor;
					
					if(this.osBrowser==2 || this.osBrowser==4 || this.osBrowser==5){
						control.input(2, this.settings.pgeEreg1);
						control.input(3, this.settings.pgeEreg2);
						control.returnCallback = this.settings.pgeOnkeydown;
						control.tabCallback ="document.getElementById(\""+this.settings.tabCallback+"\").focus()";
					}
					
				}else{
					if(this.osBrowser==1 || this.osBrowser==3){
						$('#'+this.settings.pgeId+'_down').show();
					}	
					
				}
				
			}
		}
	});	
	
})(jQuery);
function notifycallback(arg){}