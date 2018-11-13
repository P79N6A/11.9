/**
 * Created by Nature on 16/11/24.
 */

+(function(global){//ajax方法
    global.ajax = function(options){
        options = options || {};
        options.type = (options.type || "GET").toUpperCase();
        options.dataType = options.dataType || "json";
        var params = formatParams(options.data);
        	console.log(params);
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                var status = xhr.status;
                if ((status >= 200 && status < 300) || status == 304) {
                    options.success && options.success(xhr.responseText);
                } else {
                    options.fail && options.fail(status);
                }
            }
        };
        if (options.type == "GET") {
            xhr.open("GET", options.url + "?" + params, true);
            xhr.send(null);
        } else if (options.type == "POST") {
            xhr.open("POST", options.url, true);
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.send(params);
        }
    };
    function formatParams(data) {
        var arr = [];
        for (var name in data) {
            arr.push(encodeURIComponent(name) + "=" + encodeURIComponent(data[name]));
        }
        return arr.join("&");
    }
})(window);
