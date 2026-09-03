<template>
  <div class="test-page">
    <div class="page-header">
      <h1 class="page-title">Element Plus 深色主题测试</h1>
    </div>

    <!-- 顶部标签页导航 -->
    <div class="tab-navigation">
      <el-tabs v-model="activeTab" type="card" @tab-change="handleTabChange">
        <el-tab-pane label="按钮组件" name="button" />
        <el-tab-pane label="输入框组件" name="input" />
        <el-tab-pane label="下拉框组件" name="select" />
        <el-tab-pane label="时间日期选择器" name="datetime" />
        <el-tab-pane label="颜色选择器" name="color" />
        <el-tab-pane label="滑块组件" name="slider" />
        <el-tab-pane label="开关组件" name="switch" />
        <el-tab-pane label="上传组件" name="upload" />
        <el-tab-pane label="穿梭框组件" name="transfer" />
        <el-tab-pane label="进度条组件" name="progress" />
        <el-tab-pane label="标记组件" name="badge" />
        <el-tab-pane label="导航菜单" name="navmenu" />
        <el-tab-pane label="步骤条组件" name="steps" />
        <el-tab-pane label="气泡确认框" name="popconfirm" />
        <el-tab-pane label="走马灯组件" name="carousel" />
        <el-tab-pane label="折叠面板" name="collapse" />
        <el-tab-pane label="时间线组件" name="timeline" />
        <el-tab-pane label="分割线组件" name="divider" />
        <el-tab-pane label="日历组件" name="calendar" />
        <el-tab-pane label="表格组件" name="table" />
        <el-tab-pane label="表单组件" name="form" />
        <el-tab-pane label="卡片组件" name="card" />
        <el-tab-pane label="标签和消息" name="tag" />
        <el-tab-pane label="分页组件" name="pagination" />
        <el-tab-pane label="无限滚动" name="infinite" />
        <el-tab-pane label="图标组件" name="icon" />
        <el-tab-pane label="对话框组件" name="dialog" />
        <el-tab-pane label="抽屉组件" name="drawer" />
      </el-tabs>
    </div>

    <!-- 主内容区域 - 左右分栏布局 -->
    <div class="main-content">
      <!-- 左侧预览区域 -->
      <div class="preview-section">
        <div class="preview-header">
          <h3>组件预览</h3>
          <el-button 
            size="small" 
            @click="toggleCodePanel"
            :icon="showCodePanel ? 'Hide' : 'View'"
          >
            {{ showCodePanel ? '隐藏代码' : '显示代码' }}
          </el-button>
        </div>
        
        <div class="preview-content">
          <!-- 按钮组件预览 -->
          <div v-show="activeTab === 'button'" class="component-preview">
            <div class="preview-title">按钮组件预览</div>
            <div class="preview-area">
              <div class="button-group">
                <h4>主要按钮类型</h4>
                <div class="button-row">
                  <el-button type="primary">主要按钮</el-button>
                  <el-button type="success">成功按钮</el-button>
                  <el-button type="warning">警告按钮</el-button>
                  <el-button type="danger">危险按钮</el-button>
                  <el-button type="info">信息按钮</el-button>
                  <el-button>默认按钮</el-button>
                </div>
              </div>
              
              <div class="button-group">
                <h4>朴素按钮</h4>
                <div class="button-row">
                  <el-button type="primary" plain>朴素主要</el-button>
                  <el-button type="success" plain>朴素成功</el-button>
                  <el-button type="warning" plain>朴素警告</el-button>
                  <el-button type="danger" plain>朴素危险</el-button>
                  <el-button type="info" plain>朴素信息</el-button>
                  <el-button plain>朴素默认</el-button>
                </div>
              </div>
              
              <div class="button-group">
                <h4>圆形和圆角按钮</h4>
                <div class="button-row">
                  <el-button type="primary" circle>
                    <el-icon><Plus /></el-icon>
                  </el-button>
                  <el-button type="success" circle>
                    <el-icon><Check /></el-icon>
                  </el-button>
                  <el-button type="warning" circle>
                    <el-icon><Warning /></el-icon>
                  </el-button>
                  <el-button type="danger" circle>
                    <el-icon><Delete /></el-icon>
                  </el-button>
                  <el-button type="primary" round>圆角按钮</el-button>
                  <el-button type="success" round>圆角成功</el-button>
                </div>
              </div>
              
              <div class="button-group">
                <h4>加载和禁用状态</h4>
                <div class="button-row">
                  <el-button type="primary" :loading="true">加载中...</el-button>
                  <el-button type="success" :loading="true">保存中...</el-button>
                  <el-button type="primary" disabled>禁用按钮</el-button>
                  <el-button type="danger" disabled>禁用危险</el-button>
                </div>
              </div>
            </div>
          </div>

          <!-- 输入框组件预览 -->
          <div v-show="activeTab === 'input'" class="component-preview">
            <div class="preview-title">输入框组件预览</div>
            <div class="preview-area">
              <div class="input-group">
                <h4>基础输入框</h4>
                <div class="input-row">
                  <el-input placeholder="请输入内容" style="width: 300px;" />
                  <el-input placeholder="禁用状态" disabled style="width: 300px;" />
                  <el-input placeholder="只读状态" readonly style="width: 300px;" />
                </div>
              </div>
              
              <div class="input-group">
                <h4>带图标的输入框</h4>
                <div class="input-row">
                  <el-input placeholder="带前缀图标" style="width: 300px;">
                    <template #prefix>
                      <el-icon><Search /></el-icon>
                    </template>
                  </el-input>
                  <el-input placeholder="带后缀图标" style="width: 300px;">
                    <template #suffix>
                      <el-icon><Calendar /></el-icon>
                    </template>
                  </el-input>
                </div>
              </div>
              
              <div class="input-group">
                <h4>多行输入框</h4>
                <div class="input-row">
                  <el-input 
                    type="textarea" 
                    placeholder="请输入多行文本" 
                    :rows="3"
                    style="width: 400px;" 
                  />
                </div>
              </div>
              
              <div class="input-group">
                <h4>数字输入框</h4>
                <div class="input-row">
                  <el-input-number 
                    v-model="numberValue" 
                    :min="0" 
                    :max="100" 
                    style="width: 200px;" 
                  />
                  <el-input-number 
                    v-model="numberValue2" 
                    :precision="2" 
                    :step="0.1" 
                    style="width: 200px;" 
                  />
                </div>
              </div>
            </div>
          </div>

          <!-- 下拉框组件预览 -->
          <div v-show="activeTab === 'select'" class="component-preview">
            <div class="preview-title">下拉框组件预览</div>
            <div class="preview-area">
              <div class="select-group">
                <h4>基础下拉框</h4>
                <el-select v-model="basicSelect" placeholder="请选择" style="width: 300px;">
                  <el-option label="选项1" value="1" />
                  <el-option label="选项2" value="2" />
                  <el-option label="选项3" value="3" />
                  <el-option label="选项4" value="4" />
                  <el-option label="选项5" value="5" />
                </el-select>
              </div>

              <div class="select-group">
                <h4>多选下拉框</h4>
                <el-select v-model="multiSelect" multiple placeholder="多选下拉" style="width: 300px;">
                  <el-option label="多选选项1" value="multi1" />
                  <el-option label="多选选项2" value="multi2" />
                  <el-option label="多选选项3" value="multi3" />
                  <el-option label="多选选项4" value="multi4" />
                  <el-option label="多选选项5" value="multi5" />
                </el-select>
              </div>

              <div class="select-group">
                <h4>可搜索下拉框</h4>
                <el-select v-model="searchableSelect" filterable placeholder="可搜索下拉框" style="width: 300px;">
                  <el-option label="苹果" value="apple" />
                  <el-option label="香蕉" value="banana" />
                  <el-option label="橙子" value="orange" />
                  <el-option label="葡萄" value="grape" />
                  <el-option label="草莓" value="strawberry" />
                </el-select>
              </div>

              <div class="select-group">
                <h4>分组下拉框</h4>
                <el-select v-model="groupSelect" placeholder="分组下拉框" style="width: 300px;">
                  <el-option-group label="热门城市">
                    <el-option label="北京" value="beijing" />
                    <el-option label="上海" value="shanghai" />
                    <el-option label="广州" value="guangzhou" />
                  </el-option-group>
                  <el-option-group label="其他城市">
                    <el-option label="深圳" value="shenzhen" />
                    <el-option label="杭州" value="hangzhou" />
                    <el-option label="成都" value="chengdu" />
                  </el-option-group>
                </el-select>
              </div>
            </div>
          </div>

          <!-- 时间日期选择器预览 -->
          <div v-show="activeTab === 'datetime'" class="component-preview">
            <div class="preview-title">时间日期选择器预览</div>
            <div class="preview-area">
              <div class="datetime-group">
                <h4>日期选择器</h4>
                <el-date-picker
                  v-model="dateValue"
                  type="date"
                  placeholder="选择日期"
                  style="width: 300px;"
                />
              </div>

              <div class="datetime-group">
                <h4>时间选择器</h4>
                <el-time-picker
                  v-model="timeValue"
                  placeholder="选择时间"
                  style="width: 300px;"
                />
              </div>

              <div class="datetime-group">
                <h4>日期范围选择器</h4>
                <el-date-picker
                  v-model="dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  style="width: 400px;"
                />
              </div>

              <div class="datetime-group">
                <h4>日期时间选择器</h4>
                <el-date-picker
                  v-model="datetimeValue"
                  type="datetime"
                  placeholder="选择日期时间"
                  style="width: 300px;"
                />
              </div>
            </div>
          </div>

          <!-- 颜色选择器预览 -->
          <div v-show="activeTab === 'color'" class="component-preview">
            <div class="preview-title">颜色选择器预览</div>
            <div class="preview-area">
              <div class="color-group">
                <h4>基础颜色选择器</h4>
                <el-color-picker v-model="colorValue" />
              </div>

              <div class="color-group">
                <h4>带透明度颜色选择器</h4>
                <el-color-picker v-model="colorWithAlpha" show-alpha />
              </div>

              <div class="color-group">
                <h4>预定义颜色</h4>
                <el-color-picker 
                  v-model="predefinedColor" 
                  :predefine="predefineColors"
                />
              </div>

              <div class="color-group">
                <h4>不同尺寸</h4>
                <div class="color-sizes">
                  <el-color-picker v-model="colorValue" size="large" />
                  <el-color-picker v-model="colorValue" />
                  <el-color-picker v-model="colorValue" size="small" />
                </div>
              </div>
            </div>
          </div>

          <!-- 表格组件预览 -->
          <div v-show="activeTab === 'table'" class="component-preview">
            <div class="preview-title">表格组件预览</div>
            <div class="preview-area">
              <el-table :data="tableData" style="width: 100%">
                <el-table-column prop="name" label="姓名" />
                <el-table-column prop="age" label="年龄" />
                <el-table-column prop="address" label="地址" />
                <el-table-column label="操作">
                  <template #default>
                    <el-button size="small">编辑</el-button>
                    <el-button size="small" type="danger">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>

          <!-- 表单组件预览 -->
          <div v-show="activeTab === 'form'" class="component-preview">
            <div class="preview-title">表单组件预览</div>
            <div class="preview-area">
              <el-form :model="form" label-width="80px" style="max-width: 400px;">
                <el-form-item label="用户名">
                  <el-input v-model="form.name" />
                </el-form-item>
                <el-form-item label="邮箱">
                  <el-input v-model="form.email" />
                </el-form-item>
                <el-form-item label="角色">
                  <el-select v-model="form.role" placeholder="请选择角色">
                    <el-option label="管理员" value="admin" />
                    <el-option label="用户" value="user" />
                    <el-option label="访客" value="guest" />
                  </el-select>
                </el-form-item>
                <el-form-item label="状态">
                  <el-radio-group v-model="form.status">
                    <el-radio label="active">活跃</el-radio>
                    <el-radio label="inactive">禁用</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="记住我">
                  <el-checkbox v-model="form.remember">记住登录状态</el-checkbox>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary">提交</el-button>
                  <el-button>重置</el-button>
                </el-form-item>
              </el-form>
            </div>
          </div>

          <!-- 卡片组件预览 -->
          <div v-show="activeTab === 'card'" class="component-preview">
            <div class="preview-title">卡片组件预览</div>
            <div class="preview-area">
              <div class="card-group">
                <h4>基础卡片</h4>
                <div class="card-demo">
                  <el-card class="box-card">
                    <template #header>
                      <div class="card-header">
                        <span>卡片标题</span>
                        <el-button class="button" text>操作按钮</el-button>
                      </div>
                    </template>
                    <div v-for="i in 4" :key="i" class="text item">
                      {{ '列表内容 ' + i }}
                    </div>
                  </el-card>
                </div>
              </div>

              <div class="card-group">
                <h4>带图片的卡片</h4>
                <div class="card-demo">
                  <el-card :body-style="{ padding: '0px' }">
                    <img src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image" />
                    <div style="padding: 14px;">
                      <span>好吃的汉堡</span>
                      <div class="bottom">
                        <time class="time">{{ currentDate }}</time>
                        <el-button class="button" text>操作按钮</el-button>
                      </div>
                    </div>
                  </el-card>
                </div>
              </div>

              <div class="card-group">
                <h4>简单卡片</h4>
                <div class="card-demo">
                  <el-card>
                    <p>这是一个简单的卡片内容</p>
                    <p>可以包含任何内容</p>
                  </el-card>
                </div>
              </div>
            </div>
          </div>

          <!-- 标签和消息组件预览 -->
          <div v-show="activeTab === 'tag'" class="component-preview">
            <div class="preview-title">标签和消息组件预览</div>
            <div class="preview-area">
              <div class="tag-group">
                <h4>标签组件</h4>
                <div class="tag-demo">
                  <el-tag style="margin-right: 8px;">默认标签</el-tag>
                  <el-tag type="success" style="margin-right: 8px;">成功标签</el-tag>
                  <el-tag type="warning" style="margin-right: 8px;">警告标签</el-tag>
                  <el-tag type="danger" style="margin-right: 8px;">危险标签</el-tag>
                  <el-tag type="info" style="margin-right: 8px;">信息标签</el-tag>
                </div>
              </div>

              <div class="message-group">
                <h4>消息提示</h4>
                <div class="message-demo">
                  <el-button @click="showMessage('success')">成功消息</el-button>
                  <el-button @click="showMessage('warning')">警告消息</el-button>
                  <el-button @click="showMessage('error')">错误消息</el-button>
                  <el-button @click="showMessage('info')">信息消息</el-button>
                </div>
              </div>

              <div class="notification-group">
                <h4>通知组件</h4>
                <div class="notification-demo">
                  <el-button @click="showNotification('success')">成功通知</el-button>
                  <el-button @click="showNotification('warning')">警告通知</el-button>
                  <el-button @click="showNotification('error')">错误通知</el-button>
                  <el-button @click="showNotification('info')">信息通知</el-button>
                </div>
              </div>
            </div>
          </div>

          <!-- 分页组件预览 -->
          <div v-show="activeTab === 'pagination'" class="component-preview">
            <div class="preview-title">分页组件预览</div>
            <div class="preview-area">
              <div class="pagination-group">
                <h4>完整分页组件</h4>
                <el-pagination
                  background
                  v-model:current-page="currentPage"
                  v-model:page-size="pageSize"
                  :page-sizes="[10, 20, 50, 100]"
                  :total="100"
                  layout="total, sizes, prev, pager, next, jumper"
                />
              </div>
              
              <div class="pagination-group">
                <h4>简单分页组件</h4>
                <el-pagination
                  background
                  v-model:current-page="currentPage2"
                  :total="50"
                  layout="prev, pager, next"
                />
              </div>
              
              <div class="pagination-group">
                <h4>带总数分页组件</h4>
                <el-pagination
                  background
                  v-model:current-page="currentPage3"
                  :page-size="10"
                  :total="200"
                  layout="total, prev, pager, next"
                />
              </div>
            </div>
          </div>

          <!-- 无限滚动组件预览 -->
          <div v-show="activeTab === 'infinite'" class="component-preview">
            <div class="preview-title">无限滚动组件预览</div>
            <div class="preview-area">
              <div class="infinite-group">
                <h4>基础无限滚动</h4>
                <div class="infinite-demo">
                  <el-infinite-scroll 
                    :height="300" 
                    @load="loadMore"
                    :disabled="infiniteDisabled"
                  >
                    <div v-for="item in infiniteList" :key="item.id" class="infinite-item">
                      <el-card shadow="hover" style="margin-bottom: 10px;">
                        <div class="item-content">
                          <h4>项目 {{ item.id }}</h4>
                          <p>{{ item.content }}</p>
                        </div>
                      </el-card>
                    </div>
                    <div v-if="infiniteLoading" class="loading-text">加载中...</div>
                  </el-infinite-scroll>
                </div>
              </div>

              <div class="infinite-group">
                <h4>无限滚动列表</h4>
                <div class="infinite-demo">
                  <el-infinite-scroll 
                    :height="200" 
                    @load="loadMoreList"
                    :disabled="listDisabled"
                  >
                    <ul class="infinite-list">
                      <li v-for="item in infiniteListData" :key="item.id" class="infinite-list-item">
                        {{ item.text }}
                      </li>
                    </ul>
                    <div v-if="listLoading" class="loading-text">加载中...</div>
                  </el-infinite-scroll>
                </div>
              </div>
            </div>
          </div>

          <!-- 图标组件预览 -->
          <div v-show="activeTab === 'icon'" class="component-preview">
            <div class="preview-title">图标组件预览</div>
            <div class="preview-area">
              <div class="icon-group">
                <h4>基础图标</h4>
                <div class="icon-demo">
                  <el-icon><Edit /></el-icon>
                  <el-icon><Share /></el-icon>
                  <el-icon><Delete /></el-icon>
                  <el-icon><Search /></el-icon>
                  <el-icon><Setting /></el-icon>
                </div>
              </div>

              <div class="icon-group">
                <h4>不同尺寸的图标</h4>
                <div class="icon-demo">
                  <el-icon size="large"><Edit /></el-icon>
                  <el-icon><Edit /></el-icon>
                  <el-icon size="small"><Edit /></el-icon>
                </div>
              </div>

              <div class="icon-group">
                <h4>带颜色的图标</h4>
                <div class="icon-demo">
                  <el-icon color="#409EFF"><Edit /></el-icon>
                  <el-icon color="#67C23A"><Check /></el-icon>
                  <el-icon color="#E6A23C"><Warning /></el-icon>
                  <el-icon color="#F56C6C"><Delete /></el-icon>
                  <el-icon color="#909399"><InfoFilled /></el-icon>
                </div>
              </div>

              <div class="icon-group">
                <h4>图标在按钮中使用</h4>
                <div class="icon-demo">
                  <el-button type="primary" icon="Edit">编辑</el-button>
                  <el-button type="success" icon="Check">确认</el-button>
                  <el-button type="warning" icon="Warning">警告</el-button>
                  <el-button type="danger" icon="Delete">删除</el-button>
                </div>
              </div>

              <div class="icon-group">
                <h4>图标在输入框中使用</h4>
                <div class="icon-demo">
                  <el-input placeholder="带前缀图标" style="width: 300px;">
                    <template #prefix>
                      <el-icon><Search /></el-icon>
                    </template>
                  </el-input>
                  <el-input placeholder="带后缀图标" style="width: 300px;">
                    <template #suffix>
                      <el-icon><Calendar /></el-icon>
                    </template>
                  </el-input>
                </div>
              </div>
            </div>
          </div>

          <!-- 滑块组件预览 -->
          <div v-show="activeTab === 'slider'" class="component-preview">
            <div class="preview-title">滑块组件预览</div>
            <div class="preview-area">
              <div class="slider-group">
                <h4>基础滑块</h4>
                <div class="slider-demo">
                  <el-slider v-model="sliderValue" />
                  <p>当前值: {{ sliderValue }}</p>
                </div>
              </div>

              <div class="slider-group">
                <h4>带范围的滑块</h4>
                <div class="slider-demo">
                  <el-slider v-model="sliderRange" range />
                  <p>当前范围: {{ sliderRange }}</p>
                </div>
              </div>

              <div class="slider-group">
                <h4>带标记的滑块</h4>
                <div class="slider-demo">
                  <el-slider 
                    v-model="sliderWithMarks" 
                    :marks="sliderMarks"
                    :min="0"
                    :max="100"
                  />
                </div>
              </div>

              <div class="slider-group">
                <h4>不同尺寸的滑块</h4>
                <div class="slider-demo">
                  <el-slider v-model="sliderValue" size="large" />
                  <el-slider v-model="sliderValue" />
                  <el-slider v-model="sliderValue" size="small" />
                </div>
              </div>
            </div>
          </div>

          <!-- 开关组件预览 -->
          <div v-show="activeTab === 'switch'" class="component-preview">
            <div class="preview-title">开关组件预览</div>
            <div class="preview-area">
              <div class="switch-group">
                <h4>基础开关</h4>
                <div class="switch-demo">
                  <el-switch v-model="switchValue" />
                  <p>开关状态: {{ switchValue ? '开启' : '关闭' }}</p>
                </div>
              </div>

              <div class="switch-group">
                <h4>带文字的开关</h4>
                <div class="switch-demo">
                  <el-switch 
                    v-model="switchWithText"
                    active-text="开启"
                    inactive-text="关闭"
                  />
                </div>
              </div>

              <div class="switch-group">
                <h4>带图标的开关</h4>
                <div class="switch-demo">
                  <el-switch 
                    v-model="switchWithIcon"
                    active-icon="Check"
                    inactive-icon="Close"
                  />
                </div>
              </div>

              <div class="switch-group">
                <h4>不同尺寸的开关</h4>
                <div class="switch-demo">
                  <el-switch v-model="switchValue" size="large" />
                  <el-switch v-model="switchValue" />
                  <el-switch v-model="switchValue" size="small" />
                </div>
              </div>
            </div>
          </div>

          <!-- 上传组件预览 -->
          <div v-show="activeTab === 'upload'" class="component-preview">
            <div class="preview-title">上传组件预览</div>
            <div class="preview-area">
              <div class="upload-group">
                <h4>基础上传</h4>
                <div class="upload-demo">
                  <el-upload
                    action="#"
                    :auto-upload="false"
                    :on-change="handleFileChange"
                    :file-list="fileList"
                  >
                    <el-button type="primary">点击上传</el-button>
                    <template #tip>
                      <div class="el-upload__tip">
                        只能上传 jpg/png 文件，且不超过 500kb
                      </div>
                    </template>
                  </el-upload>
                </div>
              </div>

              <div class="upload-group">
                <h4>拖拽上传</h4>
                <div class="upload-demo">
                  <el-upload
                    action="#"
                    drag
                    :auto-upload="false"
                    :on-change="handleFileChange"
                    :file-list="dragFileList"
                  >
                    <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
                    <div class="el-upload__text">
                      将文件拖到此处，或<em>点击上传</em>
                    </div>
                    <template #tip>
                      <div class="el-upload__tip">
                        只能上传 jpg/png 文件，且不超过 500kb
                      </div>
                    </template>
                  </el-upload>
                </div>
              </div>
            </div>
          </div>

          <!-- 穿梭框组件预览 -->
          <div v-show="activeTab === 'transfer'" class="component-preview">
            <div class="preview-title">穿梭框组件预览</div>
            <div class="preview-area">
              <div class="transfer-group">
                <h4>基础穿梭框</h4>
                <div class="transfer-demo">
                  <el-transfer
                    v-model="transferValue"
                    :data="transferData"
                    :titles="['源列表', '目标列表']"
                    :button-texts="['到左边', '到右边']"
                  />
                </div>
              </div>

              <div class="transfer-group">
                <h4>带搜索的穿梭框</h4>
                <div class="transfer-demo">
                  <el-transfer
                    v-model="transferWithSearch"
                    :data="transferData"
                    :titles="['源列表', '目标列表']"
                    :button-texts="['到左边', '到右边']"
                    filterable
                    filter-placeholder="请输入关键词"
                  />
                </div>
              </div>
            </div>
          </div>

          <!-- 进度条组件预览 -->
          <div v-show="activeTab === 'progress'" class="component-preview">
            <div class="preview-title">进度条组件预览</div>
            <div class="preview-area">
              <div class="progress-group">
                <h4>基础进度条</h4>
                <div class="progress-demo">
                  <el-progress :percentage="50" />
                  <el-progress :percentage="80" />
                  <el-progress :percentage="100" />
                </div>
              </div>

              <div class="progress-group">
                <h4>不同状态的进度条</h4>
                <div class="progress-demo">
                  <el-progress :percentage="50" status="success" />
                  <el-progress :percentage="80" status="warning" />
                  <el-progress :percentage="100" status="exception" />
                </div>
              </div>

              <div class="progress-group">
                <h4>环形进度条</h4>
                <div class="progress-demo">
                  <el-progress type="circle" :percentage="50" />
                  <el-progress type="circle" :percentage="80" />
                  <el-progress type="circle" :percentage="100" />
                </div>
              </div>
            </div>
          </div>

          <!-- 标记组件预览 -->
          <div v-show="activeTab === 'badge'" class="component-preview">
            <div class="preview-title">标记组件预览</div>
            <div class="preview-area">
              <div class="badge-group">
                <h4>基础标记</h4>
                <div class="badge-demo">
                  <el-badge value="hot" class="item">
                    <el-button>评论</el-button>
                  </el-badge>
                  <el-badge value="new" class="item">
                    <el-button>回复</el-button>
                  </el-badge>
                </div>
              </div>

              <div class="badge-group">
                <h4>数字标记</h4>
                <div class="badge-demo">
                  <el-badge :value="12" class="item">
                    <el-button>评论</el-button>
                  </el-badge>
                  <el-badge :value="3" class="item">
                    <el-button>回复</el-button>
                  </el-badge>
                  <el-badge :value="1" class="item" type="primary">
                    <el-button>消息</el-button>
                  </el-badge>
                </div>
              </div>

              <div class="badge-group">
                <h4>最大值标记</h4>
                <div class="badge-demo">
                  <el-badge :value="200" :max="99" class="item">
                    <el-button>评论</el-button>
                  </el-badge>
                  <el-badge :value="100" :max="10" class="item">
                    <el-button>回复</el-button>
                  </el-badge>
                </div>
              </div>
            </div>
          </div>

          <!-- 导航菜单预览 -->
          <div v-show="activeTab === 'navmenu'" class="component-preview">
            <div class="preview-title">导航菜单预览</div>
            <div class="preview-area">
              <div class="navmenu-group">
                <h4>基础导航菜单</h4>
                <div class="navmenu-demo">
                  <el-menu
                    :default-active="activeMenu"
                    class="el-menu-vertical"
                    @select="handleMenuSelect"
                  >
                    <el-menu-item index="1">
                      <el-icon><Document /></el-icon>
                      <span>导航一</span>
                    </el-menu-item>
                    <el-menu-item index="2">
                      <el-icon><Setting /></el-icon>
                      <span>导航二</span>
                    </el-menu-item>
                    <el-sub-menu index="3">
                      <template #title>
                        <el-icon><Location /></el-icon>
                        <span>导航三</span>
                      </template>
                      <el-menu-item index="3-1">选项1</el-menu-item>
                      <el-menu-item index="3-2">选项2</el-menu-item>
                    </el-sub-menu>
                  </el-menu>
                </div>
              </div>
            </div>
          </div>

          <!-- 步骤条组件预览 -->
          <div v-show="activeTab === 'steps'" class="component-preview">
            <div class="preview-title">步骤条组件预览</div>
            <div class="preview-area">
              <div class="steps-group">
                <h4>基础步骤条</h4>
                <div class="steps-demo">
                  <el-steps :active="activeStep" finish-status="success">
                    <el-step title="步骤 1" description="这是一段很长很长很长的描述性文字" />
                    <el-step title="步骤 2" description="这是一段很长很长很长的描述性文字" />
                    <el-step title="步骤 3" description="这是一段很长很长很长的描述性文字" />
                    <el-step title="步骤 4" description="这是一段很长很长很长的描述性文字" />
                  </el-steps>
                  <div class="steps-actions">
                    <el-button @click="nextStep">下一步</el-button>
                    <el-button @click="prevStep">上一步</el-button>
                  </div>
                </div>
              </div>

              <div class="steps-group">
                <h4>带图标的步骤条</h4>
                <div class="steps-demo">
                  <el-steps :active="activeStep2" finish-status="success">
                    <el-step title="上传" icon="Upload" />
                    <el-step title="转码" icon="VideoPlay" />
                    <el-step title="合成" icon="Picture" />
                    <el-step title="完成" icon="CircleCheck" />
                  </el-steps>
                  <div class="steps-actions">
                    <el-button @click="nextStep2">下一步</el-button>
                    <el-button @click="prevStep2">上一步</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 气泡确认框预览 -->
          <div v-show="activeTab === 'popconfirm'" class="component-preview">
            <div class="preview-title">气泡确认框预览</div>
            <div class="preview-area">
              <div class="popconfirm-group">
                <h4>基础气泡确认框</h4>
                <div class="popconfirm-demo">
                  <el-popconfirm
                    title="确定删除这个文件吗？"
                    @confirm="handleConfirm"
                    @cancel="handleCancel"
                  >
                    <template #reference>
                      <el-button>删除</el-button>
                    </template>
                  </el-popconfirm>
                </div>
              </div>

              <div class="popconfirm-group">
                <h4>自定义确认框</h4>
                <div class="popconfirm-demo">
                  <el-popconfirm
                    title="确定要退出吗？"
                    confirm-button-text="确定"
                    cancel-button-text="取消"
                    @confirm="handleConfirm"
                    @cancel="handleCancel"
                  >
                    <template #reference>
                      <el-button type="danger">退出</el-button>
                    </template>
                  </el-popconfirm>
                </div>
              </div>
            </div>
          </div>

          <!-- 走马灯组件预览 -->
          <div v-show="activeTab === 'carousel'" class="component-preview">
            <div class="preview-title">走马灯组件预览</div>
            <div class="preview-area">
              <div class="carousel-group">
                <h4>基础走马灯</h4>
                <div class="carousel-demo">
                  <el-carousel height="200px">
                    <el-carousel-item v-for="item in carouselItems" :key="item.id">
                      <div class="carousel-content" :style="{ backgroundColor: item.color }">
                        <h3>{{ item.title }}</h3>
                        <p>{{ item.content }}</p>
                      </div>
                    </el-carousel-item>
                  </el-carousel>
                </div>
              </div>

              <div class="carousel-group">
                <h4>带指示器的走马灯</h4>
                <div class="carousel-demo">
                  <el-carousel height="200px" indicator-position="outside">
                    <el-carousel-item v-for="item in carouselItems" :key="item.id">
                      <div class="carousel-content" :style="{ backgroundColor: item.color }">
                        <h3>{{ item.title }}</h3>
                        <p>{{ item.content }}</p>
                      </div>
                    </el-carousel-item>
                  </el-carousel>
                </div>
              </div>
            </div>
          </div>

          <!-- 折叠面板预览 -->
          <div v-show="activeTab === 'collapse'" class="component-preview">
            <div class="preview-title">折叠面板预览</div>
            <div class="preview-area">
              <div class="collapse-group">
                <h4>基础折叠面板</h4>
                <div class="collapse-demo">
                  <el-collapse v-model="activeCollapse">
                    <el-collapse-item title="一致性 Consistency" name="1">
                      <div>与现实生活一致：与现实生活的流程、逻辑保持一致，遵循用户习惯的语言和概念；</div>
                      <div>在界面中一致：所有的元素和结构需保持一致，比如：设计样式、图标和文本、元素的位置等。</div>
                    </el-collapse-item>
                    <el-collapse-item title="反馈 Feedback" name="2">
                      <div>控制反馈：通过界面样式和交互动效让用户可以清晰的感知自己的操作；</div>
                      <div>页面反馈：操作后，通过页面元素的变化清晰地展现当前状态。</div>
                    </el-collapse-item>
                    <el-collapse-item title="效率 Efficiency" name="3">
                      <div>简化流程：设计简洁直观的操作流程；</div>
                      <div>清晰明确：语言表达清晰且表意明确，让用户快速理解进而作出决策；</div>
                    </el-collapse-item>
                  </el-collapse>
                </div>
              </div>
            </div>
          </div>

          <!-- 时间线组件预览 -->
          <div v-show="activeTab === 'timeline'" class="component-preview">
            <div class="preview-title">时间线组件预览</div>
            <div class="preview-area">
              <div class="timeline-group">
                <h4>基础时间线</h4>
                <div class="timeline-demo">
                  <el-timeline>
                    <el-timeline-item
                      v-for="(activity, index) in timelineActivities"
                      :key="index"
                      :timestamp="activity.timestamp"
                      :type="activity.type"
                    >
                      {{ activity.content }}
                    </el-timeline-item>
                  </el-timeline>
                </div>
              </div>
            </div>
          </div>

          <!-- 分割线组件预览 -->
          <div v-show="activeTab === 'divider'" class="component-preview">
            <div class="preview-title">分割线组件预览</div>
            <div class="preview-area">
              <div class="divider-group">
                <h4>基础分割线</h4>
                <div class="divider-demo">
                  <span>青春是一个短暂的美梦</span>
                  <el-divider />
                  <span>当你醒来时，它早已消失无踪</span>
                </div>
              </div>

              <div class="divider-group">
                <h4>带文字的分割线</h4>
                <div class="divider-demo">
                  <el-divider content-position="left">左对齐</el-divider>
                  <el-divider content-position="center">居中</el-divider>
                  <el-divider content-position="right">右对齐</el-divider>
                </div>
              </div>

              <div class="divider-group">
                <h4>垂直分割线</h4>
                <div class="divider-demo">
                  <span>文字</span>
                  <el-divider direction="vertical" />
                  <el-link type="primary">链接</el-link>
                  <el-divider direction="vertical" />
                  <el-link type="primary">链接</el-link>
                </div>
              </div>
            </div>
          </div>

          <!-- 日历组件预览 -->
          <div v-show="activeTab === 'calendar'" class="component-preview">
            <div class="preview-title">日历组件预览</div>
            <div class="preview-area">
              <div class="calendar-group">
                <h4>基础日历</h4>
                <div class="calendar-demo">
                  <el-calendar v-model="calendarValue" />
                </div>
              </div>
            </div>
          </div>

          <!-- 对话框组件预览 -->
          <div v-show="activeTab === 'dialog'" class="component-preview">
            <div class="preview-title">对话框组件预览</div>
            <div class="preview-area">
              <div class="dialog-demo">
                <el-button @click="showDialog = true">基础对话框</el-button>
                <el-button @click="showFormDialog = true">表单对话框</el-button>
                <el-button @click="showConfirmDelete">确认删除</el-button>
              </div>
              
              <!-- 基础对话框 -->
              <el-dialog v-model="showDialog" title="基础对话框" width="500px">
                <p>这是一个基础对话框，用于验证深色主题效果。</p>
                <template #footer>
                  <span class="dialog-footer">
                    <el-button @click="showDialog = false">取消</el-button>
                    <el-button type="primary" @click="showDialog = false">确定</el-button>
                  </span>
                </template>
              </el-dialog>
              
              <!-- 表单对话框 -->
              <el-dialog v-model="showFormDialog" title="表单对话框" width="600px">
                <el-form :model="dialogForm" label-width="80px">
                  <el-form-item label="用户名">
                    <el-input v-model="dialogForm.username" placeholder="请输入用户名" />
                  </el-form-item>
                  <el-form-item label="邮箱">
                    <el-input v-model="dialogForm.email" placeholder="请输入邮箱" />
                  </el-form-item>
                  <el-form-item label="角色">
                    <el-select v-model="dialogForm.role" placeholder="请选择角色" style="width: 100%;">
                      <el-option label="管理员" value="admin" />
                      <el-option label="用户" value="user" />
                      <el-option label="访客" value="guest" />
                    </el-select>
                  </el-form-item>
                </el-form>
                <template #footer>
                  <span class="dialog-footer">
                    <el-button @click="showFormDialog = false">取消</el-button>
                    <el-button type="primary" @click="handleFormSubmit">提交</el-button>
                  </span>
                </template>
              </el-dialog>
            </div>
          </div>

          <!-- 抽屉组件预览 -->
          <div v-show="activeTab === 'drawer'" class="component-preview">
            <div class="preview-title">抽屉组件预览</div>
            <div class="preview-area">
              <div class="drawer-demo">
                <el-button @click="showDrawer = true">右侧抽屉</el-button>
                <el-button @click="showFormDrawer = true">表单抽屉</el-button>
                <el-button @click="showLeftDrawer = true">左侧抽屉</el-button>
              </div>
              
              <!-- 右侧抽屉 -->
              <el-drawer v-model="showDrawer" title="右侧抽屉" direction="rtl" size="400px">
                <div class="drawer-content">
                  <p>这是一个右侧抽屉，从右侧滑出。</p>
                  <el-divider />
                  <div class="drawer-section">
                    <h4>抽屉内容区域</h4>
                    <p>可以包含各种内容，如：</p>
                    <ul>
                      <li>文本内容</li>
                      <li>表单元素</li>
                      <li>列表数据</li>
                      <li>操作按钮</li>
                    </ul>
                  </div>
                  <el-divider />
                  <div class="drawer-actions">
                    <el-button @click="showDrawer = false">取消</el-button>
                    <el-button type="primary" @click="handleDrawerConfirm">确定</el-button>
                  </div>
                </div>
              </el-drawer>
              
              <!-- 表单抽屉 -->
              <el-drawer v-model="showFormDrawer" title="表单抽屉" direction="rtl" size="500px">
                <div class="drawer-content">
                  <el-form :model="drawerForm" label-width="100px">
                    <el-form-item label="姓名">
                      <el-input v-model="drawerForm.name" placeholder="请输入姓名" />
                    </el-form-item>
                    <el-form-item label="邮箱">
                      <el-input v-model="drawerForm.email" placeholder="请输入邮箱" />
                    </el-form-item>
                    <el-form-item label="部门">
                      <el-select v-model="drawerForm.department" placeholder="请选择部门" style="width: 100%;">
                        <el-option label="技术部" value="tech" />
                        <el-option label="产品部" value="product" />
                        <el-option label="设计部" value="design" />
                        <el-option label="运营部" value="operation" />
                      </el-select>
                    </el-form-item>
                  </el-form>
                  <div class="drawer-actions">
                    <el-button @click="showFormDrawer = false">取消</el-button>
                    <el-button type="primary" @click="handleDrawerFormSubmit">保存</el-button>
                  </div>
                </div>
              </el-drawer>
              
              <!-- 左侧抽屉 -->
              <el-drawer v-model="showLeftDrawer" title="左侧抽屉" direction="ltr" size="350px">
                <div class="drawer-content">
                  <p>这是一个左侧抽屉，从左侧滑出。</p>
                  <el-divider />
                  <div class="drawer-section">
                    <h4>导航菜单</h4>
                    <el-menu>
                      <el-menu-item index="1">
                        <el-icon><HomeFilled /></el-icon>
                        <span>首页</span>
                      </el-menu-item>
                      <el-menu-item index="2">
                        <el-icon><User /></el-icon>
                        <span>用户管理</span>
                      </el-menu-item>
                      <el-menu-item index="3">
                        <el-icon><Setting /></el-icon>
                        <span>系统设置</span>
                      </el-menu-item>
                    </el-menu>
                  </div>
                </div>
              </el-drawer>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧代码面板区域 -->
      <div v-show="showCodePanel" class="code-section">
        <div class="code-header">
          <h3>代码示例</h3>
          <div class="code-actions">
            <el-button size="small" @click="toggleCodeCollapse">
              {{ isCodeCollapsed ? '展开代码' : '折叠代码' }}
            </el-button>
          </div>
        </div>
        
        <div class="code-content" :class="{ 'collapsed': isCodeCollapsed }">
          <el-tabs v-model="activeCodeTab" type="card" class="code-tabs">
            <el-tab-pane label="Vue 模板" name="template">
              <div class="code-block">
                <div class="code-header">
                  <span>Vue 模板代码</span>
                  <el-button size="small" @click="copyCode('template')">复制代码</el-button>
                </div>
                <pre class="code-content"><code>{{ getTemplateCode() }}</code></pre>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="JavaScript" name="script">
              <div class="code-block">
                <div class="code-header">
                  <span>JavaScript 代码</span>
                  <el-button size="small" @click="copyCode('script')">复制代码</el-button>
                </div>
                <pre class="code-content"><code>{{ getScriptCode() }}</code></pre>
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="CSS 样式" name="style">
              <div class="code-block">
                <div class="code-header">
                  <span>CSS 样式代码</span>
                  <el-button size="small" @click="copyCode('style')">复制代码</el-button>
                </div>
                <pre class="code-content"><code>{{ getStyleCode() }}</code></pre>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox, ElNotification } from 'element-plus'
