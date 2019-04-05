# Writing a test in Java

```java
public class MainActivityScreenshotTest {

    @Rule
    public ScreenshotRule rule = new ScreenshotRule<>(MainActivity.class);

    @ScreenshotInstrumentation
    @Test
    public void testDefault() {
        rule.assertSame();
    }
}
```