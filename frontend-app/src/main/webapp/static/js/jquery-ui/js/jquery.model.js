(function($){  
    $.fn.modalInfowindow = function(options){  
        var defaults = {};  
        var options = $.extend(defaults, options);  
        var container=$(this);  
        var width=options.width, 
            height=options.height, 
            title=options.title, 
            content=options.content, 
            aftHideReload=options.aftHideReload;  
        //模态层容器  
        var modal=$("<div id='modal'></div>");  
        modal.css("width","100%");  
        modal.css("height","100%");  
        //模态层  
        var modal_div=$("<div class='modal'></div>");  
        modal_div.css("width","100%");  
        modal_div.css("height",document.body.scrollHeight);  
        //信息框  
        var infoWin=$("<div class='infowin'></div>");  
        infoWin.css("width",width+"px");  
        infoWin.css("height",height+"px");  
        infoWin.css("position","absolute");  
        infoWin.css("top",(container.height()-height)/2+"px");  
        infoWin.css("left",(container.width()-width)/2+"px");  
        //标题  
        var infoWin_title=$("<div class='title'></div>");  
        var infoWin_title_close=$("<div class='close'>X</div>")  
        $('.close').live("click",function(){  
            modal.remove();  
            if(aftHideReload)
            	location.href = location.pathname+"?_queryable=false";
        });  
        var infoWin_title_content=$("<div class='title_content'></div>")  
        infoWin_title_content.append(title);  
        infoWin_title.append(infoWin_title_close);  
        infoWin_title.append(infoWin_title_content);  
        //内容  
        var infoWin_content=$("<div class='content'></div>");  
        infoWin_content.append(content);  
        //信息框添加标题和内容  
        infoWin.append(infoWin_title);  
        infoWin.append(infoWin_content);  
        //模态层容器添加模态层和信息框  
        modal.append(modal_div);  
        modal.append(infoWin);  
        //将模态层添加到容器  
        container.append(modal);  
    }  
})(jQuery); 