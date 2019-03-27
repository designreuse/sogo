package com.yihexinda.platformweb;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yihexinda.platformweb.service.AuthService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlatformWebApplicationTests {

	@Autowired
	private AuthService authService;

//	@Test
//	public void contextLoads() {
//		String[] arr = {"G03328","G05437","G05314","G02162","G01624","G00383","G02138","G04065",
//				"G03189","8847","8917","9393","XC","G04238","G05175","G04709","G04837","G01228",
//				"G03673","G03055","G03191","G03147","G04614","G04443","G04848","G04695","G05533",
//				"ZYFY","G8501","SHGM8457","G00954","G04097","G02972","G04310","G05222","G03357",
//				"G03693","G04117","G00997"};
//		for (String str : arr){
//			authService.findSysUserByUsername(str);
//		}
//
//	}

}
