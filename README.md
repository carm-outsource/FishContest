```text
   _____     __   _____          __          __ 
  / __(_)__ / /  / ___/__  ___  / /____ ___ / /_
 / _// (_-</ _ \/ /__/ _ \/ _ \/ __/ -_|_-</ __/
/_/ /_/___/_//_/\___/\___/_//_/\__/\__/___/\__/                                          
```

# FishContest 钓鱼大赛

[![CodeFactor](https://www.codefactor.io/repository/github/carm-outsource/FishContest/badge?s=b76fec1f64726b5f19989aace6adb5f85fdab840)](https://www.codefactor.io/repository/github/carm-outsource/FishContest)
![CodeSize](https://img.shields.io/github/languages/code-size/carm-outsource/FishContest)
[![Download](https://img.shields.io/github/downloads/carm-outsource/FishContest/total)](https://github.com/carm-outsource/FishContest/releases)
[![Java CI with Maven](https://github.com/carm-outsource/FishContest/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/carm-outsource/FishContest/actions/workflows/maven.yml)
![Support](https://img.shields.io/badge/Minecraft-Java%201.18--Latest-yellow)
![](https://visitor-badge.glitch.me/badge?page_id=FishContest.readme)

钓鱼大赛插件。开启一场钓鱼大赛，依据重量分出胜负，并依据排名给玩家发放对应奖励。

![gui](.doc/image/gui.png)

## 功能与优势

- 可配置份额的**计量单位**，无论是 长度、重量还是体积，一切都如您所愿。
- 可配置份额的**随机方式**，支持”固定份额“、”随机范围份额“ 与 ”正态分布范围“ 。
- 可配置定时开启比赛的时间，支持多个时间点。
- 可依照群系的不同配置每种鱼类/物品的出现概率与份额的随机方式。
- 提供便于玩家阅读的可翻页GUI，可以清晰的看到当前全部的玩家排名与成绩。
- *支持比赛记录。比赛结束后将保存详细的比赛内容，包括每个玩家的成绩与奖励，以便查询。

### 优势

- **轻量插件。** 适合小型服务器使用，配置简单方便。
- **规范开发。** 插件架构符合开发规范，适合新手开发者学习。
- **持续维护。** 新功能需求均可提交，大概率在后续开发中支持。

## [依赖](https://github.com/CarmJos/FishContest/network/dependencies)

- **[必须]** 插件本体基于 [Spigot-API](https://hub.spigotmc.org/stash/projects/SPIGOT) 、[BukkitAPI](http://bukkit.org/)
  实现。
- **[自带]** 消息格式基于 [MineDown](https://github.com/Phoenix616/MineDown) 实现。
    - 所有 messages.yml 均支持 MineDown 语法。
- **[推荐]** 变量部分基于 [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) 实现。

详细依赖列表可见 [Dependencies](https://github.com/CarmJos/FishContest/network/dependencies) 。

## [指令](src/main/resources/plugin.yml)

以下指令的主指令为 `/FishContest` 或 `/fc`。

- 必须参数 `<参数>`
- 可选参数 `[参数]`

```text
# reload
@ 管理指令 (FishContest.admin)
- 重载插件配置文件。


# stats [比赛ID]
- 查看某场比赛的比赛情况。
- 若不填写“比赛ID”，则会查看当前正在进行的比赛(如果有)。

# start [种类] [持续时间(秒)]
- 手动开始一场钓鱼大赛。
- 若不填写“种类”与“时间”，将使用配置文件中的默认值。

# stop
- 手动结束并结算目前正在进行的钓鱼大赛。

# suspend
- 手动终止目前正在进行的钓鱼大赛。
- 此操作将不会结算比赛奖励！
```

## 变量 (PlaceholderAPI)

安装 [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) 后，可以输入 `/papi info FishContest` 查看相关变量。

变量内容如下

```text
# %FishContest_remain_time% 
- 得到当前正在执行的比赛的剩余时间，以 时分秒 的格式显示。
# %FishContest_remain_seconds% 
- 得到当前正在执行的比赛的剩余秒数。
# %FishContest_remain_percentage% 
- 得到当前正在执行的比赛的剩余时间百分比(0~1)。
```

## 配置

### 插件配置文件 ([config.yml]())

详见源文件。

### 消息配置文件 ([messages.yml]())

支持 [MineDown 语法](https://wiki.phoenix616.dev/library:minedown:syntax)，详见源文件。

## 使用统计

[![bStats](https://bstats.org/signatures/bukkit/FishContest.svg)](https://bstats.org/plugin/bukkit/FishContest/17847)

## 支持与捐赠

若您觉得本插件做的不错，您可以捐赠支持我，感谢您成为开源项目的支持者！

由衷感谢以下支持本项目开发的朋友们：

- 本插件由 [天际正版公益服](https://github.com/YuanYuanOwO/Minecraft-Tianji-Server)
  管理员 [YuanYuan](https://github.com/YuanYuanOwO) 委托本人开发，经过授权后开源。

Many thanks to Jetbrains for kindly providing a license for me to work on this and other open-source projects.  
[![](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)](https://www.jetbrains.com/?from=https://github.com/CarmJos/UserPrefix)

## 开源协议

本项目源码采用 [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0) 开源协议。
<details>
<summary>关于 GPL 协议</summary>

> GNU General Public Licence (GPL) 有可能是开源界最常用的许可模式。GPL 保证了所有开发者的权利，同时为使用者提供了足够的复制，分发，修改的权利：
>
> #### 可自由复制
> 你可以将软件复制到你的电脑，你客户的电脑，或者任何地方。复制份数没有任何限制。
> #### 可自由分发
> 在你的网站提供下载，拷贝到U盘送人，或者将源代码打印出来从窗户扔出去（环保起见，请别这样做）。
> #### 可以用来盈利
> 你可以在分发软件的时候收费，但你必须在收费前向你的客户提供该软件的 GNU GPL 许可协议，以便让他们知道，他们可以从别的渠道免费得到这份软件，以及你收费的理由。
> #### 可自由修改
> 如果你想添加或删除某个功能，没问题，如果你想在别的项目中使用部分代码，也没问题，唯一的要求是，使用了这段代码的项目也必须使用
> GPL 协议。
>
> 需要注意的是，分发的时候，需要明确提供源代码和二进制文件，另外，用于某些程序的某些协议有一些问题和限制，你可以看一下
> @PierreJoye 写的 Practical Guide to GPL Compliance 一文。使用 GPL 协议，你必须在源代码代码中包含相应信息，以及协议本身。
>
> *以上文字来自 [五种开源协议GPL,LGPL,BSD,MIT,Apache](https://www.oschina.net/question/54100_9455) 。*
</details>
