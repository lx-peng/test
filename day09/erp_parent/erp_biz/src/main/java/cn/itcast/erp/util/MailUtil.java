package cn.itcast.erp.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 发送邮件工具
 * @author Administrator
 *
 */
public class MailUtil {
	private JavaMailSender sender;//spring 邮件发送类
	private String from;//发件人
	public JavaMailSender getJavaMailSender() {
		return sender;
	}
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.sender = javaMailSender;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public JavaMailSender getSender() {
		return sender;
	}
	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}
	/**
	 * 发送邮件
	 */
	public void sendMail(String to,String subject,String text) throws MessagingException{
		//创建邮件
		MimeMessage mimeMessage =sender.createMimeMessage();
		//邮件包装工具
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		//发件人
		helper.setFrom(from);
		//收件人
		helper.setTo(to);
		//邮件标题
		helper.setSubject(subject);
		//邮件正文
		helper.setText(text);
		//发送邮件
		sender.send(mimeMessage);
	}
}
