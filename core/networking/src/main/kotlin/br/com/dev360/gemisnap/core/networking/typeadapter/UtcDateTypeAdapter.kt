package br.com.dev360.gemisnap.core.networking.typeadapter

import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.text.ParseException
import java.text.ParsePosition
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



class UtcDateTypeAdapter : TypeAdapter<Date?>() {
    private val UTC_TIME_ZONE = TimeZone.getTimeZone("UTC")

    @Throws(IOException::class)
    override fun write(out: JsonWriter, date: Date?) {
        if (date == null) {
            out.nullValue()
        } else {
            val value = format(date, true, UTC_TIME_ZONE)
            out.value(value)
        }
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Date? {
        return try {
            when (`in`.peek()) {
                JsonToken.NULL -> {
                    `in`.nextNull()
                    null
                }
                else -> {
                    val date = `in`.nextString()
                    // Instead of using iso8601Format.parse(value), we use Jackson's date parsing
                    // This is because Android doesn't support XXX because it is JDK 1.6
                    parse(date, ParsePosition(0))
                }
            }
        } catch (e: ParseException) {
            throw JsonParseException(e)
        }
    }

    companion object {
        // Date parsing code from Jackson databind ISO8601Utils.java
        // https://github.com/FasterXML/jackson-databind/blob/2.8/src/main/java/com/fasterxml/jackson/databind/util/ISO8601Utils.java
        private const val GMT_ID = "GMT"

        /**
         * Format date into yyyy-MM-ddThh:mm:ss[.sss][Z|[+-]hh:mm]
         *
         * @param date the date to format
         * @param millis true to include millis precision otherwise false
         * @param tz timezone to use for the formatting (GMT will produce 'Z')
         * @return the date formatted as yyyy-MM-ddThh:mm:ss[.sss][Z|[+-]hh:mm]
         */
        private fun format(date: Date, millis: Boolean, tz: TimeZone): String {
            val calendar: Calendar = GregorianCalendar(tz, Locale.US)
            calendar.time = date

            // estimate capacity of buffer as close as we can (yeah, that's pedantic ;)
            var capacity = "yyyy-MM-ddThh:mm:ss".length
            capacity += if (millis) ".sss".length else 0
            capacity += if (tz.rawOffset == 0) "Z".length else "+hh:mm".length
            val formatted = StringBuilder(capacity)
            padInt(formatted, calendar[Calendar.YEAR], "yyyy".length)
            formatted.append('-')
            padInt(formatted, calendar[Calendar.MONTH] + 1, "MM".length)
            formatted.append('-')
            padInt(
                formatted,
                calendar[Calendar.DAY_OF_MONTH], "dd".length
            )
            formatted.append('T')
            padInt(
                formatted,
                calendar[Calendar.HOUR_OF_DAY], "hh".length
            )
            formatted.append(':')
            padInt(formatted, calendar[Calendar.MINUTE], "mm".length)
            formatted.append(':')
            padInt(formatted, calendar[Calendar.SECOND], "ss".length)
            if (millis) {
                formatted.append('.')
                padInt(
                    formatted,
                    calendar[Calendar.MILLISECOND], "sss".length
                )
            }
            val offset = tz.getOffset(calendar.timeInMillis)
            if (offset != 0) {
                val hours = Math.abs(offset / (60 * 1000) / 60)
                val minutes = Math.abs(offset / (60 * 1000) % 60)
                formatted.append(if (offset < 0) '-' else '+')
                padInt(formatted, hours, "hh".length)
                formatted.append(':')
                padInt(formatted, minutes, "mm".length)
            } else {
                formatted.append('Z')
            }
            return formatted.toString()
        }

        /**
         * Zero pad a number to a specified length
         *
         * @param buffer buffer to use for padding
         * @param value the integer value to pad if necessary.
         * @param length the length of the string we should zero pad
         */
        private fun padInt(buffer: StringBuilder, value: Int, length: Int) {
            val strValue = Integer.toString(value)
            for (i in length - strValue.length downTo 1) {
                buffer.append('0')
            }
            buffer.append(strValue)
        }

        /**
         * Parse a date from ISO-8601 formatted string. It expects a format
         * [yyyy-MM-dd|yyyyMMdd][T(hh:mm[:ss[.sss]]|hhmm[ss[.sss]])]?[Z|[+-]hh:mm]]
         *
         * @param date ISO string to parse in the appropriate format.
         * @param pos The position to start parsing from, updated to where parsing stopped.
         * @return the parsed date
         * @throws ParseException if the date is not in the appropriate format
         */
        @Throws(ParseException::class)
        private fun parse(date: String?, pos: ParsePosition): Date {
            var fail: Exception? = null
            try {
                var offset = pos.index

                // extract year
                val year = parseInt(date, offset, 4.let { offset += it; offset })
                if (checkOffset(date, offset, '-')) {
                    offset += 1
                }

                // extract month
                val month = parseInt(date, offset, 2.let { offset += it; offset })
                if (checkOffset(date, offset, '-')) {
                    offset += 1
                }

                // extract day
                val day = parseInt(date, offset, 2.let { offset += it; offset })
                // default time value
                var hour = 0
                var minutes = 0
                var seconds = 0
                var milliseconds =
                    0 // always use 0 otherwise returned date will include millis of current time
                if (checkOffset(date, offset, 'T')) {

                    // extract hours, minutes, seconds and milliseconds
                    hour = parseInt(
                        date,
                        1.let { offset += it; offset },
                        2.let { offset += it; offset })
                    if (checkOffset(date, offset, ':')) {
                        offset += 1
                    }
                    minutes = parseInt(date, offset, 2.let { offset += it; offset })
                    if (checkOffset(date, offset, ':')) {
                        offset += 1
                    }
                    // second and milliseconds can be optional
                    if (date!!.length > offset) {
                        val c = date[offset]
                        if (c != 'Z' && c != '+' && c != '-') {
                            seconds = parseInt(date, offset, 2.let { offset += it; offset })
                            // milliseconds can be optional in the format
                            if (checkOffset(date, offset, '.')) {
                                milliseconds = parseInt(
                                    date,
                                    1.let { offset += it; offset },
                                    3.let { offset += it; offset })
                            }
                        }
                    }
                }

                // extract timezone
                val timezoneId: String
                require(date!!.length > offset) { "No time zone indicator" }
                val timezoneIndicator = date[offset]
                if (timezoneIndicator == '+' || timezoneIndicator == '-') {
                    val timezoneOffset = date.substring(offset)
                    timezoneId = GMT_ID + timezoneOffset
                    offset += timezoneOffset.length
                } else if (timezoneIndicator == 'Z') {
                    timezoneId = GMT_ID
                    offset += 1
                } else {
                    throw IndexOutOfBoundsException("Invalid time zone indicator $timezoneIndicator")
                }
                val timezone = TimeZone.getTimeZone(timezoneId)
                if (timezone.id != timezoneId) {
                    throw IndexOutOfBoundsException()
                }
                val calendar: Calendar = GregorianCalendar(timezone)
                calendar.isLenient = false
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month - 1
                calendar[Calendar.DAY_OF_MONTH] = day
                calendar[Calendar.HOUR_OF_DAY] = hour
                calendar[Calendar.MINUTE] = minutes
                calendar[Calendar.SECOND] = seconds
                calendar[Calendar.MILLISECOND] = milliseconds
                pos.index = offset
                return calendar.time
                // If we get a ParseException it'll already have the right message/offset.
                // Other exception types can convert here.
            } catch (e: IndexOutOfBoundsException) {
                fail = e
            } catch (e: NumberFormatException) {
                fail = e
            } catch (e: IllegalArgumentException) {
                fail = e
            }
            val input = if (date == null) null else "'$date'"
            throw ParseException(
                "Failed to parse date [" + input + "]: " + fail!!.message,
                pos.index
            )
        }

        /**
         * Check if the expected character exist at the given offset in the value.
         *
         * @param value the string to check at the specified offset
         * @param offset the offset to look for the expected character
         * @param expected the expected character
         * @return true if the expected character exist at the given offset
         */
        private fun checkOffset(value: String?, offset: Int, expected: Char): Boolean {
            return offset < value!!.length && value[offset] == expected
        }

        /**
         * Parse an integer located between 2 given offsets in a string
         *
         * @param value the string to parse
         * @param beginIndex the start index for the integer in the string
         * @param endIndex the end index for the integer in the string
         * @return the int
         * @throws NumberFormatException if the value is not a number
         */
        @Throws(NumberFormatException::class)
        private fun parseInt(value: String?, beginIndex: Int, endIndex: Int): Int {
            if (beginIndex < 0 || endIndex > value!!.length || beginIndex > endIndex) {
                throw NumberFormatException(value)
            }
            // use same logic as in Integer.parseInt() but less generic we're not supporting negative values
            var i = beginIndex
            var result = 0
            var digit: Int
            if (i < endIndex) {
                digit = value[i++].digitToIntOrNull() ?: -1
                if (digit < 0) {
                    throw NumberFormatException("Invalid number: $value")
                }
                result = -digit
            }
            while (i < endIndex) {
                digit = value[i++].digitToIntOrNull() ?: -1
                if (digit < 0) {
                    throw NumberFormatException("Invalid number: $value")
                }
                result *= 10
                result -= digit
            }
            return -result
        }
    }
}