import { Plus, Check, Warning, Delete, Edit, Share, Search, Setting, Calendar, InfoFilled, Document, Location, UploadFilled, VideoPlay, Picture, CircleCheck, HomeFilled, User } from '@element-plus/icons-vue'

// 表格数据
const tableData = ref([
  {
    name: '张三',
    age: 25,
    address: '北京市朝阳区'
  },
  {
    name: '李四',
    age: 30,
    address: '上海市浦东新区'
  },
  {
    name: '王五',
    age: 28,
    address: '广州市天河区'
  }
])

// 表单数据
const form = reactive({
  name: '',
  email: '',
  role: 'admin',
  status: 'active',
  remember: false
})

// 分页数据
const currentPage = ref(1)
const pageSize = ref(20)
const currentPage2 = ref(1)
const currentPage3 = ref(1)

// 对话框数据
const showDialog = ref(false)
const showLargeDialog = ref(false)
const showSmallDialog = ref(false)
const showFormDialog = ref(false)
const showCustomDialog = ref(false)
const customCheckbox = ref(false)

// 抽屉数据
const showDrawer = ref(false)
const showLeftDrawer = ref(false)
const showTopDrawer = ref(false)
const showBottomDrawer = ref(false)
const showFormDrawer = ref(false)
const showListDrawer = ref(false)
const showNestedDrawer = ref(false)
const showInnerDrawer = ref(false)

