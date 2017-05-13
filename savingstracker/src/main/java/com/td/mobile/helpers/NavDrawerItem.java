package com.td.mobile.helpers;

 
public class NavDrawerItem {
     
    private String title;
    private Integer icon;
    private Class targetClass;
    private int backgroundColor;
    private boolean requiredLogin=true;
     
    public NavDrawerItem(){}
 
    public NavDrawerItem(String title, Integer icon, Class targetClass){
        this.title = title;
        this.icon = icon;
        this.targetClass=targetClass;
    }
     
    public NavDrawerItem(String title, Integer icon, Class targetClass, boolean requiredLogin){
    	this(title, icon, targetClass);
        this.requiredLogin = requiredLogin;
    }
    
    public NavDrawerItem(String title, Integer icon, Class targetClass, int backgroundColor){
    	this(title, icon, targetClass);
    	this.backgroundColor = backgroundColor;
    }
     
    public NavDrawerItem(String title, Integer icon, Class targetClass, boolean requiredLogin, int backgroundColor){
    	this(title, icon, targetClass, requiredLogin);
    	this.backgroundColor = backgroundColor;
    }
    
    public NavDrawerItem(String title){
        this.title = title;
    }
     
    public String getTitle(){
        return this.title;
    }
     
    public Integer getIcon(){
        return this.icon;
    }
     
    public void setTitle(String title){
        this.title = title;
    }
     
    public void setIcon(int icon){
        this.icon = icon;
    }
    
    public void setTargetClass(Class targetClass) {
    	this.targetClass = targetClass;
    }
    
    public Class getTargetClass() {
    	return targetClass;
    }

	public boolean isRequiredLogin() {
		return requiredLogin;
	}

	public void setRequiredLogin(boolean requiredLogin) {
		this.requiredLogin = requiredLogin;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
