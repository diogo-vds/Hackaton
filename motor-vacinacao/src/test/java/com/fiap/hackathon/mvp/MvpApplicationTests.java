package com.fiap.hackathon.mvp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"aws.region=us-east-1",
		"aws.sqs.notificacao.queue-url=http://localhost/test-queue"
})
class MvpApplicationTests {

	@Test
	void contextLoads() {
	}

}
