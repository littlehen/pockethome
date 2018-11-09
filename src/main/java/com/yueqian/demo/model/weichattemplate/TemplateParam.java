package com.yueqian.demo.model.weichattemplate;


/**
 * Created by wujijin on 2018/8/7.
 *
 */
public class TemplateParam {
    private String name;
    private String value;
    private String color;

    public TemplateParam(){
        super();
    }

    public TemplateParam(String name,String vlaue,String color){
        this.name = name;
        this.value = vlaue;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString(){
        return "TemplateParam [name=" + name + ", value=" + value + ", color=" + color + "]";
    }
}
