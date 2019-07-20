package com.atguigu.atcrowdfunding.tmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.mapper.TMenuMapper;
@Service
public class TMenuServiceImpl implements TMenuService {

	@Autowired
	private TMenuMapper menuMapper;
	
	Logger logger = LoggerFactory.getLogger(TMenuServiceImpl.class);
	@Override
	public List<TMenu> listMenus() {
		//查询所有的菜单
		List<TMenu> menus = menuMapper.selectByExample(null);
		//挑出父菜单集合
		//List<TMenu> pMenus = new ArrayList<TMenu>();
		Map<Integer , TMenu> pMenuMap = new HashMap<Integer , TMenu>();
		for (TMenu tMenu : menus) {
			//如果pid为0就是父菜单
			if(tMenu.getPid()==0) {
				//pMenus.add(tMenu);
				pMenuMap.put(tMenu.getId(), tMenu);
			}
		}
		//将父菜单的子菜单设置到父菜单的子菜单集合中
		for (TMenu tMenu : menus) {
			//获取当前菜单的pid，如果pid在pMenus中存在元素(id)对应，则当前菜单就是对应的子菜单
			//tMenu.getPid()//希望使用pid去定位一个父元素，list不方便，map可以
			TMenu pMenu = pMenuMap.get(tMenu.getPid());//使用子菜单的pid去获取父元素
			if(pMenu!=null) {
				//获取到了父元素 ,将当前菜单设置到pMenu的子菜单集合中
				pMenu.getMenus().add(tMenu);
				
			}
		}
		logger.debug("查询菜单集合：{}", pMenuMap.values());
		//返回组装后的父菜单集合
		return new ArrayList<TMenu>(pMenuMap.values());
	}

}
