package user_management;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import user_management.Gui.ApplicationForm;

import javax.swing.*;

@SpringBootApplication
public class ApplicationManagement {
	public static void main(String[] args) {

		FlatDarculaLaf.setup();
		ConfigurableApplicationContext contextoSpring = new SpringApplicationBuilder(ApplicationManagement.class).headless(false).web(WebApplicationType.NONE).run(args);

		SwingUtilities.invokeLater(()->{
			ApplicationForm applicationForm = contextoSpring.getBean(ApplicationForm.class);
			applicationForm.setVisible(true);
		});
	}

}
