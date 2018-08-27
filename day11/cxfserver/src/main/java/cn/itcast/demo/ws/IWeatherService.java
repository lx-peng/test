package cn.itcast.demo.ws;

import javax.jws.WebService;

@SuppressWarnings("restriction")
@WebService
public interface IWeatherService {
	//根据城市查询天气信息
	public String info(String city);
}