// 表单数据
const dialogForm = reactive({
  username: '',
  email: '',
  role: '',
  description: ''
})

const drawerForm = reactive({
  name: '',
  email: '',
  phone: '',
  department: '',
  position: '',
  remark: ''
})

// 列表数据
const searchKeyword = ref('')
const listData = ref([
  {
    id: 1,
    title: '系统更新通知',
    description: '系统将于今晚进行维护更新',
    status: '未读',
    type: 'warning',
    time: '2024-06-01 10:00'
  },
  {
    id: 2,
    title: '任务完成提醒',
    description: '您的任务已完成，请及时查看',
    status: '已读',
    type: 'success',
    time: '2024-06-01 09:30'
  },
  {
    id: 3,
    title: '安全警告',
    description: '检测到异常登录，请及时处理',
    status: '紧急',
    type: 'danger',
    time: '2024-06-01 09:00'
  },
  {
    id: 4,
    title: '新消息提醒',
    description: '您有一条新的消息',
    status: '未读',
    type: 'info',
    time: '2024-06-01 08:45'
  }
])

// 计算属性
const filteredList = computed(() => {
  if (!searchKeyword.value) return listData.value
  return listData.value.filter(item => 
    item.title.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
    item.description.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

// 时间日期选择器数据
const dateValue = ref('')
const timeValue = ref('')
const dateRange = ref([])
const datetimeValue = ref('')

// 颜色选择器数据
const colorValue = ref('#409EFF')
const colorWithAlpha = ref('rgba(64, 158, 255, 0.8)')
const predefinedColor = ref('#409EFF')
const predefineColors = ref([
  '#ff4500',
  '#ff8c00',
  '#ffd700',
  '#90ee90',
  '#00ced1',
  '#1e90ff',
  '#c71585'
])

// 卡片数据
const currentDate = ref(new Date().toLocaleDateString())

// 无限滚动数据
const infiniteList = ref([])
const infiniteLoading = ref(false)
const infiniteDisabled = ref(false)
const infiniteListData = ref([])
const listLoading = ref(false)
const listDisabled = ref(false)

// 滑块数据
const sliderValue = ref(50)
const sliderRange = ref([20, 80])
const sliderWithMarks = ref(50)
const sliderMarks = reactive({
  0: '0°C',
  20: '20°C',
  40: '40°C',
  60: '60°C',
  80: '80°C',
  100: '100°C'
})

// 开关数据
const switchValue = ref(true)
const switchWithText = ref(false)
const switchWithIcon = ref(true)

// 上传数据
const fileList = ref([])
const dragFileList = ref([])

// 穿梭框数据
const transferValue = ref([])
const transferWithSearch = ref([])
const transferData = ref([
  { key: 1, label: '选项1', disabled: false },
  { key: 2, label: '选项2', disabled: false },
  { key: 3, label: '选项3', disabled: false },
  { key: 4, label: '选项4', disabled: false },
  { key: 5, label: '选项5', disabled: false },
  { key: 6, label: '选项6', disabled: false },
  { key: 7, label: '选项7', disabled: false },
  { key: 8, label: '选项8', disabled: false },
  { key: 9, label: '选项9', disabled: false },
  { key: 10, label: '选项10', disabled: false }
])

// 导航菜单数据
const activeMenu = ref('1')

// 步骤条数据
const activeStep = ref(1)
const activeStep2 = ref(1)

// 走马灯数据
const carouselItems = ref([
  { id: 1, title: '第一页', content: '这是第一页的内容', color: '#409EFF' },
  { id: 2, title: '第二页', content: '这是第二页的内容', color: '#67C23A' },
  { id: 3, title: '第三页', content: '这是第三页的内容', color: '#E6A23C' },
  { id: 4, title: '第四页', content: '这是第四页的内容', color: '#F56C6C' }
])

// 折叠面板数据
const activeCollapse = ref(['1'])

// 时间线数据
const timelineActivities = ref([
  { timestamp: '2023-04-15', content: '活动按期开始', type: 'primary' },
  { timestamp: '2023-04-13', content: '通过审核', type: 'success' },
  { timestamp: '2023-04-11', content: '创建项目', type: 'info' },
  { timestamp: '2023-04-10', content: '项目初始化', type: 'warning' }
])

// 日历数据
const calendarValue = ref(new Date())

// 下拉框数据 - 重点测试
const basicSelect = ref('')
const defaultSelect = ref('default1')
const multiSelect = ref([])
const searchableSelect = ref('')
const remoteSelect = ref('')
const remoteOptions = ref([])
const sizeSelect = ref('')
const disabledSelect = ref('disabled1')
const clearableSelect = ref('')
const groupSelect = ref('')
const statusSelect = ref('')
const loadingSelect = ref('')

// 数字输入框数据
const numberValue = ref(10)
const numberValue2 = ref(3.14)

// 页面状态数据
const activeTab = ref('button')
const activeCodeTab = ref('template')
const showCodePanel = ref(true)
const isCodeCollapsed = ref(false)

// 各种组件代码示例
const buttonCode = ref(`<!-- 按钮组件示例 -->
<div class="button-group">
  <h4>主要按钮类型</h4>
  <div class="button-row">
    <el-button type="primary">主要按钮</el-button>
    <el-button type="success">成功按钮</el-button>
    <el-button type="warning">警告按钮</el-button>
    <el-button type="danger">危险按钮</el-button>
    <el-button type="info">信息按钮</el-button>
    <el-button>默认按钮</el-button>
  </div>
</div>

<div class="button-group">
  <h4>朴素按钮</h4>
  <div class="button-row">
    <el-button type="primary" plain>朴素主要</el-button>
    <el-button type="success" plain>朴素成功</el-button>
    <el-button type="warning" plain>朴素警告</el-button>
    <el-button type="danger" plain>朴素危险</el-button>
  </div>
</div>

<div class="button-group">
  <h4>圆形和圆角按钮</h4>
  <div class="button-row">
    <el-button type="primary" circle>
      <el-icon><Plus /></el-icon>
    </el-button>
    <el-button type="success" circle>
      <el-icon><Check /></el-icon>
    </el-button>
    <el-button type="primary" round>圆角按钮</el-button>
  </div>
</div>`)

const inputCode = ref(`<!-- 输入框组件示例 -->
<div class="input-group">
  <h4>基础输入框</h4>
  <div class="input-row">
    <el-input placeholder="请输入内容" style="width: 300px;" />
    <el-input placeholder="禁用状态" disabled style="width: 300px;" />
    <el-input placeholder="只读状态" readonly style="width: 300px;" />
  </div>
</div>

<div class="input-group">
  <h4>带图标的输入框</h4>
  <div class="input-row">
    <el-input placeholder="带前缀图标" style="width: 300px;">
      <template #prefix>
        <el-icon><Search /></el-icon>
      </template>
    </el-input>
    <el-input placeholder="带后缀图标" style="width: 300px;">
      <template #suffix>
        <el-icon><Calendar /></el-icon>
      </template>
    </el-input>
  </div>
</div>

<div class="input-group">
  <h4>多行输入框</h4>
  <div class="input-row">
    <el-input 
      type="textarea" 
      placeholder="请输入多行文本" 
      :rows="3"
      style="width: 400px;" 
    />
  </div>
</div>

<div class="input-group">
  <h4>数字输入框</h4>
  <div class="input-row">
    <el-input-number 
      v-model="numberValue" 
      :min="0" 
      :max="100" 
      style="width: 200px;" 
    />
  </div>
</div>`)

const selectCode = ref(`<!-- 下拉框组件示例 -->
<div class="select-group">
  <h4>基础下拉框</h4>
  <el-select v-model="basicSelect" placeholder="请选择" style="width: 300px;">
    <el-option label="选项1" value="1" />
    <el-option label="选项2" value="2" />
    <el-option label="选项3" value="3" />
    <el-option label="选项4" value="4" />
  </el-select>
</div>

<div class="select-group">
  <h4>多选下拉框</h4>
  <el-select v-model="multiSelect" multiple placeholder="多选下拉" style="width: 300px;">
    <el-option label="多选选项1" value="multi1" />
    <el-option label="多选选项2" value="multi2" />
    <el-option label="多选选项3" value="multi3" />
  </el-select>
</div>

<div class="select-group">
  <h4>可搜索下拉框</h4>
  <el-select v-model="searchableSelect" filterable placeholder="可搜索下拉框" style="width: 300px;">
    <el-option label="苹果" value="apple" />
    <el-option label="香蕉" value="banana" />
    <el-option label="橙子" value="orange" />
    <el-option label="葡萄" value="grape" />
  </el-select>
</div>

<div class="select-group">
  <h4>分组下拉框</h4>
  <el-select v-model="groupSelect" placeholder="分组下拉框" style="width: 300px;">
    <el-option-group label="热门城市">
      <el-option label="北京" value="beijing" />
      <el-option label="上海" value="shanghai" />
      <el-option label="广州" value="guangzhou" />
    </el-option-group>
    <el-option-group label="其他城市">
      <el-option label="深圳" value="shenzhen" />
      <el-option label="杭州" value="hangzhou" />
    </el-option-group>
  </el-select>
</div>`)

const tableCode = ref(`<!-- 表格组件示例 -->
<el-table :data="tableData" style="width: 100%">
  <el-table-column prop="name" label="姓名" />
  <el-table-column prop="age" label="年龄" />
  <el-table-column prop="address" label="地址" />
  <el-table-column label="操作">
    <template #default>
      <el-button size="small">编辑</el-button>
      <el-button size="small" type="danger">删除</el-button>
    </template>
  </el-table-column>
</el-table>

<!-- 表格数据 -->
<script setup>
const tableData = ref([
  {
    name: '张三',
    age: 25,
    address: '北京市朝阳区'
  },
  {
    name: '李四',
    age: 30,
    address: '上海市浦东新区'
  },
  {
    name: '王五',
    age: 28,
    address: '广州市天河区'
  }
])
<\/script>`)

const formCode = ref(`<!-- 表单组件示例 -->
<el-form :model="form" label-width="80px" style="max-width: 400px;">
  <el-form-item label="用户名">
    <el-input v-model="form.name" />
  </el-form-item>
  <el-form-item label="邮箱">
    <el-input v-model="form.email" />
  </el-form-item>
  <el-form-item label="角色">
    <el-select v-model="form.role" placeholder="请选择角色">
      <el-option label="管理员" value="admin" />
      <el-option label="用户" value="user" />
      <el-option label="访客" value="guest" />
    </el-select>
  </el-form-item>
  <el-form-item label="状态">
    <el-radio-group v-model="form.status">
      <el-radio label="active">活跃</el-radio>
      <el-radio label="inactive">禁用</el-radio>
    </el-radio-group>
  </el-form-item>
  <el-form-item label="记住我">
    <el-checkbox v-model="form.remember">记住登录状态</el-checkbox>
  </el-form-item>
  <el-form-item>
    <el-button type="primary">提交</el-button>
    <el-button>重置</el-button>
  </el-form-item>
</el-form>

<!-- 表单数据 -->
<script setup>
const form = reactive({
  name: '',
  email: '',
  role: 'admin',
  status: 'active',
  remember: false
})
<\/script>`)

const dialogCode = ref(`<!-- 对话框组件示例 -->
<!-- 基础对话框 -->
<el-dialog v-model="showDialog" title="基础对话框" width="500px">
  <p>这是一个基础对话框，用于验证深色主题效果。</p>
  <template #footer>
    <span class="dialog-footer">
      <el-button @click="showDialog = false">取消</el-button>
      <el-button type="primary" @click="showDialog = false">确定</el-button>
    </span>
  </template>
</el-dialog>

<!-- 表单对话框 -->
<el-dialog v-model="showFormDialog" title="表单对话框" width="600px">
  <el-form :model="dialogForm" label-width="80px">
    <el-form-item label="用户名">
      <el-input v-model="dialogForm.username" placeholder="请输入用户名" />
    </el-form-item>
    <el-form-item label="邮箱">
      <el-input v-model="dialogForm.email" placeholder="请输入邮箱" />
    </el-form-item>
    <el-form-item label="角色">
      <el-select v-model="dialogForm.role" placeholder="请选择角色" style="width: 100%;">
        <el-option label="管理员" value="admin" />
        <el-option label="用户" value="user" />
        <el-option label="访客" value="guest" />
      </el-select>
    </el-form-item>
  </el-form>
  <template #footer>
    <span class="dialog-footer">
      <el-button @click="showFormDialog = false">取消</el-button>
      <el-button type="primary" @click="handleFormSubmit">提交</el-button>
    </span>
  </template>
</el-dialog>

<!-- 对话框数据 -->
<script setup>
const showDialog = ref(false)
const showFormDialog = ref(false)
const dialogForm = reactive({
  username: '',
  email: '',
  role: ''
})
<\/script>`)

const drawerCode = ref(`<!-- 抽屉组件示例 -->
<!-- 右侧抽屉 -->
<el-drawer v-model="showDrawer" title="右侧抽屉" direction="rtl" size="400px">
  <div class="drawer-content">
    <p>这是一个右侧抽屉，从右侧滑出。</p>
    <el-divider />
    <div class="drawer-section">
      <h4>抽屉内容区域</h4>
      <p>可以包含各种内容，如：</p>
      <ul>
        <li>文本内容</li>
        <li>表单元素</li>
        <li>列表数据</li>
        <li>操作按钮</li>
      </ul>
    </div>
    <el-divider />
    <div class="drawer-actions">
      <el-button @click="showDrawer = false">取消</el-button>
      <el-button type="primary" @click="handleDrawerConfirm">确定</el-button>
    </div>
  </div>
</el-drawer>

<!-- 表单抽屉 -->
<el-drawer v-model="showFormDrawer" title="表单抽屉" direction="rtl" size="500px">
  <div class="drawer-content">
    <el-form :model="drawerForm" label-width="100px">
      <el-form-item label="姓名">
        <el-input v-model="drawerForm.name" placeholder="请输入姓名" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="drawerForm.email" placeholder="请输入邮箱" />
      </el-form-item>
      <el-form-item label="部门">
        <el-select v-model="drawerForm.department" placeholder="请选择部门" style="width: 100%;">
          <el-option label="技术部" value="tech" />
          <el-option label="产品部" value="product" />
          <el-option label="设计部" value="design" />
        </el-select>
      </el-form-item>
    </el-form>
    <div class="drawer-actions">
      <el-button @click="showFormDrawer = false">取消</el-button>
      <el-button type="primary" @click="handleDrawerFormSubmit">保存</el-button>
    </div>
  </div>
</el-drawer>

<!-- 抽屉数据 -->
<script setup>
const showDrawer = ref(false)
const showFormDrawer = ref(false)
const drawerForm = reactive({
  name: '',
  email: '',
  department: ''
})
<\/script>`)

const fullPageCode = ref(`<!-- 完整的 Element Plus 测试页面 -->
<template>
  <div class="test-page">
    <div class="page-header">
      <h1 class="page-title">Element Plus 深色主题测试</h1>
    </div>

    <div class="test-sections">
      <!-- 按钮测试 -->
      <div class="test-section">
        <h3>按钮组件 - 炫酷效果</h3>
        <div class="test-content">
          <div class="button-group">
            <h4>主要按钮类型</h4>
            <div class="button-row">
              <el-button type="primary">主要按钮</el-button>
              <el-button type="success">成功按钮</el-button>
              <el-button type="warning">警告按钮</el-button>
              <el-button type="danger">危险按钮</el-button>
              <el-button type="info">信息按钮</el-button>
              <el-button>默认按钮</el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 输入框测试 -->
      <div class="test-section">
        <h3>输入框组件 - 深色主题</h3>
        <div class="test-content">
          <div class="input-group">
            <h4>基础输入框</h4>
            <div class="input-row">
              <el-input placeholder="请输入内容" style="width: 300px;" />
              <el-input placeholder="禁用状态" disabled style="width: 300px;" />
            </div>
          </div>
        </div>
      </div>

      <!-- 下拉框测试 -->
      <div class="test-section">
        <h3>下拉框组件 - 重点测试</h3>
        <div class="test-content">
          <div class="select-group">
            <h4>基础下拉框</h4>
            <el-select v-model="basicSelect" placeholder="请选择" style="width: 300px;">
              <el-option label="选项1" value="1" />
              <el-option label="选项2" value="2" />
              <el-option label="选项3" value="3" />
            </el-select>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'

// 下拉框数据
const basicSelect = ref('')

// 样式部分
<\/script>

<style lang="scss" scoped>
.test-page {
  padding: 24px;
}

.test-sections {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.test-section {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 24px;
  box-shadow: var(--shadow-lg);
  
  h3 {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 16px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.test-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.button-group, .input-group, .select-group {
  background: var(--bg-tertiary);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.button-row, .input-row {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}
</style>`)

// 远程搜索方法
const remoteSearch = (query) => {
  if (query !== '') {
    // 模拟远程搜索
    const mockData = [
      { label: '北京', value: 'beijing' },
      { label: '上海', value: 'shanghai' },
      { label: '广州', value: 'guangzhou' },
      { label: '深圳', value: 'shenzhen' },
      { label: '杭州', value: 'hangzhou' },
      { label: '成都', value: 'chengdu' },
      { label: '武汉', value: 'wuhan' },
      { label: '西安', value: 'xian' }
    ]
    
    remoteOptions.value = mockData.filter(item => 
      item.label.toLowerCase().includes(query.toLowerCase())
    )
  } else {
    remoteOptions.value = []
  }
}

// 显示消息
const showMessage = (type) => {
  const messages = {
    success: '操作成功！',
    warning: '警告信息',
    error: '操作失败！',
    info: '提示信息'
  }
  ElMessage({
    type,
    message: messages[type]
  })
}

// 显示通知
const showNotification = (type) => {
  const notifications = {
    success: {
      title: '成功',
      message: '这是一条成功的通知消息'
    },
    warning: {
      title: '警告',
      message: '这是一条警告的通知消息'
    },
    error: {
      title: '错误',
      message: '这是一条错误的通知消息'
    },
    info: {
      title: '信息',
      message: '这是一条信息的通知消息'
    }
  }
  
  ElNotification({
    type,
    title: notifications[type].title,
    message: notifications[type].message
  })
}

// 无限滚动方法
const loadMore = () => {
  infiniteLoading.value = true
  setTimeout(() => {
    const newItems = []
    const startIndex = infiniteList.value.length + 1
    for (let i = 0; i < 10; i++) {
      newItems.push({
        id: startIndex + i,
        content: `这是第 ${startIndex + i} 个项目的内容`
      })
    }
    infiniteList.value.push(...newItems)
    infiniteLoading.value = false
    
    if (infiniteList.value.length >= 50) {
      infiniteDisabled.value = true
    }
  }, 1000)
}

const loadMoreList = () => {
  listLoading.value = true
  setTimeout(() => {
    const newItems = []
    const startIndex = infiniteListData.value.length + 1
    for (let i = 0; i < 10; i++) {
      newItems.push({
        id: startIndex + i,
        text: `列表项 ${startIndex + i}`
      })
    }
    infiniteListData.value.push(...newItems)
    listLoading.value = false
    
    if (infiniteListData.value.length >= 30) {
      listDisabled.value = true
    }
  }, 1000)
}

// 文件上传处理
const handleFileChange = (file, fileList) => {
  ElMessage.success('文件选择成功')
}

// 导航菜单处理
const handleMenuSelect = (index) => {
  ElMessage.success(`选择了菜单项: ${index}`)
}

// 步骤条处理
const nextStep = () => {
  if (activeStep.value < 4) {
    activeStep.value++
  }
}

const prevStep = () => {
  if (activeStep.value > 1) {
    activeStep.value--
  }
}

const nextStep2 = () => {
  if (activeStep2.value < 4) {
    activeStep2.value++
  }
}

const prevStep2 = () => {
  if (activeStep2.value > 1) {
    activeStep2.value--
  }
}

// 气泡确认框处理
const handleConfirm = () => {
  ElMessage.success('确认操作成功')
}

const handleCancel = () => {
  ElMessage.info('取消操作')
}

// 对话框操作方法
const showConfirmDelete = () => {
  ElMessageBox.confirm(
    '确定要删除这条记录吗？',
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    ElMessage({
      type: 'success',
      message: '删除成功',
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消删除',
    })
  })
}

const showAlertMessage = () => {
  ElMessageBox.alert('这是一个警告消息', '警告', {
    confirmButtonText: '确定',
    type: 'warning',
  })
}

const showPromptDialog = () => {
  ElMessageBox.prompt('请输入您的姓名', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /\S+/,
    inputErrorMessage: '姓名不能为空'
  }).then(({ value }) => {
    ElMessage({
      type: 'success',
      message: `您的姓名是: ${value}`,
    })
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '已取消输入',
    })
  })
}

