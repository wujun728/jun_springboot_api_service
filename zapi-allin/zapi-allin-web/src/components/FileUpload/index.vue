<template>
  <div class="file-upload-container">
    <!-- 上传区域 -->
    <div
      class="upload-area"
      :class="{
        'upload-area-dragover': isDragOver,
        'upload-area-disabled': disabled,
        'upload-area-multiple': multiple,
      }"
      @click="handleClick"
      @drop="handleDrop"
      @dragover="handleDragOver"
      @dragleave="handleDragLeave"
      @dragenter="handleDragEnter"
    >
      <!-- 拖拽提示 -->
      <div v-if=" !isUploading" class="upload-tip">
        <el-icon class="upload-icon">
          <Upload />
        </el-icon>
        <div class="upload-text">
          <span class="primary-text">点击上传</span>
          <span class="secondary-text">或将文件拖拽到此处</span>
        </div>
        <div v-if="accept" class="upload-hint">支持格式：{{ accept }}</div>
        <div v-if="maxSize" class="upload-hint">
          最大文件大小：{{ formatFileSize(maxSize) }}
        </div>
      </div>

      <!-- 上传进度 -->
      <div v-if="isUploading" class="upload-progress">
        <el-progress
          :percentage="uploadProgress"
          :status="uploadStatus"
          :stroke-width="6"
        />
        <div class="progress-text">
          {{ currentFileName }} - {{ uploadProgress }}%
        </div>
      </div>

    </div>
    <!-- 文件列表 -->
    <div v-if="showFileList&&fileList.length"  class="file-list">
      <div
        v-for="(file, index) in fileList"
        :key="file.id || index"
        class="file-item"
      >
        <!-- 文件图标 -->
        <div class="file-icon">
          <el-icon v-if="isImage(file.fileType)">
            <Picture />
          </el-icon>
          <el-icon v-else-if="isDocument(file.fileType)">
            <Document />
          </el-icon>
          <el-icon v-else-if="isVideo(file.fileType)">
            <VideoPlay />
          </el-icon>
          <el-icon v-else-if="isAudio(file.fileType)">
            <Headset />
          </el-icon>
          <el-icon v-else>
            <Files />
          </el-icon>
        </div>

        <!-- 文件信息 -->
        <div class="file-info">
          <div class="file-name" :title="file.fileName">
            <span class="file-name-text">
              {{ file.fileName }}
            </span>
            <span v-if="file.fileType" class="file-type">
              {{ file.fileType.toUpperCase() }}
            </span>
          </div>
          <div class="file-meta">
            {{ formatFileSize(file.fileSize) }}
          </div>
        </div>

        <!-- 文件操作 -->
        <div class="file-actions" @click.stop>
          <!-- 预览按钮 -->
          <el-button
            v-if="isImage(file.fileType)"
            type="primary"
            size="small"
            @click.stop="previewFile(file)"
          >
            <el-icon>
              <View />
            </el-icon>
            预览
          </el-button>

          <!-- 下载按钮 -->
          <el-button
            type="primary"
            size="small"
            @click.stop="downloadFile(file)"
          >
            <el-icon>
              <Download />
            </el-icon>
            下载
          </el-button>

          <!-- 删除按钮 -->
          <el-button
            v-if="!disabled"
            type="danger"
            size="small"
            @click.stop="removeFile(index)"
          >
            <el-icon>
              <Delete />
            </el-icon>
            删除
          </el-button>
        </div>
      </div>
    </div>

    <!-- 隐藏的文件输入框 -->
    <input
      ref="fileInput"
      type="file"
      :accept="accept"
      :multiple="multiple"
      style="display: none"
      @change="handleFileChange"
    />

    <!-- 图片预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      title="图片预览"
      width="60%"
      center
      :before-close="closePreview"
    >
      <div class="preview-container">
        <img
          v-if="previewImage"
          :src="previewImage"
          alt="预览图片"
          class="preview-image"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  Upload,
  Picture,
  Document,
  VideoPlay,
  Headset,
  Files,
  View,
  Download,
  Delete,
} from "@element-plus/icons-vue";
import { fileApi } from "@/api";

