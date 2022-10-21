package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ShareItAppTest {

    @Test
    void contextLoads() {
    }

    @Test
    public void testMain() {
        ShareItApp.main(new String[]{});
        assertTrue(true);
    }
}