// 表单提交方法
const handleFormSubmit = () => {
  ElMessage({
    type: 'success',
    message: '表单提交成功！',
  })
  showFormDialog.value = false
}

const handleCustomConfirm = () => {
  ElMessage({
    type: 'success',
    message: '已同意条款并继续！',
  })
  showCustomDialog.value = false
}

// 抽屉操作方法
const handleDrawerConfirm = () => {
  ElMessage({
    type: 'success',
    message: '抽屉操作成功！',
  })
  showDrawer.value = false
}

const handleDrawerFormSubmit = () => {
  ElMessage({
    type: 'success',
    message: '表单保存成功！',
  })
  showFormDrawer.value = false
}



// 页面控制方法
const toggleCodePanel = () => {
  showCodePanel.value = !showCodePanel.value
}

const toggleCodeCollapse = () => {
  isCodeCollapsed.value = !isCodeCollapsed.value
}

const handleTabChange = (tabName) => {
  activeTab.value = tabName
  activeCodeTab.value = 'template'
}

// 获取代码内容的方法
const getTemplateCode = () => {
  switch (activeTab.value) {
    case 'button':
      return buttonCode.value
    case 'input':
      return inputCode.value
    case 'select':
      return selectCode.value
    case 'datetime':
      return `<!-- 时间日期选择器示例 -->
<div class="datetime-group">
  <h4>日期选择器</h4>
  <el-date-picker
    v-model="dateValue"
    type="date"
    placeholder="选择日期"
    style="width: 300px;"
  />
</div>

<div class="datetime-group">
  <h4>时间选择器</h4>
  <el-time-picker
    v-model="timeValue"
    placeholder="选择时间"
    style="width: 300px;"
  />
</div>

<div class="datetime-group">
  <h4>日期范围选择器</h4>
  <el-date-picker
    v-model="dateRange"
    type="daterange"
    range-separator="至"
    start-placeholder="开始日期"
    end-placeholder="结束日期"
    style="width: 400px;"
  />
</div>

<div class="datetime-group">
  <h4>日期时间选择器</h4>
  <el-date-picker
    v-model="datetimeValue"
    type="datetime"
    placeholder="选择日期时间"
    style="width: 300px;"
  />
</div>`
    case 'color':
      return `<!-- 颜色选择器示例 -->
<div class="color-group">
  <h4>基础颜色选择器</h4>
  <el-color-picker v-model="colorValue" />
</div>

<div class="color-group">
  <h4>带透明度颜色选择器</h4>
  <el-color-picker v-model="colorWithAlpha" show-alpha />
</div>

<div class="color-group">
  <h4>预定义颜色</h4>
  <el-color-picker 
    v-model="predefinedColor" 
    :predefine="predefineColors"
  />
</div>

<div class="color-group">
  <h4>不同尺寸</h4>
  <div class="color-sizes">
    <el-color-picker v-model="colorValue" size="large" />
    <el-color-picker v-model="colorValue" />
    <el-color-picker v-model="colorValue" size="small" />
  </div>
</div>`
    case 'table':
      return tableCode.value
    case 'form':
      return formCode.value
    case 'card':
      return `<!-- 卡片组件示例 -->
<div class="card-group">
  <h4>基础卡片</h4>
  <div class="card-demo">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>卡片标题</span>
          <el-button class="button" text>操作按钮</el-button>
        </div>
      </template>
      <div v-for="i in 4" :key="i" class="text item">
        {{ '列表内容 ' + i }}
      </div>
    </el-card>
  </div>
</div>

<div class="card-group">
  <h4>带图片的卡片</h4>
  <div class="card-demo">
    <el-card :body-style="{ padding: '0px' }">
      <img src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image" />
      <div style="padding: 14px;">
        <span>好吃的汉堡</span>
        <div class="bottom">
          <time class="time">{{ currentDate }}</time>
          <el-button class="button" text>操作按钮</el-button>
        </div>
      </div>
    </el-card>
  </div>
</div>

<div class="card-group">
  <h4>简单卡片</h4>
  <div class="card-demo">
    <el-card>
      <p>这是一个简单的卡片内容</p>
      <p>可以包含任何内容</p>
    </el-card>
  </div>
</div>`
    case 'tag':
      return `<!-- 标签和消息组件示例 -->
<div class="tag-group">
  <h4>标签组件</h4>
  <div class="tag-demo">
    <el-tag style="margin-right: 8px;">默认标签</el-tag>
    <el-tag type="success" style="margin-right: 8px;">成功标签</el-tag>
    <el-tag type="warning" style="margin-right: 8px;">警告标签</el-tag>
    <el-tag type="danger" style="margin-right: 8px;">危险标签</el-tag>
    <el-tag type="info" style="margin-right: 8px;">信息标签</el-tag>
  </div>
</div>

<div class="message-group">
  <h4>消息提示</h4>
  <div class="message-demo">
    <el-button @click="showMessage('success')">成功消息</el-button>
    <el-button @click="showMessage('warning')">警告消息</el-button>
    <el-button @click="showMessage('error')">错误消息</el-button>
    <el-button @click="showMessage('info')">信息消息</el-button>
  </div>
</div>

<div class="notification-group">
  <h4>通知组件</h4>
  <div class="notification-demo">
    <el-button @click="showNotification('success')">成功通知</el-button>
    <el-button @click="showNotification('warning')">警告通知</el-button>
    <el-button @click="showNotification('error')">错误通知</el-button>
    <el-button @click="showNotification('info')">信息通知</el-button>
  </div>
</div>`
    case 'pagination':
      return `<!-- 分页组件示例 -->
<div class="pagination-group">
  <h4>完整分页组件</h4>
  <el-pagination
    background
    v-model:current-page="currentPage"
    v-model:page-size="pageSize"
    :page-sizes="[10, 20, 50, 100]"
    :total="100"
    layout="total, sizes, prev, pager, next, jumper"
  />
</div>

<div class="pagination-group">
  <h4>简单分页组件</h4>
  <el-pagination
    background
    v-model:current-page="currentPage2"
    :total="50"
    layout="prev, pager, next"
  />
</div>

<div class="pagination-group">
  <h4>带总数分页组件</h4>
  <el-pagination
    background
    v-model:current-page="currentPage3"
    :page-size="10"
    :total="200"
    layout="total, prev, pager, next"
  />
</div>`
    case 'infinite':
      return `<!-- 无限滚动组件示例 -->
<div class="infinite-group">
  <h4>基础无限滚动</h4>
  <div class="infinite-demo">
    <el-infinite-scroll 
      :height="300" 
      @load="loadMore"
      :disabled="infiniteDisabled"
    >
      <div v-for="item in infiniteList" :key="item.id" class="infinite-item">
        <el-card shadow="hover" style="margin-bottom: 10px;">
          <div class="item-content">
            <h4>项目 {{ item.id }}</h4>
            <p>{{ item.content }}</p>
          </div>
        </el-card>
      </div>
      <div v-if="infiniteLoading" class="loading-text">加载中...</div>
    </el-infinite-scroll>
  </div>
</div>

<div class="infinite-group">
  <h4>无限滚动列表</h4>
  <div class="infinite-demo">
    <el-infinite-scroll 
      :height="200" 
      @load="loadMoreList"
      :disabled="listDisabled"
    >
      <ul class="infinite-list">
        <li v-for="item in infiniteListData" :key="item.id" class="infinite-list-item">
          {{ item.text }}
        </li>
      </ul>
      <div v-if="listLoading" class="loading-text">加载中...</div>
    </el-infinite-scroll>
  </div>
</div>`
    case 'icon':
      return `<!-- 图标组件示例 -->
<div class="icon-group">
  <h4>基础图标</h4>
  <div class="icon-demo">
    <el-icon><Edit /></el-icon>
    <el-icon><Share /></el-icon>
    <el-icon><Delete /></el-icon>
    <el-icon><Search /></el-icon>
    <el-icon><Setting /></el-icon>
  </div>
</div>

<div class="icon-group">
  <h4>不同尺寸的图标</h4>
  <div class="icon-demo">
    <el-icon size="large"><Edit /></el-icon>
    <el-icon><Edit /></el-icon>
    <el-icon size="small"><Edit /></el-icon>
  </div>
</div>

<div class="icon-group">
  <h4>带颜色的图标</h4>
  <div class="icon-demo">
    <el-icon color="#409EFF"><Edit /></el-icon>
    <el-icon color="#67C23A"><Check /></el-icon>
    <el-icon color="#E6A23C"><Warning /></el-icon>
    <el-icon color="#F56C6C"><Delete /></el-icon>
    <el-icon color="#909399"><InfoFilled /></el-icon>
  </div>
</div>

<div class="icon-group">
  <h4>图标在按钮中使用</h4>
  <div class="icon-demo">
    <el-button type="primary" icon="Edit">编辑</el-button>
    <el-button type="success" icon="Check">确认</el-button>
    <el-button type="warning" icon="Warning">警告</el-button>
    <el-button type="danger" icon="Delete">删除</el-button>
  </div>
</div>

<div class="icon-group">
  <h4>图标在输入框中使用</h4>
  <div class="icon-demo">
    <el-input placeholder="带前缀图标" style="width: 300px;">
      <template #prefix>
        <el-icon><Search /></el-icon>
      </template>
    </el-input>
    <el-input placeholder="带后缀图标" style="width: 300px;">
      <template #suffix>
        <el-icon><Calendar /></el-icon>
      </template>
    </el-input>
  </div>
</div>`
    case 'dialog':
      return dialogCode.value
    case 'drawer':
      return drawerCode.value
    default:
      return ''
  }
}

