package info.myplace.api.place.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.myplace.api.place.dto.HolidayDto;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PublicDataClient {

  private final WebClient webClient;

  public PublicDataClient(WebClient.Builder webClientBuilder) {
    this.webClient =
        webClientBuilder
            .baseUrl("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService")
            .build();
  }

  public Flux<HolidayDto> getHolidayList(int year) {
    webClient.get().uri("/getRestDeInfo").exchange();
    //        .flatMap(
    //            clientResponse -> {
    //                clientResponse
    //                    .bodyToFlux(ResponseDto.class)
    //                    .map(r -> r.getResponse().getBody().getResult().toDtoFlux());
    //                return Flux.just(HolidayDto.builder().build());
    //            });
    return Flux.just(HolidayDto.builder().build());
  }

  @Getter
  public static class ResponseDto {

    private Response response;

    @Getter
    public class Response {

      private Header header;

      private Body body;
    }

    @Getter
    public class Header {

      // 결과코드
      private String resultCode;

      // 결과메시지
      private String resultMsg;
    }

    @Getter
    public class Body {

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

      public Flux<HolidayDto> toDtoFlux() {
        List<HolidayDto> holidayDtos = new ArrayList<>();
        for (Item item : this.getItems()) {
          holidayDtos.add(new HolidayDto(item.getDay(), item.getDateName()));
        }
        return Flux.fromIterable(holidayDtos);
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
