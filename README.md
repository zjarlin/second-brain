Second Brain - 现代化知识/后台管理系统

Second Brain 是一个基于 Kotlin 和 Compose Multiplatform 开发的现代化桌面应用，旨在提供强大的个人知识管理解决方案。本项目采用前后端分离架构，并集成了智能化的知识管理功能，帮助用户更好地组织和利用个人知识。

## 项目特性

### 1. 现代化技术栈
- 基于 Kotlin 和 Compose Multiplatform 的跨平台开发
- 前后端分离架构设计
- KSP (Kotlin Symbol Processing) 注解处理

### 2. 核心功能
- **知识库管理**
  - Markdown 文档支持
  - 全文检索
- **知识图谱**
  - 可视化知识关联
  - 交互式浏览
- **智能助手**
  - 基于知识库的问答
  - 上下文感知

## 技术架构

### 前端技术栈
- **UI框架**: Compose Multiplatform
- **状态管理**: Kotlin Coroutines Flow
- **UI组件**: Material 3 Design
- **动画效果**: Compose Animation

### 后端技术栈
- **开发语言**: Kotlin
- **注解处理**: KSP

[//]: # (- **网络通信**: Ktor)
[//]: # (- **序列化**: Kotlinx Serialization)

## 项目结构

```
.
├── backend/          # 后端服务实现
├── front/            # 前端桌面应用
├── ksp-annotation/   # 注解定义
├── ksp-processor/    # 注解处理器
```

## 开发环境配置

### 系统要求
- JDK 11+
- Kotlin 1.8+
- Gradle 7.x

### 快速开始

1. 克隆项目
```bash
git clone <repository-url>
cd second-brain
```

2. 构建项目
```bash
./gradlew build
```

3. 运行应用
```bash
./gradlew :front:run
```

## 贡献指南

我们欢迎各种形式的贡献，包括但不限于：

- 提交问题和建议
- 改进文档
- 提交代码修复
- 添加新功能

提交PR前请确保：

1. 代码符合项目规范
2. 添加必要的测试用例
3. 更新相关文档
