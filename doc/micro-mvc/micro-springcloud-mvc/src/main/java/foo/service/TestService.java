package foo.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.nh.micro.service.InjectGroovy;

import foo.dto.MicroTestDto;

/**
 * 
 * @author ninghao
 *
 */
@InjectGroovy(name="TestService")
public interface TestService {
	
	public MicroTestDto test(String id);
	
	@Transactional
	public void create();
}
