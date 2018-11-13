/**
 * 
 */
package com.yeepay.g3.core.nccashier.service;

import java.util.List;
import java.util.Map;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.facade.cwh.enumtype.UserType;
import com.yeepay.g3.facade.cwh.param.BindCardDTO;
import com.yeepay.g3.facade.nccashier.dto.BankCardReponseDTO;
import com.yeepay.g3.facade.nccashier.dto.BussinessTypeResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.CardValidateRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CardValidateResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.OrderNoticeDTO;
import com.yeepay.g3.facade.nccashier.dto.PassCardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.Person;
import com.yeepay.g3.facade.nccashier.dto.SupportBanksResponseDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncmember.dto.GetUsableRespDTO;

/**
 * @author xueping.ni
 * @since：2016年11月21日 下午6:26:58
 *
 */
public interface CashierBindCardService {

	/**
	 * 获取原始的绑卡列表
	 * @param paymentRequest
	 * @return
	 */
	public List<BindCardDTO> getBindCardList(PaymentRequest paymentRequest);
	/**
	 * 获取是否默认设置同人限制信息
	 * @param paymentRequest
	 * @return
	 */
	public Map<String,Boolean> canSetSamePersonLimit(PaymentRequest paymentRequest);
	/**
	 * 获取默认的持卡人信息
	 * @param payRequest
	 * @return
	 */
	public Person getDefautRealPerson(PaymentRequest payRequest);
	/**
	 * 获取非本人的绑卡信息
	 * @param payRequest
	 * @return
	 */
	public List<BindCardDTO> getBindCardBelongsOthers(PaymentRequest payRequest);
	/**
	 * 获取本人信息
	 * @param paymentRequest
	 * @return
	 */
	public Person getRealPersonInfo(PaymentRequest paymentRequest);
	/**
	 * 解绑非本人的绑卡
	 * @param bindCards2Others
	 */
	public void unbindCards(List<BindCardDTO> bindCards2Others,PaymentRequest paymentRequest,String cause );
	

	/**
	 * 获取绑卡列表
	 * @param id
	 * @param external
	 * @return
	 */
	public List<BindCardDTO> getBindCardList(String id,
			UserType external);

	
	/**
	 * 过滤（获取）本人的绑卡
	 * @param bindCards
	 * @param person
	 */
	public List<BindCardDTO> filterCardBelongtoOne(List<BindCardDTO> bindCards,Person person);
	/**
	 * 获取非本人的绑卡（不包括证件和姓名为空的绑卡）
	 * @param bindCards
	 * @param person
	 */
	public List<BindCardDTO> filterCardBelongtoOther(List<BindCardDTO> bindCards,Person person);
	/**
	 * 获取绑卡列表的持卡人信息
	 * @param bindCards
	 * @param payRequest 
	 * @return
	 */
	public List<Person> getPersonFromBindList(List<BindCardDTO> bindCards, PaymentRequest payRequest);
	/**
	 * 解绑绑卡
	 * @param bindCard
	 * @param payRequest
	 * @param cause
	 */
	public void unbindCard(BindCardDTO bindCard, PaymentRequest payRequest,
			String cause);
	/**
	 * 设置同人限制值
	 * @param person
	 * @param payRequest
	 */
	public void setSamePersonLimit(Person person, PaymentRequest payRequest,String reason);
	
	/**
	 * 移除非本人的绑卡
	 * 
	 * @param bindCardlist
	 * @param person
	 */
	public void removeCardBelongtoOther(List<BindCardDTO> bindCardlist, Person person);
	
	/**
	 * 判断绑卡是否属于同一人（并且持卡人姓名和证件号都不为空）
	 * @param bindCardlist
	 * @return
	 */
	public List<BindCardDTO> judgeBindCardBelongToOne(List<BindCardDTO> bindCardlist);
	
	/**
	 * 直接从用户中心获取绑卡列表
	 * @param payRequest
	 * @return
	 */
	public List<BindCardDTO> getBindCardListFromUserCenter(PaymentRequest payRequest);
	
	/**
	 * 是否默认设置同人限制值
	 * @param payRequest
	 * @param bindCards
	 * @return
	 */
	public boolean canSetSamePersonLimit(List<BindCardDTO> bindCards);
	
	/**
	 * 从绑卡列表中获取持卡人信息
	 * @param bindCards
	 * @return
	 */
	public List<Person> getPersonFromBindList(List<BindCardDTO> bindCards);
	/**
	 * 绑卡
	 * @param paymentRequest
	 * @param paymentRecord
	 * @return 同步确认支付接口调用的绑卡，返回绑卡id
	 */
	public String bindCard(PaymentRequest paymentRequest,
			PaymentRecord paymentRecord);
	
	/**
	 * 对接用户中心，获取用户可用绑卡列表及外部用户授权问题 
	 * 
	 * @param payRequest
	 * @return
	 */
	public GetUsableRespDTO getUseableBindCardList(PaymentRequest payRequest);

	/**
	 * 根据绑卡id获取CardInfoDTO，用于一键支付或绑卡支付的二次支付下单
	 * @param bindId
	 * @return
	 */
	CardInfoDTO getCardInfoDTO(String bindId);

}
