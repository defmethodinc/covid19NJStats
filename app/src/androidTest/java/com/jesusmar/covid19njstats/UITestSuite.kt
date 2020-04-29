package com.jesusmar.covid19njstats

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)

@Suite.SuiteClasses(
    MainActivityTest::class,
    GetDataFromAPITaskTest::class
)

class UITestSuite {

}