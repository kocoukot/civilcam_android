package com.civilcam

import com.civilcam.ext_features.ext.clearPhone
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun matchedPhone() {
        val phoneList = listOf(
            "+7 960 626-17-08",
            "+1 319-465-4654",
            "+1 313-929-3992",
            "+998 97 111 98 51",
            "+1 319-655-7951"
        )
        val responseList = listOf(
            "3139293992",
            "3198765462",
        )

        val filteredList = phoneList.filter {
            it.clearPhone().takeLast(10) in responseList
        }
        assertEquals(true, filteredList.size == 1)
    }
}