// 定义组件属性
const props = defineProps({
  // 文件列表
  modelValue: {
    type: Array,
    default: () => [],
  },
  // 是否多选
  multiple: {
    type: Boolean,
    default: false,
  },
  // 接受的文件类型
  accept: {
    type: String,
    default: "",
  },
  // 最大文件大小（字节）
  maxSize: {
    type: Number,
    default: 0,
  },
  // 是否禁用
  disabled: {
    type: Boolean,
    default: false,
  },
  // 上传路径
  path: {
    type: String,
    default: "",
  },
  // 文件描述
  description: {
    type: String,
    default: "",
  },
  // 上传用户ID
  uploadUserId: {
    type: [String, Number],
    default: null,
  },
  // 上传用户名
  uploadUsername: {
    type: String,
    default: "",
  },
  // 是否自动上传
  autoUpload: {
    type: Boolean,
    default: true,
  },
  // 是否显示文件列表
  showFileList:{
    type:Boolean,
    default:true
  }
});

// 定义事件
const emit = defineEmits([
  "update:modelValue",
  "upload-start",
  "upload-success",
  "upload-error",
  "file-remove",
  "file-preview",
  "file-download",
]);

// 响应式数据
const fileInput = ref(null);
const isDragOver = ref(false);
const isUploading = ref(false);
const uploadProgress = ref(0);
const uploadStatus = ref("");
const currentFileName = ref("");
const previewVisible = ref(false);
const previewImage = ref("");

// 计算属性
const fileList = computed({
  get: () => props.modelValue,
  set: (value) => emit("update:modelValue", value),
});

// 文件类型判断
const isImage = (fileType) => {
  return fileType && fileType.startsWith("image/");
};

const isDocument = (fileType) => {
  const docTypes = [
    "application/pdf",
    "application/msword",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    "text/plain",
  ];
  return fileType && docTypes.includes(fileType);
};

const isVideo = (fileType) => {
  return fileType && fileType.startsWith("video/");
};

const isAudio = (fileType) => {
  return fileType && fileType.startsWith("audio/");
};

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (!bytes) return "0 B";

  const k = 1024;
  const sizes = ["B", "KB", "MB", "GB", "TB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));

  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i];
};

// 验证文件
const validateFile = (file) => {
  // 检查文件类型
  if (props.accept) {
    const acceptTypes = props.accept.split(",").map((type) => type.trim());
    const fileType = file.type;
    const fileName = file.name;

    const isValid = acceptTypes.some((type) => {
      if (type.startsWith(".")) {
        // 扩展名匹配
        return fileName.toLowerCase().endsWith(type.toLowerCase());
      } else {
        // MIME类型匹配
        return (
          fileType === type || fileType.startsWith(type.replace("/*", "/"))
        );
      }
    });

    if (!isValid) {
      ElMessage.error(`不支持的文件类型：${file.name}`);
      return false;
    }
  }

  // 检查文件大小
  if (props.maxSize && file.size > props.maxSize) {
    ElMessage.error(`文件大小超过限制：${file.name}`);
    return false;
  }

  return true;
};

// 处理文件选择
const handleFileChange = (event) => {
  const files = Array.from(event.target.files);
  if (files.length === 0) return;

  // 验证文件
  const validFiles = files.filter(validateFile);
  if (validFiles.length === 0) return;

  // 如果是单选模式，只保留第一个文件
  const selectedFiles = props.multiple ? validFiles : [validFiles[0]];

  if (props.autoUpload) {
    uploadFiles(selectedFiles);
  } else {
    // 手动上传模式，将文件添加到列表中
    const newFiles = selectedFiles.map((file) => ({
      id: Date.now() + Math.random(),
      fileName: file.name,
      originalFileName: file.name,
      fileSize: file.size,
      fileType: file.type,
      file: file,
      status: "pending",
    }));

    fileList.value = props.multiple
      ? [...fileList.value, ...newFiles]
      : newFiles;
  }

  // 清空input值，允许重复选择同一文件
  event.target.value = "";
};

