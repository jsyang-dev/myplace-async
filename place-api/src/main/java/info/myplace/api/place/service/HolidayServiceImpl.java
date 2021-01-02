package info.myplace.api.place.service;

import info.myplace.api.place.client.HolidayClient;
import info.myplace.api.place.dto.HolidayDto;
import info.myplace.api.place.dto.HolidayGenerateDto;
import info.myplace.api.place.exception.HolidayNotFoundException;
import info.myplace.api.place.mapper.HolidayMapper;
import info.myplace.api.place.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
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

  @Override
  @Transactional
  public Mono<HolidayDto> create(HolidayDto holidayDto) {
    return Mono.just(holidayMapper.toEntity(holidayDto))
        .map(holidayRepository::save)
        .map(holidayMapper::toDto);
  }

  @Override
  public Mono<HolidayDto> read(long id) {
    return Mono.just(
            holidayRepository.findById(id).orElseThrow(() -> new HolidayNotFoundException(id)))
        .map(holidayMapper::toDto);
  }

  @Override
  public Flux<HolidayDto> readList(int year) {
    return getHolidayDtoFlux(getStartDateOfYear(year), getEndDateOfYear(year));
  }

  @Override
  public Flux<HolidayDto> readList(int year, int month) {
    return getHolidayDtoFlux(getStartDateOfMonth(year, month), getEndDateOfMonth(year, month));
  }

  @Override
  public Flux<HolidayDto> readList(int year, int month, int day) {
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
  public Flux<HolidayDto> generate(HolidayGenerateDto holidayGenerateDto) {

    int year = holidayGenerateDto.getYear();
    int month = holidayGenerateDto.getMonth();

    if (month > 0) {
      holidayRepository.deleteByDateBetween(
          getStartDateOfMonth(year, month), getEndDateOfMonth(year, month));
    } else {
      holidayRepository.deleteByDateBetween(getStartDateOfYear(year), getEndDateOfYear(year));
    }

    Flux<HolidayDto> holidayDtoFlux = holidayClient.getHolidayList(year, month);
    holidayDtoFlux.map(holidayMapper::toEntity).subscribe(holidayRepository::save);

    return holidayDtoFlux;
  }

  private Flux<HolidayDto> getHolidayDtoFlux(LocalDate startDate, LocalDate endDate) {
    return Flux.concat(
        getHolidayDtoFluxByPeriod(startDate, endDate),
        getHolidayDtoFluxByPeriod(startDate.withYear(1900), endDate.withYear(1900)));
  }

  private Flux<HolidayDto> getHolidayDtoFluxByPeriod(LocalDate startDate, LocalDate endDate) {
    return Flux.fromIterable(holidayRepository.findByDateBetween(startDate, endDate))
        .map(holidayMapper::toDto);
  }

  private LocalDate getStartDateOfMonth(int year, int month) {
    return LocalDate.of(year, month, 1);
  }

  private LocalDate getEndDateOfMonth(int year, int month) {
    return YearMonth.of(year, month).atEndOfMonth();
  }

  private LocalDate getStartDateOfYear(int year) {
    return LocalDate.of(year, 1, 1);
  }

  private LocalDate getEndDateOfYear(int year) {
    return LocalDate.of(year, 12, 31);
  }
}
