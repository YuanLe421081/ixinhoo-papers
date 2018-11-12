package com.ixinhoo.papers.dto.common;


/**
 * 验证码dto
 *
 * @author 448778074@qq.com (cici)
 */
public class ValidCodeImageDto implements java.io.Serializable{
    private String data;//校验的数据
    private String image;//图片的base64编码

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
