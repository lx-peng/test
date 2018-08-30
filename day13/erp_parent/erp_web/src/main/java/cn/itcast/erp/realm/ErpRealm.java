package cn.itcast.erp.realm;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;

public class ErpRealm extends AuthorizingRealm {
	private IEmpBiz empBiz;
	private IMenuBiz menuBiz;
	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
	}
	
	public void setMenuBiz(IMenuBiz menuBiz) {
		this.menuBiz = menuBiz;
	}

	//授权方法
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("授权");
		//得到当前登录的用户
		Emp emp=(Emp)principals.getPrimaryPrincipal();
		//获取登录用户所对应的菜单权限集合
		List<Menu> menus=empBiz.getMenusByEmpuuid(emp.getUuid());
		SimpleAuthorizationInfo sai=new SimpleAuthorizationInfo();
		for (Menu menu : menus) {
			sai.addStringPermission(menu.getMenuname());
		}
		return sai;
	}
	//认证方法
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("认证");
		//转成实现类就可以得到用户名和密码
		UsernamePasswordToken upt=(UsernamePasswordToken)token;
		String pwd=new String(upt.getPassword());
		//根据用户名和密码查找用户
		Emp emp=null;
		try {
			emp = empBiz.findByUsernameAndPwd(upt.getUsername(), pwd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//验证成功
		if(null!=emp){
			//返回认证信息
			//参数1：主角，就是登录用户
			//参数2：证书或凭证 在这里我们用的是密码
			//参数2：当前realm的名称
			return new SimpleAuthenticationInfo(emp,pwd,getName());
		}
		//返回空则表示用户名或密码错误
		return null;
	}

}