const getScriptCode = () => {
  const scriptTemplates = {
    button: `// 按钮组件数据
const buttonLoading = ref(false)

// 按钮事件处理
const handleButtonClick = () => {
  buttonLoading.value = true
  setTimeout(() => {
    buttonLoading.value = false
    ElMessage.success('按钮点击成功！')
  }, 1000)
}`,
    input: `// 输入框数据
const inputValue = ref('')
const numberValue = ref(10)

// 输入框事件处理
const handleInput = (value) => {
}`,
    select: `// 下拉框数据
const selectValue = ref('')
const multiSelect = ref([])

// 下拉框选项
const options = [
  { label: '选项1', value: '1' },
  { label: '选项2', value: '2' },
  { label: '选项3', value: '3' }
]

// 下拉框事件处理
const handleSelectChange = (value) => {
}`,
    datetime: `// 时间日期选择器数据
const dateValue = ref('')
const timeValue = ref('')
const dateRange = ref([])
const datetimeValue = ref('')

// 时间日期事件处理
const handleDateChange = (date) => {
}

const handleTimeChange = (time) => {
}

const handleDateRangeChange = (range) => {
}`,
    color: `// 颜色选择器数据
const colorValue = ref('#409EFF')
const colorWithAlpha = ref('rgba(64, 158, 255, 0.8)')
const predefinedColor = ref('#409EFF')
const predefineColors = ref([
  '#ff4500',
  '#ff8c00',
  '#ffd700',
  '#90ee90',
  '#00ced1',
  '#1e90ff',
  '#c71585'
])

// 颜色选择器事件处理
const handleColorChange = (color) => {
}`,
    table: `// 表格数据
const tableData = ref([
  {
    name: '张三',
    age: 25,
    address: '北京市朝阳区'
  },
  {
    name: '李四',
    age: 30,
    address: '上海市浦东新区'
  }
])

// 表格事件处理
const handleEdit = (row) => {
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
  })
}`,
    form: `// 表单数据
const form = reactive({
  name: '',
  email: '',
  role: 'admin',
  status: 'active',
  remember: false
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

// 表单提交
const handleSubmit = () => {
  ElMessage.success('表单提交成功！')
}`,
    card: `// 卡片数据
const currentDate = ref(new Date().toLocaleDateString())

// 卡片事件处理
const handleCardAction = () => {
  ElMessage.success('卡片操作成功！')
}`,
    infinite: `// 无限滚动数据
const infiniteList = ref([])
const infiniteLoading = ref(false)
const infiniteDisabled = ref(false)
const infiniteListData = ref([])
const listLoading = ref(false)
const listDisabled = ref(false)

// 无限滚动方法
const loadMore = () => {
  infiniteLoading.value = true
  setTimeout(() => {
    const newItems = []
    const startIndex = infiniteList.value.length + 1
    for (let i = 0; i < 10; i++) {
      newItems.push({
        id: startIndex + i,
        content: \`这是第 \${startIndex + i} 个项目的内容\`
      })
    }
    infiniteList.value.push(...newItems)
    infiniteLoading.value = false
    
    if (infiniteList.value.length >= 50) {
      infiniteDisabled.value = true
    }
  }, 1000)
}

const loadMoreList = () => {
  listLoading.value = true
  setTimeout(() => {
    const newItems = []
    const startIndex = infiniteListData.value.length + 1
    for (let i = 0; i < 10; i++) {
      newItems.push({
        id: startIndex + i,
        text: \`列表项 \${startIndex + i}\`
      })
    }
    infiniteListData.value.push(...newItems)
    listLoading.value = false
    
    if (infiniteListData.value.length >= 30) {
      listDisabled.value = true
    }
  }, 1000)
}`,
    icon: `// 图标使用示例
// 在模板中使用图标
// <el-icon><Edit /></el-icon>
// <el-icon size="large"><Edit /></el-icon>
// <el-icon color="#409EFF"><Edit /></el-icon>

// 图标事件处理
const handleIconClick = () => {
  ElMessage.success('图标点击成功！')
}`,
    tag: `// 消息和通知方法
const showMessage = (type) => {
  const messages = {
    success: '操作成功！',
    warning: '警告信息',
    error: '操作失败！',
    info: '提示信息'
  }
  ElMessage({
    type,
    message: messages[type]
  })
}

const showNotification = (type) => {
  const notifications = {
    success: {
      title: '成功',
      message: '这是一条成功的通知消息'
    },
    warning: {
      title: '警告',
      message: '这是一条警告的通知消息'
    },
    error: {
      title: '错误',
      message: '这是一条错误的通知消息'
    },
    info: {
      title: '信息',
      message: '这是一条信息的通知消息'
    }
  }
  
  ElNotification({
    type,
    title: notifications[type].title,
    message: notifications[type].message
  })
}`,
    pagination: `// 分页数据
const currentPage = ref(1)
const pageSize = ref(20)
const currentPage2 = ref(1)
const currentPage3 = ref(1)

// 分页事件处理
const handleCurrentChange = (page) => {
  // 这里可以调用接口获取数据
}

const handleSizeChange = (size) => {
  // 这里可以重新获取数据
}`,
    dialog: `// 对话框数据
const showDialog = ref(false)
const showFormDialog = ref(false)
const dialogForm = reactive({
  username: '',
  email: '',
  role: ''
})

// 对话框事件处理
const handleFormSubmit = () => {
  ElMessage.success('表单提交成功！')
  showFormDialog.value = false
}`,
    drawer: `// 抽屉数据
const showDrawer = ref(false)
const showFormDrawer = ref(false)
const drawerForm = reactive({
  name: '',
  email: '',
  department: ''
})

// 抽屉事件处理
const handleDrawerConfirm = () => {
  ElMessage.success('操作成功！')
  showDrawer.value = false
}

const handleDrawerFormSubmit = () => {
  ElMessage.success('表单保存成功！')
  showFormDrawer.value = false
}`
  }
  
  return scriptTemplates[activeTab.value] || ''
}

