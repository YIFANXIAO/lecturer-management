package learning.way.lecturer.management.bases;

import learning.way.lecturer.management.Application;
import learning.way.lecturer.management.Extension.MariaDB4jExtension;
import learning.way.lecturer.management.SpringApplicationContext;
import learning.way.lecturer.management.TruncateDatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import java.sql.SQLException;

@ExtendWith({MariaDB4jExtension.class, SpringExtension.class})
@ActiveProfiles("test")
@SpringBootTest(classes = Application.class)
@Rollback
@AutoConfigureMockMvc
public class TestBase {
	protected static final String RESPONSE_STATUS_ENUM_SUCCESS = "SUCCESS";
	private static Logger log = LoggerFactory.getLogger(TestBase.class);

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private SpringApplicationContext springApplicationContext;

	@Autowired
	private TruncateDatabaseService truncateDatabaseService;

	private void prepareGlobalConfiguration() {
	}

	@BeforeEach
	protected void setUp() {
		this.prepareGlobalConfiguration();
		try {
			truncateDatabaseService.restartIdWith(1, true, null);
		} catch (SQLException e) {
			log.info(e.getStackTrace().toString());
		} finally {
			truncateDatabaseService.closeResource();
		}
		springApplicationContext.setApplicationContext(context);
	}
}



