package com.rapo.rapo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
// This next line is very important. It tells the test to use your real MySQL database
// instead of an in-memory one.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testDatabaseConnection() {
        // This test does not check any logic.
        // Its only purpose is to see if the database part of the application can start.
        // If this test passes, your database connection is 100% working.
        assertThat(userRepository).isNotNull();
    }
}