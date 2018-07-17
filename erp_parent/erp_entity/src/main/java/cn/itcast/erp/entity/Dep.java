package cn.itcast.erp.entity;
/**
 * 部门实体类
 * @author Administrator
 *
 */
public class Dep {

	private Long uuid;//部门编号
	private String name;//部门名称
	private String tele;//部门电话
	public Long getUuid() {
		return uuid;
	}
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	@Override
	public String toString() {
		return "Dep [uuid=" + uuid + ", name=" + name + ", tele=" + tele + "]";
	}
	public Dep(Long uuid, String name, String tele) {
		super();
		this.uuid = uuid;
		this.name = name;
		this.tele = tele;
	}
	public Dep() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
