<template>
  <div class="home-container">
    <!-- 主要内容区域 -->
    <div class="hero-section">
      <div class="hero-content">
        <div class="hero-text">
          <h1 class="hero-title">
            <el-icon class="title-icon"><Tools /></el-icon>
            可视化表单设计器
          </h1>
          <p class="hero-description">
            基于Vue3 + Element UI Plus的可视化表单设计器
            <br>
            支持拖拽设计、实时预览、代码编辑功能
          </p>
          <div class="hero-actions">
            <el-button type="primary" size="large" @click="$router.push('/designer')">
              <el-icon><Edit /></el-icon>
              开始设计
            </el-button>
            <el-button size="large" @click="$router.push('/preview')">
              <el-icon><View /></el-icon>
              查看示例
            </el-button>
          </div>
        </div>
        <div class="hero-image">
          <div class="mockup-browser">
            <div class="browser-header">
              <div class="browser-dots">
                <span class="dot red"></span>
                <span class="dot yellow"></span>
                <span class="dot green"></span>
              </div>
            </div>
            <div class="browser-content">
              <div class="form-preview">
                <div class="form-title">表单预览</div>
                <div class="form-item">
                  <div class="form-label">用户名</div>
                  <div class="form-input"></div>
                </div>
                <div class="form-item">
                  <div class="form-label">邮箱</div>
                  <div class="form-input"></div>
                </div>
                <div class="form-item">
                  <div class="form-label">性别</div>
                  <div class="form-select"></div>
                </div>
                <div class="form-actions">
                  <div class="form-button primary"></div>
                  <div class="form-button"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 功能特性区域 -->
    <div class="features-section">
      <div class="section-header">
        <h2>核心功能</h2>
        <p>强大的功能让表单设计变得简单高效</p>
      </div>
      
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon primary">
            <el-icon><Grid /></el-icon>
          </div>
          <h3>拖拽设计</h3>
          <p>直观的拖拽界面，快速构建复杂表单</p>
          <el-button text type="primary" @click="$router.push('/designer')">
            开始体验 <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
        
        <div class="feature-card">
          <div class="feature-icon success">
            <el-icon><View /></el-icon>
          </div>
          <h3>实时预览</h3>
          <p>多设备响应式预览，所见即所得</p>
          <el-button text type="success" @click="$router.push('/preview')">
            查看预览 <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
        
        <div class="feature-card">
          <div class="feature-icon warning">
            <el-icon><Code /></el-icon>
          </div>
          <h3>代码编辑</h3>
          <p>支持实时代码编写</p>
          <el-button text type="warning" @click="$router.push('/designer')">
            代码编辑 <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>
        
      </div>
    </div>
    
    <!-- 技术栈区域 -->
    <div class="tech-section">
      <div class="section-header">
        <h2>技术栈</h2>
        <p>基于现代化的前端技术栈构建</p>
      </div>
      
      <div class="tech-grid">
        <div class="tech-item">
          <div class="tech-logo vue">Vue</div>
          <span>Vue 3</span>
        </div>
        <div class="tech-item">
          <div class="tech-logo ts">TS</div>
          <span>TypeScript</span>
        </div>
        <div class="tech-item">
          <div class="tech-logo element">EP</div>
          <span>Element Plus</span>
        </div>
        <div class="tech-item">
          <div class="tech-logo vite">V</div>
          <span>Vite</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

/* 主要内容区域 */
.hero-section {
  padding: 80px 40px;
  max-width: 1200px;
  margin: 0 auto;
}

.hero-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}

.hero-text {
  text-align: left;
}

.hero-title {
  font-size: 48px;
  font-weight: 700;
  margin: 0 0 24px 0;
  display: flex;
  align-items: center;
  gap: 16px;
  line-height: 1.2;
}

.title-icon {
  font-size: 56px;
  color: #ffd700;
}

.hero-description {
  font-size: 18px;
  line-height: 1.6;
  margin: 0 0 32px 0;
  opacity: 0.9;
}

.hero-actions {
  display: flex;
  gap: 16px;
}

.hero-actions .el-button {
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
}

/* 浏览器模拟器 */
.hero-image {
  display: flex;
  justify-content: center;
}

.mockup-browser {
  width: 400px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  transform: perspective(1000px) rotateY(-5deg) rotateX(5deg);
}

.browser-header {
  height: 40px;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  padding: 0 16px;
}

.browser-dots {
  display: flex;
  gap: 8px;
}

.dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

.dot.red { background: #ff5f57; }
.dot.yellow { background: #ffbd2e; }
.dot.green { background: #28ca42; }

.browser-content {
  padding: 24px;
  background: white;
  color: #333;
}

.form-preview {
  font-size: 14px;
}

.form-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #333;
}

.form-item {
  margin-bottom: 16px;
}

.form-label {
  font-size: 13px;
  color: #666;
  margin-bottom: 6px;
}

.form-input, .form-select {
  height: 32px;
  background: #f8f9fa;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
}

.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.form-button {
  height: 32px;
  width: 80px;
  background: #f0f0f0;
  border-radius: 4px;
}

.form-button.primary {
  background: #409eff;
}

/* 功能特性区域 */
.features-section {
  padding: 80px 40px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.section-header h2 {
  font-size: 36px;
  font-weight: 600;
  margin: 0 0 16px 0;
}

.section-header p {
  font-size: 18px;
  opacity: 0.8;
  margin: 0;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 32px;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  padding: 32px 24px;
  text-align: center;
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-8px);
}

.feature-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  font-size: 32px;
}

.feature-icon.primary { background: rgba(64, 158, 255, 0.2); color: #409eff; }
.feature-icon.success { background: rgba(103, 194, 58, 0.2); color: #67c23a; }
.feature-icon.warning { background: rgba(230, 162, 60, 0.2); color: #e6a23c; }
.feature-icon.danger { background: rgba(245, 108, 108, 0.2); color: #f56c6c; }

.feature-card h3 {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 12px 0;
}

.feature-card p {
  opacity: 0.8;
  margin: 0 0 20px 0;
  line-height: 1.5;
}

/* 技术栈区域 */
.tech-section {
  padding: 80px 40px;
  background: rgba(0, 0, 0, 0.1);
}

.tech-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 32px;
  max-width: 1000px;
  margin: 0 auto;
}

.tech-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.tech-logo {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 24px;
  color: white;
}

.tech-logo.vue { background: linear-gradient(135deg, #4fc08d, #44a08d); }
.tech-logo.ts { background: linear-gradient(135deg, #3178c6, #235a97); }
.tech-logo.element { background: linear-gradient(135deg, #409eff, #3080d0); }
.tech-logo.vite { background: linear-gradient(135deg, #646cff, #535bf2); }

.tech-item span {
  font-weight: 500;
  opacity: 0.9;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-content {
    grid-template-columns: 1fr;
    gap: 40px;
    text-align: center;
  }
  
  .hero-title {
    font-size: 36px;
    justify-content: center;
  }
  
  .mockup-browser {
    width: 300px;
    transform: none;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
  }
  
  .tech-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
