package com.shopify.testify.interfaces;

public interface ScreenshotTestModifications {


    ScreenshotTestModifications setViewModifications(ViewModification viewModification);

    ScreenshotTestModifications setScreenshotViewProvider(ViewProvider viewProvider);

    ScreenshotTestModifications setEspressoActions(EspressoActions espressoActions);

    ScreenshotTestModifications setHideSoftKeyboard(boolean hideSoftKeyboard);

    ScreenshotTestModifications setHideScrollbars(boolean hideScrollbars);

    ScreenshotTestModifications setHidePasswords(boolean hidePasswords);

    ScreenshotTestModifications setHideCursor(boolean hideCursor);

    ScreenshotTestModifications setHideTextSuggestions(boolean hideTextSuggestions);

    ScreenshotTestModifications setUseSoftwareRenderer(boolean useSoftwareRenderer);

}