const getStyleCode = () => {
  const styleTemplates = {
    button: `.button-group {
  margin-bottom: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.button-row {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}`,
    input: `.input-group {
  margin-bottom: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.input-row {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}`,
    select: `.select-group {
  margin-bottom: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
  }
}`,
    datetime: `.datetime-group {
  margin-bottom: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}`,
    color: `.color-group {
  margin-bottom: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.color-sizes {
  display: flex;
  gap: 12px;
  align-items: center;
}`,
    table: `.el-table {
  margin-top: 16px;
}

.table-actions {
  display: flex;
  gap: 8px;
}`,
    form: `.el-form {
  max-width: 400px;
}

.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 16px;
}`,
    card: `.card-group {
  margin-bottom: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.card-demo {
  max-width: 400px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text.item {
  margin-bottom: 18px;
}

.image {
  width: 100%;
  display: block;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.time {
  font-size: 13px;
  color: #999;
}`,
    infinite: `.infinite-group {
  margin-bottom: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.infinite-demo {
  border: 1px solid var(--border-color);
  border-radius: 4px;
  overflow: hidden;
}

.infinite-item {
  padding: 10px;
}

.item-content h4 {
  margin: 0 0 8px 0;
  color: var(--text-primary);
}

.item-content p {
  margin: 0;
  color: var(--text-secondary);
}

.infinite-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.infinite-list-item {
  padding: 10px;
  border-bottom: 1px solid var(--border-color);
  color: var(--text-primary);
}

.loading-text {
  text-align: center;
  padding: 20px;
  color: var(--text-secondary);
}`,
    icon: `.icon-group {
  margin-bottom: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.icon-demo {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: center;
}`,
    tag: `.tag-group, .message-group, .notification-group {
  margin-bottom: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.tag-demo, .message-demo, .notification-demo {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}`,
    pagination: `.pagination-group {
  margin-bottom: 24px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}`,
    dialog: `.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}`,
    drawer: `.drawer-content {
  padding: 16px;
}

.drawer-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid var(--border-color);
}`
  }
  
  return styleTemplates[activeTab.value] || ''
}

