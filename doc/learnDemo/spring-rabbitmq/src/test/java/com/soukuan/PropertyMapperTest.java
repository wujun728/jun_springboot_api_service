package com.soukuan;

import com.soukuan.bean.A;
import com.soukuan.domain.B;
import com.soukuan.domain.C;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PropertyMapperTest {

    @Test
    public void sendCallBack() {
          PropertyMapper map = PropertyMapper.get();
          A a = new A();
          a.setA1("a1");
          a.setA1("a2");
          B b = new B();
          b.setB1("b1");
          b.setB1("b1");
          map.from(a::getA1).to(b::setB1);
          System.out.println(b.getB1());
    }

}