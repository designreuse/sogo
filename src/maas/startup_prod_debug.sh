#!/bin/sh
#功能简介：启动上层目录下的jar文件
#参数简介：
#    $1:jar文件名（包含后缀名）
#    注意：jar文件必须位于startup.sh目录的上一层目录。

#启动参数
JAVA_OPTS="-server -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 -Xms1400m -Xmx1400m -Xmn600m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m -Xverify:none -XX:+DisableExplicitGC -Dspring.profiles.active=prod -Dsun.misc.URLClassPath.disableJarChecking=true  -Djava.awt.headless=true"

arg_name=${1%*/}
this_dir="$( cd "$( dirname "$0"  )" && pwd )"
parent_dir=`dirname "${this_dir}"`


jar_file="${this_dir}/${arg_name}/target/${arg_name}-0.0.1-SNAPSHOT.war"

#父目录下jar文件存在
if [ -f "${jar_file}" ]; then
	
    nohup java $JAVA_OPTS -jar ${jar_file}  > ${arg_name}.out 2>&1 &
	echo "启动 ${jar_file}，日志文件 ${arg_name}.out"
    exit 0
else
    jar_file="${this_dir}/${arg_name}/target/${arg_name}-0.0.1-SNAPSHOT.jar"
	nohup java $JAVA_OPTS -jar ${jar_file}  > ${arg_name}.out 2>&1 &
	echo "启动 ${jar_file}，日志文件 ${arg_name}.out"
    exit 0
fi





#启动jar包；重定向标准错误输出到文件，丢掉标准输出

