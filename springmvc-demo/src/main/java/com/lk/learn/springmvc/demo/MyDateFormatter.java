package com.lk.learn.springmvc.demo;

import org.springframework.format.Formatter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//public class MyDateFormatter implements Formatter {
    /*
     * Copyright 2002-2018 the original author or authors.
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

    /**
     * A formatter for {@link java.util.Date} types.
     * Allows the configuration of an explicit date pattern and locale.
     *
     * @author Keith Donald
     * @author Juergen Hoeller
     * @author Phillip Webb
     * @since 3.0
     * @see SimpleDateFormat
     */
public class MyDateFormatter implements Formatter<Date> {

        private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

        private static final Map<String, String> ISO_PATTERNS;

        static {
            Map<String, String> formats = new HashMap<>();
            formats.put("0", "yyyy-MM");
//            formats.put("1", "yyyy.MM.dd");
            formats.put("2", "yyyy/MM/dd");
            formats.put("3", "HH:mm:ss.SSSXXX");
            // formats.put("3", "HH:mm:ss.SSSXXX");
            formats.put("4", "yyyy-MM-dd HH:mm:ss.SSSXXX");
            formats.put("5", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            formats.put("6", "yyyy-MM-dd'T'HH:mm:ss");
            formats.put("7", "yyyy-MM-dd'T'HH:mm:ss.SSS");
            ISO_PATTERNS = Collections.unmodifiableMap(formats);
        }


        @Nullable
        private String pattern = "yyyy-MM-dd";

        private int style = DateFormat.DEFAULT;

        @Nullable
        private String stylePattern;

        @Nullable
        private String iso;

        @Nullable
        private TimeZone timeZone;

        private boolean lenient = false;


        /**
         * Create a new default DateFormatter.
         */
        public MyDateFormatter() {
        }

        /**
         * Create a new DateFormatter for the given date pattern.
         */
        public MyDateFormatter(String pattern) {
            this.pattern = pattern;
        }


        /**
         * Set the pattern to use to format date values.
         * <p>If not specified, DateFormat's default style will be used.
         */
        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        /**
         * Set the style to use to format date values.
         * <p>If not specified, DateFormat's default style will be used.
         * @see DateFormat#DEFAULT
         * @see DateFormat#SHORT
         * @see DateFormat#MEDIUM
         * @see DateFormat#LONG
         * @see DateFormat#FULL
         */
        public void setStyle(int style) {
            this.style = style;
        }

        /**
         * Set the two character to use to format date values. The first character used for
         * the date style, the second is for the time style. Supported characters are
         * <ul>
         * <li>'S' = Small</li>
         * <li>'M' = Medium</li>
         * <li>'L' = Long</li>
         * <li>'F' = Full</li>
         * <li>'-' = Omitted</li>
         * </ul>
         * This method mimics the styles supported by Joda-Time.
         * @param stylePattern two characters from the set {"S", "M", "L", "F", "-"}
         * @since 3.2
         */
        public void setStylePattern(String stylePattern) {
            this.stylePattern = stylePattern;
        }

        /**
         * Set the TimeZone to normalize the date values into, if any.
         */
        public void setTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
        }

        /**
         * Specify whether or not parsing is to be lenient. Default is false.
         * <p>With lenient parsing, the parser may allow inputs that do not precisely match the format.
         * With strict parsing, inputs must match the format exactly.
         */
        public void setLenient(boolean lenient) {
            this.lenient = lenient;
        }


        @Override
        public String print(Date date, Locale locale) {
            return getDateFormat(locale).format(date);
        }

        @Override
        public Date parse(String text, Locale locale) throws ParseException {
//            return getDateFormat(locale).parse(text);
            Date parse = null;
            try {
                if (StringUtils.hasLength(this.pattern)) {
                    parse = new SimpleDateFormat(this.pattern, locale).parse(text);
                }
            } catch (ParseException e) {
                // e.printStackTrace();

                Collection<String> values = ISO_PATTERNS.values();
                for (Iterator<String> iterator = values.iterator(); iterator.hasNext(); ) {
                    String next =  iterator.next();
                    System.out.println("next = " + next);
                    try {
                        parse =  new SimpleDateFormat(next, locale).parse(text);
                        break;
                    } catch (ParseException e2) {
                        // e2.printStackTrace();
                    }
                }
            }
            return parse;
        }


        protected DateFormat getDateFormat(Locale locale) {
            DateFormat dateFormat = createDateFormat(locale);
            if (this.timeZone != null) {
                dateFormat.setTimeZone(this.timeZone);
            }
            dateFormat.setLenient(this.lenient);
            return dateFormat;
        }

        private DateFormat createDateFormat(Locale locale) {
            if (StringUtils.hasLength(this.pattern)) {
                return new SimpleDateFormat(this.pattern, locale);
            }
            Collection<String> values = ISO_PATTERNS.values();
            for (Iterator<String> iterator = values.iterator(); iterator.hasNext(); ) {
                String next =  iterator.next();
                System.out.println("next = " + next);
            }

            return DateFormat.getDateInstance(this.style, locale);
        }

        private int getStylePatternForChar(int index) {
            if (this.stylePattern != null && this.stylePattern.length() > index) {
                switch (this.stylePattern.charAt(index)) {
                    case 'S': return DateFormat.SHORT;
                    case 'M': return DateFormat.MEDIUM;
                    case 'L': return DateFormat.LONG;
                    case 'F': return DateFormat.FULL;
                    case '-': return -1;
                }
            }
            throw new IllegalStateException("Unsupported style pattern '" + this.stylePattern + "'");
        }

    }
