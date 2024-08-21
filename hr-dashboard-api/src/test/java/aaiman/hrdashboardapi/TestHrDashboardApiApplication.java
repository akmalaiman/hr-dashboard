package aaiman.hrdashboardapi;

import org.springframework.boot.SpringApplication;

public class TestHrDashboardApiApplication {

        public static void main(String[] args) {
                SpringApplication.from(HrDashboardApiApplication::main).with(TestcontainersConfiguration.class).run(args);
        }

}
