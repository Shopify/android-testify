package com.shopify.testify

import org.gradle.api.tasks.TaskAction

class ShowTimeZoneTask extends TestifyDefaultTask {

    @Override
    String getDescription() {
        return "Shows the emulator time zone"
    }

    @Override
    def taskAction() {
        def showTimeZone = [DeviceUtility.getAdbPath(), '-e', 'shell', 'getprop', 'persist.sys.timezone']
        def log = showTimeZone.execute().text
        log.eachLine { line -> println line }
    }
}