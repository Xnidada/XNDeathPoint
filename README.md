# XNDeathPoint

## 更好的死亡点记录与传送

- 是否还在为自己其他插件back指令经常被覆盖而烦恼

- 是否还在为无法添加多个死亡记录烦恼

- 支持指定世界中开启与关闭



## 快快添加此插件,解决你的烦恼



---



### 指令与权限

deathpoint可简写为dp

| 指令 | 权限 | 作用 |

| --- | --- | --- |

[COLOR=rgb(255, 255, 255)]| deathpoint open | deathpoint.use | 打开死亡传送点菜单 |[/COLOR]

| deathpoint bk | deathpoint.use | 直接传送至最近一次死亡点 |

| deathpoint open [玩家名] | deathpoint.use.other | 给指定玩家打开死亡传送点菜单 |

| deathpoint reload | deathpoint.reload | 重载配置文件 |

---



### 配置文件

```yml

# 消息配置

messages:

  prefix: "&f[&bXNDeathPoint&f] "

  no_permission: "&c你没有权限使用此命令"

  no_permission_other: "&c你没有权限帮助其他玩家打开死亡点界面"

  player_not_online: "&c玩家 {player} 不在线或不存在"

  only_player: "&c只有玩家可以使用此命令"

  gui_open_confirm: "&a已为玩家 {player} 打开死亡点界面"

  gui_open_self: "&a管理员已为你打开死亡点界面"

  no_death_records: "&c你还没有死亡记录"

  world_not_loaded: "&c死亡点所在世界未加载，无法传送"

  teleport_success: "&a已传送到死亡点 #{slot}"

  teleport_recent: "&a已传送到最近一次死亡点"

  cost_parse_error: "&c传输点费用解析错误，请联系管理员！"

  not_enough_money: "&c你没有足够的金币传送到此死亡点！"

  config_reload: "&a已重新加载配置文件"

  console_only_player: "&c只有玩家可以使用此命令打开自己的死亡点界面"



gui:

  # GUI 标题

  title: "&f[&bXNDeathPoint&f] &c死亡点传送"

  # 自定义 gui 中显示

  items:

    point1:

      # 显示材质

      material: "BED"

      # 显示名字

      name: "&c死亡点 #1"

      # 更多lore

      lore:

        - "&7世界: &f{world}"

        - "&7坐标: &f{x}, {y}, {z}"

        - "&7时间: &f{time}"

        - "&6所需金币: &e{cost}金币"

    point2:

      material: "BED"

      name: "&c死亡点 #2"

      lore:

        - "&7世界: &f{world}"

        - "&7坐标: &f{x}, {y}, {z}"

        - "&7时间: &f{time}"

        - "&6所需金币: &e{cost}金币"

    point3:

      material: "BED"

      name: "&c死亡点 #3"

      lore:

        - "&7世界: &f{world}"

        - "&7坐标: &f{x}, {y}, {z}"

        - "&7时间: &f{time}"

        - "&6所需金币: &e{cost}金币"

    point4:

      material: "BED"

      name: "&c死亡点 #4"

      lore:

        - "&7世界: &f{world}"

        - "&7坐标: &f{x}, {y}, {z}"

        - "&7时间: &f{time}"

        - "&6所需金币: &e{cost}金币"

    point5:

      material: "BED"

      name: "&c死亡点 #5"

      lore:

        - "&7世界: &f{world}"

        - "&7坐标: &f{x}, {y}, {z}"

        - "&7时间: &f{time}"

        - "&6所需金币: &e{cost}金币"

    point6:

      material: "BED"

      name: "&c死亡点 #6"

      lore:

        - "&7世界: &f{world}"

        - "&7坐标: &f{x}, {y}, {z}"

        - "&7时间: &f{time}"

        - "&6所需金币: &e{cost}金币"

    point7:

      material: "BED"

      name: "&c死亡点 #7"

      lore:

        - "&7世界: &f{world}"

        - "&7坐标: &f{x}, {y}, {z}"

        - "&7时间: &f{time}"

        - "&6所需金币: &e{cost}金币"

    point8:

      material: "BED"

      name: "&c死亡点 #8"

      lore:

        - "&7世界: &f{world}"

        - "&7坐标: &f{x}, {y}, {z}"

        - "&7时间: &f{time}"

        - "&6所需金币: &e{cost}金币"

    point9:

      material: "BED"

      name: "&c死亡点 #9"

      lore:

        - "&7世界: &f{world}"

        - "&7坐标: &f{x}, {y}, {z}"

        - "&7时间: &f{time}"

        - "&6所需金币: &e{cost}金币"



# 传送时所需花费

teleport_costs:

  point1: 0

  point2: 10

  point3: 20

  point4: 30

  point5: 40

  point6: 50

  point7: 60

  point8: 70

  point9: 80



# 开启的世界

enabled_worlds:

  - world

  - world_nether

  - world_the_end

```



---



### 使用说明



1. 将插件进行载入(这不用教了吧)

2. 按照注释修改config.yml配置文件

    - [![pk57pHP.md.png](https://s21.ax1x.com/2024/07/15/pk57pHP.md.png)](https://imgse.com/i/pk57pHP)

3. 使用`deathpoint reload`重载配置

4. 给予玩家`deathpoint.use`权限

5. 玩家死亡后使用命令`/deathpoint open`即可使用



#### 玩家死亡后记录点位

[![pk57Vjs.png](https://s21.ax1x.com/2024/07/15/pk57Vjs.png)](https://imgse.com/i/pk57Vjs)



#### 进行传送

[![pk57mBq.md.png](https://s21.ax1x.com/2024/07/15/pk57mBq.md.png)](https://imgse.com/i/pk57mBq)



---



### 已知BUG:



暂无



---



### 统计

![bstats.org/signatures/bukkit/XNDeathPoint.svg](https://bstats.org/signatures/bukkit/XNDeathPoint.svg)
