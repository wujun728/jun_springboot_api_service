package com.itboy.util;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

/**
 * @ClassName: ScheduleUtils
 * @Description: 定时任务类
 * @author lixk
 * @date 2017年8月16日 下午1:27:13
 * @version [1.0, 2017年8月16日]
 * @since version 1.0
 */
@Slf4j
public class ScheduleUtils {

	private ScheduleUtils() {
	}

	// task集合
	private static final Map<String, Task> TASK_MANAGER = new HashMap<String, Task>();
	// 定时器线程池
	private static final ScheduledExecutorService EXECUTOR_POOL = Executors.newScheduledThreadPool(10);
	// 定时任务队列
	private static final BlockingQueue<Task> TASK_QUEUE = new LinkedBlockingQueue<Task>();


	// 静态初始化方法
	static {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Task task = TASK_QUEUE.take();
						// 任务有效，则执行任务
						if (task.isEffective()) {
							task.execute();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		executor.shutdown();
	}

	/**
	 * 
	 * @Title: add
	 * @Description: 添加定时任务
	 * @param job
	 * @throws Exception
	 */
	public synchronized static void add(Job job) throws Exception {
		cancel(job); // 终结执行中的任务
		Task task = new Task(TASK_QUEUE, EXECUTOR_POOL, job.getClassName(), job.getMethodName(), job.getCron(),job.getJobName());
		TASK_MANAGER.put(job.getJobName(), task);
		// 将任务加入队列
		TASK_QUEUE.put(task);
	}

	/**
	 * 
	 * @Title: cancel
	 * @Description: 取消任务
	 * @param job
	 */
	public synchronized static void cancel(Job job) {
		if (job == null) {
			return;
		}
		String jobName = job.getJobName();
		if (jobName == null) {
			return;
		}
		Task task = TASK_MANAGER.get(jobName);
		if (task != null) {
			// 关闭任务，停止任务线程
			task.setEffective(false);
			ScheduledFuture<?> future = task.getFuture();
			if (future != null) {
				future.cancel(true);
			}
		}
		TASK_MANAGER.remove(jobName);
	}

	/**
	 * 
	 * @ClassName: Task
	 * @Description: 任务内部类
	 * @author lixk
	 * @date 2017年8月16日 下午7:38:44
	 * @version [1.0, 2017年8月16日]
	 * @since version 1.0
	 */
	private static class Task {
		private BlockingQueue<Task> queue; // 任务队列
		private CronTrigger trigger; // cron触发器
		private ScheduledExecutorService executor; // 定时器线程池
		private Class<?> clazz; // 反射类名
		private Object targetObject; // 反射对象
		private Method method; // 反射方法
		private Task self; // task对象自己
		private ScheduledFuture<?> future; // task对象的future
		private boolean effective = true; // task对象状态
		private String jobName;

		public Task(BlockingQueue<Task> queue, ScheduledExecutorService executor, String className, String methodName, String cron,String jobName) throws Exception {
			this.queue = queue;
			this.executor = executor;
			this.trigger = new CronTrigger(cron);
			this.clazz = Class.forName(className);
			this.targetObject = clazz.newInstance();
			this.method = clazz.getDeclaredMethod(methodName,String.class);
			this.self = this;
			this.jobName = jobName;
		}

		public void execute() throws Exception {
			Date now = new Date();
			long delay = trigger.next(now).getTime() - now.getTime(); // 等待时间

			this.future = executor.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						method.invoke(targetObject,jobName);
					} catch (Exception e) {
						log.error("定时任务执行异常：", e);
					} finally{
						// 把当前任务加入队列
						try {
							queue.put(self);
						} catch (InterruptedException e) {
							log.error("添加定时任务到队列异常：", e);
						}
					}
				}
			}, delay, TimeUnit.MILLISECONDS);

		}

		public String getJobName() {
			return jobName;
		}

		public void setJobName(String jobName) {
			this.jobName = jobName;
		}

		public ScheduledFuture<?> getFuture() {
			return future;
		}

		public boolean isEffective() {
			return effective;
		}

		public void setEffective(boolean effective) {
			this.effective = effective;
		}
	}

	/********************************** 定时任务实体类 ************************************/
	/**
	 * 
	 * @ClassName: Job
	 * @Description: 定时任务实体类
	 * @author lixk
	 * @date 2017年8月14日 下午3:03:51
	 * @version [1.0, 2017年8月14日]
	 * @since version 1.0
	 */
	public static class Job {
		private String jobName; // 任务名称
		private String className; // 定时任务类名
		private String methodName; // 定时任务方法名
		private String cron; // 定时任务cron表达式
		private Integer status; // 定时任务状态

		public String getJobName() {
			return jobName;
		}

		public void setJobName(String jobName) {
			this.jobName = jobName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public String getCron() {
			return cron;
		}

		public void setCron(String cron) {
			this.cron = cron;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}
	}
}

