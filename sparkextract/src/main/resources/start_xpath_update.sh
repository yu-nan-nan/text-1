#!/bin/bash
source /etc/profile
delete_day=`date -d -2day '+%Y%m%d'`

echo delete_day:$delete_day
# 调用python程序，读取redis，更新广播文件，并上传hdfs保存
/usr/local/bin/python /home/qingniu/hainiu_cralwer/download_page/xpath_config.py

# 删除 前2天的广播变量 ， 每小时执行一次
rm -rf /home/qingniu/xpath_cache_file/xpath_file${delete_day}*
hadoop fs -rmr hdfs://ns1/user/qingniu/xpath_cache_file/xpath_file${delete_day}*
