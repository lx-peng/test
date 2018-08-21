package cn.itcast.demo;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class TestSend {
	private JavaMailSender javaMailSender;
	
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendMail() throws Exception{
		//创建邮件
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		//邮件包装工具
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		//发件人
		helper.setFrom("1982736125@qq.com");
		//收件人
		helper.setTo("luoxiaop1@163.com");
		//邮件标题
		helper.setSubject("Mail Test");
		//邮件内容
		helper.setText("发送成功----~~~~~~");
		//发送邮件
		javaMailSender.send(mimeMessage);
	}
}
