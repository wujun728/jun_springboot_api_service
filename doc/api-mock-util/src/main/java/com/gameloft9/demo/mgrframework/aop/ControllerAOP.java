package com.gameloft9.demo.mgrframework.aop;

import com.gameloft9.demo.mgrframework.beans.response.IResult;
import com.gameloft9.demo.mgrframework.utils.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 处理统一返回、参数校验
 * @author gameloft9 2017-11-10
 */
@Slf4j
@Aspect
@Component
public class ControllerAOP {

	/**
	 * 创建返回切点,这里只切返回IResult的方法，没有切返回String和void的方法。
	 * */
	@Pointcut("execution(public com.gameloft9.demo.mgrframework.beans.response.IResult *(..))")
	public void resultPtCut() {}

	/**
	 * 环绕通知处理
	 * */
	@Around("resultPtCut()")
	public Object handlerControllerMethods(ProceedingJoinPoint pjp) throws Throwable{
		long startTime = System.currentTimeMillis();
		log.info(pjp.getSignature()+"请求参数:{}",pjp.getArgs());
		IResult result;//业务返回结果

		CheckUtil.checkModel(pjp);//校验model上面的参数

		result = (IResult) pjp.proceed();//业务处理

		log.info("返回结果:{}",result);
		log.info(pjp.getSignature() + "处理耗费时间:" + (System.currentTimeMillis() - startTime)+"ms");
		return result;
	}

}
