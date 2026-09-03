package com.zapi.support.event;

import com.zapi.entity.StampSealRequest;
import org.springframework.context.ApplicationEvent;

/***
 * 签章事件
 * @author zhonglz
 *
 */
public class SealSignDataEvent extends ApplicationEvent{

	private StampSealRequest stampSealRequest;
	
	public SealSignDataEvent(Object source, StampSealRequest stampSealRequest) {
		super(source);
		this.stampSealRequest = stampSealRequest;
	}


	public StampSealRequest getStampSealRequest() {
		return stampSealRequest;
	}


	public void setStampSealRequest(StampSealRequest stampSealRequest) {
		this.stampSealRequest = stampSealRequest;
	}
	
	

}
