package info.myplace.api.place.service;

import info.myplace.api.place.client.HolidayClient;
import info.myplace.api.place.dto.HolidayDto;
import info.myplace.api.place.exception.HolidayNotFoundException;
import info.myplace.api.place.mapper.HolidayMapper;
import info.myplace.api.place.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

  private final HolidayRepository holidayRepository;
  private final HolidayMapper holidayMapper;
  private final HolidayClient holidayClient;

  @Value("${app.api.holiday.url}")
  private String url;

  @Override
  @Transactional
  public Mono<HolidayDto> create(HolidayDto holidayDto) {
    return Mono.just(holidayMapper.toEntity(holidayDto))
        .map(holidayRepository::save)
        .map(holidayMapper::toDto);
  }

  @Override
  public Flux<HolidayDto> getList(int year) {
    return getHolidayDtoFlux(LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31));
  }

  @Override
  public Flux<HolidayDto> getList(int year, int month) {
    return getHolidayDtoFlux(
        LocalDate.of(year, month, 1), YearMonth.of(year, month).atEndOfMonth());
  }

  @Override
  public Flux<HolidayDto> getList(int year, int month, int day) {
    return getHolidayDtoFlux(LocalDate.of(year, month, day), LocalDate.of(year, month, day));
  }

  @Override
  @Transactional
  public Mono<HolidayDto> update(long id, HolidayDto holidayDto) {
    return Mono.just(
            holidayRepository.findById(id).orElseThrow(() -> new HolidayNotFoundException(id)))
        .map(holiday -> holiday.update(holidayDto))
        .map(holidayMapper::toDto);
  }

  @Override
  @Transactional
  public void delete(long id) {
    holidayRepository.deleteById(id);
  }

  @Override
  @Transactional
  public Flux<HolidayDto> generate(int year) {
    return holidayClient.getHolidayList(year);
  }

  @Override
  public Flux<HolidayDto> generate(int year, int month) {

    Flux<HolidayDto> holidayDtoFlux = holidayClient.getHolidayList(year, month);

    // TODO: Flux 순서 해결
    holidayDtoFlux
        .map(holidayMapper::toEntity)
        .subscribe(entity -> System.out.println("1" + holidayRepository.save(entity)));

    return holidayDtoFlux;
  }

  private Flux<HolidayDto> getHolidayDtoFlux(LocalDate startDate, LocalDate endDate) {
    return Flux.concat(
        getHolidayDtoFluxByPeriod(startDate, endDate),
        getHolidayDtoFluxByPeriod(startDate.withYear(1900), endDate.withYear(1900)));
  }

  private Flux<HolidayDto> getHolidayDtoFluxByPeriod(LocalDate startDate, LocalDate endDate) {
    return Flux.fromIterable(holidayRepository.findByPeriod(startDate, endDate))
        .map(holidayMapper::toDto);
  }
}
