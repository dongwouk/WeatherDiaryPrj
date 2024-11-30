package zerobase.weather.error;

public class InvalidDateException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "너무 과거 혹은 미래의 날짜입니다.";

    // 기본 메시지를 사용하는 생성자
    public InvalidDateException() {
        super(DEFAULT_MESSAGE);
    }

    // 사용자 정의 메시지를 사용하는 생성자
    public InvalidDateException(String message) {
        super(message);
    }
}
