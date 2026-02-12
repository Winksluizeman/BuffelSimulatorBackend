package org.example.buffelsimulatorbackend;

import org.springframework.boot.SpringApplication;

public class TestBuffelSimulatorBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(BuffelSimulatorBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
