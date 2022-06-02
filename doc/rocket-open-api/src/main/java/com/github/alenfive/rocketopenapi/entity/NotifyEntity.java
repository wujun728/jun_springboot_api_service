package com.github.alenfive.rocketopenapi.entity;

import com.github.alenfive.rocketapi.entity.vo.RefreshMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyEntity {
    private RefreshMapping refreshMapping;
    private String identity;
}
