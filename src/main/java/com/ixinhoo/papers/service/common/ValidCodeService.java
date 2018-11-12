package com.ixinhoo.papers.service.common;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.chunecai.crumbs.code.util.number.CodeNumberMaker;
import com.chunecai.crumbs.code.util.key.DesEncodes;
import com.chunecai.crumbs.code.util.key.Encodes;
import com.google.common.base.Strings;
import com.ixinhoo.papers.dto.common.ValidCodeImageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

@Service
@Transactional(readOnly = true)
public class ValidCodeService {
    private static final Logger logger = LoggerFactory.getLogger(ValidCodeService.class);
    private static final int NUMBER_OF_CHS = 4;
    private static final int IMG_WIDTH = 65;
    private static final int IMG_HEIGHT = 25;

//    @Value("${des.key}")
    private String desKey="2018paperscicipaperscicipaperscici";

    public DetailDto<ValidCodeImageDto> makeImageCodeData() {
        DetailDto<ValidCodeImageDto> dto = new DetailDto<>(true);
        ValidCodeImageDto validCodeImageDto = new ValidCodeImageDto();
        String code = CodeNumberMaker.getInstance().randomNumCode(true, NUMBER_OF_CHS);
        //验证码加密传输
        byte[] key = Encodes.decodeBase64(desKey);
        try {
            validCodeImageDto.setData(Encodes.encodeBase64((DesEncodes.des3EncodeECB(key, code.getBytes("UTF-8" )))));
        } catch (Exception e) {
            logger.error("数据加密异常:{}", e);
        }
        BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);    // 实例化BufferedImage
        Graphics g = image.getGraphics();
        //TODO cici 由于linux系统的字体原因，单独设置使用的字体
        //定义字体样式
//        Font mfont=new Font("宋体",Font.BOLD,40);
//        //设置字体
//        g.setFont(mfont);
        Color c = new Color(200, 200, 255);                                             // 验证码图片的背景颜色
        g.setColor(c);
        g.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);  // 图片的边框
        Random r = new Random();
        for (int i = 0; i < NUMBER_OF_CHS; i++) {
            char index = code.charAt(i);
            g.setColor(new Color(r.nextInt(88), r.nextInt(210), r.nextInt(150)));       // 随机一个颜色
            g.drawString(index + "", 15 * i + 3, 18);                              // 画出字符
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
            byte[] bytes = baos.toByteArray();
            String base64Image = Encodes.encodeBase64(bytes).trim();//返回Base64编码过的字节数组字符串// 向页面输出图像
            validCodeImageDto.setImage(base64Image);
            dto.setDetail(validCodeImageDto);
        } catch (IOException e) {
            logger.error("生成错误:{}", e);
        }
        return dto;
    }

    public StatusDto validImageCodeByData(String data, String code) {
        StatusDto dto = new StatusDto(false);
        if (Strings.isNullOrEmpty(data)) {
            dto.setMsg("data为空");
        } else if (Strings.isNullOrEmpty(code)) {
            dto.setMsg("验证码为空");
        } else {
            //验证码加密传输
            byte[] key = Encodes.decodeBase64(desKey);
            try {
                String codeDes = new String(DesEncodes.des3DecodeECB(key,Encodes.decodeBase64(data)), "UTF-8");
                // 增加万能验证码0822，方便测试
                if (code.equals(codeDes) || code.equals("0822")) {
                    dto.setStatus(true);
                } else {
                    dto.setMsg("验证码错误");
                }
            } catch (Exception e) {
                logger.error("数据解密异常:{}", e);
            }
        }
        return dto;
    }
    public static void main(String[] args){
        System.out.println(JsonMapper.nonDefaultMapper().toJson(new ValidCodeService().makeImageCodeData()));
    }
}