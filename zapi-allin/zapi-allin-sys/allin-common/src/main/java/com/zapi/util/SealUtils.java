package com.zapi.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;


/*****
 * 印章生成
 * @author xiaozhong
 *
 */
public class SealUtils {

	
	private static final int width = 320;// 图片宽度
	private static final int height = 160;// 图片高度
	public static void main(String[] args) throws Exception {
		
		//方章1
//		ResultUtils info = sealFanz03(80, "张三");
//		if(info.getCode()==ResultUtils.CODE_SUCCESS){
//			BufferedImage fz1 =(BufferedImage) info.getObject();
//			try {
//				String filePath = "C:\\Users\\Administrator\\Desktop\\我的印章\\" + UuidUtils.uuid() + ".png";
//				ImageIO.write(fz1, "png", new File(filePath)); // 将其保存在D:\\下，得有这个目录
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		}else{
//		}
				
		//方章2
		BufferedImage fz2 =changfangx("张三");//有边框
		try {
			String filePath = "C:\\Users\\Administrator\\Desktop\\测试\\" + UuidUtils.uuid() + ".png";
			ImageIO.write(fz2, "png", new File(filePath)); // 将其保存在D:\\下，得有这个目录
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//方章3
//		BufferedImage fz3 =sealFanz01("张三");//无边框
//		try {
//			String filePath = "C:\\Users\\Administrator\\Desktop\\证书\\" + UuidUtils.uuid() + ".png";
//			ImageIO.write(fz3, "png", new File(filePath)); // 将其保存在D:\\下，得有这个目录
//		} catch (Exception ex) {
//			ex.printStackTrace();
//	    }

		//圆章
//		String message = "这是一个企业章";
//		String centerName = "测试章";
//		BufferedImage yz =startChapterGraphics2D(message,centerName);//无边框
//		try {
//			String filePath = "C:\\Users\\Lenovo\\Desktop\\我的印章\\" + UuidUtils.uuid() + ".png";
//			ImageIO.write(yz, "png", new File(filePath)); // 将其保存在D:\\下，得有这个目录
//		} catch (Exception ex) {
//			ex.printStackTrace();
//	    }
	}
	
	/**
	 *  椭圆章  前面不加空格
	 * @param message
	 * @param centerName
	 * @return
	 */
	public static BufferedImage	startEllipseGraphics2D(String message, String centerName){
		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();
		// 增加下面代码使得背景透明
		buffImg = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		g.dispose();
		g = buffImg.createGraphics();
		// 背景透明代码结束
		g.setColor(Color.RED);
		// 设置锯齿圆滑
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g.setStroke(new BasicStroke(3));
		g.drawOval(2, 2, width-4, height-10);
		g.setFont(new Font("宋体", Font.BOLD, 21));
		
		
		g.drawString(centerName, (width-21*centerName.trim().length())/2,height/2+20);
		
		// 根据输入字符串得到字符数组
        String[] messages2 = message.split("", 0);
        String[] messages = new String[messages2.length];
        System.arraycopy(messages2, 0, messages, 0, messages2.length);
        int fontsize = 30;
        int radius = height/2;// 周半径
		int CENTERX = width / 2;// 画图所出位置
		int CENTERY = height / 2;// 画图所处位置
		Font f = new Font("Serif", Font.BOLD, fontsize);
		int ilength = messages.length;
		FontRenderContext context = g.getFontRenderContext();
		Rectangle2D bounds = f.getStringBounds(message, context);

		// 字符宽度＝字符串长度/字符数
		double char_interval = (bounds.getWidth() / ilength);
		// 上坡度
		double ascent = -bounds.getY();
		double ascentx = -bounds.getX();

		int first = 0, second = 0;
		boolean odd = false;
		if (ilength % 2 == 1) {
			first = (ilength - 1) / 2;
			odd = true;
		} else {
			first = (ilength) / 2 - 1;
			second = (ilength) / 2;
			odd = false;
		}

		double radius2 = radius+ascentx+60+50;
		double x0 = CENTERX;
		double y0 = CENTERY - radius + ascent;
		// 旋转角度
		double a = 2 * Math.asin(char_interval / (2 * radius2));

		if (odd) {
			g.setFont(f);
			g.drawString(messages[first], (float) (x0 - char_interval / 2), (float) y0);

			// 中心点的右边
			for (int i = first + 1; i < ilength; i++) {
				double aa = (i - first) * a;
				double ax = radius2 * Math.sin(aa);
				double ay = radius2 - radius2 * Math.cos(aa);
				AffineTransform transform = AffineTransform.getRotateInstance(aa);// ,x0 + ax, y0 + ay);
				Font f2 = f.deriveFont(transform);
				g.setFont(f2);
				g.drawString(messages[i], (float) (x0 + ax - char_interval / 2 * Math.cos(aa)),
						(float) (y0 + ay - char_interval / 2 * Math.sin(aa)));
			}
			// 中心点的左边
			for (int i = first - 1; i > -1; i--) {
				double aa = (first - i) * a;
				double ax = radius2 * Math.sin(aa);
				double ay = radius2 - radius2 * Math.cos(aa);
				AffineTransform transform = AffineTransform.getRotateInstance(-aa);// ,x0 + ax, y0 + ay);
				Font f2 = f.deriveFont(transform);
				g.setFont(f2);
				g.drawString(messages[i], (float) (x0 - ax - char_interval / 2 * Math.cos(aa)),
						(float) (y0 + ay + char_interval / 2 * Math.sin(aa)));
			}

		} else {
			// 中心点的右边
			for (int i = second; i < ilength; i++) {
				double aa = (i - second + 0.5) * a;
				double ax = radius2 * Math.sin(aa);
				double ay = radius2 - radius2 * Math.cos(aa);
				AffineTransform transform = AffineTransform.getRotateInstance(aa);// ,x0 + ax, y0 + ay);
				Font f2 = f.deriveFont(transform);
				g.setFont(f2);
				g.drawString(messages[i], (float) (x0 + ax - char_interval / 2 * Math.cos(aa)),
						(float) (y0 + ay - char_interval / 2 * Math.sin(aa)));
			}

			// 中心点的左边
			for (int i = first; i > -1; i--) {
				double aa = (first - i + 0.5) * a;
				double ax = radius2 * Math.sin(aa);
				double ay = radius2 - radius2 * Math.cos(aa);
				AffineTransform transform = AffineTransform.getRotateInstance(-aa);// ,x0 + ax, y0 + ay);
				Font f2 = f.deriveFont(transform);
				g.setFont(f2);
				g.drawString(messages[i], (float) (x0 - ax - char_interval / 2 * Math.cos(aa)),
						(float) (y0 + ay + char_interval / 2 * Math.sin(aa)));
			}
		}

		return buffImg;
		
	}
	
	/***
	 * 方章3
	 * @param sideLength
	 * @param message
	 * @return
	 */
	public static BufferedImage sealFanz03(int sideLength, String message) {
		if(message.length()==3){
			message="1"+message+"印";
		}else if(message.length()==2){
			message="1"+message+"之印";
		}else if(message.length()==1){
			message="1"+message+"之印章";
		}else{
			message="1"+message;
		}
		BufferedImage info = startSquareGraphics2D(sideLength,message);

		return info;
	}
	
	/**
	 *  方章3
	 * @param sideLength
	 * @param message
	 * @return
	 */
	public static BufferedImage startSquareGraphics2D(int sideLength, String message) {
		int height = sideLength;
		if (message.trim().length() <= 2) {
			height /= 2;
		}
		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(sideLength, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();
		// 增加下面代码使得背景透明
		buffImg = g.getDeviceConfiguration().createCompatibleImage(sideLength, height, Transparency.TRANSLUCENT);
		g.dispose();
		g = buffImg.createGraphics();
		// 背景透明代码结束
		g.setColor(Color.RED);
		// 设置锯齿圆滑
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(3));
		g.drawRect(0, 0, sideLength - 1, height - 1);
		int fontWidth = sideLength / 2 - 5; // 计算文字合适大小
		int messageWidth = fontWidth * 2; // 计算出文字的总宽度
		int CENTERX = (sideLength - messageWidth) / 2;// 计算出字体开始的位置
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, fontWidth));// 设置字体信息
		int CENTERY = fontWidth - sideLength / 22 + 2;
		if (message.trim().length() <= 2) {
			CENTERY = fontWidth - sideLength / 15;
		}
		String[] messages2 = message.split("", 0);
		String[] messages = new String[messages2.length - 1];
		System.arraycopy(messages2, 1, messages, 0, messages2.length - 1);
		for (int i = 0; i < 2; i++) {// 画出前面两个字符
			if(message.trim().length() > 2) {
				g.drawString(messages[i], CENTERX + fontWidth , CENTERY+fontWidth*i);
			}else {
				g.drawString(messages[i], CENTERX + fontWidth * i, CENTERY);
			}
		}
		if (message.trim().length() > 2) {
			for (int i = 2; i < message.length() - 1; i++) {
				//g.drawString(messages[i], CENTERX + fontWidth * (i - 2), CENTERY+ fontWidth);
				g.drawString(messages[i], CENTERX , CENTERY+fontWidth*(i-2));
			}
		}

