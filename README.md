# Hanabi Classic

## 🌐 Language / 语言选择

- [English](README-EN.md)
- [中文](README.md)

---

Hanabi Classic 是 Hanabi 的一个经典版本，旨在提供一个经典、怀旧的游戏体验。

---

## 🔔 注意事项

- 本项目不保证相关绕过功能的可用性，仅供怀旧体验使用。
- 欢迎社区开发者贡献绕过代码，以提升服务器内的可玩性。
- 本项目采用**自定义源代码许可证**，请务必阅读文末的使用声明。

鉴于本人精力有限，本项目主要由社区维护。  
若有开发者贡献达到一定标准，可申请本仓库的读写权限。

---

## 📝 开发计划（TODO）

- [ ] 移植 Hanabi 早期 ClickGUI
- [x] 移植 Azureware 的 ClickGUI
- [ ] 移植 AzureWare 早期 UI
- [ ] 修复 Hypixel 绕过
- [ ] 修复大量 Bug
- [ ] 添加对其他服务器的绕过支持

---

## ⚙️ 项目配置

### JVM 启动参数

```bash
-Dfml.coreMods.load=cn.hanabi.injection.MixinLoader
````

### 使用 IntelliJ IDEA

```bash
gradlew setupDecompWorkspace idea genIntelliJRuns build
```

### 使用 Eclipse

```bash
gradlew setupDecompWorkspace eclipse build
```

### 导出构建（Export）

```bash
gradlew clean build
```

---

## ⚠️ 使用声明（License Notice）

本项目源代码仅供学习、研究及非商业用途。**禁止在未经作者书面许可的情况下，将全部或部分代码用于商业用途。** 包括但不限于：

* 用于盈利产品或服务；
* 整合入商业项目中；
* 部署在收费系统中；
* 用于违法犯罪活动。

如需商业许可，请联系：\[[SuperSkidder@qq.com](mailto:SuperSkidder@qq.com)]。

本项目保留所有权利，未经授权的商业使用将被视为侵权行为，可能引发法律责任。