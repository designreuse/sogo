package com.yihexinda.nodeweb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yihexinda.nodeweb.service.MessageService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeWebApplicationTests {
	@Autowired
	private MessageService messageService;

	@Test
	public void contextLoads() {
		messageService.rushOrderSuccessConfirm(1002,"790bfe5e-d93c-542a-e053-3f830d0acf7b",1);
	}

}
