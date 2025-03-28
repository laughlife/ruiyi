package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.service.ImageService;
import com.liwei.ruiyi.utils.DateUtils;
import com.liwei.ruiyi.utils.ReadProUtils;
import com.liwei.ruiyi.utils.StringUtils;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Repository("imageService")
public class ImageServiceImpl implements ImageService {

    private String imageServiceUrl = ReadProUtils.ReadProperties("imageServiceUrl");

    @Override
    public JSONObject createOrderImage(File convertFile, String name, List<Integer> list) {
        JSONObject j = new JSONObject();

        boolean readImageStatus = false;
        //首先判断图片是否解码成功，如果不能解码成功，直接返回失败代码
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(convertFile);
            readImageStatus = true;
        } catch (IOException e) {
            // 记录完整错误信息
            System.err.println("加载图片失败: " + e.getMessage());
            e.printStackTrace();
        }

        if (!readImageStatus) {
            j.put("code", -1);
            j.put("msg", "图片解码失败");
            return j;
        } else {
            j.put("code", 0);
            j.put("msg", "图片解码成功");

        }
        String fullUrl = imageServiceUrl + "temp/" + DateUtils.getSystemDate() + "/";
        String imageSavePath = convertFile.getAbsolutePath().substring(0, convertFile.getAbsolutePath().lastIndexOf(File.separator));
        JSONArray ja = new JSONArray();
        String mainPic = createMainImage(originalImage, imageSavePath, name); //主图
        ja.add(fullUrl + mainPic);

        for (int i = 0; i < list.size(); i++) {
            String subPic = createSubImage(originalImage, imageSavePath, name, list.get(i)); //副图
            ja.add(fullUrl + subPic);
        }

        j.put("data", ja);
        //底图
        return j;
    }

    /**
     * 生成主图
     *
     * @param originalImage 原始图片
     * @param imageSavePath 图片保存路径
     * @param name          商品名称
     * @return
     */
    private String createMainImage(BufferedImage originalImage, String imageSavePath, String name) {
        try {
            // 画布参数
            int canvasSize = 1000;       // 画布尺寸 1000x1000
            int imageHeight = 800;        // 固定图片高度
            Color textColor = Color.RED;  // 文字颜色

            // 1. 创建白色背景画布
            BufferedImage combinedImage = new BufferedImage(canvasSize, canvasSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = combinedImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, canvasSize, canvasSize);

            // 2. 智能图片缩放
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            // 计算缩放尺寸（固定高度800，宽度按比例）
            int scaledHeight = imageHeight;
            int scaledWidth = (int) (originalWidth * (imageHeight * 1.0 / originalHeight));

            // 执行高质量缩放
            BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = scaledImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
            g.dispose();

            // 3. 居中绘制图片（自动处理横竖版）
            int x = (canvasSize - scaledWidth) / 2; // 水平居中
            int y = 0;                             // 顶部对齐
            g2d.drawImage(scaledImage, x, y, null);

            // 4. 添加红色文字
            Font font = new Font("Microsoft YaHei", Font.BOLD, 120);
            g2d.setFont(font);
            g2d.setColor(textColor);

            // 计算文字位置（底部300px区域居中）
            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D textBounds = fm.getStringBounds(name, g2d);

            // 文字区域参数
            int textAreaHeight = 200;
            int textY = canvasSize - textAreaHeight;
            int textX = (canvasSize - (int) textBounds.getWidth()) / 2;
            int textBaselineY = textY + ((textAreaHeight - (int) textBounds.getHeight()) / 2) + fm.getAscent();

            // 绘制文字（带抗锯齿）
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawString(name, textX, textBaselineY);

            g2d.dispose();

            // 5. 保存图片
            String newFileName = StringUtils.getRandomString() + ".jpg";
            File output = new File(imageSavePath, newFileName);
            ImageIO.write(combinedImage, "jpg", output);

            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "error_" + System.currentTimeMillis();
        }
    }

    private String createSubImage(BufferedImage originalImage, String imageSavePath, String name, int number) {
        try {
            // 画布参数
            int canvasSize = 1000;       // 画布尺寸 1000x1000
            int rightPanelWidth = 200;    // 右侧数字区域宽度
            int imageHeight = 800;
            Color textColor = Color.RED;  // 文字颜色

            // 1. 创建白色背景画布
            BufferedImage combinedImage = new BufferedImage(canvasSize, canvasSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = combinedImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, canvasSize, canvasSize);

            // 2. 智能图片缩放（最大边800）
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            // 计算缩放比例（限制最大边为800）
            // 计算缩放尺寸（固定高度800，宽度按比例）
            int scaledHeight = imageHeight;
            int scaledWidth = (int) (originalWidth * (imageHeight * 1.0 / originalHeight));

            // 执行高质量缩放
            BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = scaledImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
            g.dispose();

            // 3. 居中绘制图片（自动处理横竖版）
            int x = (canvasSize - scaledWidth) / 2; // 水平居中
            int y = 0;                             // 顶部对齐
            g2d.drawImage(scaledImage, x, y, null);

            // 4. 添加底部文字（保持原逻辑）
            Font nameFont = new Font("Microsoft YaHei", Font.BOLD, 120);
            g2d.setFont(nameFont);
            g2d.setColor(textColor);

            // 底部文字区域参数
            int textAreaHeight = 200;
            int textY = canvasSize - textAreaHeight;

            // 计算底部文字位置
            FontMetrics nameFm = g2d.getFontMetrics();
            Rectangle2D nameBounds = nameFm.getStringBounds(name, g2d);
            int textX = (canvasSize - (int) nameBounds.getWidth()) / 2;
            int textBaselineY = textY + ((textAreaHeight - (int) nameBounds.getHeight()) / 2) + nameFm.getAscent();

            // 绘制底部文字
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawString(name, textX, textBaselineY);

            // 5. 添加右侧数量显示
            Font numberFont = new Font("Microsoft YaHei", Font.BOLD, 150); // 放大数字字体
            g2d.setFont(numberFont);

            // 右侧区域参数
            int numberAreaX = canvasSize - rightPanelWidth;
            int numberAreaY = 0;
            int numberAreaHeight = 800;

            // 计算数字位置
            String numberStr = String.valueOf(number);
            FontMetrics numberFm = g2d.getFontMetrics();
            Rectangle2D numberBounds = numberFm.getStringBounds(numberStr, g2d);

            // 水平居中计算
            int numberX = numberAreaX + (rightPanelWidth - (int) numberBounds.getWidth()) / 2;
            // 垂直居中计算
            int numberY = numberAreaY + (numberAreaHeight - (int) numberBounds.getHeight()) / 2 + numberFm.getAscent();

            // 绘制数字
            g2d.setColor(new Color(255, 255, 255, 150)); // 半透明背景
            g2d.fillRect(numberAreaX, numberAreaY, rightPanelWidth, numberAreaHeight);

            g2d.setColor(textColor);
            g2d.drawString(numberStr, numberX, numberY);

            g2d.dispose();

            // 6. 保存图片
            String newFileName = StringUtils.getRandomString() + ".jpg";
            File output = new File(imageSavePath, newFileName);
            ImageIO.write(combinedImage, "jpg", output);

            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "error_" + System.currentTimeMillis();
        }
    }
}
