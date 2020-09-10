package cn.dsx.test;

import cn.dsx.utils.ImageUtils;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

public class ImageTest {
    public static void main(String[] args) throws IOException {
        testInputBase64();
    }

    /**
     * base64 添加水印
     * @throws IOException
     */
    public static void testInputBase64() throws IOException {
        File directory = new File("src/main/resources");
        String courseFile = directory.getCanonicalPath();
        String imagePath = courseFile + "/static/image";
        String exportPath = courseFile + "/static/image/export";
        File imageFileDir = new File(imagePath);
        if (!imageFileDir.exists()) {
            imageFileDir.mkdir();
        }
        File imageexportDir = new File(exportPath);
        if (!imageexportDir.exists()) {
            imageexportDir.mkdir();
        }
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long id = snowflake.nextId();
        String imageName = id + ".png";
        Font font = new Font("宋体", Font.PLAIN, 16);
        String srcImgPath = imagePath + "\\template.png";
        String tarImgPath = exportPath + "\\" + imageName;
        String watermarkContent = "豆绍勋的签名";
        Color color = new Color(192, 192, 192, 128);


        String imageToBase64 = ImageUtils.getImgBase64(new File(srcImgPath));

        String base64Img = ImageUtils.addWatermark(imageToBase64, watermarkContent, color, font);

        //String base64Img = ImageUtils.addWatermark(srcImgPath, tarImgPath, watermarkContent, color, font);
        System.out.println(base64Img);

        byte[] bytes = Base64.decodeBase64(base64Img);
        File target = new File(exportPath, imageName);
        OutputStream os = new FileOutputStream(target);
        os.write(bytes);
        os.flush();
        os.close();

    }



    /**
     * base64
     * @throws IOException
     */
    public static void testBase64ToImg() throws IOException {
        File directory = new File("src/main/resources");
        String courseFile = directory.getCanonicalPath();
        String imagePath = courseFile + "/static/image";
        String exportPath = courseFile + "/static/image/export";
        File imageFileDir = new File(imagePath);
        if (!imageFileDir.exists()) {
            imageFileDir.mkdir();
        }
        File imageexportDir = new File(exportPath);
        if (!imageexportDir.exists()) {
            imageexportDir.mkdir();
        }
        //获取图片文件
        File srcImgfile = new File(imagePath + "\\template.png");

        String imageToBase64 = ImageUtils.getImgBase64(srcImgfile);

        byte[] bytes = Base64.decodeBase64(imageToBase64);
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long id = snowflake.nextId();

        String imageName = id + ".png";
        File target = new File(exportPath, imageName);
        OutputStream os = new FileOutputStream(target);
        os.write(bytes);
        os.flush();
        os.close();
    }




}
