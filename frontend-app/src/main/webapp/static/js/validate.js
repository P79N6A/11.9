function validateTime(start,end,max){
    var  days,dateStart,dateEnd;
    dateStart=new Date(start);
    dateEnd=new Date(end);
    if(!compareDate(dateStart,dateEnd))
        return false;
    if(Math.abs(dateEnd-dateStart)>(max*1000*60*60*24)){
        MessageBoxExt.alert("时间间隔不能超过"+max+"天");
        return false;
    }
    return true;
}

function compareDateByStr(start,end){
    var  dateStart,dateEnd;
    if(isBlank(start)){
        MessageBoxExt.alert("开始时间不能为空");
        return false;
    }
    if(isBlank(end)){
        MessageBoxExt.alert("结束时间不能为空");
        return false;
    }
    dateStart=new Date(start);
    dateEnd=new Date(end);
    if(!compareDate(dateStart,dateEnd))
        return false;
    return true;
}

function compareDate(dateStart,dateEnd){
    if(dateStart>dateEnd){
        MessageBoxExt.alert("结束日期不能小于开始日期!");
        return false;
    }
    return true;
}

function isBlank(str){
    if (str==null||str==""||trimStr(str)=='') {
        return true;
    }
    return false;
}

function trimStr(str) {
    if(str==null)
        return null;
    var result = "";
    for(var i=0;i<str.length;i++){
        if(str[i]!=" ")
            result += str[i];
    }
    return result;
}

function checkNum(item) {
    if(isNaN(item.value)){
        showMsg("请输入数字!");
        item.value = "";
    }
}