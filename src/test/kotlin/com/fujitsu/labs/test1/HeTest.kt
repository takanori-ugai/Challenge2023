package com.fujitsu.labs.test1

import org.junit.Assert.assertEquals

class HeTest {

    @org.junit.Test
    fun he2() {
        val heTest = He()
        assertEquals("11", heTest.he2("1"))
    }
}
