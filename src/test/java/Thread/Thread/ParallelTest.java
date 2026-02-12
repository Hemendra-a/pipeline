package Thread.Thread;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

class Runner extends Thread {
    private String url;
    private String taskName;
    private String searchTerm;

    public Runner(String taskName, String url, String searchTerm) {
        this.taskName = taskName;
        this.url = url;
        this.searchTerm = searchTerm;
    }

    @Override
    public void run() {
        // Step 1: Initialize driver for this specific thread
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        try {
            // ACTION 1: Maximize and Navigate
            driver.manage().window().maximize();
            driver.get(url);
            System.out.println(taskName + " - Action 1: Navigated to " + url);

            // ACTION 2: Get Page Title
            String title = driver.getTitle();
            System.out.println(taskName + " - Action 2: Page Title is " + title);

            // ACTION 3: Search Interaction (Handles Google, Amazon, and YouTube)
            if (url.contains("youtube.com")) {
                // YouTube specific: name="search_query"
                wait.until(ExpectedConditions.elementToBeClickable(By.name("search_query"))).sendKeys(searchTerm + Keys.ENTER);
            } else if (url.contains("amazon")) {
                // Amazon specific: id="twotabsearchtextbox"
                driver.findElement(By.id("twotabsearchtextbox")).sendKeys(searchTerm + Keys.ENTER);
            } else {
                // Google/Default: name="q"
                driver.findElement(By.name("q")).sendKeys(searchTerm + Keys.ENTER);
            }
            System.out.println(taskName + " - Action 3: Searching for [" + searchTerm + "]");

            // ACTION 4: Capture URL after search
            Thread.sleep(3000); // Wait for results to load
            System.out.println(taskName + " - Action 4: New URL: " + driver.getCurrentUrl());

            // ACTION 5: Browser Command (Refresh)
            driver.navigate().refresh();
            System.out.println(taskName + " - Action 5: Page Refreshed successfully.");

        } catch (Exception e) {
            System.out.println("Exception in " + taskName + ": " + e.getMessage());
        } finally {
            System.out.println("Finished actions for " + taskName);
            driver.quit(); // Ensure browser closes
        }
    }
}

public class ParallelTest {
    public static void main(String[] args) {
        // Updated to use YouTube as the third browser
        Runner t1 = new Runner("Thread-Google", "https://www.google.com", "Selenium Multithreading");
        Runner t2 = new Runner("Thread-Amazon", "https://www.amazon.in", "Java Programming Books");
        Runner t3 = new Runner("Thread-YouTube", "https://www.youtube.com", "Java Lambda Expressions");

        // Start all 3 simultaneously
        t1.start();
        t2.start();
        t3.start();
    }
}