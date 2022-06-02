package groovy.controller

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import java.sql.PreparedStatement;
import groovy.json.*;
import groovy.template.MicroMvcTemplate;

import com.nh.micro.db.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


class MatchMvcResultList extends MicroMvcTemplate{
	public String tableName="match_result";
	public String getTableName(HttpServletRequest httpRequest){
		return tableName;
	}

	



}