		return buffImg;
	}
	
	/**
	 *  方章2有边框长方形
	 * @param message
	 * @return
	 */
	public static BufferedImage changfangx(String message) {
		// 限制最多显示4个字符，超过的截断
		if (message.length() > 4) {
			message = message.substring(0, 4);
		}
		
		String[] messagesstr = message.split("", 0);
		int height = 150;//图片高度
		int width = 150;//图片宽度
		int fontWidth = 0; // 计算文字合适大小
		int centerx =  0;// 计算出字体开始的x轴位置
		int centery = 0;//计算出字体开始的y轴位置
		int xtWidth = 15;//线条下方对称空出空白部分
		
		if(messagesstr.length==1) {
			fontWidth = 65; 
			centerx =  40;
			centery = 95;
			xtWidth = 12;
		}else if(messagesstr.length==2) {
			fontWidth = 65; 
			centerx =  10;
			centery = 100;
			xtWidth = 12;
		}else if(messagesstr.length==3) {
			fontWidth = 44; 
			centerx =  8;
			centery = 95;
		}else if(messagesstr.length==4) {
			fontWidth = 35; 
			centerx =  5;
			centery = 92;
		}
		
		return changfangx(message,height,width,fontWidth,centerx,centery,xtWidth);
	}
	
	/**
	 *  方章2有边框长方形
	 * @param message
	 * @return
	 */
	public static BufferedImage changfangx(String message,int height,int width,int fontWidth,int centerx,int centery,int xtWidth) {
		int messageWidth = fontWidth * message.length()+5; // 计算出文字的总宽度
		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();
		// 增加下面代码使得背景透明
		buffImg = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		g.dispose();
		g = buffImg.createGraphics();
		// 背景透明代码结束
		g.setColor(Color.RED);
		// 设置锯齿圆滑
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(4));
		g.drawRect(centerx-2, centery-fontWidth, messageWidth, fontWidth+xtWidth);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, fontWidth));// 设置字体信息
		String[] messagesstr = message.split("", 0);
		for (int i = 0; i < messagesstr.length; i++) {// 画出前面两个字符
			g.drawString(messagesstr[i],  centerx,  centery);
			centerx=centerx+fontWidth;
		}
		return buffImg;
	}
	
	/**
	 *  方章1无边框
	 * @param message
	 * @return
	 */
	public static BufferedImage sealFanz01(String message) {
		// 限制最多显示4个字符，超过的截断
		if (message.length() > 4) {
			message = message.substring(0, 4);
		}

		String[] messagesstr = message.split("", 0);
		int height = 150;//图片高度
		int width = 150;//图片宽度
		int fontWidth = 0; // 计算文字合适大小
		int centerx =  0;// 计算出字体开始的x轴位置
		int centery = 0;//计算出字体开始的y轴位置
		if(messagesstr.length==1) {
			fontWidth = 65; 
			centerx =  40;
			centery = 95;
		}else if(messagesstr.length==2) {
			fontWidth = 65; 
			centerx =  10;
			centery = 95;
		}else if(messagesstr.length==3) {
			fontWidth = 45; 
			centerx =  10;
			centery = 95;
		}else if(messagesstr.length==4) {
			fontWidth = 35; 
			centerx =  5;
			centery = 92;
		}
		return sealFanz01(message,height,width,fontWidth,centerx,centery);
	}
	
	/**
	 *  方章1无边框
	 * @param message
	 * @return
	 */
	public static BufferedImage sealFanz01(String message,int height,int width,int fontWidth,int centerx,int centery) {
		
		int messageWidth = fontWidth * message.length()+5; // 计算出文字的总宽度
		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();
		// 增加下面代码使得背景透明
		buffImg = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		g.dispose();
		g = buffImg.createGraphics();
		// 背景透明代码结束
		g.setColor(Color.RED);
		// 设置锯齿圆滑
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(10));
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, fontWidth));// 设置字体信息
		String[] messagesstr = message.split("", 0);
		for (int i = 0; i < messagesstr.length; i++) {// 画出前面两个字符
			g.drawString(messagesstr[i],  centerx,  centery);
			centerx=centerx+fontWidth;
		}
		return buffImg;
	}
	
	/**
	 * 圆章
	 * @param message
	 * @param centerName
	 * @return
	 */
	public static BufferedImage startChapterGraphics2D( String message, String centerName) {
    	message=" "+message;
    	int WIDTH = 150;
    	int HEIGHT = 150;
        // 定义图像buffer：高度，宽度，半透明
        BufferedImage buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);       
        //绘图
        Graphics2D g = buffImg.createGraphics();      
        g.setColor(Color.RED);
        //线条的宽度
        g.setStroke(new BasicStroke(3));
        //设置锯齿圆滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        //绘制圆
        //周半径
        int radius = HEIGHT/2-5;
        //画图所出位置
        int CENTERX = WIDTH/2;
        //画图所处位置
        int CENTERY = HEIGHT/2;
        
        //画圆
        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(CENTERX, CENTERY, CENTERX + radius, CENTERY + radius);
        g.draw(circle);
        
        //绘制中间的五角星
        g.setFont(new Font("宋体", Font.BOLD, 36));
        g.drawString("★", CENTERX-18, CENTERY+(20/3+4));    
 
        //写入签名
        g.setFont(new Font("宋体", Font.BOLD, 14));// 写入签名
        g.drawString(centerName, CENTERX-35, CENTERY +(40));
        
        
        //根据输入字符串得到字符数组
        String[] messages2 = message.split("",0);
        String[] messages = new String[messages2.length-1];
        System.arraycopy(messages2,1,messages,0,messages2.length-1);
        
        //输入的字数
        int ilength = messages.length;
        
        //设置message字体属性
        int fontsize = 16;
        
        Font f = new Font("Serif", Font.BOLD, fontsize);
 
        //正确测量文本所需的信息容器
        FontRenderContext context = g.getFontRenderContext();
        //规定的位置(X,Y)和尺寸(宽x高)定义的字体
        Rectangle2D bounds = f.getStringBounds(message, context);
        
        //字符宽度＝字符串长度/字符数
        double char_interval = (bounds.getWidth() / ilength);
        //上坡度
        double ascent = -bounds.getY()+5;
 
        int first = 0,second = 0;
        boolean odd = false;
        if (ilength%2 == 1)
        {
            first = (ilength-1)/2;
            odd = true;
        }
        else
        {
            first = (ilength)/2-1;
            second = (ilength)/2;
            odd = false;
        }
        
        double radius2 = radius - ascent;
        double x0 = CENTERX;
        double y0 = CENTERY - radius + ascent;
        //旋转角度
        double a = 2*Math.asin(char_interval/(2*radius2));
        
        if (odd)
        {
            g.setFont(f);
            g.drawString(messages[first], (float)(x0 - char_interval/2), (float)y0);
            
            //中心点的右边打印
            for (int i=first+1;i<ilength;i++)
            {
                double aa = (i - first) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 + ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay - char_interval/2* Math.sin(aa)));
            }
            //中心点的左边打印
            for (int i=first-1;i>-1;i--)
            {
                double aa = (first - i) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 - ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay + char_interval/2* Math.sin(aa)));
            }
            
        }
        else
        {
            //中心点的右边打印
            for (int i=second;i<ilength;i++)
            {
                double aa = (i - second + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 + ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay - char_interval/2* Math.sin(aa)));
            }
            
            //中心点的左边打印
            for (int i=first;i>-1;i--)
            {
                double aa = (first - i + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages[i], (float)(x0 - ax - char_interval/2* Math.cos(aa)), (float)(y0 + ay + char_interval/2* Math.sin(aa)));
            }
        }
        
        return buffImg;
	}
	
	
}
