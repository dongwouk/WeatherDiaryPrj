package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {
    private final DiaryService diaryService;

    @Scheduled(cron = "0 0 1 * * *")
    public void saveDailyWeatherData() {
        log.info("Scheduled task started: Saving daily weather data.");
        try {
            diaryService.saveWeatherData(); // 메소드 이름 수정
            log.info("Weather data saved successfully.");
        } catch (Exception e) {
            log.error("Error occurred while saving daily weather data.", e);
        }
    }
}
