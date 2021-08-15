package cn.edu.scu.virjarjcd.util.springdom;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Propert {
	private String name;
	private AbstractBean value;
}
