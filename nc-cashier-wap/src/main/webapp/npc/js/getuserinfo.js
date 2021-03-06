$(document).ready(function() {
	function detectOS() {
		var sUserAgent = navigator.userAgent;
		var isWin = (navigator.platform == "Win32")
				|| (navigator.platform == "Windows");
		var isMac = (navigator.platform == "Mac68K")
				|| (navigator.platform == "MacPPC")
				|| (navigator.platform == "Macintosh")
				|| (navigator.platform == "MacIntel");
		if (isMac)
			return "Mac";
		var isUnix = (navigator.platform == "X11")
				&& !isWin && !isMac;
		if (isUnix)
			return "Unix";
		var isLinux = (String(navigator.platform).indexOf(
				"Linux") > -1);
		if (isLinux)
			return "Linux";
		if (isWin) {
			if(sUserAgent.indexOf("Win") > -1){
				return "Windows";
			}
		}
		return "other";
	}
	
	$("#systemType").attr("value", detectOS());
})