import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setupAll() {

    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

// Задача №1: Заказ карты (Happy Path)

    @Test
    void shouldSubmitRequestSuccessfully() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов-Смирнов Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();

        assertEquals(expected, actual);
    }

// Задача №2: Проверка валидации полей

    @Test
    void shouldShowErrorIfInvalidName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivan Petrov");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();

        assertEquals(expectedText, actualText);
    }

    @Test
    void shouldShowErrorIfEmptyName() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        String expectedText = "Поле обязательно для заполнения";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();

        assertEquals(expectedText, actualText);
    }

    @Test
    void shouldShowErrorIfInvalidPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("89991112233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();

        assertEquals(expectedText, actualText);
    }

    @Test
    void shouldShowErrorIfEmptyPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        String expectedText = "Поле обязательно для заполнения";
        String actualText = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();

        assertEquals(expectedText, actualText);
    }

    @Test
    void shouldShowErrorIfCheckboxUnchecked() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79991112233");
        driver.findElement(By.className("button")).click();

        assertTrue(driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).isDisplayed());
    }

}
