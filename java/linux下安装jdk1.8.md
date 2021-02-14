## 1、下载

百度网盘下载：https://pan.baidu.com/s/1ToR8EMuSUgT7mvGI3uOeyQ 提取码：s8d7

## 2、解压

```
tar -zxvf jdk-8u162-linux-x64.tar.gz
```

解压后的目录`/home/soft/jdk1.8.0_162`

## 3、配置环境变量

```shell
vi /etc/profile
#shift+G滚动到文件最底部，添加如下代码
export JAVA_HOME=/home/soft/jdk1.8.0_162
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
#保存并退出
# source /etc/profile 使配置生效
```

## 4、验证安装是否成功

```shell
[root@localhost jdk1.8.0_162]# java -version
java version "1.8.0_162"
Java(TM) SE Runtime Environment (build 1.8.0_162-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.162-b12, mixed mode)
```



