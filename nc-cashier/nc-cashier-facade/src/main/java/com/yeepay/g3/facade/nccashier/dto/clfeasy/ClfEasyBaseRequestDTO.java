package com.yeepay.g3.facade.nccashier.dto.clfeasy;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-10-17 15:56
 **/

import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.utils.common.StringUtils;

/**
 *
 * @description:
 *
 * @author: jimin.zhou
 *
 * @create: 2018-10-17 15:56
 **/


public class ClfEasyBaseRequestDTO {

    /** 用户标识号 */
    private String userNo;

    /** 用户标识类型 */
    private String userType;

    /** 用户IP */
    private String userIp;

    /** 分期期数 */
    private String period;

	/*卡信息如下 */
    /** 卡号 */
    private String cardno;

    /** 持卡人姓名 */
    private String name;

    /** 身份证号 */
    private String idno;

    /** 银行预留手机号 */
    private String phone;

    /** CVV */
    private String cvv2;

    /** 有效期 */
    private String valid;

    /** 补充项：取款密码 */
    private String pass;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * 将入参中的卡信息六项，转换成CardInfoDTO返回
     * @return
     */
    public CardInfoDTO getCardInfoDTO(){
        if(StringUtils.isBlank(getIdno()) && StringUtils.isBlank(getName()) && StringUtils.isBlank(getPhone()) &&
                StringUtils.isBlank(getCvv2()) && StringUtils.isBlank(getValid()) && StringUtils.isBlank(getPass())){
            return null;
        }
        CardInfoDTO cardInfoDTO = new CardInfoDTO();
        cardInfoDTO.setIdno(getIdno());
        cardInfoDTO.setName(getName());
        cardInfoDTO.setPhone(getPhone());
        cardInfoDTO.setCvv2(getCvv2());
        cardInfoDTO.setValid(getValid());
        cardInfoDTO.setPass(getPass());
        return cardInfoDTO;
    }


    /**
     * 清空入参中的卡信息，用于不需要提交补充项而调用方传入了补充项时
     */
    public void cleanCardInfo(){
        setIdno(null);
        setName(null);
        setPhone(null);
        setCvv2(null);
        setValid(null);
        setPass(null);
    }

}
