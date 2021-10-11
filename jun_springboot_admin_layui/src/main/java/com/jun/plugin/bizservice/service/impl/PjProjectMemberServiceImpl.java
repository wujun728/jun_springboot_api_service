package com.jun.plugin.bizservice.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jun.plugin.bizservice.mapper.PjProjectMemberMapper;
import com.jun.plugin.bizservice.entity.PjProjectMemberEntity;
import com.jun.plugin.bizservice.service.PjProjectMemberService;


@Service("pjProjectMemberService")
public class PjProjectMemberServiceImpl extends ServiceImpl<PjProjectMemberMapper, PjProjectMemberEntity> implements PjProjectMemberService {


}