/**************************************** cron表达式解析工具类 *******************************************/

/**
 * Date sequence generator for a
 * <a href="http://www.manpagez.com/man/5/crontab/">Crontab pattern</a>,
 * allowing clients to specify a pattern that the sequence matches.
 *
 * <p>
 * The pattern is a list of six single space-separated fields: representing
 * second, minute, hour, day, month, weekday. Month and weekday names can be
 * given as the first three letters of the English names.
 *
 * <p>
 * Example patterns:
 * <ul>
 * <li>"0 0 * * * *" = the top of every hour of every day.</li>
 * <li>"*&#47;10 * * * * *" = every ten seconds.</li>
 * <li>"0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.</li>
 * <li>"0 0 6,19 * * *" = 6:00 AM and 7:00 PM every day.</li>
 * <li>"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every
 * day.</li>
 * <li>"0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays</li>
 * <li>"0 0 0 25 12 ?" = every Christmas Day at midnight</li>
 * </ul>
 *
 * @author Dave Syer
 * @author Juergen Hoeller
 * @since 3.0
 * @see CronTrigger
 */
class CronTrigger {

	private final String expression;

	private final TimeZone timeZone;

	private final BitSet months = new BitSet(12);

	private final BitSet daysOfMonth = new BitSet(31);

	private final BitSet daysOfWeek = new BitSet(7);

	private final BitSet hours = new BitSet(24);

	private final BitSet minutes = new BitSet(60);

	private final BitSet seconds = new BitSet(60);

	/**
	 * Construct a {@link CronTrigger} from the pattern provided, using the
	 * default {@link TimeZone}.
	 * 
	 * @param expression
	 *            a space-separated list of time fields
	 * @throws IllegalArgumentException
	 *             if the pattern cannot be parsed
	 * @see TimeZone#getDefault()
	 */
	public CronTrigger(String expression) {
		this(expression, TimeZone.getDefault());
	}

	/**
	 * Construct a {@link CronTrigger} from the pattern provided, using the
	 * specified {@link TimeZone}.
	 * 
	 * @param expression
	 *            a space-separated list of time fields
	 * @param timeZone
	 *            the TimeZone to use for generated trigger times
	 * @throws IllegalArgumentException
	 *             if the pattern cannot be parsed
	 */
	public CronTrigger(String expression, TimeZone timeZone) {
		this.expression = expression;
		this.timeZone = timeZone;
		parse(expression);
	}

	/**
	 * Return the cron pattern that this sequence generator has been built for.
	 */
	String getExpression() {
		return this.expression;
	}

	/**
	 * Get the next {@link Date} in the sequence matching the Cron pattern and
	 * after the value provided. The return value will have a whole number of
	 * seconds, and will be after the input value.
	 * 
	 * @param date
	 *            a seed value
	 * @return the next value matching the pattern
	 */
	public Date next(Date date) {
		/*
		 * The plan:
		 * 
		 * 1 Start with whole second (rounding up if necessary)
		 * 
		 * 2 If seconds match move on, otherwise find the next match: 2.1 If
		 * next match is in the next minute then roll forwards
		 * 
		 * 3 If minute matches move on, otherwise find the next match 3.1 If
		 * next match is in the next hour then roll forwards 3.2 Reset the
		 * seconds and go to 2
		 * 
		 * 4 If hour matches move on, otherwise find the next match 4.1 If next
		 * match is in the next day then roll forwards, 4.2 Reset the minutes
		 * and seconds and go to 2
		 */

		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(this.timeZone);
		calendar.setTime(date);

		// First, just reset the milliseconds and try to calculate from there...
		calendar.set(Calendar.MILLISECOND, 0);
		long originalTimestamp = calendar.getTimeInMillis();
		doNext(calendar, calendar.get(Calendar.YEAR));

		if (calendar.getTimeInMillis() == originalTimestamp) {
			// We arrived at the original timestamp - round up to the next whole
			// second and try again...
			calendar.add(Calendar.SECOND, 1);
			doNext(calendar, calendar.get(Calendar.YEAR));
		}

		return calendar.getTime();
	}

