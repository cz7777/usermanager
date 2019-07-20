package com.atguigu.atcrowdfunding.managerimpl.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.alibaba.druid.sql.visitor.functions.If;
import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.managerimpl.service.TadminService;
import com.atguigu.atcrowdfunding.tmenu.TMenuService;

import sun.tools.tree.IfStatement;

@Controller
public class TadminController {
	
	//调用service层属性
	@Autowired
	TadminService tadminService;
	//设置日志打印
	Logger logger = LoggerFactory.getLogger(TadminController.class);
	
	//异步处理注销请求的方法   必须返回json
		@ResponseBody
		@RequestMapping("/admin/logout")
		public String logout(HttpSession session) {
			logger.debug("退出登录方法");
			session.invalidate();
			logger.debug("退出登陆方法成功");
			return "ok";//让浏览器重定向到index页面
		}
		

	//定义一个登陆方法，接收表单提交的数据,设置管理员登陆请求/admin/login
	@RequestMapping("/login")
	public String doLogin(TAdmin tAdmin,HttpSession session,Model model) {
		logger.debug("进入登录方法", tAdmin);
		//controller层调用service层处理数据（service在数据库中走一遭,把前端用户输入提交给service层）交给controller层引用变量
		TAdmin loginAdmin = tadminService.login(tAdmin);
	logger.debug("进入方法完毕",tAdmin);
	if (loginAdmin!=null) {
		//进入登录方法后会判断是否登陆成功，成功与失败都会跳转到不同的页面
		//存入session域map
		session.setAttribute("tAdmin",loginAdmin);
		
		//成功则重定向到成功页面（使用重定向是因为防止转发会重复提交数据）
		//将响应发给用户,/main.html
		/*return "redirect:/admin/main";*/ //加了redirect：项目名//admin/main 
		return "admin/main";
		}
		/*
		 * <property name="prefix" value="/WEB-INF/views/"></property>
			<property name="suffix" value=".jsp"></property>
		 */
	
	
	//如果登录失败
	else {
		model.addAttribute("errorMsg", "密码或者用户名错误");
		return "login";
	}
	}
	/*@RequestMapping("/admin/main")
	public String login() {
		return "admin/main"; 
	}	可以注销的原因是下面的额跳转到main页面的起点和终点是一样的
	*/
	
	@Autowired
	TMenuService menuService;
	//处理跳转到  main页面的请求
		@RequestMapping("/admin/main")
		public String toMainPage(HttpSession session) {
			//查询所有的菜单，然后共享，再到main.jsp页面中获取显示菜单即可
			List<TMenu> menus = menuService.listMenus();
			//使用转发: 以后会结合redis缓存，针对所有用户都会查询的热门数据 提供缓存
			//存在session域中：
			session.setAttribute("menus", menus);
			//菜单表：
			//在main页面显示数据
			return "admin/main";
		}
}
