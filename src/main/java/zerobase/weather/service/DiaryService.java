package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDateException;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;
    private final ApiService apiService;

    @Transactional
    public void saveWeatherData() {
        DateWeather dateWeather = fetchAndSaveWeather();
        dateWeatherRepository.save(dateWeather);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void createDiary(LocalDate date, String text) {
        log.info("Started to create diary");

        // 날씨 데이터 가져오기
        DateWeather dateWeather = getDateWeather(date);

        // DB에 넣기
        Diary nowDiary = new Diary();
        nowDiary.setDateWeather(dateWeather);
        nowDiary.setText(text);

        diaryRepository.save(nowDiary);
        log.info("Diary created successfully");
    }

    public List<Diary> readDiary(LocalDate date) {
        if (date.isAfter(LocalDate.ofYearDay(2050, 1))) {
            throw new InvalidDateException("Date cannot be beyond year 2050");
        }
        log.info("Retrieving diary entries for date: {}", date);
        return diaryRepository.findAllByDate(date);
    }

    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        log.info("Retrieving diary entries from {} to {}", startDate, endDate);
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    @Transactional
    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        if (nowDiary == null) {
            throw new InvalidDateException("No diary found for the provided date");
        }
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
        log.info("Diary updated for date: {}", date);
    }

    @Transactional
    public void deleteDiary(LocalDate date) {
        log.info("Deleting diaries for date: {}", date);
        diaryRepository.deleteAllByDate(date);
    }

    private DateWeather getDateWeather(LocalDate date) {
        return dateWeatherRepository.findAllByDate(date)
                .stream()
                .findFirst()
                .orElseGet(this::fetchAndSaveWeather);
    }

    private DateWeather fetchAndSaveWeather() {
        // API 호출을 통해 현재 날씨 데이터 가져오기
        String weatherData = apiService.getWeatherString();
        Map<String, Object> parsedWeather = parseWeather(weatherData);

        DateWeather dateWeather = mapToWeatherEntity(parsedWeather);
        dateWeatherRepository.save(dateWeather);
        log.info("Weather data fetched and saved successfully");
        return dateWeather;
    }

    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            log.error("Error while parsing weather data", e);
            throw new RuntimeException("Failed to parse weather data", e);
        }

        Map<String, Object> resultMap = new HashMap<>();
        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));

        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));

        return resultMap;
    }

    private DateWeather mapToWeatherEntity(Map<String, Object> weatherData) {
        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(weatherData.get("main").toString());
        dateWeather.setIcon(weatherData.get("icon").toString());
        dateWeather.setTemperature((Double) weatherData.get("temp"));
        return dateWeather;
    }
}