	private void doNext(Calendar calendar, int dot) {
		List<Integer> resets = new ArrayList<Integer>();

		int second = calendar.get(Calendar.SECOND);
		List<Integer> emptyList = Collections.emptyList();
		int updateSecond = findNext(this.seconds, second, calendar, Calendar.SECOND, Calendar.MINUTE, emptyList);
		if (second == updateSecond) {
			resets.add(Calendar.SECOND);
		}

		int minute = calendar.get(Calendar.MINUTE);
		int updateMinute = findNext(this.minutes, minute, calendar, Calendar.MINUTE, Calendar.HOUR_OF_DAY, resets);
		if (minute == updateMinute) {
			resets.add(Calendar.MINUTE);
		} else {
			doNext(calendar, dot);
		}

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int updateHour = findNext(this.hours, hour, calendar, Calendar.HOUR_OF_DAY, Calendar.DAY_OF_WEEK, resets);
		if (hour == updateHour) {
			resets.add(Calendar.HOUR_OF_DAY);
		} else {
			doNext(calendar, dot);
		}

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int updateDayOfMonth = findNextDay(calendar, this.daysOfMonth, dayOfMonth, daysOfWeek, dayOfWeek, resets);
		if (dayOfMonth == updateDayOfMonth) {
			resets.add(Calendar.DAY_OF_MONTH);
		} else {
			doNext(calendar, dot);
		}

		int month = calendar.get(Calendar.MONTH);
		int updateMonth = findNext(this.months, month, calendar, Calendar.MONTH, Calendar.YEAR, resets);
		if (month != updateMonth) {
			if (calendar.get(Calendar.YEAR) - dot > 4) {
				throw new IllegalArgumentException("Invalid cron expression \"" + this.expression + "\" led to runaway search for next trigger");
			}
			doNext(calendar, dot);
		}

	}

