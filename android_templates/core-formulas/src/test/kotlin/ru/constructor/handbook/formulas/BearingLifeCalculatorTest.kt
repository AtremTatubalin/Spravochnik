package ru.constructor.handbook.formulas

import kotlin.test.Test
import kotlin.test.assertEquals

class BearingLifeCalculatorTest {
    @Test fun ratioTwoBallBearing() {
        val result = BearingLifeCalculator().calculate(BearingLifeInput(20_000.0, 10_000.0, 3.0, 1_000.0))
        assertEquals(8.0, result.output.lifeMillionRevolutions, 1e-12)
        assertEquals(133.3333333333, result.output.lifeHours, 1e-9)
    }
}
