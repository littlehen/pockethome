package com.yueqian.demo.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.swing.ImageIcon;

public class ImgUtil {
	
//	 public static void main(String[] args) throws ParseException {
//
//	        String bigImg = "images/myshare.jpg";
//	        String content = "邀请码:666666";
//	        String outPath = "C:\\Users\\WuJiJin\\Desktop\\" + System.currentTimeMillis() + ".jpg";
//	        try {
//	            bigImgAddSmallImgAndText(bigImg,content, 145, 508, outPath);
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//
//	    }

	    private static String getFileTemplatePath() {
	        String result = "";
	        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	        File file = new File(path);
	        result = file.getParent();
	        result = result.replace("%20", " ");
	        //if (result.toLowerCase().indexOf("bin") >= 0)
//	        System.out.println(result);
	        System.out.println("路径1:"+path);
	        System.out.println("路径2:"+result);
	        return result;
	        //return result + "/bin";
	    }
	    
	 /***
     * 在一张大图张添加小图和文字
     * @param bigImgPath 大图的路径
     * @param content   文字内容
     * @param cx    文字在大图上x抽位置
     * @param cy    文字在大图上y抽位置
     * @param outPathWithFileName 结果输出路径
	 * @return 
     */
    public static BufferedImage bigImgAddSmallImgAndText(String bigImgPath
            , String content, int cx, int cy
           // , String outPathWithFileName
            ) throws IOException {
    	System.out.println("------bigImgAddSmallImgAndText 我进来了---------");
        //主图片的路径
        InputStream is = new FileInputStream(new File(getFileTemplatePath() + "/" + bigImgPath));
        //通过JPEG图象流创建JPEG数据流解码器
        JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
        //解码当前JPEG数据流，返回BufferedImage对象
        BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
        //得到画笔对象
        Graphics g = buffImg.getGraphics();


        //最后一个参数用来设置字体的大小
        if (content != null) {
            Font f = new Font("宋体", Font.PLAIN, 25);
            Color mycolor = Color.WHITE;//new Color(0, 0, 255);
            g.setColor(mycolor);
            g.setFont(f);
            g.drawString(content, cx, cy); //表示这段文字在图片上的位置(cx,cy) .第一个是你设置的内容。
        }
        g.dispose();
        //OutputStream os = new FileOutputStream(outPathWithFileName);
        //创键编码器，用于编码内存中的图象数据。
        //JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
        //en.encode(buffImg);
        is.close();
        //os.close();
        System.out.println("----------bigImgAddSmallImgAndText 我走了-----------");
        return buffImg;
    }
}
