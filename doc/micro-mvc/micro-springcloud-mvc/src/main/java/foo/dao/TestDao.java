package foo.dao;

import java.util.List;
import java.util.Map;

import com.nh.micro.dao.mapper.InjectDao;
import com.nh.micro.dao.mapper.ListInnerClass;
import com.nh.micro.dao.mapper.MicroCommonMapper;
import com.nh.micro.dao.mapper.MicroPageInfo;
import com.nh.micro.orm.MicroDbName;

import foo.dto.MicroTestDto;


/**
 * 
 * @author ninghao
 *
 */
@InjectDao
@MicroDbName
public interface TestDao extends MicroCommonMapper<MicroTestDto> {
	
	public int updateInfo(Map paramMap);
	
	public int insertInfo(Map paramMap);
	
	@ListInnerClass(name=MicroTestDto.class)
	public List<MicroTestDto> queryInfosByPage(Map paramMap,MicroPageInfo pageInfo);	

	public MicroTestDto queryInfoById(Map paramMap);	


}