// 上传文件
const uploadFiles = async (files) => {
  if (files.length === 0) return;

  isUploading.value = true;
  uploadProgress.value = 0;
  uploadStatus.value = "";

  // 通知父组件开始上传
  emit("upload-start", files.length);

  const uploadPromises = files.map(async (file, index) => {
    currentFileName.value = file.name;

    try {
      const options = {
        path: props.path,
        description: props.description,
        uploadUserId: props.uploadUserId,
        uploadUsername: props.uploadUsername,
      };

      const response = await fileApi.uploadFile(file, options);

      if (response.code === 200) {
        const fileInfo = response.data;

        // 更新进度
        uploadProgress.value = ((index + 1) / files.length) * 100;

        // 添加到文件列表
        if (props.multiple) {
          fileList.value = [...fileList.value, fileInfo];
        } else {
          fileList.value = [fileInfo];
        }

        emit("upload-success", fileInfo, file);
        // 移除单个文件上传成功消息，避免重复提示

        return fileInfo;
      } else {
        throw new Error(response.message || "上传失败");
      }
    } catch (error) {
      uploadStatus.value = "exception";
      emit("upload-error", error, file);
      ElMessage.error(`文件 ${file.name} 上传失败：${error.message}`);
      throw error;
    }
  });

  try {
    const results = await Promise.all(uploadPromises);
    uploadStatus.value = "success";

    // 显示总体上传成功消息
    const successCount = results.length;
    if (successCount > 0) {
      ElMessage.success(`成功上传 ${successCount} 个文件`);
    }
  } catch (error) {
    uploadStatus.value = "exception";
  } finally {
    isUploading.value = false;
    uploadProgress.value = 0;
    currentFileName.value = "";
  }
};

// 手动上传
const upload = () => {
  const pendingFiles = fileList.value.filter(
    (file) => file.status === "pending"
  );
  if (pendingFiles.length === 0) {
    ElMessage.warning("没有待上传的文件");
    return;
  }

  const files = pendingFiles.map((file) => file.file).filter(Boolean);
  uploadFiles(files);
};

// 删除文件（仅删除前端列表，不调用后台接口）
const removeFile = async (index) => {
  const file = fileList.value[index];

  try {
    await ElMessageBox.confirm(
      `确定要删除文件 "${file.fileName}" 吗？`,
      "确认删除",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    );

    // 从列表中移除（不调用后台删除接口，仅删除前端列表）
    const newFileList = [...fileList.value];
    newFileList.splice(index, 1);
    fileList.value = newFileList;

    emit("file-remove", file, index);
    ElMessage.success("文件已移除");
  } catch (error) {
    // 用户取消删除
  }
};

// 预览文件
const previewFile = (file) => {
  if (isImage(file.fileType)) {
    previewImage.value = file.fileUrl || `/api/file/download/${file.id}`;
    previewVisible.value = true;
    emit("file-preview", file);
  } else {
    ElMessage.info("该文件类型不支持预览");
  }
};

// 关闭预览
const closePreview = () => {
  previewVisible.value = false;
  previewImage.value = "";
};

// 下载文件
const downloadFile = async (file) => {
  try {
    const response = await fileApi.downloadFile(file.id);
    const blob = new Blob([response.data]);
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = file.fileName;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);

    emit("file-download", file);
    ElMessage.success("文件下载成功");
  } catch (error) {
    ElMessage.error("文件下载失败");
  }
};

// 拖拽相关事件
const handleDragOver = (e) => {
  e.preventDefault();
  if (!props.disabled) {
    isDragOver.value = true;
  }
};

const handleDragLeave = (e) => {
  e.preventDefault();
  isDragOver.value = false;
};

