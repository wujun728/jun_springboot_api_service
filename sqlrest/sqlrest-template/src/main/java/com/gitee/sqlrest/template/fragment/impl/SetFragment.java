package com.gitee.sqlrest.template.fragment.impl;

import com.gitee.sqlrest.template.fragment.SqlFragment;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Wen
 * 
 */
public class SetFragment extends TrimFragment {

	private static List<String> suffixList = Arrays.asList(",");

	public SetFragment(SqlFragment contents) {
		super(contents, "SET", null, null, suffixList);
	}

}
