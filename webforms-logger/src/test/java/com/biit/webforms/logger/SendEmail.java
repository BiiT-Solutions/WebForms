package com.biit.webforms.logger;

import org.testng.annotations.Test;

@Test(groups = { "sendEmail" })
public class SendEmail {

	@Test
	public void checkLoggerName() {
		WebformsLogger.info(SendEmail.class.getName(), "Initializing the logger... ");
		System.out.println(" ----------------------- EXPECTED ERROR ------------------------");
		WebformsLogger.errorMessage(SendEmail.class.getName(), new Exception(
				"Catastrophic Error: Y2K problem has arrieved with some years of delay!"));
		System.out.println(" --------------------- END EXPECTED ERROR ----------------------");
	}
}
