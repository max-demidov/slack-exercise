package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Cucumber parallel test runner.
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"steps"},
    tags = "not @Ignore",
    plugin = {
      "pretty",
      "html:target/cucumber-reports/cucumber-pretty",
      "json:target/cucumber-reports/json-reports/CucumberTestReport.json",
      "rerun:target/cucumber-reports/rerun-reports/rerun.txt"
    })
public class RunCucumberTest extends AbstractTestNGCucumberTests {

  @DataProvider(parallel = true)
  public Object[][] scenarios() {
    return super.scenarios();
  }
}
