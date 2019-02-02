package com.yueqian.demo.model.menu;

//多个按钮组装成的菜单，我们又需要一个Menu类
public class Menu {
	//一级菜单
	private Button[] button;

	public Button[] getButton() {
		return button;
	}

	public void setButton(Button[] button) {
		this.button = button;
	}
	
	
}
