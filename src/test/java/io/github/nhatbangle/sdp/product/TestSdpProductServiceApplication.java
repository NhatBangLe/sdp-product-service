package io.github.nhatbangle.sdp.product;

import org.springframework.boot.SpringApplication;

public class TestSdpProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(SdpProductServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
