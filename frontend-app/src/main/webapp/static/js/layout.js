$(document).ready(function () {
	setInterval(function(){
		$('#bodyContent').css({'height':$(window).height()-92+'px'});
	},200);
	//alert($(window).height());
	
	//权限控制插件
	$('#leftPwOpts').live('click',function(event){
		var currOption=$(event.target);
		if(currOption.is(':not(li)')) return false;
		var paramOpts=art.dialog({
			title:'权限设置',
			fixed:true,
			lock:true,
			ok:function(){
				currOption.unbind('click');
				$('#rightPwOpts').append(currOption);
				
			},
			okVal:'添加权限',
			cancel:true,
			opacity:0.1
			
		});
		console.log(currOption.attr('id'));
		
		paramOpts.content(
			'<div class="param_opts">'+
				'<p><input type="checkbox" id="param1"><label for="param1">参数1</label></p>'+
				'<p><input type="checkbox" id="param2"><label for="param2">参数2</label></p>'+
				'<p><input type="checkbox" id="param3"><label for="param3">参数3</label></p>'+
				'<p><input type="checkbox" id="param4"><label for="param4">参数4</label></p>'+
			'</div>');
		
	});
	$('#rightPwOpts').live('click',function(event){
		var currOption=$(event.target);
		if(currOption.is(':not(li)')) return false;
		var paramOpts=art.dialog({
			title:'权限设置',
			fixed:true,
			lock:true,
			button:[
				{
					name:'移除权限',
					callback:function(){
						currOption.unbind('click');
						$('#leftPwOpts').append(currOption);
					},
					focus:true
				},
				{
					name:'修改权限',
					callback:function(){
						art.dialog({
							icon: 'succeed',
							content: '修改成功！',
							time:2,
							title:false
						});
						
						
					}
				}
			],
			cancel:true,
			opacity:0.1
		});
		paramOpts.content(
			'<div class="param_opts">'+
				'<p><input type="checkbox" id="param1"><label for="param1">参数1</label></p>'+
				'<p><input type="checkbox" id="param2"><label for="param2">参数2</label></p>'+
				'<p><input type="checkbox" id="param3"><label for="param3">参数3</label></p>'+
				'<p><input type="checkbox" id="param4"><label for="param4">参数4</label></p>'+
			'</div>');
	});
	
	$('.power_options ul li').hover(function(){
		$(this).find('.options').show();
	},
	function(){
		$(this).find('.options').hide();
	}).click(function(){
		$(this).addClass('active').siblings().removeClass('active');
	});
	
	
});
