package info.myplace.api.place.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
class TagControllerTest {

  @Autowired private WebTestClient webTestClient;
}
