package cn.itcast.erp.action;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ErpException;

/**
 * 供应商Action 
 * @author Administrator
 *
 */
public class SupplierAction extends BaseAction<Supplier> {
	private File file;//上传文件
	private String fileFileName;//文件名
	private String fileContentType;//文件类型
	/**remote传过来的参数 **/
	private String q;
	private ISupplierBiz supplierBiz;
	public void setSupplierBiz(ISupplierBiz supplierBiz) {
		this.supplierBiz = supplierBiz;
		super.setBaseBiz(this.supplierBiz);
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public String getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	/**
	 * 自动补全
	 */
	public void list(){
		if(null==getT1()){
			setT1(new Supplier());
		}
		getT1().setName(q);
		super.list();
	}
/**
 * 导出excel文件
 */
	public void export(){
		String filename="";
		//根据类型生成文件名
		if("1".equals(getT1().getType())){
			filename="供应商.xls";
		}if("2".equals(getT1().getType())){
			filename="客户.xls";
		}
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("Content-Disposition","attachment;filename="+new String(filename.getBytes(),"ISO-8859-1"));//中文名称进行转码
			//调用导出业务
			supplierBiz.export(response.getOutputStream(), getT1());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//导入数据
		public void doImport(){
			//文件类型判断
			if(!"application/vnd.ms-excel".equals(fileContentType)){
				ajaxReturn(false,"上传的文件必须为excel文件");
				return;
			}
			try {
				supplierBiz.doImport(new FileInputStream(file));
				ajaxReturn(true,"上传的文件成功");
			} catch (ErpException e) {
				ajaxReturn(false,e.getMessage());
			} catch (IOException e) {
				ajaxReturn(false,"上传文件失败");
				e.printStackTrace();
			}
		}
}