	private int findNextDay(Calendar calendar, BitSet daysOfMonth, int dayOfMonth, BitSet daysOfWeek, int dayOfWeek, List<Integer> resets) {

		int count = 0;
		int max = 366;
		// the DAY_OF_WEEK values in java.util.Calendar start with 1 (Sunday),
		// but in the cron pattern, they start with 0, so we subtract 1 here
		while ((!daysOfMonth.get(dayOfMonth) || !daysOfWeek.get(dayOfWeek - 1)) && count++ < max) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
			dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			reset(calendar, resets);
		}
		if (count >= max) {
			throw new IllegalArgumentException("Overflow in day for expression \"" + this.expression + "\"");
		}
		return dayOfMonth;
	}

	/**
	 * Search the bits provided for the next set bit after the value provided,
	 * and reset the calendar.
	 * 
	 * @param bits
	 *            a {@link BitSet} representing the allowed values of the field
	 * @param value
	 *            the current value of the field
	 * @param calendar
	 *            the calendar to increment as we move through the bits
	 * @param field
	 *            the field to increment in the calendar (@see {@link Calendar}
	 *            for the static constants defining valid fields)
	 * @param lowerOrders
	 *            the Calendar field ids that should be reset (i.e. the ones of
	 *            lower significance than the field of interest)
	 * @return the value of the calendar field that is next in the sequence
	 */
	private int findNext(BitSet bits, int value, Calendar calendar, int field, int nextField, List<Integer> lowerOrders) {
		int nextValue = bits.nextSetBit(value);
		// roll over if needed
		if (nextValue == -1) {
			calendar.add(nextField, 1);
			reset(calendar, Arrays.asList(field));
			nextValue = bits.nextSetBit(0);
		}
		if (nextValue != value) {
			calendar.set(field, nextValue);
			reset(calendar, lowerOrders);
		}
		return nextValue;
	}

	/**
	 * Reset the calendar setting all the fields provided to zero.
	 */
	private void reset(Calendar calendar, List<Integer> fields) {
		for (int field : fields) {
			calendar.set(field, field == Calendar.DAY_OF_MONTH ? 1 : 0);
		}
	}

	// Parsing logic invoked by the constructor

	/**
	 * Parse the given pattern expression.
	 */
	private void parse(String expression) throws IllegalArgumentException {
		String[] fields = expression.split(" ");
		if (!areValidCronFields(fields)) {
			throw new IllegalArgumentException(String.format("Cron expression must consist of 6 fields (found %d in \"%s\")", fields.length, expression));
		}
		setNumberHits(this.seconds, fields[0], 0, 60);
		setNumberHits(this.minutes, fields[1], 0, 60);
		setNumberHits(this.hours, fields[2], 0, 24);
		setDaysOfMonth(this.daysOfMonth, fields[3]);
		setMonths(this.months, fields[4]);
		setDays(this.daysOfWeek, replaceOrdinals(fields[5], "SUN,MON,TUE,WED,THU,FRI,SAT"), 8);
		if (this.daysOfWeek.get(7)) {
			// Sunday can be represented as 0 or 7
			this.daysOfWeek.set(0);
			this.daysOfWeek.clear(7);
		}
	}

	/**
	 * Replace the values in the comma-separated list (case insensitive) with
	 * their index in the list.
	 * 
	 * @return a new String with the values from the list replaced
	 */
	private String replaceOrdinals(String value, String commaSeparatedList) {
		String[] list = commaSeparatedList.split(",");
		for (int i = 0; i < list.length; i++) {
			String item = list[i].toUpperCase();
			value = value.toUpperCase().replace(item, "" + i);
		}
		return value;
	}

	private void setDaysOfMonth(BitSet bits, String field) {
		int max = 31;
		// Days of month start with 1 (in Cron and Calendar) so add one
		setDays(bits, field, max + 1);
		// ... and remove it from the front
		bits.clear(0);
	}

	private void setDays(BitSet bits, String field, int max) {
		if (field.contains("?")) {
			field = "*";
		}
		setNumberHits(bits, field, 0, max);
	}

	private void setMonths(BitSet bits, String value) {
		int max = 12;
		value = replaceOrdinals(value, "FOO,JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC");
		BitSet months = new BitSet(13);
		// Months start with 1 in Cron and 0 in Calendar, so push the values
		// first into a longer bit set
		setNumberHits(months, value, 1, max + 1);
		// ... and then rotate it to the front of the months
		for (int i = 1; i <= max; i++) {
			if (months.get(i)) {
				bits.set(i - 1);
			}
		}
	}

	private void setNumberHits(BitSet bits, String value, int min, int max) {
		String[] fields = value.split(",");
		for (String field : fields) {
			if (!field.contains("/")) {
				// Not an incrementer so it must be a range (possibly empty)
				int[] range = getRange(field, min, max);
				bits.set(range[0], range[1] + 1);
			} else {
				String[] split = field.split("/");
				if (split.length > 2) {
					throw new IllegalArgumentException("Incrementer has more than two fields: '" + field + "' in expression \"" + this.expression + "\"");
				}
				int[] range = getRange(split[0], min, max);
				if (!split[0].contains("-")) {
					range[1] = max - 1;
				}
				int delta = Integer.valueOf(split[1]);
				if (delta <= 0) {
					throw new IllegalArgumentException("Incrementer delta must be 1 or higher: '" + field + "' in expression \"" + this.expression + "\"");
				}
				for (int i = range[0]; i <= range[1]; i += delta) {
					bits.set(i);
				}
			}
		}
	}

	private int[] getRange(String field, int min, int max) {
		int[] result = new int[2];
		if (field.contains("*")) {
			result[0] = min;
			result[1] = max - 1;
			return result;
		}
		if (!field.contains("-")) {
			result[0] = result[1] = Integer.valueOf(field);
		} else {
			String[] split = field.split("-");
			if (split.length > 2) {
				throw new IllegalArgumentException("Range has more than two fields: '" + field + "' in expression \"" + this.expression + "\"");
			}
			result[0] = Integer.valueOf(split[0]);
			result[1] = Integer.valueOf(split[1]);
		}
		if (result[0] >= max || result[1] >= max) {
			throw new IllegalArgumentException("Range exceeds maximum (" + max + "): '" + field + "' in expression \"" + this.expression + "\"");
		}
		if (result[0] < min || result[1] < min) {
			throw new IllegalArgumentException("Range less than minimum (" + min + "): '" + field + "' in expression \"" + this.expression + "\"");
		}
		if (result[0] > result[1]) {
			throw new IllegalArgumentException("Invalid inverted range: '" + field + "' in expression \"" + this.expression + "\"");
		}
		return result;
	}

	/**
	 * Determine whether the specified expression represents a valid cron
	 * pattern.
	 * <p>
	 * Specifically, this method verifies that the expression contains six
	 * fields separated by single spaces.
	 * 
	 * @param expression
	 *            the expression to evaluate
	 * @return {@code true} if the given expression is a valid cron expression
	 * @since 4.3
	 */
	public static boolean isValidExpression(String expression) {
		String[] fields = expression.split(" ");
		return areValidCronFields(fields);
	}

	private static boolean areValidCronFields(String[] fields) {
		return (fields != null && fields.length == 6);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CronTrigger)) {
			return false;
		}
		CronTrigger otherCron = (CronTrigger) other;
		return (this.months.equals(otherCron.months) && this.daysOfMonth.equals(otherCron.daysOfMonth) && this.daysOfWeek.equals(otherCron.daysOfWeek) && this.hours.equals(otherCron.hours) && this.minutes.equals(otherCron.minutes) && this.seconds.equals(otherCron.seconds));
	}

	@Override
	public int hashCode() {
		return (17 * this.months.hashCode() + 29 * this.daysOfMonth.hashCode() + 37 * this.daysOfWeek.hashCode() + 41 * this.hours.hashCode() + 53 * this.minutes.hashCode() + 61 * this.seconds.hashCode());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + this.expression;
	}


}