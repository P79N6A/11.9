// JavaScript Document
var popup = function (arg, otherArg, callback, thisArg) {
    var content = '';
    var hideCloseBtn = false; //是否显示关闭按钮
    var titleText = "提示";
    switch (arg) {
        case 0: //短信验证码
            content = '<div class="popCont"><p class="statement" id="statement">已向您的手机号<span class="fa">' + otherArg + '</span>发送了手机验证码，请查收</p><div class="mt25">验证码：<input id="verifycode" readonly maxlength="10" type="text" class="smsc" onfocus="hideCodeMsg()" /><span class="keepTime" id="keepTime"><i></i><span id="seconds">60秒后可重新获取</span></span></div><div class="marconta"><p id="errorverifycode" class="error none">请输入正确手机验证码</p><p id="reSmsVerifycodeFail" class="error none">重新获取验证码失败，请稍后重新获取</p><p id="confirmPayVerifycodeError" class="error none"></p><p><a id="popupSubmitBtn" href="javascript:popupSubmitBtn()" click-on="false"  class="greenBtn mb20"><i class="leftBorder"></i>确认支付</a></p></div></div>';
            break;
        case 1: //语音验证码
        	    if(sendSMSNo!=null){
               content = '<div class="popCont"><p class="statement" id="statement">'+ sendSMSNo +'为您播报语音验证码，请注意接听</p><div class="mt25">验证码：<input id="verifycode" readonly maxlength="10" type="text" class="smsc" onfocus="hideCodeMsg()" /><span class="keepTime" id="keepTime"><i></i><span id="seconds">60秒后可重新获取</span></span></div><div class="marconta"><p id="errorverifycode" class="error none">请输入正确手机验证码</p><p id="reSmsVerifycodeFail" class="error none">重新获取验证码失败，请稍后重新获取</p><p id="confirmPayVerifycodeError" class="error none"></p><p><a id="popupSubmitBtn" href="javascript:popupSubmitBtn()" click-on="false" class="greenBtn mb20"><i class="leftBorder"></i>确认支付</a></p></div></div>';}
        	    else{
        	       content = '<div class="popCont"><p class="statement"><span style="color: #0073C7;">语音</span>验证码播报中，请注意接听</p><div class="mt25">验证码：<input id="verifycode" readonly maxlength="10" type="text" class="smsc" onfocus="hideCodeMsg()" /><span class="keepTime" id="keepTime"><i></i><span id="seconds">60秒后可重新获取</span></span></div><div class="marconta"><p id="errorverifycode" class="error none">请输入正确手机验证码</p><p id="reSmsVerifycodeFail" class="error none">重新获取验证码失败，请稍后重新获取</p><p id="confirmPayVerifycodeError" class="error none"></p><p><a id="popupSubmitBtn" href="javascript:popupSubmitBtn()" click-on="false" class="greenBtn mb20"><i class="leftBorder"></i>确认支付</a></p></div></div>';
        	    }
        	    break;
        case 2: //安装控件
            titleText = "安装安全控件提示";
            content = '<div class="popCont"><p class="control f16 fb">为保证银行卡信息安全请安装该控件。</p><p class="install"><a href="#" class="greenBtn mb20"><i class="leftBorder"></i>下载并安装</a></p><p class="conther f12">安装完成后，请点击 <a href="#" target="_blank">刷新</a> 浏览器或重启浏览器</p><p class="conther mt10 mb50 f12">如果您无法完成安装，请点此 <a href="#" target="_blank">更换信用卡</a> 完成支付。</p></div>';
            break;
        case 3: //支付失败
            content = '<div class="popCont"><p class="payFail fb f16">支付失败</p><p class="wtx">有效期或卡背末三位输入错误，请重新输入</p><p class="ftb"><a href="javascript:closeAction()" class="greenBtn mb20"><i class="leftBorder"></i>确定</a></p></div>';
            break;
        case 4: //正在支付
            content = '<p class="waitPay"><span><img src="images/022.gif" height="15" class="mr10 vm" /></span>正在支付，请耐心等候...</p>';
            hideCloseBtn = true;
            break;
        case 5: //支付协议
            titleText = "易宝一键支付服务协议";
            var text = '<p class="tind">易宝一键支付服务协议（以下简称“本协议”）是您自愿与易宝支付有限公司（以下简称“易宝支付”）就易宝一键支付服务（以下简称“本服务”）的使用所签订的有效合约。您有权利不使用本服务；但如果您选择使用本服务，将视为您自愿与易宝支付达成此协议并接受本协议的全部条款和规则。</p><p class="tind">在您接受本协议之前，请仔细阅读协议内容，如果您不同意本协议的任何条款或规则，请不要使用本服务。</p>';
            content = '<div class="proContainer">' + text + '</div><p class="prob"><a href="javascript:checkProtocal();closeAction()" class="greenBtn mb20"><i class="leftBorder"></i>已阅读并同意该协议</a></p>';
            break;
        case 6: //不支持安装控件
            titleText = "安全控件提示";
            content = '<div class="popCont"><p class="nonsupport f14 fb">检测到您的系统不支持安装该控件，请使用信用卡或更换windows系统完成支付。</p><p class="changeCrited mt10 mb50 f12">点此 <a href="#" target="_blank">更换信用卡</a></p></div>';
            hideCloseBtn = true;
            break;
        case 7: //绑卡支付的短验框
            content = '<div class="popCont"><p class="statement none" id="statement"></p><div class="mt25">验证码：<input id="verifycode" readonly maxlength="10" type="text" class="smsc" onfocus="hideCodeMsg()"/><span class="keepTime enable" id="keepTime"><i></i><span id="seconds">获取验证码</span></span></div><div class="marconta"><p id="reSmsVerifycodeFail" class="error none">获取验证码失败，请稍后重新获取</p><p id="errorverifycode" class="error none">请输入正确手机验证码</p><p id="confirmPayVerifycodeError" class="error none"></p><p><a id="popupSubmitBtn" href="javascript:popupSubmitBtn()" click-on="false"  class="greenBtn mb20"><i class="leftBorder"></i>确认支付</a></p></div></div>';
            break;
        case 8: //中国银行快捷支付服务协议
            titleText = "中国银行快捷支付服务协议";
            var text = '<p>请客户认真阅读本协议文本。</p>' +
                '<p class="tind">1.中国银行股份有限公司借记卡快捷支付业务（以下简称“快捷支付业务”）是中国银行股份有限公司（以下简称“中国银行”）和易宝支付有限公司  公司（下称“合作支付机构”）联合为中国银行借记卡持卡客户（以下简称“客户”）提供的一项电子支付服务。凡是持有中国银行股份有限公司在中国大陆地区发行的借记卡的客户，均可申请该业务。</p>' +
                '<p class="tind">2.客户在签约快捷支付业务时，将收到中国银行发送的带有手机交易码的签约确认短信。客户应认真阅读本协议，在同意本协议约定内容的前提下输入手机交易码。客户输入手机交易码即视同为同意本协议内容。</p>' +
                '<p class="tind">3.客户通过互联网（Internet）在合作支付机构网站或客户端，选择在线与中国银行签订本服务协议，将指定的借记卡与此时登录的账户进行关联，开通快捷支付功能，并承担由此产生的一切责任。</p>' +
                '<p class="tind">4.客户同意，中国银行以合作支付机构传送至中国银行的户名（如与合作支付机构约定不校验姓名则删除）、卡号、手机号码、证件号码（如与合作支付机构约定不校验证件号码则删除）等身份要素作为确认客户身份、为客户开通快捷支付业务的唯一证据。中国银行比对户名（如与合作支付机构约定不校验姓名则删除）、卡号、手机号码、证件号码（如与合作支付机构约定不校验证件号码则删除）与客户在银行预留信息一致后，通过95566发送手机交易码至客户预留手机号上，并对合作支付机构传送的客户填写的手机交易码进行验证，验证一致通过后，完成客户指定的借记卡与此时所登录账户的关联。客户须保证在中国银行预留手机号码为本人手机号码并确保其准确性。基于提高交易安全的目的，中国银行有权变更上述身份要素。客户须妥善保管本人中国银行借记卡信息，本人身份信息、手机交易码、安全认证工具等敏感信息，不得将敏感信息向他人透露。因客户银行预留信息错误，或个人信息、手机等保管不善产生的风险及损失由客户本人承担。</p>' +
                '<p class="tind">5.开通快捷支付业务后，客户同意并授权中国银行在本协议约定的服务范围内，将其所关联借记卡的原有支付验证方式变更为按照合作支付机构发送的交易指令从客户指定的借记卡账户中支付相应款项。客户需在其绑定的借记卡的余额内进行支付，否则中国银行有权拒绝执行交易指令。</p>' +
                '<p class="tind">6.中国银行可根据业务发展需要，设置或修改支付限额，客户对此无异议，客户办理快捷支付业务时需同时受中国银行和合作支付机构设置的支付限额的约束。在任何情况下，支付金额不应超过中国银行或合作支付机构设置的支付限额（以较低者为准）。如实际支付金额大于规定的支付限额，中国银行将拒绝执行交易指令。</p>' +
                '<p class="tind">7.本协议项下，中国银行仅提供通过合作支付机构发生的支付结算服务。客户因通过合作支付机构购买商品而产生的一切关于商品或服务质量、商品交付，交易款项扣收与划付的争议均由客户与商户或商品/服务的实际销售商自行协商解决。</p>' +
                '<p class="tind">8.客户使用借记卡快捷支付业务应防范的风险包括但不限于：</p>' +
                '<p class="tind">（1）客户借记卡快捷支付使用的用户名、账户、密码等重要信息可能被其他人猜出、偷窥或通过木马病毒、假冒网站、欺诈电话或短信等手段窃取，从而导致客户信息泄露、资金损失、账户被恶意操作等后果。</p>' +
                '<p class="tind">（2）手机可能被盗或未经许可被他人使用，若手机银行密码同时被窃取，可能造成账户资金损失等情况；若客户更换手机号时未取消以原手机号开通的短信提醒服务，当原手机号被他人使用时，可能造成客户的账户信息泄露等情况。</p>' +
                '（3）客户使用设备的操作系统、杀毒软件、网络防火墙等客户端软件未及时更新或安装补丁程序，可能导致客户信息泄露、资金损失、账户被恶意操作等后果。</p>' +
                '<p class="tind">9.客户应采取风险防范措施，安全使用借记卡快捷支付业务，相关措施包括不限于：</p>' +
                '<p class="tind">（1）妥善保护借记卡快捷支付业务使用的用户名、账户、密码等重要身份识别标识、身份认证信息及工具，确保上述重要信息和认证工具不经口头或其它方式发生泄露或被盗用。</p>' +
                '<p class="tind">（2）对所使用设备的操作系统、杀毒软件、网络防火墙等客户端软件采取有效的措施（如及时更新系统或安装补丁程序等）防范操作系统软件漏洞风险、电子设备中的病毒等。</p>' +
                '<p class="tind">（3）妥善保管借记卡、手机及与借记卡快捷支付业务相关的重要身份证件等资料，不交给其他人保管，不放置在不安全场所。</p>' +
                '<p class="tind">（4）避免使用容易被其他人猜出或破解的密码（如：采用生日、电话号码等与个人信息相关的信息或具有规律性的数字或字母组合作为快捷支付业务密码），避免设置与其他用途相同的密码（如：采用电子邮件密码作为借记卡快捷支付密码）。客户应定期或不定期更新其密码。</p>' +
                '<p class="tind">（5） 应经常关注账户内资金变化，如发现任何通过中国银行借记卡快捷支付从事的未经客户许可的操作及交易，客户应马上与支付机构或中国银行取得联系。</p>' +
                '<p class="tind">10.借记卡快捷支付业务资金清算可能出现异常及差错交易，银行及合作支付机构将对异常及差错交易及时进行纠正处理。银行及合作支付机构将根据客户要求提供交易记录的时间和方式等信息。</p>' +
                '<p class="tind">11.客户不得利用中国银行快捷支付业务进行虚假交易、洗钱、诈骗、恐怖融资等行为，且有义务配合中国银行进行相关调查，一旦客户拒绝配合进行相关调查或中国银行认为客户存在或涉嫌虚假交易、洗钱或任何其他非法活动、欺诈或违反诚信原则的行为、或违反本协议约定的，中国银行有权采取以下一种、多种或全部措施：</p><p>（1）暂停或终止提供本协议项下快捷支付业务服务；</p><p>（2）终止本协议；</p><p>（3）取消客户的用卡资格。</p>' +
                '<p class="tind">12.客户同意中国银行就快捷支付业务的开展，向支付机构提供客户留存在中国银行的户名（如与支付机构约定不校验姓名则去掉）、银行卡号、证件号码（如与支付机构约定不校验证件号码则去掉）、手机号码等信息。</p>' +
                '<p class="tind">13.客户可以登录中国银行官方网站撤销本人名下指定借记卡与支付机构账户关联的快捷支付功能。</p>' +
                '<p class="tind">14.中国银行系统升级、业务变化、收费变更，或根据业务发展需要修改本协议，中国银行将提前进行公告。若客户有异议，有权选择撤销相关服务，若客户选择继续接受该服务的，视为客户同意并接受相关业务或协议按变更或修改后的内容执行。双方同意，本协议所称公告均指在中国银行网点、中国银行网站【http://www.boc.cn】、电子银行渠道等进行公告。</p>' +
                '<p class="tind">15.选择打勾的客户，视同已通读上述条款，对条款的内容及其后果已充分理解，对所有内容均无异议。</p>' +
                '<p class="tind">16.本协议在客户于支付机构提供的页面服务协议栏位打勾并提交后生效。本协议的生效、履行及解释均使用中华人民共和国法律（为本协议之目的，不含中国香港、澳门、台湾地区法律）。本协议项下的争议向中国银行所在地人民法院起诉。</p>' +
                '<p class="tind">合同使用说明：本合同为个人支付客户使用中国银行借记卡快捷支付服务时与中国银行签订的服务协议。中国银行借记卡快捷支付合作支付机构应在支付客户签约中国银行借记卡快捷支付服务时向客户展示该协议，客户阅签该协议后方可签约借记卡快捷支付服务。</p>';
            content = '<div class="proContainer">' + text + '</div><p class="prob"><a href="javascript:checkProtocal();closeAction()" class="greenBtn mb20"><i class="leftBorder"></i>已阅读并同意该协议</a></p>';
            break;
        default:
            console.log("参数传入失败");
            return;
    }
    var hd = '<span title="关闭" class="close" id="J-close" onclick="closeAction()"></span><div class="hd fb">' + titleText + '</div>';
    var rootNode = document.createElement("div");
    rootNode.className = "popup";
    rootNode.id = "J-popup";
    rootNode.innerHTML = hd + content;
    document.body.appendChild(rootNode);
    YEEPAY.popping("J-popup", true);
    if (hideCloseBtn) {
        rootNode.removeChild(YEEPAY.get("J-close"));
    }
    if (typeof callback == "function") {
        callback.call(thisArg);
    }


    function timing(elem, t, callback, context) {//倒计时
        /**
         * @param [elem] DOM 显示倒计时的节点
         * @param [t] number 倒计时长
         * @param [callback] function 回调函数
         * @param [context] object 回调函数上下文
         * */
        var timeId = null;
        YEEPAY.removeHandler(document.getElementById("keepTime"), "click", requestSms);
        YEEPAY.removeHandler(document.getElementById("keepTime"), "click", timingBridge);
        if (document.getElementById("verifycode") != null)document.getElementById("verifycode").readOnly = false;
        timeId = setTimeout(function () {
            if (t > 0) {
                elem.innerHTML = t + "秒后可重新获取";
                t--;
                timeId = setTimeout(arguments.callee, 1000);
            } else {
                t = 0;
                clearTimeout(timeId);
                typeof callback == "function" ? callback.call(context) : false;
            }
        }, 0);
    }

    function timingBridge() {
        YEEPAY.removeClass(keepTime, "enable");
        timing(seconds, 60, timingCallback);
    }

    function timingCallback() {
        YEEPAY.addClass(keepTime, "enable");
        seconds.innerHTML = "重新获取";
        YEEPAY.addHandler(keepTime, "click", requestSms);
        YEEPAY.addHandler(keepTime, "click", timingBridge);
    }

    if (arg == 0 || arg == 1) {
        var seconds = document.getElementById("seconds");
        var keepTime = document.getElementById("keepTime");
        timing(seconds, 60, timingCallback);
    }
    if (arg == 7) {
        var seconds = document.getElementById("seconds");
        var keepTime = document.getElementById("keepTime");
        YEEPAY.addHandler(keepTime, "click", requestSms);
        YEEPAY.addHandler(keepTime, "click", timingBridge);
    }
}
function closeAction() { //关闭弹出层
    YEEPAY.addHandler(YEEPAY.get("J-close"), "click", function () {
        YEEPAY.closed("J-popup");
    });
    YEEPAY.get("J-close").click();
}

