package org.ssssssss.script.functions;

import org.ssssssss.script.annotation.Comment;

import java.util.regex.Pattern;

/**
 * String 扩展
 */
public class StringExtension {

	@Comment("校验文本是否符合正则")
	public boolean match(String source, @Comment(name = "pattern", value = "正则表达式") Pattern pattern) {
		return pattern.matcher(source).find();
	}

	@Comment("正则替换字符串")
	public String replace(String source, @Comment(name = "pattern", value = "正则表达式") Pattern pattern,
						  @Comment(name = "replacement", value = "替换字符串") String replacement){
		return pattern.matcher(source).replaceAll(replacement);
	}
}
