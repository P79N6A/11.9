var Form={init:function(a){this.file().placeholder();return this},placeholder:function(h){if(!("placeholder" in document.createElement("input"))){var f=document.getElementsByTagName("input"),k=document.getElementsByTagName("textarea");var h=h||"placeholder";var e=[];var a=0;function g(j){var n=j.getBoundingClientRect().left,m=j.getBoundingClientRect().top,i=j.offsetHeight;var o=document.createElement("label");o.className=h;o.innerHTML=j.getAttribute("placeholder");e.push(o);document.body.appendChild(o);var s=(i-o.offsetHeight)/2;var l=parseInt(j.currentStyle.paddingLeft),q=parseInt(j.currentStyle.borderLeftWidth),r=parseInt(j.currentStyle.paddingTop),p=parseInt(j.currentStyle.borderTopWidth);o.style.cssText="position:absolute;left:"+(n+l+q)+"px;";if(j.nodeName.toLowerCase()=="input"){o.style.top=(m+s)+"px"}else{o.style.top=(m+r+p)+"px"}}for(var d=0,b=f.length;d<b;d++){if(f[d].type=="text"){f[d].value=f[d].getAttribute("placeholder");YEEPAY.addClass(f[d],h);YEEPAY.addHandler(f[d],"focus",function(i){var j=YEEPAY.getTarget(i);if(j.value==j.getAttribute("placeholder")){j.value="";YEEPAY.removeClass(j,h)}});YEEPAY.addHandler(f[d],"blur",function(i){var j=YEEPAY.getTarget(i);if(j.value==""){j.value=j.getAttribute("placeholder");YEEPAY.addClass(j,h)}})}else{if(f[d].type=="password"){f[d]._index=a;a++;g(f[d]);YEEPAY.addHandler(f[d],"focus",function(i){var j=YEEPAY.getTarget(i);if(j.value==""){e[j._index].style.display="none"}});YEEPAY.addHandler(f[d],"blur",function(i){var j=YEEPAY.getTarget(i);if(j.value==""){e[j._index].style.display="block"}})}}}for(var c=0,b=k.length;c<b;c++){k[c]._index=a;a++;g(k[c]);YEEPAY.addHandler(k[c],"focus",function(i){var j=YEEPAY.getTarget(i);if(j.value==""){e[j._index].style.display="none"}});YEEPAY.addHandler(k[c],"blur",function(i){var j=YEEPAY.getTarget(i);if(j.value==""){e[j._index].style.display="block"}})}}return this},radio:function(){var f=[];var e=[];var a=document.getElementsByTagName("input");var d=0;for(var c=0;c<a.length;c++){if(a[c].type=="radio"){YEEPAY.addClass(a[c].parentNode,"hidden");f.push(a[c]);var b=document.createElement("label");b.innerHTML=a[c].value;b.setAttribute("name",a[c].name);b._index=d;b.className="radioLabel";d++;b.style.cssText="position:absolute; display:inline-block; background:url("+path+"/images/icon2.png) no-repeat -2px -367px; height:14px; padding-left:18px; overflow:hidden; visibility: visible;";b.style.left=a[c].offsetLeft+"px";b.style.top=a[c].offsetTop+"px";e.push(b);a[c].parentNode.appendChild(b);b.onclick=function(){for(var g=0;g<e.length;g++){if(e[g].getAttribute("name")==this.getAttribute("name")){e[g].style.backgroundPosition="-2px -367px";e[g].setAttribute("checked","false")}}this.style.backgroundPosition="-2px -529px";this.setAttribute("checked","true");f[this._index].click()};b.onmouseover=function(){if(this.getAttribute("checked")=="true"){this.style.backgroundPosition="-2px -529px"}else{this.style.backgroundPosition="-2px -475px"}};b.onmouseout=function(){if(this.getAttribute("checked")=="true"){this.style.backgroundPosition="-2px -421px"}else{this.style.backgroundPosition="-2px -367px"}};if(a[c].checked){b.onclick();b.setAttribute("checked","true")}}}return this},checkbox:function(){var a=[];var f=[];var b=document.getElementsByTagName("input");var e=0;for(var d=0;d<b.length;d++){if(b[d].type=="checkbox"){YEEPAY.addClass(b[d].parentNode,"hidden");a.push(b[d]);var c=document.createElement("label");c.id="checkboxBtn";c.innerHTML=b[d].value;c.setAttribute("checked","false");c._index=e;e++;c.style.cssText="position:absolute; display:inline-block; background:url("+path+"/images/icon2.png) no-repeat -2px -583px; height:14px; padding-left:18px; overflow:hidden; visibility: visible;";c.style.left=b[d].offsetLeft+"px";c.style.top=b[d].offsetTop+"px";f.push(c);b[d].parentNode.appendChild(c);c.onclick=function(){if(this.getAttribute("checked")=="true"){this.setAttribute("checked","false");this.style.backgroundPosition="-2px -583px"}else{this.setAttribute("checked","true");this.style.backgroundPosition="-2px -745px"}a[this._index].click()};c.onmouseover=function(){if(this.getAttribute("checked")=="true"){this.style.backgroundPosition="-2px -745px"}else{this.style.backgroundPosition="-2px -691px"}};c.onmouseout=function(){if(this.getAttribute("checked")=="true"){this.style.backgroundPosition="-2px -637px"}else{this.style.backgroundPosition="-2px -583px"}};if(b[d].checked){c.onclick()}}}return this},file:function(a){var g=[];var f=[];var a=a||"\u70b9\u51fb\u4e0a\u4f20";var b=document.getElementsByTagName("input");for(var e=0;e<b.length;e++){if(b[e].type=="file"){YEEPAY.addClass(b[e],"hidden");g.push(b[e]);var c=document.createElement("input");c.type="text";c.className="uploadFile";c.placeholder=a;c.style.cssText="position:absolute; padding-right:25px; background:url("+path+"/images/icon2.png) no-repeat 97% -790px; visibility: visible;left:"+b[e].offsetLeft+"px;top:"+b[e].offsetTop+"px;";f.push(c);b[e].parentNode.appendChild(c)}}for(var d=0;d<g.length;d++){(function(h){f[h].onclick=function(){g[h].click()};g[h].onchange=function(){f[h].value=this.value}})(d)}return this},selects:function(){var c=document.getElementsByTagName("select");var d=this;function e(g){var f=document.createElement("div"),m=document.createElement("ul"),o=document.createElement("span"),n=document.createElement("span");f.className="simuSel";m.className="optionWrap";n.className="arrow";var h=0;if(g.currentStyle){h=parseInt(g.currentStyle.paddingLeft)}else{h=parseInt(document.defaultView.getComputedStyle(g,null).paddingLeft)}f.style.cssText="position:absolute;left:"+g.offsetLeft+"px;top:"+g.offsetTop+"px;width:"+g.offsetWidth+"px;height:"+g.offsetHeight+"px;";m.style.cssText="position:absolute; z-index:300; display:none; left:-1px;top:"+g.offsetHeight+"px;width:"+g.offsetWidth+"px;";o.style.cssText="display:block;line-height:"+g.offsetHeight+"px;margin-left:"+h+"px;";n.style.cssText="position:absolute; right:4px; display:block; width:10px; height:6px; background:url("+path+"/images/icon2.png) no-repeat -6px -275px; overflow:hidden;top:"+((g.offsetHeight-3)/2)+"px;";var k=g.getElementsByTagName("option");for(var j=0,l=k.length;j<l;j++){var p=document.createElement("li");p._index=j;p.innerHTML=k[j].innerHTML;p.style.cssText="height:"+g.offsetHeight+"px;line-height:"+g.offsetHeight+"px;";m.appendChild(p);if(k[j].selected){o.innerHTML=k[j].innerHTML}}f.appendChild(o);f.appendChild(m);f.appendChild(n);YEEPAY.addHandler(f,"click",function(i){YEEPAY.stopPropagation(i);document.documentElement.click();m.style.display="block"});YEEPAY.addHandler(document.documentElement,"click",function(i){m.style.display="none"});YEEPAY.addHandler(m,"click",function(i){YEEPAY.stopPropagation(i);target=YEEPAY.getTarget(i);o.innerHTML=target.innerHTML;k[target._index].selected="true";m.style.display="none"});g.parentNode.appendChild(f)}for(var b=0,a=c.length;b<a;b++){YEEPAY.addClass(c[b],"hidden");e(c[b])}return this},formatBankCard:function(b){var a=document.getElementById(b);YEEPAY.addHandler(document.getElementById(b),"keyup",function(f){if(f.keyCode!==8&&f.keyCode!==37&&f.keyCode!==39){var g=(a.value).replace(/[^\d]/g,"");var e=19;if(g.length<e){e=g.length}var c="";for(var d=0;d<e;d++){c=c+g.substring(d,d+1);if(d!=0&&(d+1)%4==0){c=c+" "}}a.value=c}});return this}};Form.init();