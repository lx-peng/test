package cn.itcast.demo.ws.impl;

import cn.itcast.demo.ws.IWeatherService;
/**
 * 天气服务类
 * @author Administrator
 *
 */
public class WeatherService implements IWeatherService {
	/**
	 * 根据城市查询天气信息
	 */
	public String info(String city) {
		if("北京".equals(city)){
			return "雾霾";
		}
		return "晴天";
	}

}
