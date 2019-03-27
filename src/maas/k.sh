arg_name=${1%*/}

ps -ef | grep ${arg_name} | awk '{print $2}' | xargs kill -9 