// 复制代码方法
const copyCode = (type) => {
  let codeToCopy = ''
  
  switch (type) {
    case 'template':
      codeToCopy = getTemplateCode()
      break
    case 'script':
      codeToCopy = getScriptCode()
      break
    case 'style':
      codeToCopy = getStyleCode()
      break
    default:
      codeToCopy = ''
  }
  
  if (codeToCopy) {
    navigator.clipboard.writeText(codeToCopy).then(() => {
      ElMessage({
        type: 'success',
        message: '代码已复制到剪贴板！',
      })
    }).catch(() => {
      ElMessage({
        type: 'error',
        message: '复制失败，请手动复制',
      })
    })
  }
}
</script>

<style lang="scss" scoped>
.test-page {
  padding: 24px;
}

.test-sections {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.test-section {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 24px;
  box-shadow: var(--shadow-lg);
  
  h3 {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 16px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.test-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.button-group, .input-group, .select-group {
  background: var(--bg-tertiary);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.button-row, .input-row {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}

// 主内容区域布局
.main-content {
  display: flex;
  gap: 24px;
  min-height: 600px;
}

// 左侧预览区域
.preview-section {
  flex: 1;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  box-shadow: var(--shadow-lg);
  overflow: hidden;
  
  .preview-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 24px;
    background: var(--bg-tertiary);
    border-bottom: 1px solid var(--border-color);
    
    h3 {
      font-size: 16px;
      font-weight: 600;
      color: var(--text-primary);
      margin: 0;
    }
  }
  
  .preview-content {
    padding: 24px;
    height: calc(100% - 60px);
    overflow-y: auto;
  }
}

// 组件预览样式
.component-preview {
  .preview-title {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 20px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
  }
  
  .preview-area {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
}

// 右侧代码面板区域
.code-section {
  width: 500px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  box-shadow: var(--shadow-lg);
  overflow: hidden;
  
  .code-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 24px;
    background: var(--bg-tertiary);
    border-bottom: 1px solid var(--border-color);
    
    h3 {
      font-size: 16px;
      font-weight: 600;
      color: var(--text-primary);
      margin: 0;
    }
    
    .code-actions {
      display: flex;
      gap: 8px;
    }
  }
  
  .code-content {
    height: calc(100% - 60px);
    overflow: hidden;
    transition: height 0.3s ease;
    
    &.collapsed {
      height: 0;
    }
  }
}

// 代码标签页样式
.code-tabs {
  height: 100%;
  
  .el-tabs__content {
    height: calc(100% - 40px);
    overflow: hidden;
  }
  
  .el-tab-pane {
    height: 100%;
  }
}

// 代码块样式
.code-block {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: var(--bg-secondary);
  border-radius: 8px;
  overflow: hidden;
  
  .code-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    background: var(--bg-tertiary);
    border-bottom: 1px solid var(--border-color);
    
    span {
      color: var(--text-primary);
      font-size: 14px;
      font-weight: 500;
    }
  }
  
  .code-content {
    flex: 1;
    margin: 0;
    padding: 16px;
    background: var(--bg-secondary);
    color: var(--text-primary);
    font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
    font-size: 13px;
    line-height: 1.6;
    overflow: auto;
    white-space: pre-wrap;
    word-break: break-word;
    
    code {
      color: inherit;
      background: none;
      padding: 0;
      border-radius: 0;
      font-family: inherit;
      font-size: inherit;
    }
  }
}

// 顶部标签页导航
.tab-navigation {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 16px 24px;
  box-shadow: var(--shadow-lg);
  margin-bottom: 24px;
  
  .el-tabs__header {
    margin-bottom: 0;
  }
}

// 组件组样式
.button-group, .input-group, .select-group, .datetime-group, .color-group, .card-group, .tag-group, .message-group, .notification-group, .pagination-group, .infinite-group, .icon-group, .slider-group, .switch-group, .upload-group, .transfer-group, .progress-group, .badge-group, .navmenu-group, .steps-group, .popconfirm-group, .carousel-group, .collapse-group, .timeline-group, .divider-group, .calendar-group {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

// 滑块样式
.slider-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

// 开关样式
.switch-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  gap: 16px;
}

// 上传样式
.upload-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

// 穿梭框样式
.transfer-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

// 进度条样式
.progress-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

// 标记样式
.badge-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
  display: flex;
  gap: 16px;
  align-items: center;
}

// 导航菜单样式
.navmenu-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
  
  .el-menu-vertical {
    width: 200px;
  }
}

