package com.xmbl.ops.base;


import java.time.LocalDate;

import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * @Author Noseparte
 * @Compile 2018年11月12日 -- 下午5:48:53
 * @Version 1.0
 * @Description		定义全局抽象类		
 */
@Slf4j
public class BaseController {

	// --------------------------------------------------------- params  

	public ModelAndView getModelAndView() {
		log.info("[ { 构建一个试图开始： } ],构建时间:--,{} ", LocalDate.now());
		return new ModelAndView();
	}
	
	
	
	
}
