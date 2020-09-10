package cn.dsx.utils;


import cn.hutool.core.convert.Convert;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Objects;


public class ImageUtils {



    /**
     * @param base64Image      base64编码
     * @param watermarkContent 水印内容
     * @param color            水印颜色
     * @param font             水印字体
     * @return 带水印图片的base64编码
     * @throws IOException
     */
    public static String addWatermark(String base64Image, String watermarkContent, Color color, Font font) throws IOException {
        String imageToBase64 = "";
        FileOutputStream outputStream = null;
        Image srcImg = null;
        Graphics2D graphics = null;
        try {
            //转换成图片
            BufferedImage bufferedImage = base64ToBufferedImage(base64Image);
            srcImg = bufferedImage;

            //获取图片的宽和高
            int srcImgwidth = bufferedImage.getWidth();
            int srcImgheight = bufferedImage.getHeight();

            //画水印需要一个画板    创建一个画板
            bufferedImage =  new BufferedImage(srcImgwidth, srcImgheight, BufferedImage.TYPE_INT_BGR);

            //创建一个2D的图像
            graphics = bufferedImage.createGraphics();

            // 下面两行代码 解决透明背景变黑问题
            bufferedImage = graphics.getDeviceConfiguration().createCompatibleImage(srcImgwidth, srcImgheight, Transparency.TRANSLUCENT);
            graphics = bufferedImage.createGraphics();

            //画出来
            graphics.drawImage(srcImg, 0, 0, srcImgwidth, srcImgheight, null);

            //设置水印的颜色
            graphics.setColor(color);

            //设置水印的字体
            graphics.setFont(font);

            //设置水印坐标
            int x = srcImgwidth * 19 / 20 - getwaterMarkLength(watermarkContent, graphics);
            int y = srcImgheight * 9 / 10;

            //根据获取的坐标 在相应的位置画出水印
            graphics.drawString(watermarkContent, x, y);

            //释放画板的资源
            graphics.dispose();

            //输出新的图片
            //outputStream = new FileOutputStream(tarImgPath);

            // 生成base64编码
            imageToBase64 = BufferedImageToBase64(bufferedImage);

            ////创建新的图片
            //ImageIO.write(buffImg, "png", outputStream);


        } finally {
            if (outputStream != null) {
                //刷新流
                outputStream.flush();
                //关闭流
                outputStream.close();
            }
        }
        return imageToBase64;
    }





    /**
     * @param srcImgPath       原图片的路径
     * @param tarImgPath       新图片的路径
     * @param watermarkContent 水印的内容
     * @param color            水印的颜色
     * @param font             水印的字体
     */
    public static String addWatermark(String srcImgPath, String tarImgPath, String watermarkContent, Color color, Font font) throws IOException {
        String imageToBase64 = "";
        FileOutputStream outputStream = null;
        File srcImgfile = null;
        Image srcImg = null;
        Graphics2D graphics = null;
        try {
            //获取图片文件
            srcImgfile = new File(srcImgPath);

            //把文件转换成图片
            srcImg = ImageIO.read(srcImgfile);

            //获取图片的宽和高
            int srcImgwidth = srcImg.getWidth(null);
            int srcImgheight = srcImg.getHeight(null);

            //画水印需要一个画板    创建一个画板
            BufferedImage buffImg = new BufferedImage(srcImgwidth, srcImgheight, BufferedImage.TYPE_INT_BGR);

            //创建一个2D的图像
            graphics = buffImg.createGraphics();

            // 下面两行代码 解决透明背景变黑问题
            buffImg = graphics.getDeviceConfiguration().createCompatibleImage(srcImgwidth, srcImgheight, Transparency.TRANSLUCENT);
            graphics = buffImg.createGraphics();

            //画出来
            graphics.drawImage(srcImg, 0, 0, srcImgwidth, srcImgheight, null);

            //设置水印的颜色
            graphics.setColor(color);

            //设置水印的字体
            graphics.setFont(font);

            //设置水印坐标
            int x = srcImgwidth * 19 / 20 - getwaterMarkLength(watermarkContent, graphics);
            int y = srcImgheight * 9 / 10;

            //根据获取的坐标 在相应的位置画出水印
            graphics.drawString(watermarkContent, x, y);

            //释放画板的资源
            graphics.dispose();

            //输出新的图片
            //outputStream = new FileOutputStream(tarImgPath);

            // 生成base64编码
            imageToBase64 = BufferedImageToBase64(buffImg);

            ////创建新的图片
            //ImageIO.write(buffImg, "png", outputStream);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                //刷新流
                outputStream.flush();
                //关闭流
                outputStream.close();
            }
        }
        return imageToBase64;
    }

    /**
     * 获取水印的坐标
     *
     * @param watermarkContent 水印内容
     * @param g                2d图像
     * @return 水印的长度
     */
    public static int getwaterMarkLength(String watermarkContent, Graphics2D g) {
        Font font = g.getFont();
        char[] data = watermarkContent.toCharArray();
        int length = watermarkContent.length();
        FontMetrics fontMetrics = g.getFontMetrics(font);
        return fontMetrics.charsWidth(data, 0, length);

    }

    /**
     * base64 编码转换为 BufferedImage
     *
     * @param bufferedImage
     * @return
     */
    public static String BufferedImageToBase64(BufferedImage bufferedImage) {
        // io流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 写入流中
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 转换成字节
        byte[] bytes = baos.toByteArray();
        Base64.Encoder encoder = Base64.getEncoder();

        // 转换成base64串
        String png_base64 = encoder.encodeToString(bytes);
        // 删除 \r\n
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");
        return png_base64;
    }

    /**
     * base64 编码转换为 BufferedImage
     *
     * @param base64
     * @return
     */
    public static BufferedImage base64ToBufferedImage(String base64) {
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            byte[] bytes1 = decoder.decode(base64);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getImgBase64(File srcImgfile) throws IOException {
        InputStream in = new FileInputStream(srcImgfile);
        byte[] data = new byte[in.available()];

        in.read(data);
        in.close();
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        String encode = encoder.encode(Objects.requireNonNull(data));
        String imageToBase64 = encode.replaceAll("\n", "").replaceAll("\r", "");
        return imageToBase64;
    }

    public static Image Base64ToImg(String base64str) throws IOException {
        BASE64Decoder encoder = new BASE64Decoder();
        byte[] bytes = encoder.decodeBuffer(base64str);
        return Toolkit.getDefaultToolkit().createImage(bytes, 0, bytes.length);
    }
}
