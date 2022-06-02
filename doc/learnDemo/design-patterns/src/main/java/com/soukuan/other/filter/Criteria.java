package com.soukuan.other.filter;

import java.util.List;

public interface Criteria {
   public List<Person> meetCriteria(List<Person> persons);
}