// 步骤条样式
.steps-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

.steps-actions {
  margin-top: 16px;
  display: flex;
  gap: 12px;
}

// 气泡确认框样式
.popconfirm-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

// 走马灯样式
.carousel-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

.carousel-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  text-align: center;
  
  h3 {
    margin: 0 0 16px 0;
    font-size: 24px;
  }
  
  p {
    margin: 0;
    font-size: 16px;
  }
}

// 折叠面板样式
.collapse-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

// 时间线样式
.timeline-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

// 分割线样式
.divider-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

// 日历样式
.calendar-demo {
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
}

// 演示区域样式
.dialog-demo, .drawer-demo {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
}

// 响应式设计
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
  }
  
  .code-section {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .test-page {
    padding: 16px;
  }
  
  .main-content {
    gap: 16px;
  }
  
  .preview-section, .code-section {
    .preview-header, .code-header {
      padding: 12px 16px;
    }
    
    .preview-content {
      padding: 16px;
    }
  }
  
  .tab-navigation {
    padding: 12px 16px;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

// 对话框样式
.dialog-group {
  background: var(--bg-tertiary);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.dialog-row {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}

.custom-dialog-content {
  .dialog-header-info {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 16px;
    background: var(--bg-tertiary);
    border-radius: 8px;
    margin-bottom: 16px;
    
    span {
      color: var(--text-primary);
      font-weight: 500;
    }
  }
  
  .dialog-body {
    p {
      margin-bottom: 12px;
      color: var(--text-secondary);
    }
    
    ul {
      margin-bottom: 16px;
      padding-left: 20px;
      
      li {
        color: var(--text-secondary);
        margin-bottom: 4px;
      }
    }
    
    .custom-actions {
      margin-top: 16px;
      padding-top: 16px;
      border-top: 1px solid var(--border-color);
    }
  }
}

// 抽屉样式
.drawer-group {
  background: var(--bg-tertiary);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  
  h4 {
    color: var(--text-primary);
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    background: transparent;
  }
}

.drawer-row {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  align-items: center;
}

.drawer-content {
  padding: 16px;
  
  p {
    color: var(--text-secondary);
    margin-bottom: 16px;
  }
  
  .drawer-section {
    margin-bottom: 16px;
    
    h4 {
      color: var(--text-primary);
      font-size: 16px;
      font-weight: 600;
      margin-bottom: 12px;
    }
    
    ul {
      padding-left: 20px;
      
      li {
        color: var(--text-secondary);
        margin-bottom: 4px;
      }
    }
  }
  
  .drawer-actions {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    margin-top: 24px;
    padding-top: 16px;
    border-top: 1px solid var(--border-color);
  }
}

.notification-list {
  .notification-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px;
    border-radius: 6px;
    background: var(--bg-secondary);
    margin-bottom: 8px;
    
    span {
      color: var(--text-primary);
      font-size: 14px;
    }
  }
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}

.list-item-content {
  flex: 1;
  
  .item-title {
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 4px;
  }
  
  .item-desc {
    color: var(--text-secondary);
    font-size: 14px;
    margin-bottom: 8px;
  }
  
  .item-meta {
    display: flex;
    align-items: center;
    gap: 12px;
    
    .item-time {
      color: var(--text-muted);
      font-size: 12px;
    }
  }
}

.nested-content {
  h4 {
    color: var(--text-primary);
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 12px;
  }
  
  p {
    color: var(--text-secondary);
    margin-bottom: 16px;
  }
}

@media (max-width: 768px) {
  .test-page {
    padding: 16px;
  }
  
  .test-section {
    padding: 16px;
  }
  
  .test-content {
    flex-direction: column;
  }
  
  .select-group {
    margin-bottom: 16px;
    padding: 12px;
  }
}
</style> 