const handleDragEnter = (e) => {
  e.preventDefault();
  if (!props.disabled) {
    isDragOver.value = true;
  }
};

const handleDrop = (e) => {
  e.preventDefault();
  isDragOver.value = false;

  if (props.disabled) return;

  const files = Array.from(e.dataTransfer.files);
  if (files.length === 0) return;

  // 验证文件
  const validFiles = files.filter(validateFile);
  if (validFiles.length === 0) return;

  // 如果是单选模式，只保留第一个文件
  const selectedFiles = props.multiple ? validFiles : [validFiles[0]];

  if (props.autoUpload) {
    uploadFiles(selectedFiles);
  } else {
    // 手动上传模式
    const newFiles = selectedFiles.map((file) => ({
      id: Date.now() + Math.random(),
      fileName: file.name,
      originalFileName: file.name,
      fileSize: file.size,
      fileType: file.type,
      file: file,
      status: "pending",
    }));

    fileList.value = props.multiple
      ? [...fileList.value, ...newFiles]
      : newFiles;
  }
};

// 点击上传区域
const handleClick = () => {
  if (!props.disabled && !isUploading.value) {
    fileInput.value?.click();
  }
};

// 暴露方法给父组件
defineExpose({
  upload,
  clear: () => {
    fileList.value = [];
  },
});
</script>

<style lang="scss" scoped>
.file-upload-container {
  width: 100%;
}

.upload-area {
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  background-color: #fafafa;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  min-height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.upload-area:hover {
  border-color: #409eff;
  background-color: #f0f9ff;
}

.upload-area-dragover {
  border-color: #409eff;
  background-color: #f0f9ff;
}

.upload-area-disabled {
  cursor: not-allowed;
  /* opacity: 0.6; */
}

.upload-area-disabled:hover {
  border-color: #d9d9d9;
  background-color: #fafafa;
}

.upload-tip {
  padding: 20px;
}

.upload-icon {
  font-size: 48px;
  color: #c0c4cc;
  margin-bottom: 16px;
}

.upload-text {
  margin-bottom: 8px;
}

.primary-text {
  font-size: 16px;
  color: #606266;
  font-weight: 500;
}

.secondary-text {
  font-size: 14px;
  color: #909399;
  margin-left: 8px;
}

.upload-hint {
  font-size: 12px;
  color: #c0c4cc;
  margin-top: 4px;
}

.upload-progress {
  width: 100%;
  padding: 20px;
}

.progress-text {
  margin-top: 8px;
  font-size: 14px;
  color: #606266;
}

.file-list {
  margin-top: 10px;
  width: 100%;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #fff;
}

.file-item {
  display: flex;
  align-items: center;
  padding: 12px;
  margin-bottom: 8px;
  transition: all 0.3s;
  &:not(:last-child){
    border-bottom: 1px solid #e4e7ed;
  }
}

.file-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.file-item:last-child {
  margin-bottom: 0;
}

.file-icon {
  margin-right: 12px;
  font-size: 24px;
  color: #409eff;
  width: 24px;
  text-align: center;
}

.file-info {
  flex: 1;
  overflow: hidden;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
}

.file-name {
  flex: 1;
  overflow: hidden;
  display: flex;
  align-items: center;
  gap: 8px;
  .file-name-text{
    line-height: 18px;
    flex: 1;
    font-size: 14px;
    color: #303133;
    font-weight: 500;
    margin-bottom: 4px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.file-meta {
  width: 50px;
  line-height: normal;
  font-size: 12px;
  color: #909399;
}

.file-type {
  line-height: 18px;
  flex-shrink: 0;
  background-color: #f0f2f5;
  padding: 2px 6px;
  border-radius: 2px;
  font-size: 11px;
}

.file-actions {
  display: flex;
  gap: 4px;
  margin-left: 12px;
}

.preview-container {
  text-align: center;
}

.preview-image {
  max-width: 100%;
  max-height: 60vh;
  object-fit: contain;
}
</style>
