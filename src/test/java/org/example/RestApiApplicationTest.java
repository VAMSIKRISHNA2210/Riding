package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;


@SpringBootTest
class RestApiApplicationTest {

    @MockBean
    private ApplicationContext applicationContext;

    @Test
    void mainMethodLoads() {
        RestApiApplication.main(new String[]{});
    }
}
