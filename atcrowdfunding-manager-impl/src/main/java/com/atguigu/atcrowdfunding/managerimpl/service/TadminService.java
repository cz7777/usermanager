package com.atguigu.atcrowdfunding.managerimpl.service;

import java.util.List;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.atguigu.atcrowdfunding.bean.TAdminExample;
import com.atguigu.atcrowdfunding.common.utils.MD5Util;
import com.atguigu.atcrowdfunding.mapper.TAdminMapper;

@Service
public class TadminService {
	
	@Autowired
	//这里封装的是执行SQL语句的crud方法
	TAdminMapper tAdminMapper;
	public TAdmin login(TAdmin tAdmin) {
		//给前端用户输入的密码信息进行加密
		String psw = MD5Util.digest(tAdmin.getUserpswd());
		
		//创建一个Tadminexample
		TAdminExample tAdminExample = new TAdminExample();
		//调用tAdminExample方法
		
		tAdminExample.createCriteria().andUsernameEqualTo(tAdmin.getLoginacct()).andUserpswdEqualTo(psw);
		//操作数据库，将操作数据库的对象放进去
		List<TAdmin> list = tAdminMapper.selectByExample(tAdminExample);
		if (list!=null&&list.size()==1) {
			return list.get(0);
			
		}
		else {
			return null;
		}
		
	}
}	
