package org.gorgeous

import groovy.transform.CompileStatic
import org.junit.runner.RunWith
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@CompileStatic
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("ut")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AbstractTest {

}
