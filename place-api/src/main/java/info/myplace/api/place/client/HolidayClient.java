package info.myplace.api.place.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.myplace.api.place.dto.HolidayDto;
import info.myplace.api.place.exception.WebClientFailException;
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

  public Flux<HolidayDto> getHolidayList(int year, int month) {

    String path = "/getRestDeInfo";

    return webClient
        .get()
        .uri(
            uriBuilder ->
                UriComponentsBuilder.fromUri(uriBuilder.path(path).build())
                    .queryParam("solYear", year)
                    .queryParam("solMonth", (month == 0 ? null : String.format("%02d", month)))
                    .queryParam("ServiceKey", key)
                    .queryParam("_type", "json")
                    .queryParam("numOfRows", 100)
                    .build(true)
                    .toUri())
        .retrieve()
        .onStatus(
            HttpStatus::isError,
            clientResponse ->
                Mono.error(new WebClientFailException(clientResponse.statusCode(), path)))
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
          holidayDtos.add(
              HolidayDto.builder()
                  .date(item.getDay())
                  .name(item.getDateName())
                  .autoGenerated(true)
                  .build());
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
