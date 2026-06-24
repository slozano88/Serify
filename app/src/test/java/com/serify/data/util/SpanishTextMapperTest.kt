package com.serify.data.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SpanishTextMapperTest {

    @Test
    fun genre_translatesKnownValuesIgnoringCaseAndSpaces() {
        assertEquals("Acción", SpanishTextMapper.genre("  ACTION "))
        assertEquals("Ciencia ficción", SpanishTextMapper.genre("sci-fi"))
    }

    @Test
    fun genre_keepsUnknownValues() {
        assertEquals("Documentary", SpanishTextMapper.genre("Documentary"))
    }

    @Test
    fun status_translatesApiValues() {
        assertEquals("En emisión", SpanishTextMapper.status("running"))
        assertEquals("Finalizada", SpanishTextMapper.status("ENDED"))
        assertEquals("Por determinar", SpanishTextMapper.status("to be determined"))
    }

    @Test
    fun status_preservesNull() {
        assertNull(SpanishTextMapper.status(null))
    }
}
