package org.ssssssss.script.functions.linq;

import org.ssssssss.script.annotation.Comment;
import org.ssssssss.script.annotation.Function;
import org.ssssssss.script.functions.DateExtension;
import org.ssssssss.script.functions.TemporalAccessorExtension;

import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * Linq中的函数
 */
public class LinqFunctions {

	@Function
	@Comment("判断值是否为空")
	public Object ifnull(@Comment(name = "target", value = "目标值") Object target,
						 @Comment(name = "trueValue", value = "为空的值") Object trueValue) {
		return target == null ? trueValue : target;
	}

	@Function
	@Comment("日期格式化")
	public String date_format(@Comment(name = "target", value = "目标日期") Date target,
							  @Comment(name = "pattern", value = "格式") String pattern) {
		return target == null ? null : DateExtension.format(target, pattern);
	}

	@Function
	@Comment("日期格式化")
	public String date_format(@Comment(name = "target", value = "目标日期") Date target) {
		return target == null ? null : DateExtension.format(target, "yyyy-MM-dd HH:mm:ss");
	}


	@Function
	@Comment("日期格式化")
	public String date_format(@Comment(name = "target", value = "目标日期") TemporalAccessor target,
							  @Comment(name = "pattern", value = "格式") String pattern) {
		return target == null ? null : TemporalAccessorExtension.format(target, pattern);
	}

	@Function
	@Comment("取当前时间")
	public Date now() {
		return new Date();
	}

	@Function
	@Comment("取当前时间戳(秒)")
	public long current_timestamp() {
		return System.currentTimeMillis() / 1000;
	}

	@Function
	@Comment("取当前时间戳(毫秒)")
	public long current_timestamp_millis() {
		return System.currentTimeMillis();
	}

}
