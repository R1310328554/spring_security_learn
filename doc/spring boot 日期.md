# 

	private void addFormatters() {
		addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
		if (JSR_354_PRESENT) {
			addFormatter(new CurrencyUnitFormatter());
			addFormatter(new MonetaryAmountFormatter());
			addFormatterForFieldAnnotation(
					new Jsr354NumberFormatAnnotationFormatterFactory());
		}
		registerJsr310();
		if (JODA_TIME_PRESENT) {
			registerJodaTime();
		}
		registerJavaDate();
	}




	public static void addDefaultFormatters(FormatterRegistry formatterRegistry) {
		// Default handling of number values
		formatterRegistry.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

		// Default handling of monetary values
		if (jsr354Present) {
			formatterRegistry.addFormatter(new CurrencyUnitFormatter());
			formatterRegistry.addFormatter(new MonetaryAmountFormatter());
			formatterRegistry.addFormatterForFieldAnnotation(new Jsr354NumberFormatAnnotationFormatterFactory());
		}

		// Default handling of date-time values

		// just handling JSR-310 specific date and time types
		new DateTimeFormatterRegistrar().registerFormatters(formatterRegistry);

		if (jodaTimePresent) {
			// handles Joda-specific types as well as Date, Calendar, Long
			new JodaTimeFormatterRegistrar().registerFormatters(formatterRegistry);
		}
		else {
			// regular DateFormat-based Date, Calendar, Long converters
			new DateFormatterRegistrar().registerFormatters(formatterRegistry);
		}
	}
	
	
	默认jsr354Present、 jodaTimePresent都是false； jsr354Present 就是；  
		jodaTimePresent就是  org.joda.time.开头的那些日期类
		jodaTimePresent就是 javax.money.开头的类， 包括CurrencyUnitFormatter、 MonetaryAmountFormatter、
		
	DateFormatterRegistrar 包含如DateToLongConverter 之类的转换器，但不包含java.lang.String -> java.util.Date ； 以及下面的！
	DateTimeFormatAnnotationFormatterFactory 就是 对Date、Calendar、Long 3个类型字段，同时包含DateTimeFormat注解的格式化！
	
	DateTimeFormatterRegistrar 包含了 java.time 的一下类， 如 LocalTime、Instant、 Period、Duration、Year、Month
	
	addDefaultFormatters 其实是添加那些 不需要 格式的 转换器！
	DateTimeFormat 
	
