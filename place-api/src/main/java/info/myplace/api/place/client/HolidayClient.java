package info.myplace.api.place.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.myplace.api.place.dto.HolidayDto;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class HolidayClient {

  @Value("${app.api.holiday.key}")
  private String key;

  private final WebClient webClient;

  public HolidayClient(
      WebClient.Builder webClientBuilder, @Value("${app.api.holiday.url}") String url) {
    this.webClient = webClientBuilder.baseUrl(url).build();
  }

  public Flux<HolidayDto> getHolidayList(Integer year) {
    return getHolidayList(year, null);
  }

  public Flux<HolidayDto> getHolidayList(Integer year, Integer month) {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                UriComponentsBuilder.fromUri(uriBuilder.path("/getRestDeInfo1").build())
                    .queryParam("solYear", year)
                    .queryParam("solMonth", month)
                    .queryParam("ServiceKey", key)
                    .queryParam("_type", "json")
                    .queryParam("numOfRows", 100)
                    .build(true)
                    .toUri())
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(RuntimeException::new))
        .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(RuntimeException::new))
        .bodyToMono(ResponseDto.class)
        .map(responseDto -> responseDto.getResponse().getBody().getResult().toDtos())
        .flatMapMany(Flux::fromIterable);
  }

  @Getter
  public static class ResponseDto {

    private Response response;

    @Getter
    public static class Response {

      private Header header;

      private Body body;
    }

    @Getter
    public static class Header {

      // 결과코드
      private String resultCode;

      // 결과메시지
      private String resultMsg;
    }

    @Getter
    public static class Body {

      // 결과
      @JsonProperty("items")
      private Result result;

      // 페이지당항목수
      private int numOfRows;

      // 페이지
      private int pageNo;

      // 모든항목수
      private int totalCount;
    }

    @Getter
    public static class Result {

      @JsonProperty("item")
      private List<Item> items;

      public List<HolidayDto> toDtos() {
        List<HolidayDto> holidayDtos = new ArrayList<>();
        for (Item item : this.getItems()) {
          holidayDtos.add(new HolidayDto(item.getDay(), item.getDateName()));
        }
        return holidayDtos;
      }
    }

    @Getter
    public static class Item {

      // 날짜
      private LocalDate day;

      // 순번
      private int seq;

      // 종류
      private String dateKind;

      // 공공기관 휴일여부
      private boolean isHoliday;

      // 명칭
      private String dateName;

      @JsonProperty("locdate")
      public void setDay(String locdate) {
        this.day = LocalDate.parse(locdate, DateTimeFormatter.ofPattern("yyyyMMdd"));
      }

      @JsonProperty("isHoliday")
      public void setIsHoliday(String isHoliday) {
        this.isHoliday = "Y".equals(isHoliday);
      }
    }
  }
}
