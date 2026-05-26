package org.example.buffelsimulatorbackend;

import org.springframework.boot.SpringApplication;

import org.example.buffelsimulatorbackend.BuffelSimulatorBackendApplication;

public class TestBuffelSimulatorBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(BuffelSimulatorBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
